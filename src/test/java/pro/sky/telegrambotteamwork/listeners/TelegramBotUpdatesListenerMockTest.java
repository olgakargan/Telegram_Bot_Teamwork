package pro.sky.telegrambotteamwork.listeners;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambotteamwork.enums.Role;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.UserRepository;
import pro.sky.telegrambotteamwork.service.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pro.sky.telegrambotteamwork.constants.CommandMessageUserConstant.*;
import static pro.sky.telegrambotteamwork.constants.KeyboardMessageUserConstant.*;
import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerMockTest {

    @Mock
    private TelegramBot telegramBot;
    @Spy
    private MenuService menuService;
    @Mock
    private UserService userService;
    @Mock
    private CheckService checkService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DogService dogService;
    @Mock
    private CatService catService;
    @Mock
    private ReportDataService reportDataService;
    @Mock
    private ImageService imageService;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    // Тест на реакцию на команду "Старт"
    @Test
    public void handleStartTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("text-update.json").toURI()));
        Update update = getUpdate(json, START);
        when(checkService.hasMessage(eq(update))).thenReturn(true);
        when(checkService.hasText(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuSubscribe(update, SUBSCRIBE_TO_BOT_MESSAGE);
    }

    // Тест на реакцию на команду "Старт"
    @Test
    public void handleStartTwoTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("text-update.json").toURI()));
        Update update = getUpdate(json, START);
        Collection<User> roleUsers = new ArrayList<>();
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasMessage(eq(update))).thenReturn(true);
        when(checkService.hasText(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(SUBSCRIBE_TO_BOT_MESSAGE);
        verify(menuService).loadingTheMenuSubscribe(update, SUBSCRIBE_TO_BOT_MESSAGE);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuSubscribe(null, null));
    }

    // Тест на реакцию по нажатии кнопки "DOG"
    @Test
    public void callbackQueryDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-dog.json").toURI()));
        Update update = getUpdate(json, DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(DOG_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "CAT"
    @Test
    public void callbackQueryCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-cat.json").toURI()));
        Update update = getUpdate(json, CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(CAT_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Выбрать другого питомца"
    @Test
    public void callbackQueryAnotherPetTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-another-pet.json").toURI()));
        Update update = getUpdate(json, ANOTHER_PET);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Информация о том, каких питомцев еще можно взять");
    }

    // Тест на реакцию по нажатии кнопки "Узнать информацию о приюте собак"
    @Test
    public void callbackQueryInfoMenuDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-info-menu-dog.json").toURI()));
        Update update = getUpdate(json, INFORMATION_ABOUT_THE_SHELTER_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(INFORMATION_ABOUT_THE_SHELTER_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, INFORMATION_ABOUT_THE_SHELTER_MESSAGE, INFORMATION_MENU_DOG);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Узнать информацию о приюте кошек"
    @Test
    public void callbackQueryInfoMenuCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-info-menu-dog.json").toURI()));
        Update update = getUpdate(json, INFORMATION_ABOUT_THE_SHELTER_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(INFORMATION_ABOUT_THE_SHELTER_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, INFORMATION_ABOUT_THE_SHELTER_MESSAGE, INFORMATION_MENU_CAT);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Как взять собаку из приюта"
    @Test
    public void callbackQueryTakeFromShelterMenuDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-take-from-shelter-menu-dog.json").toURI()));
        Update update = getUpdate(json, TAKE_A_FROM_A_SHELTER_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(TAKE_A_PET_FROM_A_SHELTER_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, TAKE_A_PET_FROM_A_SHELTER_MESSAGE, TAKE_A_FROM_A_SHELTER_DOG_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Как взять кошку из приюта"
    @Test
    public void callbackQueryTakeFromShelterMenuCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-take-from-shelter-menu-dog.json").toURI()));
        Update update = getUpdate(json, TAKE_A_FROM_A_SHELTER_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(TAKE_A_PET_FROM_A_SHELTER_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, TAKE_A_PET_FROM_A_SHELTER_MESSAGE, TAKE_A_FROM_A_SHELTER_CAT_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Прислать отчет о собаке"
    @Test
    public void callbackQueryReportMenuDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, PET_REPORT_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(PET_REPORT_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, PET_REPORT_MESSAGE, DOG_REPORT_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Прислать отчет о кошке"
    @Test
    public void callbackQueryReportMenuCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, PET_REPORT_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(PET_REPORT_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, PET_REPORT_MESSAGE, CAT_REPORT_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Позвать волонтера для собаки"
    @Test
    public void callbackQueryCallVolunteerMenuDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, CALL_A_VOLUNTEER_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(CALL_A_VOLUNTEER_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, CALL_A_VOLUNTEER_MESSAGE, CALL_A_VOLUNTEER_DOG_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Позвать волонтера для кошки"
    @Test
    public void callbackQueryCallVolunteerMenuCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, CALL_A_VOLUNTEER_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(CALL_A_VOLUNTEER_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, CALL_A_VOLUNTEER_MESSAGE, CALL_A_VOLUNTEER_CAT_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Вернуться назад"
    @Test
    public void callbackQueryGoBackMenuDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(WELCOME_MESSAGE);
        verify(menuService).loadingTheMenuDogAndCatCallbackQuery(update, WELCOME_MESSAGE, CHOOSING_PET_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuDogAndCatCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Вернуться назад"
    @Test
    public void callbackQueryGoBackMenuCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(WELCOME_MESSAGE);
        verify(menuService).loadingTheMenuDogAndCatCallbackQuery(update, WELCOME_MESSAGE, CHOOSING_PET_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuDogAndCatCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "О нашем питомнике"
    @Test
    public void aboutOurNurseryDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, ABOUT_OUR_NURSERY_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ABOUT_OUR_NURSERY_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "О нашем питомнике"
    @Test
    public void aboutOurNurseryCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, ABOUT_OUR_NURSERY_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ABOUT_OUR_NURSERY_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Скорая помощь для животных"
    @Test
    public void ambulanceForAnimalsDog() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, AMBULANCE_FOR_ANIMALS_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(AMBULANCE_FOR_ANIMALS_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Скорая помощь для животных"
    @Test
    public void ambulanceForAnimalsCat() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, AMBULANCE_FOR_ANIMALS_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(AMBULANCE_FOR_ANIMALS_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Инструкция помощи"
    @Test
    public void instructionsForCallingAnAmbulanceDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Инструкция помощи"
    @Test
    public void instructionsForCallingAnAmbulanceCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(INSTRUCTIONS_FOR_CALLING_AN_AMBULANCE_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Реабилитация для питомца"
    @Test
    public void rehabilitationForSpecialAnimalsDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, REHABILITATION_FOR_SPECIAL_ANIMALS_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(REHABILITATION_FOR_SPECIAL_ANIMALS_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Реабилитация для питомца"
    @Test
    public void rehabilitationForSpecialAnimalsCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, REHABILITATION_FOR_SPECIAL_ANIMALS_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(REHABILITATION_FOR_SPECIAL_ANIMALS_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Техника безопасности"
    @Test
    public void safetyPrecautionsDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, SAFETY_PRECAUTIONS_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Техника безопасности для собак");
    }

    // Тест на реакцию по нажатии кнопки "Техника безопасности"
    @Test
    public void safetyPrecautionsCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, SAFETY_PRECAUTIONS_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Техника безопасности для кошек");
    }

    // Тест на реакцию по нажатии кнопки "Реквизиты"
    @Test
    public void requisitesDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, REQUISITES_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(REQUISITES_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Реквизиты"
    @Test
    public void requisitesCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, REQUISITES_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(REQUISITES_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Контакты охраны"
    @Test
    public void securityContactsDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, SECURITY_CONTACTS_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Контакты охраны для собак");
    }

    // Тест на реакцию по нажатии кнопки "Контакты охраны"
    @Test
    public void securityContactsCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, SECURITY_CONTACTS_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Контакты охраны для кошек");
    }

    // Тест на реакцию по нажатии кнопки "Вернуться назад"
    @Test
    public void goBackInformationDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_INFORMATION_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(DOG_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Вернуться назад"
    @Test
    public void goBackInformationCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_INFORMATION_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(CAT_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Здоровье собаки в питомнике"
    @Test
    public void areDogsInShelterHealthyTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, ARE_DOGS_IN_SHELTER_HEALTHY);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ARE_PET_IN_SHELTER_HEALTHY_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Здоровье кошки в питомнике"
    @Test
    public void areCatsInShelterHealthyTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, ARE_CATS_IN_SHELTER_HEALTHY);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ARE_PET_IN_SHELTER_HEALTHY_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Вы решились взять собаку?"
    @Test
    public void youDecidedToTakeDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, YOU_DECIDED_TO_TAKE_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(YOU_DECIDED_TO_TAKE_PET_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Вы решились взять кошку?"
    @Test
    public void youDecidedToTakeCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, YOU_DECIDED_TO_TAKE_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(YOU_DECIDED_TO_TAKE_PET_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Если у вас уже есть собака"
    @Test
    public void ifYouAlreadyHaveDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, IF_YOU_ALREADY_HAVE_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(IF_YOU_ALREADY_HAVE_PET_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Если у вас уже есть кошка"
    @Test
    public void ifYouAlreadyHaveCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, IF_YOU_ALREADY_HAVE_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(IF_YOU_ALREADY_HAVE_PET_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Процедура передачи собаки"
    @Test
    public void dogTransferProcedureTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, DOG_TRANSFER_PROCEDURE);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(PET_TRANSFER_PROCEDURE_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Процедура передачи кошки"
    @Test
    public void catTransferProcedureTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, CAT_TRANSFER_PROCEDURE);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(PET_TRANSFER_PROCEDURE_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Каталог собак"
    @Test
    public void dogCatalogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, DOG_CATALOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Каталог собак");
    }

    // Тест на реакцию по нажатии кнопки "Каталог кошек"
    @Test
    public void catCatalogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, CAT_CATALOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Каталог кошек");
    }

    // Тест на реакцию по нажатии кнопки "Вернуться назад"
    @Test
    public void goBackTakeFromShelterDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_TAKE_A_FROM_A_SHELTER_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(DOG_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Вернуться назад"
    @Test
    public void goBackTakeFromShelterCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_TAKE_A_FROM_A_SHELTER_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(CAT_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Информация об отчете"
    @Test
    public void informationAboutReportDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, INFORMATION_ABOUT_REPORT_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(INFORMATION_ABOUT_REPORT_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Информация об отчете"
    @Test
    public void informationAboutReportCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, INFORMATION_ABOUT_REPORT_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(INFORMATION_ABOUT_REPORT_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Прислать отчет"
    @Test
    public void sendReportDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, SEND_REPORT_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ADD_REPORT_DATA_PREVIEW_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Прислать отчет"
    @Test
    public void sendReportCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, SEND_REPORT_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ADD_REPORT_DATA_PREVIEW_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Вернуться назад"
    @Test
    public void goBackPetReportDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_PET_REPORT_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(DOG_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Вернуться назад"
    @Test
    public void goBackPetReportCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_PET_REPORT_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(CAT_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Вопрос волонтеру"
    @Test
    public void questionToVolunteerDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, QUESTION_TO_VOLUNTEER_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(QUESTION_TO_VOLUNTEER_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Вопрос волонтеру"
    @Test
    public void questionToVolunteerCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, QUESTION_TO_VOLUNTEER_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(QUESTION_TO_VOLUNTEER_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Стать волонтером"
    @Test
    public void becomeVolunteerDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, BECOME_A_VOLUNTEER_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(BECOME_A_VOLUNTEER);
    }

    // Тест на реакцию по нажатии кнопки "Стать волонтером"
    @Test
    public void becomeVolunteerCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, BECOME_A_VOLUNTEER_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(BECOME_A_VOLUNTEER);
    }

    // Тест на реакцию по нажатии кнопки "Вернуться назад"
    @Test
    public void goBackCallVolunteerDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_CALL_A_VOLUNTEER_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(DOG_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Вернуться назад"
    @Test
    public void goBackCallVolunteerCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_CALL_A_VOLUNTEER_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleUsers = new ArrayList<>();
        roleUsers.add(user);
        Collection<User> roleVolunteers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(CAT_MESSAGE);
        verify(menuService).loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU);
        assertThrows(Exception.class, () -> menuService.loadingTheMenuCallbackQuery(null, null, null));
    }

    // Тест на реакцию по нажатии кнопки "Памятка для волонтеров"
    @Test
    public void memoForVolunteerTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, MEMO_FOR_A_VOLUNTEER);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleVolunteers = new ArrayList<>();
        roleVolunteers.add(user);
        Collection<User> roleUsers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(MEMO_FOR_A_VOLUNTEER_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Обязанности волонтеров"
    @Test
    public void dutiesOfVolunteersTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, DUTIES_OF_VOLUNTEERS);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleVolunteers = new ArrayList<>();
        roleVolunteers.add(user);
        Collection<User> roleUsers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(DUTIES_OF_VOLUNTEERS_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Форма одежды"
    @Test
    public void formOfClothingTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, FORM_OF_CLOTHING);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleVolunteers = new ArrayList<>();
        roleVolunteers.add(user);
        Collection<User> roleUsers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(FORM_OF_CLOTHING_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Волонтерские выезды"
    @Test
    public void volunteerTripsTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, VOLUNTEER_TRIPS);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleVolunteers = new ArrayList<>();
        roleVolunteers.add(user);
        Collection<User> roleUsers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(VOLUNTEER_TRIPS_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Добавить собаку"
    @Test
    public void addDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, ADD_DOG);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleVolunteers = new ArrayList<>();
        roleVolunteers.add(user);
        Collection<User> roleUsers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ADD_DOG_PREVIEW_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Добавить кошку"
    @Test
    public void addCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, ADD_CAT);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleVolunteers = new ArrayList<>();
        roleVolunteers.add(user);
        Collection<User> roleUsers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ADD_CAT_PREVIEW_MESSAGE);
    }

    // Тест на реакцию по нажатии кнопки "Добавить другого питомца"
    @Test
    public void addPetTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, ADD_PET);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleVolunteers = new ArrayList<>();
        roleVolunteers.add(user);
        Collection<User> roleUsers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(checkService.hasCallbackQuery(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ADD_PET_MESSAGE);
    }

    // Тест на реакцию на команду "Добавить собаку"
    @Test
    public void addDogCommandTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("text-update.json").toURI()));
        Update update = getUpdate(json, ADD_DOG_COMMAND);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleVolunteers = new ArrayList<>();
        roleVolunteers.add(user);
        Collection<User> roleUsers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(checkService.hasMessage(eq(update))).thenReturn(true);
        when(checkService.hasText(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ADD_DOG_PREVIEW_2_MESSAGE);
    }

    // Тест на реакцию на команду "Добавить собаку"
    @Test
    public void addDogCommandTwoTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("text-update.json").toURI()));
        Update update = getUpdate(json, ADD_DOG_BD_COMMAND);

        User user = new User("FirstName", "LastName", "UserName", 22L, 123L);
        Collection<User> roleVolunteers = new ArrayList<>();
        roleVolunteers.add(user);
        Collection<User> roleUsers = new ArrayList<>();
        when(userRepository.findUserByRole(eq(Role.ROLE_VOLUNTEER))).thenReturn(roleVolunteers);
        when(userRepository.findUserByRole(eq(Role.ROLE_USER))).thenReturn(roleUsers);
        when(checkService.hasMessage(eq(update))).thenReturn(true);
        when(checkService.hasText(eq(update))).thenReturn(true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(12345L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ADD_DOG_MESSAGE);
    }


    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }

}
