package pro.sky.telegrambotteamwork.listeners;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;

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
//            String phoneNumber = update.message().contact().phoneNumber();

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

    public void sendMessage(long chatId, String message) {
        SendResponse sendResponse = telegramBot.execute(new SendMessage(chatId, message));
    }
}