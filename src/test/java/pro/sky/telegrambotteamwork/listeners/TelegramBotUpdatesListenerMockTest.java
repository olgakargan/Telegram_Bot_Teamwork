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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambotteamwork.repository.UserRepository;
import pro.sky.telegrambotteamwork.service.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

import static org.mockito.Mockito.verify;
import static pro.sky.telegrambotteamwork.constants.CommandMessageUserConstant.START;
import static pro.sky.telegrambotteamwork.constants.KeyboardMessageUserConstant.*;
import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerMockTest {

    @Mock
    private TelegramBot telegramBot;
    @Mock
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

    @Test
    public void handleStartTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("text_update.json").toURI()));
        Update update = getUpdate(json, START);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuDogAndCat(update, WELCOME_MESSAGE, CHOOSING_PET_MENU);
    }

    @Test
    public void callbackQueryDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-dog.json").toURI()));
        Update update = getUpdate(json, DOG);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuCallbackQuery(update, DOG_MESSAGE, MAIN_DOG_MENU);

    }

    @Test
    public void callbackQueryCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-cat.json").toURI()));
        Update update = getUpdate(json, CAT);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuCallbackQuery(update, CAT_MESSAGE, MAIN_CAT_MENU);
    }

    @Test
    public void callbackQueryAnotherPetTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-another-pet.json").toURI()));
        Update update = getUpdate(json, ANOTHER_PET);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot.execute(argumentCaptor.capture()));
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(1L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo("Информация о том, каких питомцев еще можно взять");
    }

    @Test
    public void callbackQueryInfoMenuDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-info-menu-dog.json").toURI()));
        Update update = getUpdate(json, INFORMATION_ABOUT_THE_SHELTER_DOG);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuCallbackQuery(update, INFORMATION_ABOUT_THE_SHELTER_MESSAGE, INFORMATION_MENU_DOG);
    }

    @Test
    public void callbackQueryTakeFromShelterMenuDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query-take-from-shelter-menu-dog.json").toURI()));
        Update update = getUpdate(json, TAKE_A_FROM_A_SHELTER_DOG);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuCallbackQuery(update, TAKE_A_PET_FROM_A_SHELTER_MESSAGE, TAKE_A_FROM_A_SHELTER_DOG_MENU);
    }

    @Test
    public void callbackQueryReportMenuDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, PET_REPORT_DOG);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuCallbackQuery(update, PET_REPORT_MESSAGE, DOG_REPORT_MENU);
    }

    @Test
    public void callbackQueryCallVolunteerMenuDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, CALL_A_VOLUNTEER_DOG);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuCallbackQuery(update, CALL_A_VOLUNTEER_MESSAGE, CALL_A_VOLUNTEER_DOG_MENU);
    }

    @Test
    public void callbackQueryGoBackMenuDogTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_DOG);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuDogAndCatCallbackQuery(update, WELCOME_MESSAGE, CHOOSING_PET_MENU);
    }

    @Test
    public void callbackQueryInfoMenuCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, INFORMATION_ABOUT_THE_SHELTER_CAT);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuCallbackQuery(update, INFORMATION_ABOUT_THE_SHELTER_MESSAGE, INFORMATION_MENU_CAT);
    }

    @Test
    public void callbackQueryTakeFromShelterMenuCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, TAKE_A_FROM_A_SHELTER_CAT);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuCallbackQuery(update, TAKE_A_PET_FROM_A_SHELTER_MESSAGE, TAKE_A_FROM_A_SHELTER_CAT_MENU);
    }

    @Test
    public void callbackQueryReportMenuCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, PET_REPORT_CAT);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuCallbackQuery(update, PET_REPORT_MESSAGE, CAT_REPORT_MENU);
    }

    @Test
    public void callbackQueryCallVolunteerMenuCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, CALL_A_VOLUNTEER_CAT);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuCallbackQuery(update, CALL_A_VOLUNTEER_MESSAGE, CALL_A_VOLUNTEER_CAT_MENU);
    }

    @Test
    public void callbackQueryGoBackMenuCatTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("callback-query.json").toURI()));
        Update update = getUpdate(json, GO_BACK_CAT);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        verify(menuService).loadingTheMenuDogAndCatCallbackQuery(update, WELCOME_MESSAGE, CHOOSING_PET_MENU);
    }


    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }
}
