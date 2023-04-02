package pro.sky.telegrambotteamwork.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambotteamwork.model.Cat;
import pro.sky.telegrambotteamwork.repository.CatRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatServiceTests {
    @Mock
    private CatRepository catRepository;
    @InjectMocks
    private CatService catService;

    public static final Long TEST_ID = 1L;
    public static final Long TEST_ID_2 = 2L;
    public static final String TEST_CAT_NAME = "Алекс";
    public static final String TEST_BREED = "Британский";
    public static final int TEST_YEAR_OF_BIRTH = 2;
    public static final String TEST_DESCRIPTION = "Описание";
    public static final Cat TEST_CAT = new Cat(TEST_CAT_NAME, TEST_BREED, TEST_YEAR_OF_BIRTH, TEST_DESCRIPTION);

    @Test
    public void addCatTest() {
        assertNotNull(catRepository);

        Mockito.when(catRepository.save(TEST_CAT)).thenReturn(TEST_CAT);
        assertEquals(TEST_CAT_NAME, TEST_CAT.getCatName());
        assertEquals(TEST_BREED, TEST_CAT.getBreed());
        assertEquals(TEST_YEAR_OF_BIRTH, TEST_CAT.getYearOfBirth());
        assertEquals(TEST_DESCRIPTION, TEST_CAT.getDescription());
        Assertions.assertThat(TEST_CAT).isEqualTo(catService.addCat(TEST_CAT));

    }

    @Test
    public void updateCatTest() {
        assertNotNull(catRepository);

        Mockito.when(catRepository.findById(TEST_CAT.getId())).thenReturn(Optional.ofNullable(TEST_CAT));
        Mockito.when(catRepository.save(TEST_CAT)).thenReturn(TEST_CAT);
        Cat actual = catService.updateCat(TEST_CAT);
        Assertions.assertThat(actual).isEqualTo(TEST_CAT);
    }

    @Test
    public void findCatTest() {
        assertNotNull(catRepository);

        Mockito.when(catRepository.findById(TEST_ID)).thenReturn(Optional.of(TEST_CAT));
        Assertions.assertThat(TEST_CAT).isEqualTo(catService.findCat(TEST_ID));
        Cat actual = catService.findCat(TEST_ID);

        Assertions.assertThat(actual.getId()).isEqualTo(TEST_CAT.getId());
        Assertions.assertThat(actual.getCatName()).isEqualTo(TEST_CAT.getCatName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(TEST_CAT.getBreed());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(TEST_CAT.getYearOfBirth());
        Assertions.assertThat(actual.getDescription()).isEqualTo(TEST_CAT.getDescription());
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> catService.findCat(TEST_ID_2));
    }

    @Test
    public void deleteCatTest() {
        assertNotNull(catRepository);

        Mockito.when(catRepository.findById(TEST_ID)).thenReturn(Optional.of(TEST_CAT));
        Cat expected = catService.deleteCat(TEST_ID);
        Cat actual = catService.deleteCat(TEST_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
        when(catRepository.findById(TEST_ID)).thenReturn(Optional.of(TEST_CAT));
        expected = TEST_CAT;
        actual = catService.deleteCat(TEST_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}