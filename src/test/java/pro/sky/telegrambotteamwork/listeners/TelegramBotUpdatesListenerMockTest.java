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
import org.springframework.test.context.ActiveProfiles;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;

import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.*;



@NotNull

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TelegramBotUpdatesListenerMockTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;


    @Test
    public void handleStartTest(Object SELECT_TEXT) throws IOException, URISyntaxException {
        String json = Files.readString(
                Paths.get(Objects.requireNonNull(TelegramBotUpdatesListenerMockTest.class.getResource("text_update.json")).toURI()));
        Update update = getUpdateMessage(json, "/start");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(1234567809L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(WELCOME_MESSAGE);
    }

    @Test
    public void  handleCatShelterSelectTest() throws IOException,URISyntaxException {
        String json = Files.readString(
                Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("data_update.json").toURI()));
        Update update = getUpdateMessage(json, INFORMATION_ABOUT_THE_SHELTER_MESSAGE);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, Mockito.times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(1234567809L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(CAT_MESSAGE);
    }

    @Test
    public void handleDogShelterSelectTest() throws URISyntaxException, IOException {
        String json = Files.readString(
                Paths.get(TelegramBotUpdatesListenerMockTest.class.getResource("id.json").toURI()));
        Update update = getUpdateMessage(json, INFORMATION_ABOUT_THE_SHELTER_MESSAGE);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, Mockito.times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(1234567809L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(DOG_MESSAGE);
    }


    private Update getUpdateMessage(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }

}