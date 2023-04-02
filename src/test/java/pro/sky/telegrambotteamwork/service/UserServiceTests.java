package pro.sky.telegrambotteamwork.service;


import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.model.Update;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.UserRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    private static final Long TEST_ID = 1L;
    private static final Long TEST_ID_2 = 123L;
    private static final String TEST_FIRST_NAME = "Иван";
    private static final String TEST_LAST_NAME = "Иванов";
    private static final String TEST_USER_NAME = "ivanIvanov";
    private static final Long TEST_USER_ID = 12345L;
    private static final Long TEST_CHAT_ID = 67890L;
    private final User TEST_USER = new User(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_USER_NAME, TEST_USER_ID, TEST_CHAT_ID);


    @Test
    public void addUserTest() {
        assertNotNull(userRepository);

        Mockito.when(userRepository.save(TEST_USER)).thenReturn(TEST_USER);
        assertEquals(TEST_FIRST_NAME, TEST_USER.getFirstName());
        assertEquals(TEST_LAST_NAME, TEST_USER.getLastName());
        assertEquals(TEST_USER_NAME, TEST_USER.getUserName());
        assertEquals(TEST_USER_ID, TEST_USER.getUserId());
        assertEquals(TEST_CHAT_ID, TEST_USER.getChatId());
        Assertions.assertThat(TEST_USER).isEqualTo(userService.addUser(TEST_USER));
    }

    @Test
    public void updateUserTest() {
        assertNotNull(userRepository);

        Mockito.when(userRepository.findById(TEST_USER.getId())).thenReturn(Optional.ofNullable(TEST_USER));
        Mockito.when(userRepository.save(TEST_USER)).thenReturn(TEST_USER);
        User actual = userService.updateUser(TEST_USER);
        Assertions.assertThat(actual).isEqualTo(TEST_USER);
    }

    @Test
    public void findUserTest() {
        assertNotNull(userRepository);

        Mockito.when(userRepository.findById(TEST_ID)).thenReturn(Optional.of(TEST_USER));
        Assertions.assertThat(TEST_USER).isEqualTo(userService.findUser(TEST_ID));
        User actual = userService.findUser(TEST_ID);

        Assertions.assertThat(actual.getId()).isEqualTo(TEST_USER.getId());
        Assertions.assertThat(actual.getFirstName()).isEqualTo(TEST_USER.getFirstName());
        Assertions.assertThat(actual.getLastName()).isEqualTo(TEST_USER.getLastName());
        Assertions.assertThat(actual.getUserName()).isEqualTo(TEST_USER.getUserName());
        Assertions.assertThat(actual.getUserId()).isEqualTo(TEST_USER.getUserId());
        Assertions.assertThat(actual.getChatId()).isEqualTo(TEST_USER.getChatId());
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userService.findUser(TEST_ID_2));

    }

    @Test
    public void saveUserTest() throws URISyntaxException, IOException {
        String json = Files.readString(Paths.get(UserServiceTests.class.getResource("save-user.json").toURI()));
        Update update = getUpdate(json);
        User user = new User("Иван", "Иванов", "userName", 12345L, 12345L);

        assertNotNull(userRepository);
        Assertions.assertThat(user.getFirstName()).isEqualTo(update.message().contact().firstName());
        Assertions.assertThat(user.getLastName()).isEqualTo(update.message().contact().lastName());
        Assertions.assertThat(user.getUserName()).isEqualTo(update.message().from().username());
        Assertions.assertThat(user.getUserId()).isEqualTo(update.message().contact().userId());
        Assertions.assertThat(user.getChatId()).isEqualTo(update.message().chat().id());
    }

    private Update getUpdate(String json) {
        return BotUtils.fromJson(json, Update.class);
    }
}