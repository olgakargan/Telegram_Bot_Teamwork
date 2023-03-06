package pro.sky.telegrambotteamwork.constants;

<<<<<<< HEAD
import pro.sky.telegrambotteamwork.serviceImpl.ReportDataService;
=======
import java.util.List;
>>>>>>> origin/dev

/**
 * Класс, с константными переменными
 */
public class UserRequestConstant {
    // Константная переменная, которая выводит приветственное сообщение пользователю
    public static final String WELCOME_MESSAGE = "Мы приют для бездомных животных \"ЗООПОМОЩЬ\", расположенный в городе Астане. Мы хотим помочь людям, которые задумываются о том, чтобы забрать собаку или кошку домой. Что вы хотите узнать?";
    public static final String DATING_RULES = "Бот может выдать правила знакомства с собакой до того, как можно забрать ее из приюта.";
    public static final String LIST_OF_DOCUMENTS = "Бот может выдать список документов, необходимых для того, чтобы взять собаку из приюта.";
    public static final String LIST_OF_RECOMMENDATIONS_FOR_TRANSPORTATION = "Бот может выдать список рекомендаций по транспортировке животного.";
    public static final String LIST_OF_RECOMMENDATIONS_FOR_ARRANGEMENT_OF_A_PUPPY = "Бот может выдать список рекомендаций по обустройству дома для щенка.";
    public static final String LIST_OF_RECOMMENDATIONS_FOR_ARRANGEMENT_FOR_AN_ADULT_DOG = "Бот может выдать список рекомендаций по обустройству дома для взрослой собаки.";
    public static final String LIST_OF_RECOMMENDATIONS_FOR_ARRANGEMENT_OF_A_DOG_WITH_DISABILITIES = "Бот может выдать список рекомендаций по обустройству дома для собаки с ограниченными возможностями (зрение, передвижение).";
    public static final String TIPS_FROM_A_DOG_HANDLER = "Бот может выдать советы кинолога по первичному общению с собакой.";
    public static final String RECOMMENDATIONS_OF_PROVEN_DOG_HANDLERS = "Бот может выдать рекомендации по проверенным кинологам для дальнейшего обращения к ним.";
    public static final String REASONS_FOR_REFUSAL = "Бот может выдать список причин, почему могут отказать и не дать забрать собаку из приюта. ";
    public static final String ABOUT_OUR_NURSERY_DETAILED = "\"ЗООПОМОЩЬ\" – мы ежедневно спасаем животных от смерти и благодаря именно вашему звонку вы даёте пострадавшему животному второй шанс на жизнь. Мы — Фонд помощи особенным животным \"ЗООПОМОЩЬ\" который был создан в 2021 году. На сегодняшний день распространенной проблемой в России заключается в существование бездомных больных животных как следствие безответственного и равнодушного отношения человека к ним. Большинство людей заняты своими делами и до животных им дела нет, поэтому наш благотворительный фонд начал решать эту проблему.";
    public static final String AMBULANCE_FOR_ANIMALS_DETAILS = "В нашем благотворительном фонде есть бригада выездной скорой помощи, которая помогает животным попавших в трудные жизненные ситуации, например: ДТП, атака догхантеров. Мы работаем по заявкам граждан и каждый день спасаем животных.";
    public static final String INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_DETAILS = "1. Для начала убедитесь, что животное пошло на контакт с вами, если оно находится в сознании.\n" + "2. Сделайте звонок на номер горячей линии или сотовый телефон: \n" + "3. Находитесь рядом с пострадавшим животным\n" + "4. Дождитесь приезда бригады скорой помощи\n" + "Мы ежедневно спасаем животных от смерти и благодаря именно вашему звонку вы даёте пострадавшему животному второй шанс на жизнь.";
    public static final String REHABILITATION_FOR_SPECIAL_ANIMALS_DETAILS = "Мы занимаемся полным восстановлением, реабилитацией и пожизненным содержанием животных, попавших в беду. Ежедневно мы спасаем животных от смерти и даём шанс на новую жизнь.\n" + "Миссия фонда — оказать быструю и необходимую помощь животным. На сегодняшний день во всём мире развивается благотворительная деятельность и помощь животным – это доброе дело, которое необходимо развивать в рамках цивилизованного общества.";
    public static final String REQUISITES_DETAILS = "Приют для бездомных животных \"ЗООПОМОЩЬ\"\n" + "Расчётный счёт: 40700000000040000000\n" + "Банк: ОТДЕЛЕНИЕ БАНКА ГОРОДА АСТАНЫ ЗАО\n" + "БИК: 042000000\n" + "Кор. Cчёт: 30100000000000000000\n" + "ОГРН: 1210000000000\n" + "ИНН: 4020000000\n" + "КПП: 402000000";
    public static final String CONTACTS_DETAILS = "Казахстан, г. Астана, ул. Ленина, 1";
    // Команды боту
    public static final String START = "/start";
    public static final String ERROR_MESSAGE = "Извини, я ограничен в ответах, напиши /start - для начала работы";
    public static final String MENU = "/menu";
    public static final String INFORMATION_ABOUT_THE_SHELTER = "Узнать информацию о приюте";
    public static final String TAKE_A_PET_FROM_A_SHELTER = "Как взять питомца из приюта";
    public static final String PET_REPORT = "Прислать отчет о питомце";
    public static final String CALL_A_VOLUNTEER = "Позвать волонтера";
<<<<<<< HEAD
    
    public static Object daysOfReports;
    public static ReportDataService reportDataService;
    public static String REGEXE_MESSAG;
}
=======
    public static final String SUBSCRIBE = "Подписаться";

    public static final String ABOUT_OUR_NURSERY = "О нашем питомнике";
    public static final String AMBULANCE_FOR_ANIMALS = "Скорая помощь для животных";
    public static final String INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE = "Инструкция по вызову скорой помощи";
    public static final String REHABILITATION_FOR_SPECIAL_ANIMALS = "Реабилитация для особенных животных";
    public static final String REQUISITES = "Реквизиты";
    public static final String CONTACTS = "Контакты";
    public static final List<String> MAIN_MENU = List.of(
            INFORMATION_ABOUT_THE_SHELTER,
            TAKE_A_PET_FROM_A_SHELTER,
            PET_REPORT,
            CALL_A_VOLUNTEER,
            SUBSCRIBE
    );
    public static final List<String> INFORMATION_MENU = List.of(
            ABOUT_OUR_NURSERY,
            AMBULANCE_FOR_ANIMALS,
            INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE,
            REHABILITATION_FOR_SPECIAL_ANIMALS,
            REQUISITES,
            CONTACTS
    );

}
>>>>>>> origin/dev
