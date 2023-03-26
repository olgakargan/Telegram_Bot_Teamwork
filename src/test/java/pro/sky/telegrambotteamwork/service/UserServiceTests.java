package pro.sky.telegrambotteamwork.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.UserRepository;

import java.util.Optional;

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
    private final User user = new User(TEST_ID, TEST_FIRST_NAME, TEST_LAST_NAME, TEST_USER_NAME, TEST_USER_ID, TEST_CHAT_ID);


    @Test
    public void addUserTest() {
        assertNotNull(userRepository);

        Mockito.when(userRepository.save(user)).thenReturn(user);
        org.assertj.core.api.Assertions.assertThat(user).isEqualTo(userService.addUser(user));
    }

    @Test
    public void findUserTest() {
        assertNotNull(userRepository);

        Mockito.when(userRepository.findById(TEST_ID)).thenReturn(Optional.of(user));
        org.assertj.core.api.Assertions.assertThat(user).isEqualTo(userService.findUser(TEST_ID));
        org.assertj.core.api.Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> userService.findUser(TEST_ID_2));

    }
}
