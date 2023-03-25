package pro.sky.telegrambotteamwork.listeners;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static pro.sky.telegrambotteamwork.constants.CommandMessageUserConstant.*;
import static pro.sky.telegrambotteamwork.constants.KeyboardMessageUserConstant.*;
import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.*;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambotteamwork.enums.Role;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.*;
import pro.sky.telegrambotteamwork.service.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private UserService userService;
    @Mock
    private CheckService checkService;
    @Mock
    private UserRepository userRepository;
    @Spy
    private MenuService menuService;
    @InjectMocks
    private TelegramBotUpdatesListener listener;

    @Test  //вариант когда новый пользователь только запускает бот
    public void startTestWhenFirstTimeStart() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("mocks_for_updates.json").toURI()));
        Update update = getUpdate(jsonUpdates, START);
        Collection<User> rolesUser = new ArrayList<>();
        Collection<User> rolesVolunteer = new ArrayList<>();
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasMessage(update)).thenReturn(true);
        when(checkService.hasText(update)).thenReturn(true);

        listener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(121L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(SUBSCRIBE_TO_BOT_MESSAGE);
    }

    @Test  //вариант когда запускает бот волонтёр
    public void startTestWhenUserVolunteer() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("mocks_for_updates.json").toURI()));
        Update update = getUpdate(jsonUpdates, START);
        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        Collection<User> rolesVolunteer = new ArrayList<>();
        rolesVolunteer.add(user);
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasMessage(update)).thenReturn(true);
        when(checkService.hasText(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        //проверяем что пошел в запуск
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(121L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(WELCOME_VOLUNTEER_MESSAGE);
    }

    @Test       //вариант когда зарегистрированный пользователь запускает /start
    public void startTestWhenUserNotVolunteer() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("mocks_for_updates.json").toURI()));
        Update update = getUpdate(jsonUpdates, START);
        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        rolesUser.add(user);
        Collection<User> rolesVolunteer = new ArrayList<>();
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasMessage(update)).thenReturn(true);
        when(checkService.hasText(update)).thenReturn(true);

        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        //проверяем что пошел в запуск
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(121L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(WELCOME_MESSAGE);
    }

    @Test  //тестируем сохранение нового пользователя в базу если есть контакты
    public void testAddUserIfGetContact() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("mocks_for_updates.json").toURI()));
        Update update = getUpdate(jsonUpdates, START);
        Collection<User> rolesUser = new ArrayList<>();
        Collection<User> rolesVolunteer = new ArrayList<>();
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasMessage(update)).thenReturn(false); //это позволит нам пойти в вариант сохранения
        when(checkService.hasContact(update)).thenReturn(true);

        listener.process(Collections.singletonList(update));
        verify(userService).saveUser(any(User.class), eq(update));
    }

    @Test       //вариант когда зарегистрированному пользователю показывается меню выбора собака/кошка
    public void testUserDogOrCattMenu() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("mocks_for_updates.json").toURI()));

        Update update = getUpdate(jsonUpdates, START);

        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        rolesUser.add(user);
        Collection<User> rolesVolunteer = new ArrayList<>();
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasMessage(update)).thenReturn(true);
        when(checkService.hasText(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(121L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(WELCOME_MESSAGE);
    }


    @Test
    //вариант когда зарегистрированный пользователь запускает отправку отчёта ADD_REPORT_DATA_PREVIEW_2_MESSAGE
    public void testUserAddingReportMenu() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("mocks_for_updates.json").toURI()));
        Update update = getUpdate(jsonUpdates, ADD_REPORT_DATA_COMMAND);
        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        rolesUser.add(user);
        Collection<User> rolesVolunteer = new ArrayList<>();
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasMessage(update)).thenReturn(true);
        when(checkService.hasText(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(121L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(ADD_REPORT_DATA_PREVIEW_2_MESSAGE);
        assertThat(actualMessage.getParameters().get("text")).isNotEqualTo(ADD_REPORT_DATA_MESSAGE);
    }

    @Test       //вариант когда зарегистрированный пользователь запускает отправку отчёта ADD_REPORT_DATA_MESSAGE
    public void testUserAddingReportMenuDB() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("mocks_for_updates.json").toURI()));
        Update update = getUpdate(jsonUpdates, ADD_REPORT_DATA_BD_COMMAND);
        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        rolesUser.add(user);
        Collection<User> rolesVolunteer = new ArrayList<>();
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasMessage(update)).thenReturn(true);
        when(checkService.hasText(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(121L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(ADD_REPORT_DATA_MESSAGE);
        assertThat(actualMessage.getParameters().get("text")).isNotEqualTo(ADD_REPORT_DATA_PREVIEW_2_MESSAGE);
    }

    @Test       //вариант когда зарегистрированный пользователь запускает отправку отчёта
    public void testUserMenuDog() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(jsonUpdates, DOG);

        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        rolesUser.add(user);
        Collection<User> rolesVolunteer = new ArrayList<>();
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasCallbackQuery(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(DOG_MESSAGE);
    }

    @Test       //вариант когда зарегистрированный пользователь запускает отправку отчёта
    public void testUserMenuCat() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(jsonUpdates, CAT);

        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        rolesUser.add(user);
        Collection<User> rolesVolunteer = new ArrayList<>();
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasCallbackQuery(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(CAT_MESSAGE);
    }

    @Test       //вариант когда зарегистрированный пользователь запускает отправку отчёта
    public void testUserMenuPet() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(jsonUpdates, ANOTHER_PET);

        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        rolesUser.add(user);
        Collection<User> rolesVolunteer = new ArrayList<>();
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasCallbackQuery(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo("Информация о том, каких питомцев еще можно взять");
    }

    @Test       //вариант когда волонтёру показывается меню  ADD_DOG/CAT_Menu
    public void testVolunteerAddDogMenu() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("mocks_for_updates.json").toURI()));

        Update update1 = getUpdate(jsonUpdates, ADD_DOG_COMMAND);
        Update update2 = getUpdate(jsonUpdates, ADD_DOG_BD_COMMAND);
        Update update3 = getUpdate(jsonUpdates, ADD_CAT_COMMAND);
        Update update4 = getUpdate(jsonUpdates, ADD_CAT_BD_COMMAND);

        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        Collection<User> rolesVolunteer = new ArrayList<>();
        rolesVolunteer.add(user);
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasMessage(update1)).thenReturn(true);
        when(checkService.hasText(update1)).thenReturn(true);
        when(checkService.hasMessage(update2)).thenReturn(true);
        when(checkService.hasText(update2)).thenReturn(true);
        when(checkService.hasMessage(update3)).thenReturn(true);
        when(checkService.hasText(update3)).thenReturn(true);
        when(checkService.hasMessage(update4)).thenReturn(true);
        when(checkService.hasText(update4)).thenReturn(true);

        listener.process(Arrays.asList(update1, update2, update3, update4));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(4)).execute(argumentCaptor.capture());
        List<SendMessage> actualMessages = argumentCaptor.getAllValues();
        assertThat(actualMessages.get(0).getParameters().get("chat_id")).isEqualTo(121L);
        assertThat(actualMessages.get(0).getParameters().get("text")).isEqualTo(ADD_DOG_PREVIEW_2_MESSAGE);
        assertThat(actualMessages.get(1).getParameters().get("chat_id")).isEqualTo(121L);
        assertThat(actualMessages.get(1).getParameters().get("text")).isEqualTo(ADD_DOG_MESSAGE);
        assertThat(actualMessages.get(2).getParameters().get("chat_id")).isEqualTo(121L);
        assertThat(actualMessages.get(2).getParameters().get("text")).isEqualTo(ADD_CAT_PREVIEW_2_MESSAGE);
        assertThat(actualMessages.get(3).getParameters().get("chat_id")).isEqualTo(121L);
        assertThat(actualMessages.get(3).getParameters().get("text")).isEqualTo(ADD_CAT_MESSAGE);

    }

    @Test       //вариант когда волонтёру показывается меню  INFORMATION_FOR_VOLUNTEER
    public void testVolunteerWelcomeMenu() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback-query.json").toURI()));

        Update update = getUpdate(jsonUpdates, INFORMATION_FOR_VOLUNTEER);
        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        Collection<User> rolesVolunteer = new ArrayList<>();
        rolesVolunteer.add(user);
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasCallbackQuery(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(INFORMATION_WELCOME_MESSAGE);
    }

    @Test       //вариант когда волонтёру показывается меню  ADD_A_PET
    public void testVolunteerAddPetMenu() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback-query.json").toURI()));

        Update update = getUpdate(jsonUpdates, ADD_A_PET);
        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        Collection<User> rolesVolunteer = new ArrayList<>();
        rolesVolunteer.add(user);
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasCallbackQuery(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(ADD_A_PET_MESSAGE);
    }

    @Test       //вариант когда волонтёру показывается меню  REPORTS_OF_ADOPTIVE_PARENTS
    public void testVolunteerReportsMenu() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback-query.json").toURI()));

        Update update = getUpdate(jsonUpdates, REPORTS_OF_ADOPTIVE_PARENTS);
        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        Collection<User> rolesVolunteer = new ArrayList<>();
        rolesVolunteer.add(user);
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasCallbackQuery(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(REPORTS_OF_ADOPTIVE_PARENTS_MESSAGE);
    }

    @Test       //вариант когда волонтёру показывается меню  MAKE_A_VOLUNTEER
    public void testVolunteerMakeVolunteerMenu() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback-query.json").toURI()));

        Update update = getUpdate(jsonUpdates, MAKE_A_VOLUNTEER);
        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        Collection<User> rolesVolunteer = new ArrayList<>();
        rolesVolunteer.add(user);
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasCallbackQuery(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(MAKE_A_VOLUNTEER_MESSAGE);
    }

    @Test       //вариант когда волонтёру показывается меню  MEMO_FOR_A_VOLUNTEER
    public void testVolunteerInfoMenu() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback-query.json").toURI()));

        Update update = getUpdate(jsonUpdates, MEMO_FOR_A_VOLUNTEER);
        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        Collection<User> rolesVolunteer = new ArrayList<>();
        rolesVolunteer.add(user);
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasCallbackQuery(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(MEMO_FOR_A_VOLUNTEER_MESSAGE);
    }

    @Test       //вариант когда волонтёру показывается меню  DUTIES_OF_VOLUNTEERS
    public void testVolunteerDutiesMenu() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback-query.json").toURI()));

        Update update = getUpdate(jsonUpdates, DUTIES_OF_VOLUNTEERS);
        User user = new User(1L, "FirstName", "LastName", "userName", 22L, 111L);
        Collection<User> rolesUser = new ArrayList<>();
        Collection<User> rolesVolunteer = new ArrayList<>();
        rolesVolunteer.add(user);
        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasCallbackQuery(update)).thenReturn(true);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(DUTIES_OF_VOLUNTEERS_MESSAGE);
    }

    //метод для формирования апдейтов из json файла с заданным ответом
    private Update getUpdate(String jsonUpdates, String replaced) {
        return BotUtils.fromJson(jsonUpdates.replace("%command%", replaced), Update.class);
    }


}
