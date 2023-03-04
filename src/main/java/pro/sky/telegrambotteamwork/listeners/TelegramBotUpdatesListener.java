package pro.sky.telegrambotteamwork.listeners;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.PetRepository;
import pro.sky.telegrambotteamwork.repository.ReportDataRepository;
import pro.sky.telegrambotteamwork.repository.UserRepository;
import pro.sky.telegrambotteamwork.serviceImpl.ReportDataService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pro.sky.telegrambotteamwork.constants.UserRequestConstant.*;

/**
 * Основной класс с логикой телеграм-бота.
 * Этот класс расширяет {@link UpdatesListener}
 */
@Service
@Data
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final UserRepository userRepository;

    /**
     * Метод, который вызывается сразу после инициализации свойств
     */
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * Основной метод класса {@link TelegramBotUpdatesListener}.
     * Обрабатывает сообщения пользователю
     *
     * @param updates Параметр входящего обновления
     * @return Возвращает все обновления
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Запрос от пользователя: {}", update);
            String message = update.message().text();
            long chatId = update.message().chat().id();
            String name = update.message().chat().firstName();
            long userId = update.message().from().id();

            switch (message) {
                case START: {
                    sendMessage(chatId, "Здравствуйте " + name + "! " + WELCOME_MESSAGE);
                    break;
                }
                case MENU: {
                    SendMessage sendMessage = new SendMessage(chatId, "Выберите из списка, что вы хотите узнать.")
                            .replyMarkup(new ReplyKeyboardMarkup(new String[][]{
                                    {INFORMATION_ABOUT_THE_SHELTER, TAKE_A_PET_FROM_A_SHELTER},
                                    {PET_REPORT, CALL_A_VOLUNTEER}
                            }).resizeKeyboard(true));
                    telegramBot.execute(sendMessage);
                    break;
                }
                case INFORMATION_ABOUT_THE_SHELTER: {
                    sendMessage(chatId, "Здесь будет информация о приюте");
                    break;
                }
                case TAKE_A_PET_FROM_A_SHELTER: {
                    sendMessage(chatId, "Здесь будет информация как взять питомца из приюта");
                    break;
                }
                case PET_REPORT: {
                    sendMessage(chatId, "Здесь будет информация о том, как прислать отчет о питомце");
                    break;
                }
                case CALL_A_VOLUNTEER: {
                    sendMessage(chatId, "Здесь будет информация о том, как позвать волонтера");
                    break;
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Метод, который сохраняет пользователя в базу данных
     *
     * @param name   Имя пользователя
     * @param userId Идентификатор пользователя
     */
    private void saveUser(String name, long userId) {
        User user = new User();
        user.setUserName(name);
        user.setUserId(userId);
        userRepository.save(user);
    }
    private static final String infoAboutReport = "Для отчета нужна следующая информация:\n" +
            "- Фото животного.  \n" +
            "- Рацион животного\n" +
            "- Общее самочувствие и привыкание к новому месту\n" +
            "- Изменение в поведении: отказ от старых привычек, приобретение новых.\nСкопируйте следующий пример. Не забудьте прикрепить фото";

    private static final String reportExample = "Рацион: ваш текст;\n" +
            "Самочувствие: ваш текст;\n" +
            "Поведение: ваш текст;";

    private static final String REGEX_MESSAGE = "(Рацион:)(\\s)(\\W+)(;)\n" +
            "(Самочувствие:)(\\s)(\\W+)(;)\n" +
            "(Поведение:)(\\s)(\\W+)(;)";

    private static final long telegramChatVolunteers = -748879962L;

    private long daysOfReports;

    @Autowired
    private ReportDataRepository reportRepository;
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ReportDataService reportDataService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
        userRepository = null;
    }
    //отправка сообщений в ТГ Бот
    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }

    public void getReport(Update update) {
        Pattern pattern = Pattern.compile(REGEXE_MESSAG);
        Matcher matcher = pattern.matcher(update.message().caption());
        if (matcher.matches()) {
            String ration = matcher.group(3);
            String health = matcher.group(7);
            String habits = matcher.group(11);

            GetFile getFileRequest = new GetFile(update.message().photo()[1].fileId());
            GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
            try {
                File file = getFileResponse.file();
                file.fileSize();
//                String fullPath = telegramBot.getFullFilePath(file);
                String fullPathPhoto = file.filePath();

                long timeDate = update.message().date();
                Date dateSendMessage = new Date(timeDate * 1000);
                byte[] fileContent = telegramBot.getFileContent(file);
                reportDataService.uploadReportData(update.message().chat().id(), fileContent, file,
                        ration, health, dateSendMessage, timeDate, daysOfReports);

                telegramBot.execute(new SendMessage(update.message().chat().id(), "Отчет успешно принят"));

                System.out.println("Отчет успешно принят от: " + update.message().chat().id());
            } catch (IOException e) {
                System.out.println("Ошибка загрузки фото");
            }
        } else {
            GetFile getFileRequest = new GetFile(update.message().photo()[1].fileId());
            GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
            try {
                File file = getFileResponse.file();
                file.fileSize();
                String fullPathPhoto = file.filePath();

                long timeDate = update.message().date();
                Date dateSendMessage = new Date(timeDate * 1000);
                byte[] fileContent = telegramBot.getFileContent(file);
                reportDataService.uploadReportData(update.message().chat().id(), fileContent, file, update.message().caption(),
                        fullPathPhoto, dateSendMessage, timeDate, daysOfReports);

                telegramBot.execute(new SendMessage(update.message().chat().id(), "Отчет успешно принят"));
                System.out.println("Отчет успешно принят от: " + update.message().chat().id());
            } catch (IOException e) {
                System.out.println("Ошибка загрузки фото");
            }

        }
    }
}
