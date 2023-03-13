package pro.sky.telegrambotteamwork.listeners;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

import static pro.sky.telegrambotteamwork.constants.CommandMessageUserConstant.START;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TelegramBotUpdatesListenerMockTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Test
    public void handleStartTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("text_update.json").toURI()));
        Update update = getUpdate(json, START);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

//        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
//        Mockito.verify(telegramBot.execute(argumentCaptor.capture()));
//        SendMessage actual = argumentCaptor.getValue();
//
//        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
//        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(WELCOME_MESSAGE);
    }

    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }
}
