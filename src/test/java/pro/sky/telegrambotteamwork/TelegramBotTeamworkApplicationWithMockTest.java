package pro.sky.telegrambotteamwork;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambotteamwork.controller.PetController;
import pro.sky.telegrambotteamwork.model.Pet;
import pro.sky.telegrambotteamwork.repository.PetRepository;
import pro.sky.telegrambotteamwork.service.PetService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class TelegramBotTeamworkApplicationWithMockTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetRepository petRepository;
    @SpyBean
    private PetService petService;
    @InjectMocks
    private PetController petController;

    @Test  //протестируем внесения нового животного в базу без внесения данных в реальную БД
    public void addNewTest() throws Exception {
        Long id = 1L;
        String pet_name = "ТестовыйШарик";
        String breed = "Двортерьер";
        int yearOfBirth = 2020;
        String description = "Весёлый, добрый";

        JSONObject petObject = new JSONObject();
        petObject.put("pet_name", pet_name);
        petObject.put("breed", breed);
        petObject.put("yearOfBirth", yearOfBirth);
        petObject.put("description", description);

        Pet pet = new Pet();
        pet.setId(id);
//        pet.setPet_name(pet_name);
        pet.setBreed(breed);
        pet.setYearOfBirth(yearOfBirth);
        pet.setDescription(description);

        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(petRepository.findAll()).thenReturn(List.of(pet));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/pet") //send
                        .content(petObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); //receive
    }

    @Test  //протестируем внесение изменений в данные о животном без внесения данных в реальную БД
    public void updatePetTest() throws Exception {
        Long id = 2L;
        String pet_name = "Бобик";
        String breed = "Двортерьер";
        int yearOfBirth = 2022;
        String description = "Весёлый, добрый";

        JSONObject petObject = new JSONObject();
        petObject.put("pet_name", pet_name);
        petObject.put("breed", breed);
        petObject.put("yearOfBirth", yearOfBirth);
        petObject.put("description", description);

        Pet pet = new Pet();
        pet.setId(id);
//        pet.setPet_name(pet_name);
        pet.setBreed(breed);
        pet.setYearOfBirth(yearOfBirth);
        pet.setDescription(description);

        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(petRepository.findAll()).thenReturn(List.of(pet));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/pet") //send
                        .content(petObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); //receive
    }
}
