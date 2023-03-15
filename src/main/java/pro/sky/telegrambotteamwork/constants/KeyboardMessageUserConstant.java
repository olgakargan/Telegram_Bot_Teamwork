package pro.sky.telegrambotteamwork.constants;

import java.util.List;

public class KeyboardMessageUserConstant {
    // Кнопка подписаться. Первое общение с ботом
    public static final String SUBSCRIPTION = "Подписаться";
    // Кнопки выбора питомца
    public static final String DOG = "\uD83D\uDC36";
    public static final String CAT = "\uD83D\uDC31";
    public static final String ANOTHER_PET = "Выбрать другого питомца";
    // Кнопки, после выбора питомца
    public static final String INFORMATION_ABOUT_THE_SHELTER_DOG = "Узнать информацию о приюте собак";
    public static final String TAKE_A_FROM_A_SHELTER_DOG = "Как взять собаку из приюта";
    public static final String PET_REPORT_DOG = "Прислать отчет о собаке";
    public static final String CALL_A_VOLUNTEER_DOG = "Позвать волонтера для собаки";
    public static final String GO_BACK_DOG = "Вернутьcя назад";
    public static final String INFORMATION_ABOUT_THE_SHELTER_CAT = "Узнать информацию о приюте кошек";
    public static final String TAKE_A_FROM_A_SHELTER_CAT = "Как взять кошку из приюта";
    public static final String PET_REPORT_CAT = "Прислать отчет о кошке";
    public static final String CALL_A_VOLUNTEER_CAT = "Позвать волонтера для кошки";
    public static final String GO_BACK_CAT = "Вернуться назад";
    // Кнопки об информации о питомнике
    public static final String ABOUT_OUR_NURSERY_DOG = "О нашем питомнике";
    public static final String AMBULANCE_FOR_ANIMALS_DOG = "Скорая помощь для животных";
    public static final String INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_DOG = "Инструкция помощи";
    public static final String REHABILITATION_FOR_SPECIAL_ANIMALS_DOG = "Реабилитация для питомца";
    public static final String SAFETY_PRECAUTIONS_DOG = "Техника безопасности";
    public static final String REQUISITES_DOG = "Реквизиты";
    public static final String CONTACTS_DOG = "Контакты приюта";
    public static final String SECURITY_CONTACTS_DOG = "Контакты охраны";
    public static final String GO_BACK_INFORMATION_DOG = "Вeрнуться назад";
    public static final String ABOUT_OUR_NURSERY_CAT = "O нашем питомнике";
    public static final String AMBULANCE_FOR_ANIMALS_CAT = "Скoрая помощь для животных";
    public static final String INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_CAT = "Инструкция пoмощи";
    public static final String REHABILITATION_FOR_SPECIAL_ANIMALS_CAT = "Реабилитация для питoмца";
    public static final String SAFETY_PRECAUTIONS_CAT = "Техника безопаснoсти";
    public static final String REQUISITES_CAT = "Рeквизиты";
    public static final String CONTACTS_CAT = "Кoнтакты приюта";
    public static final String SECURITY_CONTACTS_CAT = "Кoнтакты охраны";
    public static final String GO_BACK_INFORMATION_CAT = "Вернуться нaзад";
    // Кнопки о том, как взять питомца из приюта
    public static final String ARE_DOGS_IN_SHELTER_HEALTHY = "Здоровье собаки в питомнике";
    public static final String ARE_CATS_IN_SHELTER_HEALTHY = "Здоровье кошки в питомнике";
    public static final String YOU_DECIDED_TO_TAKE_DOG = "Вы решились взять собаку?";
    public static final String YOU_DECIDED_TO_TAKE_CAT = "Вы решились взять кошку?";
    public static final String IF_YOU_ALREADY_HAVE_DOG = "Если у вас уже есть собака";
    public static final String IF_YOU_ALREADY_HAVE_CAT = "Если у вас уже есть кошка";
    public static final String DOG_TRANSFER_PROCEDURE = "Процедура передачи собаки";
    public static final String CAT_TRANSFER_PROCEDURE = "Процедура передачи кошки";
    public static final String DOG_CATALOG = "Каталог собак";
    public static final String CAT_CATALOG = "Каталог кошек";
    public static final String GO_BACK_TAKE_A_FROM_A_SHELTER_DOG = "Bернуться нaзад";
    public static final String GO_BACK_TAKE_A_FROM_A_SHELTER_CAT = "Вернyться нaзад";
    // Кнопки об отчете о питомце
    public static final String INFORMATION_ABOUT_REPORT_DOG = "Инфoрмация об отчете";
    public static final String SEND_REPORT_DOG = "Прислать oтчет";
    public static final String GO_BACK_PET_REPORT_DOG = "Вернуться нaзaд";
    public static final String INFORMATION_ABOUT_REPORT_CAT = "Информация об отчете";
    public static final String SEND_REPORT_CAT = "Прислать отчет";
    public static final String GO_BACK_PET_REPORT_CAT = "Beрнуться назад";
    // Кнопки о волонтере
    public static final String QUESTION_TO_VOLUNTEER_DOG = "Вопрос волонтеру";
    public static final String BECOME_A_VOLUNTEER_DOG = "Стать волонтером";
    public static final String GO_BACK_CALL_A_VOLUNTEER_DOG = "Beрнуться нaзaд";
    public static final String QUESTION_TO_VOLUNTEER_CAT = "Вопрос волонтеру";
    public static final String BECOME_A_VOLUNTEER_CAT = "Стать волонтером";
    public static final String GO_BACK_CALL_A_VOLUNTEER_CAT = "Вернyтьcя нaзaд";

