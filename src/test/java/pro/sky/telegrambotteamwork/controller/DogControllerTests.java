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
import pro.sky.telegrambotteamwork.model.Dog;

import java.net.URI;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DogControllerTests {
    @LocalServerPort
    private int port;
    @MockBean
    private TelegramBot telegramBot;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addDogTest() {
        Dog dog = new Dog(1L, "Алекс", "Немецкая овчарка", 2, "Описание");
        ResponseEntity<Dog> response = formingUrl(constructionUriBuilderCreation().build().toUri(), dog);
        checkingTheDogsForCreation(dog, response);
    }

    @Test
    public void updateDogTest() {
        Dog dog = new Dog(1L, "Алекс", "Немецкая овчарка", 2, "Описание");
        ResponseEntity<Dog> response = formingUrl(constructionUriBuilderCreation().build().toUri(), dog);
        checkingTheDogsForCreation(dog, response);

        String newDogName = "Арчи";
        String newBreed = "Кавказская овчарка";
        Dog updateDog = response.getBody();
        updateDog.setDogName(newDogName);
        updateDog.setBreed(newBreed);

        restTemplate.put("http://localhost:" + port + "/api/dog", updateDog);
        ResponseEntity<Dog> updateResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/dog/" + updateDog.getId(), Dog.class);

        Assertions.assertThat(updateResponse.getBody()).isNotNull();
        Assertions.assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(updateResponse.getBody().getDogName()).isEqualTo(newDogName);
        Assertions.assertThat(updateResponse.getBody().getBreed()).isEqualTo(newBreed);

    }

    @Test
    public void findDogTest() {
        Dog dog = new Dog(1L, "Алекс", "Немецкая овчарка", 2, "Описание");
        ResponseEntity<Dog> response = formingUrl(constructionUriBuilderCreation().build().toUri(), dog);
        checkingTheDogsForCreation(dog, response);
        Dog findDog = response.getBody();
        ResponseEntity<Dog> findResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/dog/" + findDog.getId(), Dog.class);
        Assertions.assertThat(findResponse.getBody()).isEqualTo(findDog);
        Assertions.assertThat(findResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteDogTest() {
        Dog dog = new Dog(1L, "Арчи", "Кавказская овчарка", 2, "Описание");
        ResponseEntity<Dog> response = formingUrl(constructionUriBuilderCreation().build().toUri(), dog);
        checkingTheDogsForCreation(dog, response);
        Dog deleteDog = response.getBody();
        ResponseEntity<Dog> deleteResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/dog/" + deleteDog.getId(), Dog.class);
        Assertions.assertThat(deleteResponse.getBody()).isNotNull();
        Assertions.assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deleteResponse.getBody()).isEqualTo(deleteDog);
    }

    private ResponseEntity<Dog> formingUrl(URI uri, Dog dog) {
        return restTemplate.postForEntity(uri, dog, Dog.class);
    }

    private UriComponentsBuilder constructionUriBuilderCreation() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/dog");
    }

    private void checkingTheDogsForCreation(Dog dog, ResponseEntity<Dog> response) {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isEqualTo(dog.getId());
    }

}