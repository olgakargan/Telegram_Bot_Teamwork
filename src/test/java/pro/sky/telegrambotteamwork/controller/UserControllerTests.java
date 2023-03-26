package pro.sky.telegrambotteamwork.controller;

import com.pengrad.telegrambot.TelegramBot;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;
import pro.sky.telegrambotteamwork.model.User;

import java.net.URI;
import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTests {
    @LocalServerPort
    private int port;
    @MockBean
    private TelegramBot telegramBot;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addUserTest() {
        User user = new User(1L, "Иван", "Иванов", "@ivanIvanov", 123456789L, 987654321L);
        ResponseEntity<User> response = formingUrl(constructionUriBuilderCreation().build().toUri(), user);
        checkingTheUsersForCreation(user, response);
    }

    @Test
    public void findUserTest() {
        User user = new User(1L, "Иван", "Иванов", "@ivanIvanov", 123456789L, 987654321L);
        ResponseEntity<User> response = formingUrl(constructionUriBuilderCreation().build().toUri(), user);
        checkingTheUsersForCreation(user, response);
        User findUser = response.getBody();
        ResponseEntity<User> findResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/user/" + findUser.getId(), User.class);
        Assertions.assertThat(findResponse.getBody()).isEqualTo(findUser);
        Assertions.assertThat(findResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteUserTest() {
        User user = new User(1L, "Иван", "Иванов", "@ivanIvanov", 123456789L, 987654321L);
        user.setReportDataset(new ArrayList<>());
        ResponseEntity<User> response = formingUrl(constructionUriBuilderCreation().build().toUri(), user);
        checkingTheUsersForCreation(user, response);

        User deleteUser = response.getBody();
        ResponseEntity<User> deleteResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/user/" + deleteUser.getId(), User.class);

        Assertions.assertThat(deleteResponse.getBody()).isNotNull();
        Assertions.assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deleteResponse.getBody()).isEqualTo(deleteUser);
    }

    private ResponseEntity<User> formingUrl(URI uri, User user) {
        return restTemplate.postForEntity(uri, user, User.class);
    }

    private UriComponentsBuilder constructionUriBuilderCreation() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/user");
    }

    private void checkingTheUsersForCreation(User user, ResponseEntity<User> response) {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isEqualTo(user.getId());
    }


}
