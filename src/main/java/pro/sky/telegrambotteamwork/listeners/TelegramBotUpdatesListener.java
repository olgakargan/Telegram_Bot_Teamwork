package pro.sky.telegrambotteamwork.listeners;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.service.CheckService;
import pro.sky.telegrambotteamwork.service.MenuService;
import pro.sky.telegrambotteamwork.service.UserService;

import javax.annotation.PostConstruct;
import java.util.List;

import static pro.sky.telegrambotteamwork.constants.CommandMessageUserConstant.START;
import static pro.sky.telegrambotteamwork.constants.KeyboardMessageUserConstant.*;
import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.*;

/**
 * Основной класс с логикой телеграм-бота.
 * Этот класс расширяет {@link UpdatesListener}
 */
@Service
@AllArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final MenuService menuService;
    private final UserService userService;
    private final CheckService checkService;


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
        try {
            updates.forEach(update -> {
                logger.info("Запрос от пользователя: {}", update);
                Message messageUser = update.message();
                User user = new User();

                if (checkService.hasMessage(update)) {
                    if (checkService.hasText(update)) {
                        if (START.equals(messageUser.text())) {
                            telegramBot.execute(menuService.loadingTheMenu(messageUser, SUBSCRIBE_TO_BOT_MESSAGE, SUBSCRIPTION_MENU));
                        }
                    }
                } else if (checkService.hasCallbackQuery(update)) {
                    if (SUBSCRIPTION.equals(update.callbackQuery().data())) {
                        telegramBot.execute(menuService.loadingTheMenuDogAndCat(update, WELCOME_MESSAGE, CHOOSING_PET_MENU));
                    }
                }
                if (checkService.hasCallbackQuery(update)) {
                    if (DOG.equals(update.callbackQuery().data())) {
                        telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU));
                    } else {
                        mainMenuDog(update);
                    }
                }
                if (checkService.hasCallbackQuery(update)) {
                    if (CAT.equals(update.callbackQuery().data())) {
                        telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU));
                    } else {
                        mainMenuCat(update);
                    }
                }
                if (checkService.hasCallbackQuery(update)) {
                    if (ANOTHER_PET.equals(update.callbackQuery().data())) {
                        telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Информация о том, каких питомцев еще можно взять"));
                    }
                }
                if (checkService.hasCallbackQuery(update)) {
                    informationMenu(update);
                    takeDogMenu(update);
                    takeCatMenu(update);
                    petReportMenu(update);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Этот метод выводит главное меню для собак
     *
     * @param update входящее обновление
     */
    private void mainMenuDog(Update update) {
        if (INFORMATION_ABOUT_THE_SHELTER_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, INFORMATION_ABOUT_THE_SHELTER_MESSAGE, INFORMATION_MENU));
        } else if (TAKE_A_FROM_A_SHELTER_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, TAKE_A_PET_FROM_A_SHELTER_MESSAGE, TAKE_A_FROM_A_SHELTER_DOG_MENU));
        } else if (PET_REPORT_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, PET_REPORT_MESSAGE, PET_REPORT_MENU));
        } else if (CALL_A_VOLUNTEER_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CALL_A_VOLUNTEER_MESSAGE, CALL_A_VOLUNTEER_MENU));
        }
    }

    /**
     * Этот метод выводит главное меню для кошек
     *
     * @param update входящее обновление
     */
    private void mainMenuCat(Update update) {
        if (INFORMATION_ABOUT_THE_SHELTER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, INFORMATION_ABOUT_THE_SHELTER_MESSAGE, INFORMATION_MENU));
        } else if (TAKE_A_FROM_A_SHELTER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, TAKE_A_PET_FROM_A_SHELTER_MESSAGE, TAKE_A_FROM_A_SHELTER_CAT_MENU));
        } else if (PET_REPORT_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, PET_REPORT_MESSAGE, PET_REPORT_MENU));
        } else if (CALL_A_VOLUNTEER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CALL_A_VOLUNTEER_MESSAGE, CALL_A_VOLUNTEER_MENU));
        }
    }

    /**
     * Этот метод выводит информационное меню о питомце
     *
     * @param update входящее обновление
     */
    private void informationMenu(Update update) {
        if (ABOUT_OUR_NURSERY.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), ABOUT_OUR_NURSERY_MESSAGE));
        } else if (AMBULANCE_FOR_ANIMALS.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), AMBULANCE_FOR_ANIMALS_MESSAGE));
        } else if (INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_MESSAGE));
        } else if (REHABILITATION_FOR_SPECIAL_ANIMALS.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), REHABILITATION_FOR_SPECIAL_ANIMALS_MESSAGE));
        } else if (REQUISITES.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), REQUISITES_MESSAGE));
        } else if (CONTACTS.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), CONTACTS_MESSAGE));
        }
    }

    /**
     * Этот метод выводит меню о том, как взять собаку из приюта
     *
     * @param update входящее обновление
     */
    private void takeDogMenu(Update update) {
        if (ARE_DOGS_IN_SHELTER_HEALTHY.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), ARE_PET_IN_SHELTER_HEALTHY_MESSAGE));
        } else if (YOU_DECIDED_TO_TAKE_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), YOU_DECIDED_TO_TAKE_PET_MESSAGE));
        } else if (IF_YOU_ALREADY_HAVE_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), IF_YOU_ALREADY_HAVE_PET_MESSAGE));
        } else if (DOG_TRANSFER_PROCEDURE.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), PET_TRANSFER_PROCEDURE_MESSAGE));
        } else if (DOG_CATALOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Каталог собак"));
        }
    }

    /**
     * Этот метод выводит меню о том, как взять кошку из приюта
     *
     * @param update входящее обновление
     */
    private void takeCatMenu(Update update) {
        if (ARE_CATS_IN_SHELTER_HEALTHY.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), ARE_PET_IN_SHELTER_HEALTHY_MESSAGE));
        } else if (YOU_DECIDED_TO_TAKE_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), YOU_DECIDED_TO_TAKE_PET_MESSAGE));
        } else if (IF_YOU_ALREADY_HAVE_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), IF_YOU_ALREADY_HAVE_PET_MESSAGE));
        } else if (CAT_TRANSFER_PROCEDURE.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), PET_TRANSFER_PROCEDURE_MESSAGE));
        } else if (CAT_CATALOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Каталог кошек"));
        }
    }

    /**
     * Этот метод выводит информацию о том, как присылать отчет о питомце и кнопка с отправкой отчета
     *
     * @param update входящее обновление
     */
    private void petReportMenu(Update update) {
        if (INFORMATION_ABOUT_REPORT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), INFORMATION_ABOUT_REPORT_MESSAGE));
        } else if (SEND_REPORT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Прислать отчет о питомце"));
        }
    }

    /**
     * Метод выводит сообщения пользователю
     *
     * @param chatId идентификатор чата
     * @param text   текстовое сообщение
     */
    public void sendMessage(Long chatId, String text) {
        SendResponse response = telegramBot.execute(new SendMessage(chatId, text));
    }

}