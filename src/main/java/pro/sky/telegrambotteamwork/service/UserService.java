package pro.sky.telegrambotteamwork.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.enums.Role;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.*;

/**
 * Серивис-класс для всех пользователей ботом
 */
@Service
@AllArgsConstructor
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final TelegramBot telegramBot;
    private final UserRepository userRepository;

    /**
     * Метод, который сохраняет пользователя в базу данных
     *
     * @param user   сущность пользователя ботом
     * @param update входящее обновление
     * @return Возвращает сохраненного пользователя
     */
    public User saveUser(User user, Update update) {
        if (update.message().contact() != null) {
            String firstName = update.message().contact().firstName();
            String lastName = update.message().contact().lastName();
            String userName = update.message().chat().username();
            String phone = update.message().contact().phoneNumber();
            Long userId = update.message().from().id();
            Long chatId = update.message().chat().id();
            LocalDateTime dateTime = LocalDateTime.now();
            Collection<User> usersUserId = userRepository.findUserByUserId(userId);

            for (User userUserId : usersUserId) {
                telegramBot.execute(new SendMessage(userUserId.getChatId(), YOU_ARE_SUBSCRIBED));
            }

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserName(userName);
            user.setPhone(phone);
            user.setUserId(userId);
            user.setDateTime(dateTime);
            user.getRoles().add(Role.ROLE_USER);
            userRepository.save(user);
            telegramBot.execute(new SendMessage(chatId, YOU_HAVE_SUBSCRIBED));
            logger.info("Ползователь сохранен в базу данных: {}", user);
        }
        return userRepository.save(user);
    }

    /**
     * Метод, добавляющий пользователя в базу данных
     *
     * @param user сущность пользователя ботом
     * @return Возвращает сохраненного пользователя
     */
    public User addUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Метод, который изменяет статус пользователя на волонтера
     *
     * @param user   сущность пользователя ботом
     * @param update входящее обновление
     * @return Возвращает измененного пользователя
     */
    public User changeUser(User user, Update update) {
        Long userId = update.message().from().id();
        LocalDateTime dateTime = LocalDateTime.now();
        Collection<User> usersUserId = userRepository.findUserByUserId(userId);
        for (User userUserId : usersUserId) {
            user.setDateTime(dateTime);
            user.getRoles().add(Role.ROLE_VOLUNTEER);
            userRepository.save(user);
            telegramBot.execute(new SendMessage(userUserId.getChatId(), ARE_YOU_VOLUNTEER));
            logger.info("Пользователь переименован на волонтера: {}", user);
        }
        logger.info("Такого пользователя не существует");
        return user;
    }

    /**
     * Метод, который редактируект пользователя
     *
     * @param user сущность пользователя ботом
     * @return Возвращает измененного пользователя
     */
    public User updateUser(User user) {
        logger.info("Вызван метод редактирования пользователя: {}", user);
        if (userRepository.findById(user.getId()).orElse(null) == null) {
            return null;
        }
        return userRepository.save(user);
    }

    /**
     * Метод поиска пользователя в базе данных
     *
     * @param id идентификатор искомого пользователя
     * @return Возвращает найденного пользователя
     */
    public User findUser(Long id) {
        logger.info("Вызван метод поиска пользователя по id {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NullPointerException();
        }
        return user;
    }

}
