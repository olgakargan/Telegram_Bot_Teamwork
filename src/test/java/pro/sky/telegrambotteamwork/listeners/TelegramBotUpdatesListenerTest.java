package pro.sky.telegrambotteamwork.listeners;

import static org.mockito.Mockito.*;
import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.*;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambotteamwork.enums.Role;
import pro.sky.telegrambotteamwork.model.*;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.*;
import pro.sky.telegrambotteamwork.service.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegrambotteamwork.service.UserService;

public class TelegramBotUpdatesListenerTest {

    @Mock
    private TelegramBot telegramBot;
    @Mock
    private MenuService menuService;
    @Mock
    private UserService userService;
    @Mock
    private CheckService checkService;
    @Mock
    private DogService dogService;
    @Mock
    private CatService catService;
    @Mock
    private ReportDataService reportDataService;
    @Mock
    private ImageService imageService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageRepository imageRepository;
    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess() throws Exception {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        User user = mock(User.class);
        ReportData reportData = mock(ReportData.class);
        Collection<User> rolesUser = new ArrayList<>();
        Collection<User> rolesVolunteer = new ArrayList<>();
        List<Update> updates = new ArrayList<>();
        updates.add(update);

        when(userRepository.findUserByRole(Role.ROLE_USER)).thenReturn(rolesUser);
        when(userRepository.findUserByRole(Role.ROLE_VOLUNTEER)).thenReturn(rolesVolunteer);
        when(checkService.hasMessage(update)).thenReturn(true);
        when(checkService.hasText(update)).thenReturn(true);
        when(message.text()).thenReturn("start");
        when(update.message()).thenReturn(message);
        when(menuService.loadingTheMenuSubscribe(update, "subscribe")).thenReturn(null);

        telegramBotUpdatesListener.process(updates);

        verify(userRepository).findUserByRole(Role.ROLE_USER);
        verify(userRepository).findUserByRole(Role.ROLE_VOLUNTEER);
        verify(checkService).hasMessage(update);
        verify(checkService).hasText(update);
        verify(update.message()).text();
        verify(menuService).loadingTheMenuSubscribe(update, "subscribe");
        verify(telegramBot).execute(any(SendMessage.class));
    }
}

