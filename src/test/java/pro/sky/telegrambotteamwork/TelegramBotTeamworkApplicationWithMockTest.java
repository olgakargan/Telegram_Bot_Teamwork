package pro.sky.telegrambotteamwork;
import com.pengrad.telegrambot.TelegramBot;
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
import pro.sky.telegrambotteamwork.controller.*;
import pro.sky.telegrambotteamwork.listeners.TelegramBotUpdatesListener;
import pro.sky.telegrambotteamwork.model.Cat;
import pro.sky.telegrambotteamwork.model.Pet;
import pro.sky.telegrambotteamwork.model.ReportData;
import pro.sky.telegrambotteamwork.repository.*;
import pro.sky.telegrambotteamwork.service.*;
import pro.sky.telegrambotteamwork.service.ReportDataService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.Objects;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
    @MockBean
    private CatRepository catRepository;
    @MockBean
    private DogRepository dogRepository;
    @MockBean
    private ReportDataRepository reportDataRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TelegramBot telegramBot;

    @SpyBean
    private PetService petService;
    @SpyBean
    private CatService catService;
    @SpyBean
    private DogService dogService;
    @SpyBean
    private TelegramBotUpdatesListener telegramBotUpdatesListener;
    @SpyBean
    private ReportDataService reportDataService;
    @SpyBean
    private UserService userService;
    @SpyBean
    private MenuService menuService;
    @SpyBean
    CheckService checkService;

    @InjectMocks
    private PetController petController;
    @InjectMocks
    private CatController catController;
    @InjectMocks
    private DogController dogController;
    @InjectMocks
    private ReportDataController reportDataController;
    @InjectMocks
    private UserController userController;

    TelegramBotTeamworkApplicationWithMockTest() {
    }

    @Test  //протестируем внесения нового животного в базу без внесения данных в реальную БД
    public void addNewTest() throws Exception {
        Long id = 1L;
        String petName = "ТестовыйШарик";
        String breed = "Двортерьер";
        int yearOfBirth = 2020;
        String description = "Весёлый, добрый";

        JSONObject petObject = new JSONObject();
        petObject.put("pet_name", petName);
        petObject.put("breed", breed);
        petObject.put("yearOfBirth", yearOfBirth);
        petObject.put("description", description);

        Pet pet = new Pet();
        pet.setId(id);
        pet.setPetName(petName);
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
        String petName = "Бобик";
        String breed = "Двортерьер";
        int yearOfBirth = 2022;
        String description = "Весёлый, добрый";

        JSONObject petObject = new JSONObject();
        petObject.put("pet_name", petName);
        petObject.put("breed", breed);
        petObject.put("yearOfBirth", yearOfBirth);
        petObject.put("description", description);

        Pet pet = new Pet();
        pet.setId(id);
        pet.setPetName(petName);
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

    @Test  //протестируем внесения нового животного в базу без внесения данных в реальную БД
    public void addNewCatTest() throws Exception {
        Long id = 1L;
        String catName = "ТестовыйКот";
        String breed = "Барсик";
        int yearOfBirth = 2020;
        String description = "Весёлый, добрый";

        JSONObject catObject = new JSONObject();
        catObject.put("cat_name", catName);
        catObject.put("breed", breed);
        catObject.put("yearOfBirth", yearOfBirth);
        catObject.put("description", description);

        Cat cat = new Cat();
        cat.setId(id);
        cat.setCatName(catName);
        cat.setBreed(breed);
        cat.setYearOfBirth(yearOfBirth);
        cat.setDescription(description);

        when(catRepository.save(any(Cat.class))).thenReturn(cat);
        when(catRepository.findAll()).thenReturn(List.of(cat));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/cat") //send
                        .content(catObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); //receive
    }





}
