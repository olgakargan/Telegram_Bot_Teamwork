package pro.sky.telegrambotteamwork.listeners;

//    @BeforeEach
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testProcess() throws Exception {
//        Update update = mock(Update.class);
//        Message message = mock(Message.class);
//        User user = mock(User.class);
//        ReportData reportData = mock(ReportData.class);
//        Collection<User> rolesUser = new ArrayList<>();
//        Collection<User> rolesVolunteer = new ArrayList<>();
//        List<Update> updates = new ArrayList<>();
//        updates.add(update);
//
//        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
//        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
//        when(checkService.hasMessage(update)).thenReturn(true);
//        when(checkService.hasText(update)).thenReturn(true);
//        when(message.text()).thenReturn("start");
//        when(update.message()).thenReturn(message);
//        when(menuService.loadingTheMenuSubscribe(update, "subscribe")).thenReturn(null);
//
//        telegramBotUpdatesListener.process(updates);
//
//        verify(userRepository).findUserByRole(Role.ROLE_USER);
//        verify(userRepository).findUserByRole(Role.ROLE_VOLUNTEER);
//        verify(checkService).hasMessage(update);
//        verify(checkService).hasText(update);
//        verify(update.message()).text();
//        verify(menuService).loadingTheMenuSubscribe(update, "subscribe");
//        verify(telegramBot).execute(any(SendMessage.class));
//    }
//}
/////////

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static pro.sky.telegrambotteamwork.constants.CommandMessageUserConstant.*;
import static pro.sky.telegrambotteamwork.constants.KeyboardMessageUserConstant.*;
import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.*;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.Keyboard;
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
//    @Mock
//    private MenuService menuService;
//    @Mock
//    private UserService userService;
//    @Mock
//    private CheckService checkService;
//    @Mock
//    private DogService dogService;
//    @Mock
//    private CatService catService;
//    @Mock
//    private ReportDataService reportDataService;
//    @Mock
//    private ImageService imageService;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private ImageRepository imageRepository;

    @InjectMocks
    private TelegramBotUpdatesListener listener;

    @Test
    public void startTest() throws URISyntaxException, IOException {
        String jsonUpdates = Files.readString(Paths.get(TelegramBotUpdatesListenerTest.class.getResource("mocks_for_updates.json").toURI()));
        Update update = getUpdate(jsonUpdates, START);
        listener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        //проверяем что пошел в запуск
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actualMessage = argumentCaptor.getValue();
        assertThat(actualMessage.getParameters().get("chat_id")).isEqualTo(111L);
        assertThat(actualMessage.getParameters().get("text")).isEqualTo(SUBSCRIBE_TO_BOT_MESSAGE);
        assertThat(actualMessage.getParameters().get("parse_mode")).isEqualTo(ParseMode.Markdown.name());
    }

//метод для формирования апдейтов из json файла с заданным ответом
    private Update getUpdate(String jsonUpdates, String replaced) {
        return BotUtils.fromJson(jsonUpdates.replace("%command%", replaced), Update.class);
    }
}

//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        listener = new TelegramBotUpdatesListener(telegramBot, menuService, userService, checkService, userRepository, dogService, catService, reportDataService, imageService, imageRepository);
//    }
//
//    @Test
//    public void testProcess() {
//        Update update1 = mock(Update.class);
//        Update update2 = mock(Update.class);
//        List<Update> updates = new ArrayList<>();
//        updates.add(update1);
//        updates.add(update2);
//
//        Message message1 = mock(Message.class);
//        Message message2 = mock(Message.class);
//
//        when(update1.message()).thenReturn(message1);
//        when(update2.message()).thenReturn(message2);
//
//        when(checkService.hasMessage(update1)).thenReturn(true);
//        when(checkService.hasMessage(update2)).thenReturn(true);
//
//        when(checkService.hasText(update1)).thenReturn(true);
//        when(checkService.hasText(update2)).thenReturn(false);
//
//        when(message1.text()).thenReturn("START");
//
//        when(checkService.hasContact(update1)).thenReturn(false);
//        when(checkService.hasContact(update2)).thenReturn(true);
//
//        Collection<User> emptyCollection = new ArrayList<>();
//        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(emptyCollection);
//        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(emptyCollection);
//        SendMessage SendMessage = mock(com.pengrad.telegrambot.request.SendMessage.class);
////        when(menuService.loadingTheMenuSubscribe(update1, SUBSCRIBE_TO_BOT_MESSAGE)).thenReturn(SendMessage);
//        listener.process(updates);
//
////        ReplyKeyboardMarkup replyKeyboardMarkup = mock(ReplyKeyboardMarkup.class);
////        verify(menuService).loadingTheMenuSubscribe(update1, "Подписаться");
//        verify(userService).saveUser(any(User.class), eq(update2));
//
//        verifyNoMoreInteractions(menuService, userService);
//
//        Update update = mock(Update.class);
//        Message message = mock(Message.class);
//        CallbackQuery callbackQuery = mock(CallbackQuery.class);
//        Chat chat = mock(Chat.class);
//        when(update.message()).thenReturn(message);
//        when(update.callbackQuery()).thenReturn(callbackQuery);
//        when(callbackQuery.data()).thenReturn(DOG);
//        menuService.loadingTheMenuDogAndCat(update, WELCOME_MESSAGE, CHOOSING_PET_MENU);
////        verify(telegramBot).execute(any(SendMessage.class));
////        verify(menuService).loadingTheMenuCallbackQuery(eq(update), eq(DOG_MESSAGE), eq(MAIN_DOG_MENU));
//    }
//