    // Списки кнопок. Меню
    public static final List<String> SUBSCRIPTION_MENU = List.of(
            SUBSCRIPTION
    );
    public static final List<String> CHOOSING_PET_MENU = List.of(
            DOG,
            CAT
    );
    public static final List<String> MAIN_DOG_MENU = List.of(
            INFORMATION_ABOUT_THE_SHELTER_DOG,
            TAKE_A_FROM_A_SHELTER_DOG,
            PET_REPORT_DOG,
            CALL_A_VOLUNTEER_DOG,
            GO_BACK_DOG
    );
    public static final List<String> MAIN_CAT_MENU = List.of(
            INFORMATION_ABOUT_THE_SHELTER_CAT,
            TAKE_A_FROM_A_SHELTER_CAT,
            PET_REPORT_CAT,
            CALL_A_VOLUNTEER_CAT,
            GO_BACK_CAT
    );
    public static final List<String> INFORMATION_MENU_DOG = List.of(
            ABOUT_OUR_NURSERY_DOG,
            AMBULANCE_FOR_ANIMALS_DOG,
            INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_DOG,
            REHABILITATION_FOR_SPECIAL_ANIMALS_DOG,
            SAFETY_PRECAUTIONS_DOG,
            REQUISITES_DOG,
            CONTACTS_DOG,
            SECURITY_CONTACTS_DOG,
            GO_BACK_INFORMATION_DOG
    );
    public static final List<String> INFORMATION_MENU_CAT = List.of(
            ABOUT_OUR_NURSERY_CAT,
            AMBULANCE_FOR_ANIMALS_CAT,
            INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_CAT,
            REHABILITATION_FOR_SPECIAL_ANIMALS_CAT,
            SAFETY_PRECAUTIONS_CAT,
            REQUISITES_CAT,
            CONTACTS_CAT,
            SECURITY_CONTACTS_CAT,
            GO_BACK_INFORMATION_CAT
    );
    public static final List<String> TAKE_A_FROM_A_SHELTER_DOG_MENU = List.of(
            ARE_DOGS_IN_SHELTER_HEALTHY,
            YOU_DECIDED_TO_TAKE_DOG,
            IF_YOU_ALREADY_HAVE_DOG,
            DOG_TRANSFER_PROCEDURE,
            DOG_CATALOG,
            GO_BACK_TAKE_A_FROM_A_SHELTER_DOG
    );
    public static final List<String> TAKE_A_FROM_A_SHELTER_CAT_MENU = List.of(
            ARE_CATS_IN_SHELTER_HEALTHY,
            YOU_DECIDED_TO_TAKE_CAT,
            IF_YOU_ALREADY_HAVE_CAT,
            CAT_TRANSFER_PROCEDURE,
            CAT_CATALOG,
            GO_BACK_TAKE_A_FROM_A_SHELTER_CAT
    );
    public static final List<String> DOG_REPORT_MENU = List.of(
            INFORMATION_ABOUT_REPORT_DOG,
            SEND_REPORT_DOG,
            GO_BACK_PET_REPORT_DOG
    );
    public static final List<String> CAT_REPORT_MENU = List.of(
            INFORMATION_ABOUT_REPORT_CAT,
            SEND_REPORT_CAT,
            GO_BACK_PET_REPORT_CAT
    );
    public static final List<String> CALL_A_VOLUNTEER_DOG_MENU = List.of(
            QUESTION_TO_VOLUNTEER_DOG,
            BECOME_A_VOLUNTEER_DOG,
            GO_BACK_CALL_A_VOLUNTEER_DOG
    );
    public static final List<String> CALL_A_VOLUNTEER_CAT_MENU = List.of(
            QUESTION_TO_VOLUNTEER_CAT,
            BECOME_A_VOLUNTEER_CAT,
            GO_BACK_CALL_A_VOLUNTEER_CAT
    );
}
