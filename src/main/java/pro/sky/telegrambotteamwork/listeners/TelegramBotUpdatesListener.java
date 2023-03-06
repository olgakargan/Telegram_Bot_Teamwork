package pro.sky.telegrambotteamwork.listeners;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
<<<<<<< HEAD
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
=======
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
>>>>>>> origin/dev
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.model.User;
<<<<<<< HEAD
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
=======
import pro.sky.telegrambotteamwork.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
>>>>>>> origin/dev

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
<<<<<<< HEAD
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
=======
            Message messageUser = update.message();

            if (hasMessage(update)) {
                if (START.equals(messageUser.text())) {
                    telegramBot.execute(loadingTheMenu(messageUser, "Здравствуйте " + messageUser.chat().username() + "! " + WELCOME_MESSAGE, MAIN_MENU));
>>>>>>> origin/dev
                }
            } else if (hasCallbackQuery(update)) {
                if (INFORMATION_ABOUT_THE_SHELTER.equals(update.callbackQuery().data())) {
                    telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Здесь будет информация о приюте"));
                    telegramBot.execute(loadingTheMenuCallbackQuery(update, "Подробная информация о нашем приюте для бездомных животных. Выберите, нужную для вас информацию.", INFORMATION_MENU));
                } else if (TAKE_A_PET_FROM_A_SHELTER.equals(update.callbackQuery().data())) {
                    telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Здесь будет информация как взять питомца из приюта"));
                } else if (PET_REPORT.equals(update.callbackQuery().data())) {
                    telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Здесь будет информация о том, как прислать отчет о питомце"));
                } else if (CALL_A_VOLUNTEER.equals(update.callbackQuery().data())) {
                    telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Здесь будет информация о том, как позвать волонтера"));
                } else if (SUBSCRIBE.equals(update.callbackQuery().data())) {
                    if (hasMessage(update) && hasContact(update)) {
                        saveUser(update);
                    }
                }
            } else if (hasMessage(update) && hasCallbackQuery(update)) {
                if (ABOUT_OUR_NURSERY.equals(update.callbackQuery().data())) {
                    telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), ABOUT_OUR_NURSERY_DETAILED));
                } else if (AMBULANCE_FOR_ANIMALS.equals(update.callbackQuery().data())) {
                    telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), AMBULANCE_FOR_ANIMALS_DETAILS));
                } else if (INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE.equals(update.callbackQuery().data())) {
                    telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_DETAILS));
                } else if (REHABILITATION_FOR_SPECIAL_ANIMALS.equals(update.callbackQuery().data())) {
                    telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), REHABILITATION_FOR_SPECIAL_ANIMALS_DETAILS));
                } else if (REQUISITES.equals(update.callbackQuery().data())) {
                    telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), REQUISITES_DETAILS));
                } else if (CONTACTS.equals(update.callbackQuery().data())) {
                    telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), CONTACTS_DETAILS));
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Метод, который сохраняет пользователя в базу данных
     *
     * @param update входящее обновление
     */
    private void saveUser(Update update) {
        if (update.message().contact() != null) {
            String firstName = update.message().contact().firstName();
            String lastName = update.message().contact().lastName();
            String userName = update.message().chat().username();
            String phone = update.message().contact().phoneNumber();
            Long userId = update.message().from().id();
            Long chatId = update.message().chat().id();
            LocalDateTime dateTime = LocalDateTime.now();
            List<User> usersIds = userRepository.findAll().stream().filter(id -> id.getUserId().equals(userId)).collect(Collectors.toList());
            if (!(usersIds.equals(userId))) {
                telegramBot.execute(new SendMessage(chatId, "Вы подписаны на нашего бота"));
                return;
            }
            if (usersIds.equals(userId)) {
                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setUserName(userName);
                user.setPhone(phone);
                user.setUserId(userId);
                user.setDateTime(dateTime);
                userRepository.save(user);
                telegramBot.execute(new SendMessage(chatId, "Вы только что подписались на нашего бота! Поздравляем!"));
                logger.info("Ползователь сохранен в базе данных: {}", user);
            }
        }
    }

    /**
     * Метод, генерирующий список кнопок
     *
     * @param listOfButton обязательный параметр всех кнопок
     * @return Возвращает сгенерированную клавиатуру
     */
    private InlineKeyboardMarkup keyboardGeneration(List<String> listOfButton) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for (String list : listOfButton) {
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(list).callbackData(list));
        }

        return inlineKeyboardMarkup;
    }

    /**
     * Этот метод загружает меню под текстовое сообщение
     *
     * @param message      вся необходимая информация о пользователе, написавшем сообщение боту
     * @param messageText  текстовое сообщение
     * @param listOfButton список кнопок
     * @return Отправленное новое сообщение от бота
     */
    private SendMessage loadingTheMenu(Message message, String messageText, List<String> listOfButton) {
        Keyboard keyboard = keyboardGeneration(listOfButton);
        return new SendMessage(message.chat().id(), messageText).replyMarkup(keyboard);
    }

    /**
     * Этот метод загружает вложенное меню
     *
     * @param update       входящее обновление
     * @param messageText  текстовое сообщение
     * @param listOfButton список кнопок
     * @return Отправленное новое сообщение от бота
     */
    private SendMessage loadingTheMenuCallbackQuery(Update update, String messageText, List<String> listOfButton) {
        Keyboard keyboard = keyboardGeneration(listOfButton);
        return new SendMessage(update.callbackQuery().message().chat().id(), messageText).replyMarkup(keyboard);
    }
    private static final String infoAboutReport = "Для отчета нужна следующая информация:\n" +
            "- Фото животного.  \n" +
            "- Рацион животного\n" +
            "- Общее самочувствие и привыкание к новому месту\n" +
            "- Изменение в поведении: отказ от старых привычек, приобретение новых.\nСкопируйте следующий пример. Не забудьте прикрепить фото";

<<<<<<< HEAD
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
=======
    /**
     * Этот метод проверяет есть ли запрос информации от пользователя
     *
     * @param update входящее обновление
     * @return Возвращает true, если есть информация от пользователя
     */
    private boolean hasMessage(Update update) {
        return update.message() != null;
    }

    /**
     * Этот метод проверяет есть ли запрос обратного вызова
     *
     * @param update входящее обновление
     * @return Возвращает true, если есть запрос обратного вызова
     */
    private boolean hasCallbackQuery(Update update) {
        return update.callbackQuery() != null;
    }

    /**
     * Этот метод проверяет есть ли запрос контактной информации пользователя
     *
     * @param update входящее обновление
     * @return Возвращает true, если есть запрос контактной информации пользователя
     */
    private boolean hasContact(Update update) {
        return update.callbackQuery().message().contact() != null;
    }

    /**
     * Этот метод проверяет есть ли запрос текстовой информации пользователя
     *
     * @param update входящее обновление
     * @return Возвращает true, если есть запрос текстовой информации пользователя
     */
    private boolean hasText(Update update) {
        return update.message().text() != null;
    }

}
>>>>>>> origin/dev
