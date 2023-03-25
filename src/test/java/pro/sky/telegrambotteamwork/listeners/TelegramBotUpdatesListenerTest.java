package pro.sky.telegrambotteamwork.listeners;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pro.sky.telegrambotteamwork.constants.CommandMessageUserConstant.*;
import static pro.sky.telegrambotteamwork.constants.KeyboardMessageUserConstant.*;
import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.*;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambotteamwork.enums.Role;
import pro.sky.telegrambotteamwork.model.*;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.*;
import pro.sky.telegrambotteamwork.service.*;
import pro.sky.telegrambotteamwork.constants.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {

    @Mock
    private TelegramBot telegramBot;
//        @Mock
//    private MenuService menuService;
        @Mock
    private UserService userService;
    @Mock
    private CheckService checkService;
    //    @Mock
//    private DogService dogService;
//    @Mock
//    private CatService catService;
//    @Mock
//    private ReportDataService reportDataService;
//    @Mock
//    private ImageService imageService;
    @Mock
    private UserRepository userRepository;
//    @Mock
//    private ImageRepository imageRepository;
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


    @Test       //вариант когда зарегистрированный пользователь запускает отправку отчёта
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
    @Test       //вариант когда зарегистрированный пользователь запускает отправку отчёта
    public void testUserMenu() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback-query-dog.json.json").toURI()));
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




    //метод для формирования апдейтов из json файла с заданным ответом
    private Update getUpdate(String jsonUpdates, String replaced) {
        return BotUtils.fromJson(jsonUpdates.replace("%command%", replaced), Update.class);
    }


}
