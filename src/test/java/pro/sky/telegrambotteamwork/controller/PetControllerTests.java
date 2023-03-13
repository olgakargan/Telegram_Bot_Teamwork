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
import pro.sky.telegrambotteamwork.model.Pet;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PetControllerTests {
    @LocalServerPort
    private int port;
    @MockBean
    private TelegramBot telegramBot;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addPetTest() {
        Pet pet = new Pet(1L, "Бони", "Волнистый попугай", 2, "Описание");
        ResponseEntity<Pet> response = formingUrl(constructionUriBuilderCreation().build().toUri(), pet);
        checkingThePetsForCreation(pet, response);
    }

    @Test
    public void updatePetTest() {
        Pet pet = new Pet(1L, "Бони", "Волнистый попугай", 2, "Описание");
        ResponseEntity<Pet> response = formingUrl(constructionUriBuilderCreation().build().toUri(), pet);
        checkingThePetsForCreation(pet, response);

        String newPetName = "Клайд";
        String newBreed = "Ара";
        Pet updatePet = response.getBody();
        updatePet.setPetName(newPetName);
        updatePet.setBreed(newBreed);

        restTemplate.put("http://localhost:" + port + "/api/pet", updatePet);
        ResponseEntity<Pet> updateResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/pet/" + updatePet.getId(), Pet.class);

        Assertions.assertThat(updateResponse.getBody()).isNotNull();
        Assertions.assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(updateResponse.getBody().getPetName()).isEqualTo(newPetName);
        Assertions.assertThat(updateResponse.getBody().getBreed()).isEqualTo(newBreed);

    }

    @Test
    public void findPetTest() {
        Pet pet = new Pet(1L, "Бони", "Волнистый попугай", 2, "Описание");
        ResponseEntity<Pet> response = formingUrl(constructionUriBuilderCreation().build().toUri(), pet);
        checkingThePetsForCreation(pet, response);
        Pet findPet = response.getBody();
        ResponseEntity<Pet> findResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/pet/" + findPet.getId(), Pet.class);
        Assertions.assertThat(findResponse.getBody()).isEqualTo(findPet);
        Assertions.assertThat(findResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private ResponseEntity<Pet> formingUrl(URI uri, Pet pet) {
        return restTemplate.postForEntity(uri, pet, Pet.class);
    }

    private UriComponentsBuilder constructionUriBuilderCreation() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/pet");
    }

    private void checkingThePetsForCreation(Pet pet, ResponseEntity<Pet> response) {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isEqualTo(pet.getId());
    }
}
