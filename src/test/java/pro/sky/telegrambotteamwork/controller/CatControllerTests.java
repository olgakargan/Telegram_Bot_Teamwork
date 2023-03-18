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
import pro.sky.telegrambotteamwork.model.Cat;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CatControllerTests {
    @LocalServerPort
    private int port;
    @MockBean
    private TelegramBot telegramBot;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addCatTest() {
        Cat cat = new Cat(1L, "Алекс", "Британский", 2, "Описание");
        ResponseEntity<Cat> response = formingUrl(constructionUriBuilderCreation().build().toUri(), cat);
        checkingTheCatsForCreation(cat, response);
    }

    @Test
    public void updateCatTest() {
        Cat cat = new Cat(1L, "Алекс", "Британский", 2, "Описание");
        ResponseEntity<Cat> response = formingUrl(constructionUriBuilderCreation().build().toUri(), cat);
        checkingTheCatsForCreation(cat, response);

        String newCatName = "Арчи";
        String newBreed = "Шотландский";
        Cat updateCat = response.getBody();
        updateCat.setCatName(newCatName);
        updateCat.setBreed(newBreed);

        restTemplate.put("http://localhost:" + port + "/api/cat", updateCat);
        ResponseEntity<Cat> updateResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/cat/" + updateCat.getId(), Cat.class);

        Assertions.assertThat(updateResponse.getBody()).isNotNull();
        Assertions.assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(updateResponse.getBody().getCatName()).isEqualTo(newCatName);
        Assertions.assertThat(updateResponse.getBody().getBreed()).isEqualTo(newBreed);

    }

    @Test
    public void findCatTest() {
        Cat cat = new Cat(1L, "Алекс", "Британский", 2, "Описание");
        ResponseEntity<Cat> response = formingUrl(constructionUriBuilderCreation().build().toUri(), cat);
        checkingTheCatsForCreation(cat, response);
        Cat findCat = response.getBody();
        ResponseEntity<Cat> findResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/cat/" + findCat.getId(), Cat.class);
        Assertions.assertThat(findResponse.getBody()).isEqualTo(findCat);
        Assertions.assertThat(findResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private ResponseEntity<Cat> formingUrl(URI uri, Cat cat) {
        return restTemplate.postForEntity(uri, cat, Cat.class);
    }

    private UriComponentsBuilder constructionUriBuilderCreation() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/cat");
    }

    private void checkingTheCatsForCreation(Cat cat, ResponseEntity<Cat> response) {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isEqualTo(cat.getId());
    }

}

