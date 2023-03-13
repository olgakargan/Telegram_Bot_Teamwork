package pro.sky.telegrambotteamwork.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static pro.sky.telegrambotteamwork.constants.KeyboardMessageUserConstant.*;

/**
 * Серивис-класс для генерации и формирования меню для пользователя
 */
@Service
public class MenuService {
    private final Logger logger = LoggerFactory.getLogger(MenuService.class);

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
     * Метод, генерирующий список кнопок для собак, кошек и других пиомцев
     *
     * @param listOfButton обязательный параметр всех кнопок
     * @return Возвращает сгенерированную клавиатуру
     */
    private InlineKeyboardMarkup keyboardDogAndCat(List<String> listOfButton) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton(DOG).callbackData(DOG),
                        new InlineKeyboardButton(CAT).callbackData(CAT)
                }
        );
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(ANOTHER_PET).callbackData(ANOTHER_PET));

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
    public SendMessage loadingTheMenu(Message message, String messageText, List<String> listOfButton) {
        logger.info("Вызван метод загрузки меню приветственного сообщения");
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
    public SendMessage loadingTheMenuCallbackQuery(Update update, String messageText, List<String> listOfButton) {
        logger.info("Вызван метод загрузки меню после нажатия кнопки");
        Keyboard keyboard = keyboardGeneration(listOfButton);
        return new SendMessage(update.callbackQuery().message().chat().id(), messageText).replyMarkup(keyboard);
    }

    /**
     * Этот метод загружает вложенное меню для собак и кошек
     *
     * @param update       входящее обновление
     * @param messageText  текстовое сообщение
     * @param listOfButton список кнопок
     * @return Отправленное новое сообщение от бота
     */
    public SendMessage loadingTheMenuDogAndCat(Update update, String messageText, List<String> listOfButton) {
        logger.info("Вызван метод загрузки меню для выбора питомца");
        Keyboard keyboard = keyboardDogAndCat(listOfButton);
        return new SendMessage(update.callbackQuery().message().chat().id(), messageText).replyMarkup(keyboard);
    }
}
