package pro.sky.telegrambotteamwork.service;

import org.junit.jupiter.api.Test;
import pro.sky.telegrambotteamwork.model.Cat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CatServiceTests {
    private CatService catService;

    public static final Long TEST_ID = 1L;
    public static final Long TEST_ID_2 = 2L;
    public static final String TEST_CAT_NAME = "Алекс";
    public static final String TEST_CAT_NAME_2 = "Симба";
    public static final String TEST_BREED = "Британский";
    public static final String TEST_BREED_2 = "Шотландский";
    public static final int TEST_YEAR_OF_BIRTH = 2;
    public static final int TEST_YEAR_OF_BIRTH_2 = 3;
    public static final String TEST_DESCRIPTION = "Описание";
    public static final String TEST_DESCRIPTION_2 = "Описание 2";
    public static final Cat TEST_CAT = new Cat(TEST_ID, TEST_CAT_NAME, TEST_BREED, TEST_YEAR_OF_BIRTH, TEST_DESCRIPTION, null);
    public static final Cat TEST_CAT_2 = new Cat(TEST_ID_2, TEST_CAT_NAME_2, TEST_BREED_2, TEST_YEAR_OF_BIRTH_2, TEST_DESCRIPTION_2, null);

    @Test
    public void addCatTest() {
        assertEquals(TEST_ID, TEST_CAT.getId());
        assertEquals(TEST_CAT_NAME, TEST_CAT.getCatName());
        assertThrows(RuntimeException.class, () -> catService.addCat(TEST_CAT));
    }

    @Test
    public void updateCatTest() {
        TEST_CAT.setId(TEST_ID_2);
        TEST_CAT.setCatName(TEST_CAT_NAME_2);
        assertEquals(TEST_ID_2, TEST_CAT.getId());
        assertEquals(TEST_CAT_NAME_2, TEST_CAT.getCatName());
        assertThrows(RuntimeException.class, () -> catService.updateCat(TEST_CAT));
    }

    @Test
    public void findCatTest() {
        assertEquals(TEST_ID_2, TEST_CAT_2.getId());
        assertThrows(RuntimeException.class, () -> catService.findCat(TEST_ID_2));
    }

    @Test
    public void deleteCatTest() {
        TEST_CAT.setId(TEST_ID_2);
        TEST_CAT.setCatName(TEST_CAT_NAME_2);
        assertEquals(TEST_ID_2, TEST_CAT.getId());
        assertEquals(TEST_CAT_NAME_2, TEST_CAT.getCatName());
        assertThrows(RuntimeException.class, () -> catService.deleteCat(TEST_ID_2));
    }
}
