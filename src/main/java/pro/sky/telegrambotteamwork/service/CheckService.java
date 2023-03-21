package pro.sky.telegrambotteamwork.service;


import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Этот класс для проверки запросов от пользователя
 */
@Service
public class CheckService {
    private final Logger logger = LoggerFactory.getLogger(CheckService.class);

    /**
     * Этот метод проверяет, есть ли запрос информации от пользователя
     *
     * @param update входящее обновление
     * @return Возвращает true, если есть информация от пользователя
     */
    public boolean hasMessage(Update update) {
        logger.info("Вызван метод проверки наличия запроса от пользователя");
        return update.message() != null;
    }

    /**
     * Этот метод проверяет, есть ли запрос обратного вызова
     *
     * @param update входящее обновление
     * @return Возвращает true, если есть запрос обратного вызова
     */
    public boolean hasCallbackQuery(Update update) {
        logger.info("Вызван метод проверки наличия нажатия кнопки от пользователя");
        return update.callbackQuery() != null;
    }

    /**
     * Этот метод проверяет, есть ли запрос контактной информации пользователя
     *
     * @param update входящее обновление
     * @return Возвращает true, если есть запрос контактной информации пользователя
     */
    public boolean hasContact(Update update) {
        logger.info("Вызван метод проверки наличия запроса контактной информации от пользователя");
        return update.message().contact() != null;
    }

    /**
     * Этот метод проверяет, есть ли запрос текстовой информации пользователя
     *
     * @param update входящее обновление
     * @return Возвращает true, если есть запрос текстовой информации пользователя
     */
    public boolean hasText(Update update) {
        logger.info("Вызван метод проверки наличия текстовой информации от пользователя");
        return update.message().text() != null;
    }

    /**
     * Этот метод проверяет, есть ли сообщение от пользователя, через нажатие кнопки
     *
     * @param update входящее обновление
     * @return Возвращает true, если сообщение от пользователя, через нажатие кнопки
     */
    public boolean hasMessageCallbackQuery(Update update) {
        logger.info("Вызван метод проверки наличия запроса от пользователя после нажатия кнопки");
        return update.callbackQuery().message() != null;
    }

    /**
     * Этот метод проверяет, есть ли отправленное изображение от пользователя
     *
     * @param update входящее обновление
     * @return Возвращает true, если есть изображение от пользователя
     */
    public boolean hasPhoto(Update update) {
        logger.info("Вызван метод проверки наличия изображения от пользователя");
        return update.message().photo() != null;
    }
}
