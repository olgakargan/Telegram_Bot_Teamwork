package pro.sky.telegrambotteamwork.listeners;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.enums.Role;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.UserRepository;
import pro.sky.telegrambotteamwork.service.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static pro.sky.telegrambotteamwork.constants.CommandMessageUserConstant.*;
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
    private final static File address = new File("images/adress.jpg");
    private final TelegramBot telegramBot;
    private final MenuService menuService;
    private final UserService userService;
    private final CheckService checkService;
    private final UserRepository userRepository;
    private final DogService dogService;
    private final CatService catService;
    private final ReportDataService reportDataService;
    private final ImageService imageService;

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
                Collection<User> rolesUser = userRepository.findUserByRole(Role.ROLE_USER);
                Collection<User> rolesVolunteer = userRepository.findUserByRole(Role.ROLE_VOLUNTEER);

                if (rolesUser.isEmpty() && rolesVolunteer.isEmpty()) {
                    if (checkService.hasMessage(update) && checkService.hasText(update)) {
                        if (START.equals(messageUser.text())) {
                            telegramBot.execute(menuService.loadingTheMenuSubscribe(update, SUBSCRIBE_TO_BOT_MESSAGE));
                        }
                    } else if (checkService.hasContact(update)) {
                        userService.saveUser(user, update);
                    }
                } else if (!rolesUser.isEmpty()) {
                    if (checkService.hasMessage(update) && checkService.hasText(update)) {
                        if (START.equals(messageUser.text())) {
                            telegramBot.execute(menuService.loadingTheMenuDogAndCat(update, WELCOME_MESSAGE, CHOOSING_PET_MENU));
                        } else {
                            addReportDataMenu(update);
                        }
                    } else if (checkService.hasCallbackQuery(update)) {
                        if (DOG.equals(update.callbackQuery().data())) {
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
                    } else if (checkService.hasPhoto(update) && checkService.hasMessage(update) && checkService.hasCaption(update)) {
                        try {
                            imageService.saveImageReportData(update);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (!rolesVolunteer.isEmpty()) {
                    if (checkService.hasMessage(update) && checkService.hasText(update)) {
                        if (START.equals(messageUser.text())) {
                            telegramBot.execute(menuService.loadingTheMenu(messageUser, WELCOME_VOLUNTEER_MESSAGE, MAIN_VOLUNTEER_MENU));
                        } else {
                            addDogMenu(update);
                            addCatMenu(update);
                        }
                    } else if (checkService.hasCallbackQuery(update)) {
                        if (INFORMATION_FOR_VOLUNTEER.equals(update.callbackQuery().data())) {
                            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, INFORMATION_WELCOME_MESSAGE, INFORMATION_FOR_VOLUNTEER_MENU));
                        } else if (ADD_A_PET.equals(update.callbackQuery().data())) {
                            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, ADD_A_PET_MESSAGE, ADD_A_PET_MENU));
                        } else if (REPORTS_OF_ADOPTIVE_PARENTS.equals(update.callbackQuery().data())) {
                            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, REPORTS_OF_ADOPTIVE_PARENTS_MESSAGE, REPORTS_OF_ADOPTIVE_PARENTS_MENU));
                        } else if (MAKE_A_VOLUNTEER.equals(update.callbackQuery().data())) {
                            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, MAKE_A_VOLUNTEER_MESSAGE, MAKE_A_VOLUNTEER_MENU));
                        } else {
                            informationMenuVolunteer(update);
                            addPetMenuVolunteer(update);
                        }
                    } else if (checkService.hasPhoto(update) && checkService.hasMessage(update) && checkService.hasCaption(update)) {
                        try {
                            imageService.saveImageDog(update);
                            imageService.saveImageCat(update);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
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
            telegramBot.execute(menuService.loadingTheMenuDogAndCatCallbackQuery(update, WELCOME_MESSAGE, CHOOSING_PET_MENU));
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
            telegramBot.execute(menuService.loadingTheMenuDogAndCatCallbackQuery(update, WELCOME_MESSAGE, CHOOSING_PET_MENU));
        }
    }

    /**
     * Этот метод выводит информацию о собаках для пользователей по нажатии на кнопку
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
            telegramBot.execute(new SendPhoto(update.callbackQuery().message().chat().id(), address));
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), CONTACTS_MESSAGE));
        } else if (SECURITY_CONTACTS_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Контакты охраны для собак"));
        } else if (GO_BACK_INFORMATION_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU));
        }
    }

    /**
     * Этот метод выводит информацию о кошках для пользователей по нажатии на кнопку
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
            telegramBot.execute(new SendPhoto(update.callbackQuery().message().chat().id(), address));
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), CONTACTS_MESSAGE));
        } else if (SECURITY_CONTACTS_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), "Контакты охраны для кошек"));
        } else if (GO_BACK_INFORMATION_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU));
        }
    }

    /**
     * Этот метод выводит информацию о том, как взять собаку из приюта
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
     * Этот метод выводит информацию о том, как взять кошку из приюта
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
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), ADD_REPORT_DATA_PREVIEW_MESSAGE));
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
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), ADD_REPORT_DATA_PREVIEW_MESSAGE));
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
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), QUESTION_TO_VOLUNTEER_MESSAGE));
        } else if (BECOME_A_VOLUNTEER_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), BECOME_A_VOLUNTEER));
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
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), QUESTION_TO_VOLUNTEER_MESSAGE));
        } else if (BECOME_A_VOLUNTEER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), BECOME_A_VOLUNTEER));
        } else if (GO_BACK_CALL_A_VOLUNTEER_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(menuService.loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU));
        }
    }

    /**
     * Этот метод выводит информацию для волонтеров по нажатию кнопки
     *
     * @param update входящее обновление
     */
    private void informationMenuVolunteer(Update update) {
        if (MEMO_FOR_A_VOLUNTEER.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), MEMO_FOR_A_VOLUNTEER_MESSAGE));
        } else if (DUTIES_OF_VOLUNTEERS.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), DUTIES_OF_VOLUNTEERS_MESSAGE));
        } else if (FORM_OF_CLOTHING.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), FORM_OF_CLOTHING_MESSAGE));
        } else if (VOLUNTEER_TRIPS.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), VOLUNTEER_TRIPS_MESSAGE));
        }
    }

    /**
     * Этот метод выводит информацию о том, как добавить питомцев в базу данных
     *
     * @param update входящее обновление
     */
    private void addPetMenuVolunteer(Update update) {
        if (ADD_DOG.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), ADD_DOG_PREVIEW_MESSAGE));
        } else if (ADD_CAT.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), ADD_CAT_PREVIEW_MESSAGE));
        } else if (ADD_PET.equals(update.callbackQuery().data())) {
            telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), ADD_PET_MESSAGE));
        }
    }

    /**
     * Этот метод добавляет собаку и сохраняет ее в базу данных
     *
     * @param update входящее обновление
     */
    private void addDogMenu(Update update) {
        if (ADD_DOG_COMMAND.equals(update.message().text())) {
            telegramBot.execute(new SendMessage(update.message().chat().id(), ADD_DOG_PREVIEW_2_MESSAGE));
        } else if (ADD_DOG_BD_COMMAND.equals(update.message().text())) {
            telegramBot.execute(new SendMessage(update.message().chat().id(), ADD_DOG_MESSAGE));
        } else {
            dogService.saveDog(update);
        }
    }

    /**
     * Этот метод добавляет кошку и сохраняет ее в базу данных
     *
     * @param update входящее обновление
     */
    private void addCatMenu(Update update) {
        if (ADD_CAT_COMMAND.equals(update.message().text())) {
            telegramBot.execute(new SendMessage(update.message().chat().id(), ADD_CAT_PREVIEW_2_MESSAGE));
        } else if (ADD_CAT_BD_COMMAND.equals(update.message().text())) {
            telegramBot.execute(new SendMessage(update.message().chat().id(), ADD_CAT_MESSAGE));
        } else {
            catService.saveCat(update);
        }
    }

    /**
     * Этот метод добавляет отчет от пользователя и сохраняет его в базу данных
     *
     * @param update входящее обновление
     */
    private void addReportDataMenu(Update update) {
        if (ADD_REPORT_DATA_COMMAND.equals(update.message().text())) {
            telegramBot.execute(new SendMessage(update.message().chat().id(), ADD_REPORT_DATA_PREVIEW_2_MESSAGE));
        } else if (ADD_REPORT_DATA_BD_COMMAND.equals(update.message().text())) {
            telegramBot.execute(new SendMessage(update.message().chat().id(), ADD_REPORT_DATA_MESSAGE));
        } else {
            reportDataService.saveReportData(update, update.message().text());
        }
    }

}