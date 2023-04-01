package pro.sky.telegrambotteamwork.service;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CheckServiceTests {
    @Mock
    private Update update;

    @InjectMocks
    private CheckService checkService;

    @Test
    public void hasMessageTest() {
        assertNotNull(update);
        boolean actual = false;

        Assertions.assertThat(checkService.hasMessage(update)).isNotNull();
        Assertions.assertThat(checkService.hasMessage(update)).isEqualTo(actual);

    }

    @Test
    public void hasCallbackQueryTest() {
        assertNotNull(update);
        boolean actual = false;

        Assertions.assertThat(checkService.hasCallbackQuery(update)).isNotNull();
        Assertions.assertThat(checkService.hasCallbackQuery(update)).isEqualTo(actual);
    }

    @Test
    public void hasContactTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CheckServiceTests.class.getResource("check-contact.json").toURI()));
        Update update = getUpdate(json);
        boolean actual = true;

        assertNotNull(update);
        Assertions.assertThat(checkService.hasContact(update)).isNotNull();
        Assertions.assertThat(checkService.hasContact(update)).isEqualTo(actual);
    }

    @Test
    public void hasTextTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CheckServiceTests.class.getResource("check-text.json").toURI()));
        Update update = getUpdate(json);
        boolean actual = true;

        assertNotNull(update);
        Assertions.assertThat(checkService.hasText(update)).isNotNull();
        Assertions.assertThat(checkService.hasText(update)).isEqualTo(actual);
    }

    @Test
    public void hasMessageCallbackQueryTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CheckServiceTests.class.getResource("check-message-callback-query.json").toURI()));
        Update update = getUpdate(json);
        boolean actual = true;

        assertNotNull(update);
        Assertions.assertThat(checkService.hasMessageCallbackQuery(update)).isNotNull();
        Assertions.assertThat(checkService.hasMessageCallbackQuery(update)).isEqualTo(actual);
    }

    @Test
    public void hasCaptionTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(CheckServiceTests.class.getResource("check-caption.json").toURI()));
        Update update = getUpdate(json);
        boolean actual = true;

        assertNotNull(update);
        Assertions.assertThat(checkService.hasCaption(update)).isNotNull();
        Assertions.assertThat(checkService.hasCaption(update)).isEqualTo(actual);
    }

    private Update getUpdate(String json) {
        return BotUtils.fromJson(json, Update.class);
    }
}