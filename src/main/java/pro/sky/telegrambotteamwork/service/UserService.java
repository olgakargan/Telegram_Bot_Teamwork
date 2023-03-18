package pro.sky.telegrambotteamwork.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    public void saveUser(User user, Update update) {
        String firstName = update.callbackQuery().from().firstName();
        String lastName = update.callbackQuery().from().lastName();
        String userName = update.callbackQuery().from().username();
        Long userId = update.callbackQuery().message().from().id();
        Long chatId = update.callbackQuery().message().chat().id();
        LocalDateTime dateTime = LocalDateTime.now();
        Collection<User> usersUserId = userRepository.findUserByUserId(userId);
        if (!usersUserId.isEmpty()) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), YOU_ARE_SUBSCRIBED));
            return;
        } else {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserName(userName);
            user.setUserId(userId);
            user.setChatId(chatId);
            user.setDateTime(dateTime);
            user.setRole(Role.ROLE_USER);
            userRepository.save(user);
            telegramBot.execute(new SendMessage(chatId, YOU_HAVE_SUBSCRIBED));
            logger.info("Ползователь сохранен в базу данных: {}", user);
        }
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
    @CachePut(value = "users", key = "#user.id")
    public User changeUser(User user, Update update) {
        Long userId = update.message().from().id();
        LocalDateTime dateTime = LocalDateTime.now();
        Collection<User> usersUserId = userRepository.findUserByUserId(userId);
        for (User userUserId : usersUserId) {
            user.setDateTime(dateTime);
            user.setRole(Role.ROLE_VOLUNTEER);
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
    @CachePut(value = "users", key = "#user.id")
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
    @Cacheable("users")
    public User findUser(Long id) {
        logger.info("Вызван метод поиска пользователя по id {}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NullPointerException();
        }
        return user;
    }

}