package pro.sky.telegrambotteamwork.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegrambotteamwork.model.Cat;
import pro.sky.telegrambotteamwork.model.Dog;
import pro.sky.telegrambotteamwork.model.Image;
import pro.sky.telegrambotteamwork.model.Pet;
import pro.sky.telegrambotteamwork.repository.ImageRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

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
    private final PetService petService;

    /**
     * Метод загрузки фотографий для питомцев
     *
     * @param id        идентификатор питомца
     * @param imageFile Медиафайл фотографии
     * @throws IOException общий класс исключений ввода-вывода
     */
    public void uploadImage(Long id, MultipartFile imageFile) throws IOException {
        logger.info("Вызван метод загрузки фотографии для животных. Идентификатор: {}", id);
        Dog dog = dogService.findDog(id);
        Cat cat = catService.findCat(id);
        Pet pet = petService.findPet(id);
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
        } else if (pet != null) {
            Image imagePet = imageRepository.findByPetId(id).orElseGet(Image::new);
            imagePet.setPet(pet);
            imagePet.setFilePath(filePath.toString());
            imagePet.setMediaType(imageFile.getContentType());
            imagePet.setBytes(generateImageData(filePath));
            imageRepository.save(imagePet);
            logger.info("Вы добавили фотографию для питомца!");
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

}
