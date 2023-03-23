package pro.sky.telegrambotteamwork.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegrambotteamwork.model.Cat;
import pro.sky.telegrambotteamwork.model.Dog;
import pro.sky.telegrambotteamwork.model.Image;
import pro.sky.telegrambotteamwork.model.ReportData;
import pro.sky.telegrambotteamwork.repository.ImageRepository;
import pro.sky.telegrambotteamwork.repository.ReportDataRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.*;

/**
 * Сервис-класс для добавления фотографий
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final Logger logger = LoggerFactory.getLogger(ImageService.class);
    @Value("${path.to.images.folder}")
    private String imagesDir;
    private final ImageRepository imageRepository;
    private final DogService dogService;
    private final CatService catService;
    private final ReportDataService reportDataService;
    private final ReportDataRepository reportDataRepository;
    private final TelegramBot telegramBot;
    private static final Pattern PATTERN = Pattern.compile("([0-9]{1,})");
    private static final Pattern PATTERN_DOG = Pattern.compile("([0-9]{1,})(\\#)");
    private static final Pattern PATTERN_CAT = Pattern.compile("([0-9]{1,})(\\*)");

    /**
     * Метод загрузки фотографий для питомцев через API
     *
     * @param id        идентификатор питомца
     * @param imageFile медиафайл фотографии
     * @throws IOException общий класс исключений ввода-вывода
     */
    public void uploadImage(Long id, MultipartFile imageFile) throws IOException {
        logger.info("Вызван метод загрузки фотографии для животных и загрузки отчетов от пользователей. Идентификатор: {}", id);
        Dog dog = dogService.findDog(id);
        Cat cat = catService.findCat(id);
        ReportData reportData = reportDataService.findReportData(id);
        Path filePath = Path.of(imagesDir, id + "." + getExtensions(imageFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = imageFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        if (dog != null) {
            Image imageDog = imageRepository.findByDogId(id).orElseGet(Image::new);
            imageDog.setDog(dog);
            imageDog.setFilePath(filePath.toString());
            imageDog.setFileSize(imageFile.getSize());
            imageDog.setMediaType(imageFile.getContentType());
            imageDog.setBytes(generateImageData(filePath));
            imageRepository.save(imageDog);
            logger.info("Вы добавили фотографию для собаки!");
        } else if (cat != null) {
            Image imageCat = imageRepository.findByCatId(id).orElseGet(Image::new);
            imageCat.setCat(cat);
            imageCat.setFilePath(filePath.toString());
            imageCat.setMediaType(imageFile.getContentType());
            imageCat.setBytes(generateImageData(filePath));
            imageRepository.save(imageCat);
            logger.info("Вы добавили фотографию для кошки!");
        } else if (reportData != null) {
            Image imageReportData = imageRepository.findByReportDataId(id).orElseGet(Image::new);
            imageReportData.setReportData(reportData);
            imageReportData.setFilePath(filePath.toString());
            imageReportData.setMediaType(imageFile.getContentType());
            imageReportData.setBytes(generateImageData(filePath));
            imageRepository.save(imageReportData);
            logger.info("Вы добавили фотографию для отчета!");
        }
    }

    /**
     * Метод, для уменьшения фотографий при отображении
     *
     * @param filePath путь к фотографии
     * @return Возвращает массив байт. Файл, в байтовом виде
     * @throws IOException общий класс исключений ввода-вывода
     */
    private byte[] generateImageData(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 200);
            BufferedImage data = new BufferedImage(200, height, image.getType());
            Graphics2D graphics = data.createGraphics();
            graphics.drawImage(image, 0, 0, 200, height, null);
            graphics.dispose();

            ImageIO.write(data, getExtensions(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    /**
     * Метод, генерирующий расширение файла
     *
     * @param fileName название файла
     * @return возвращает сгенерированное расширение файла
     */
    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * Метод принимает от пользователя и сохраняет фотографию отчета на сервере Телеграма.
     * Контактная информация по фотографии записывается в базу данных и связывает
     * с ранее отправленным текстовым отчетом
     *
     * @param update входящее обновление
     * @throws IOException общий класс исключений ввода-вывода
     */
    public void saveImageReportData(Update update) throws IOException {
        Matcher matcher = PATTERN.matcher(update.message().caption());
        PhotoSize[] photoSizes = update.message().photo();

        if (photoSizes != null) {
            GetFile getFile = new GetFile(update.message().photo()[1].fileId());
            GetFileResponse getFileResponse = telegramBot.execute(getFile);
            if (getFileResponse.isOk()) {
                if (matcher.matches()) {
                    String idString = matcher.group(1);
                    Long id = Long.parseLong(idString);
                    ReportData reportData = reportDataService.findReportData(id);
                    File file = getFileResponse.file();
                    String filePath = file.filePath();
                    Long fileSize = file.fileSize();
                    String mediaType = StringUtils.getFilenameExtension(filePath);
                    byte[] bytes = telegramBot.getFileContent(file);
                    Long chatId = update.message().chat().id();

                    Image image = imageRepository.findByReportDataId(id).orElseGet(Image::new);
                    LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                    image.setFilePath(filePath);
                    image.setFileSize(fileSize);
                    image.setMediaType(mediaType);
                    image.setBytes(bytes);
                    image.setChatId(chatId);
                    image.setDateTime(dateTime);
                    image.setReportData(reportData);
                    imageRepository.save(image);
                    logger.info("Фотография отчета сохранена в базе данных");
                    telegramBot.execute(new SendMessage(update.message().chat().id(), MESSAGE_AFTER_SENDING_PHOTO));
                }
            }
        }
    }

    /**
     * Метод принимает от волонтера и сохраняет фотографию новой собаки на сервере Телеграма.
     * Контактная информация по фотографии записывается в базу данных и связывает
     * с ранее отправленной информацией о собаке
     *
     * @param update входящее обновление
     * @throws IOException общий класс исключений ввода-вывода
     */
    public void saveImageDog(Update update) throws IOException {
        Matcher matcher = PATTERN_DOG.matcher(update.message().caption());
        PhotoSize[] photoSizes = update.message().photo();

        if (photoSizes != null) {
            GetFile getFile = new GetFile(update.message().photo()[1].fileId());
            GetFileResponse getFileResponse = telegramBot.execute(getFile);
            if (getFileResponse.isOk()) {
                if (matcher.matches()) {
                    String idString = matcher.group(1);
                    Long id = Long.parseLong(idString);
                    Dog dog = dogService.findDog(id);
                    File file = getFileResponse.file();
                    String filePath = file.filePath();
                    Long fileSize = file.fileSize();
                    String mediaType = StringUtils.getFilenameExtension(filePath);
                    byte[] bytes = telegramBot.getFileContent(file);
                    Long chatId = update.message().chat().id();

                    Image image = imageRepository.findByDogId(id).orElseGet(Image::new);
                    LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                    image.setFilePath(filePath);
                    image.setFileSize(fileSize);
                    image.setMediaType(mediaType);
                    image.setBytes(bytes);
                    image.setChatId(chatId);
                    image.setDateTime(dateTime);
                    image.setDog(dog);
                    imageRepository.save(image);
                    logger.info("Фотография собаки сохранена в базе данных");
                    telegramBot.execute(new SendMessage(update.message().chat().id(), MESSAGE_AFTER_ADDING_DOG));
                }
            }
        }
    }

    /**
     * Метод принимает от волонтера и сохраняет фотографию новой кошки на сервере Телеграма.
     * Контактная информация по фотографии записывается в базу данных и связывает
     * с ранее отправленной информацией о кошке
     *
     * @param update входящее обновление
     * @throws IOException общий класс исключений ввода-вывода
     */
    public void saveImageCat(Update update) throws IOException {
        Matcher matcher = PATTERN_CAT.matcher(update.message().caption());
        PhotoSize[] photoSizes = update.message().photo();

        if (photoSizes != null) {
            GetFile getFile = new GetFile(update.message().photo()[1].fileId());
            GetFileResponse getFileResponse = telegramBot.execute(getFile);
            if (getFileResponse.isOk()) {
                if (matcher.matches()) {
                    String idString = matcher.group(1);
                    Long id = Long.parseLong(idString);
                    Cat cat = catService.findCat(id);
                    File file = getFileResponse.file();
                    String filePath = file.filePath();
                    Long fileSize = file.fileSize();
                    String mediaType = StringUtils.getFilenameExtension(filePath);
                    byte[] bytes = telegramBot.getFileContent(file);
                    Long chatId = update.message().chat().id();

                    Image image = imageRepository.findByCatId(id).orElseGet(Image::new);
                    LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                    image.setFilePath(filePath);
                    image.setFileSize(fileSize);
                    image.setMediaType(mediaType);
                    image.setBytes(bytes);
                    image.setChatId(chatId);
                    image.setDateTime(dateTime);
                    image.setCat(cat);
                    imageRepository.save(image);
                    logger.info("Фотография кошки сохранена в базе данных");
                    telegramBot.execute(new SendMessage(update.message().chat().id(), MESSAGE_AFTER_ADDING_CAT));
                }
            }
        }
    }

}
