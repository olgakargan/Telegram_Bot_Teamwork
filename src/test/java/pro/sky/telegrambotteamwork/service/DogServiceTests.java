package pro.sky.telegrambotteamwork.service;


import org.junit.jupiter.api.Test;
import pro.sky.telegrambotteamwork.model.Dog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DogServiceTests {
    private DogService dogService;

    public static final Long TEST_ID = 3L;
    public static final Long TEST_ID_2 = 4L;
    public static final String TEST_DOG_NAME = "Алекс";
    public static final String TEST_DOG_NAME_2 = "Симба";
    public static final String TEST_BREED = "Немецкая овчарка";
    public static final String TEST_BREED_2 = "Такса";
    public static final int TEST_YEAR_OF_BIRTH = 2;
    public static final int TEST_YEAR_OF_BIRTH_2 = 3;
    public static final String TEST_DESCRIPTION = "Описание";
    public static final String TEST_DESCRIPTION_2 = "Описание 2";
    public static final Dog TEST_DOG = new Dog(TEST_ID, TEST_DOG_NAME, TEST_BREED, TEST_YEAR_OF_BIRTH, TEST_DESCRIPTION);
    public static final Dog TEST_DOG_2 = new Dog(TEST_ID_2, TEST_DOG_NAME_2, TEST_BREED_2, TEST_YEAR_OF_BIRTH_2, TEST_DESCRIPTION_2);

    @Test
    public void addDogTest() {
        assertEquals(TEST_ID, TEST_DOG.getId());
        assertEquals(TEST_DOG_NAME, TEST_DOG.getDogName());
        assertThrows(RuntimeException.class, () -> dogService.addDog(TEST_DOG));
    }

    @Test
    public void updateCatTest() {
        TEST_DOG.setId(TEST_ID_2);
        TEST_DOG.setDogName(TEST_DOG_NAME_2);
        assertEquals(TEST_ID_2, TEST_DOG.getId());
        assertEquals(TEST_DOG_NAME_2, TEST_DOG.getDogName());
        assertThrows(RuntimeException.class, () -> dogService.updateDog(TEST_DOG));
    }

    @Test
    public void findCatTest() {
        assertEquals(TEST_ID_2, TEST_DOG_2.getId());
        assertThrows(RuntimeException.class, () -> dogService.findDog(TEST_ID_2));
    }

    @Test
    public void deleteCatTest() {
        TEST_DOG.setId(TEST_ID_2);
        TEST_DOG.setDogName(TEST_DOG_NAME_2);
        assertEquals(TEST_ID_2, TEST_DOG.getId());
        assertEquals(TEST_DOG_NAME_2, TEST_DOG.getDogName());
        assertThrows(RuntimeException.class, () -> dogService.deleteDog(TEST_ID_2));
    }
}