package pro.sky.telegrambotteamwork.listeners;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.enums.Role;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.UserRepository;
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
        try {
            updates.forEach(update -> {
                logger.info("Запрос от пользователя: {}", update);
                Message messageUser = update.message();
                User user = new User();

                if (checkService.hasMessage(update) && checkService.hasText(update)) {
                    if (START.equals(messageUser.text())) {
                        telegramBot.execute(menuService.loadingTheMenu(messageUser, SUBSCRIBE_TO_BOT_MESSAGE, SUBSCRIPTION_MENU));
                    }
                } else if (checkService.hasCallbackQuery(update)) {
                    if (SUBSCRIPTION.equals(update.callbackQuery().data()) && user.getRole().equals(Role.ROLE_USER)) {
                        logger.info("Пользователь юзер!");
                        userService.saveUser(user, update);
                        telegramBot.execute(menuService.loadingTheMenuDogAndCat(update, WELCOME_MESSAGE, CHOOSING_PET_MENU));
                    } else if (DOG.equals(update.callbackQuery().data())) {
                        telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU));
                    } else if (CAT.equals(update.callbackQuery().data())) {
                        telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU));
                    } else if (ANOTHER_PET.equals(update.callbackQuery().data())) {
                        telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Информация о том, каких питомцев еще можно взять"));
                    } else {
                        mainMenuDog(update);
                        informationMenuDog(update);
                        takeDogMenu(update);
                        dogReportMenu(update);
                        callVolunteerDogMenu(update);
                        mainMenuCat(update);
                        informationMenuCat(update);
                        takeCatMenu(update);
                        catReportMenu(update);
                        callVolunteerCatMenu(update);
                    }
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
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, INFORMATION_ABOUT_THE_SHELTER_MESSAGE, INFORMATION_MENU_DOG));
        } else if (TAKE_A_FROM_A_SHELTER_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, TAKE_A_PET_FROM_A_SHELTER_MESSAGE, TAKE_A_FROM_A_SHELTER_DOG_MENU));
        } else if (PET_REPORT_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, PET_REPORT_MESSAGE, DOG_REPORT_MENU));
        } else if (CALL_A_VOLUNTEER_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CALL_A_VOLUNTEER_MESSAGE, CALL_A_VOLUNTEER_DOG_MENU));
        } else if (GO_BACK_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuDogAndCat(update, WELCOME_MESSAGE, CHOOSING_PET_MENU));
        }
    }

    /**
     * Этот метод выводит главное меню для кошек
     *
     * @param update входящее обновление
     */
    private void mainMenuCat(Update update) {
        if (INFORMATION_ABOUT_THE_SHELTER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, INFORMATION_ABOUT_THE_SHELTER_MESSAGE, INFORMATION_MENU_CAT));
        } else if (TAKE_A_FROM_A_SHELTER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, TAKE_A_PET_FROM_A_SHELTER_MESSAGE, TAKE_A_FROM_A_SHELTER_CAT_MENU));
        } else if (PET_REPORT_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, PET_REPORT_MESSAGE, CAT_REPORT_MENU));
        } else if (CALL_A_VOLUNTEER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CALL_A_VOLUNTEER_MESSAGE, CALL_A_VOLUNTEER_CAT_MENU));
        } else if (GO_BACK_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuDogAndCat(update, WELCOME_MESSAGE, CHOOSING_PET_MENU));
        }
    }

    /**
     * Этот метод выводит информационное меню о собаке
     *
     * @param update входящее обновление
     */
    private void informationMenuDog(Update update) {
        if (ABOUT_OUR_NURSERY_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), ABOUT_OUR_NURSERY_MESSAGE));
        } else if (AMBULANCE_FOR_ANIMALS_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), AMBULANCE_FOR_ANIMALS_MESSAGE));
        } else if (INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_MESSAGE));
        } else if (REHABILITATION_FOR_SPECIAL_ANIMALS_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), REHABILITATION_FOR_SPECIAL_ANIMALS_MESSAGE));
        } else if (SAFETY_PRECAUTIONS_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Техника безопасности для собак"));
        } else if (REQUISITES_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), REQUISITES_MESSAGE));
        } else if (CONTACTS_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), CONTACTS_MESSAGE));
        } else if (SECURITY_CONTACTS_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Контакты охраны для собак"));
        } else if (GO_BACK_INFORMATION_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU));
        }
    }

    /**
     * Этот метод выводит информационное меню о кошке
     *
     * @param update входящее обновление
     */
    private void informationMenuCat(Update update) {
        if (ABOUT_OUR_NURSERY_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), ABOUT_OUR_NURSERY_MESSAGE));
        } else if (AMBULANCE_FOR_ANIMALS_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), AMBULANCE_FOR_ANIMALS_MESSAGE));
        } else if (INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_MESSAGE));
        } else if (REHABILITATION_FOR_SPECIAL_ANIMALS_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), REHABILITATION_FOR_SPECIAL_ANIMALS_MESSAGE));
        } else if (SAFETY_PRECAUTIONS_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Техника безопасности для кошек"));
        } else if (REQUISITES_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), REQUISITES_MESSAGE));
        } else if (CONTACTS_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), CONTACTS_MESSAGE));
        } else if (SECURITY_CONTACTS_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Контакты охраны для кошек"));
        } else if (GO_BACK_INFORMATION_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU));
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
        } else if (GO_BACK_TAKE_A_FROM_A_SHELTER_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU));
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
        } else if (GO_BACK_TAKE_A_FROM_A_SHELTER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU));
        }
    }

    /**
     * Этот метод выводит информацию о том, как присылать отчет о собаке и кнопка с отправкой отчета
     *
     * @param update входящее обновление
     */
    private void dogReportMenu(Update update) {
        if (INFORMATION_ABOUT_REPORT_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), INFORMATION_ABOUT_REPORT_MESSAGE));
        } else if (SEND_REPORT_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Прислать отчет о собаке"));
        } else if (GO_BACK_PET_REPORT_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU));
        }
    }

    /**
     * Этот метод выводит информацию о том, как присылать отчет о кошке и кнопка с отправкой отчета
     *
     * @param update входящее обновление
     */
    private void catReportMenu(Update update) {
        if (INFORMATION_ABOUT_REPORT_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), INFORMATION_ABOUT_REPORT_MESSAGE));
        } else if (SEND_REPORT_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Прислать отчет о кошке"));
        } else if (GO_BACK_PET_REPORT_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU));
        }
    }

    /**
     * Этот метод даёт обратную свзь с волонтером по собакам и отправкой запроса на получение статуса волонтера
     *
     * @param update входящее обновление
     */
    private void callVolunteerDogMenu(Update update) {
        if (QUESTION_TO_VOLUNTEER_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Вопрос волонтеру о собаке"));
        } else if (BECOME_A_VOLUNTEER_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Стать волонтером по собакам"));
        } else if (GO_BACK_CALL_A_VOLUNTEER_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU));
        }
    }

    /**
     * Этот метод даёт обратную свзь с волонтером по кошкам и отправкой запроса на получение статуса волонтера
     *
     * @param update входящее обновление
     */
    private void callVolunteerCatMenu(Update update) {
        if (QUESTION_TO_VOLUNTEER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Вопрос волонтеру о кошке"));
        } else if (BECOME_A_VOLUNTEER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Стать волонтером по кошкам"));
        } else if (GO_BACK_CALL_A_VOLUNTEER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU));
        }
    }

}