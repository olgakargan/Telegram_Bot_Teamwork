package pro.sky.telegrambotteamwork.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambotteamwork.model.Dog;
import pro.sky.telegrambotteamwork.repository.DogRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DogServiceTests {
    @Mock
    private DogRepository dogRepository;
    @InjectMocks
    private DogService dogService;

    public static final Long TEST_ID = 3L;
    public static final Long TEST_ID_2 = 4L;
    public static final String TEST_DOG_NAME = "Алекс";
    public static final String TEST_BREED = "Немецкая овчарка";
    public static final int TEST_YEAR_OF_BIRTH = 2;
    public static final String TEST_DESCRIPTION = "Описание";
    public static final Dog TEST_DOG = new Dog(TEST_DOG_NAME, TEST_BREED, TEST_YEAR_OF_BIRTH, TEST_DESCRIPTION);

    @Test
    public void addDogTest() {
        assertNotNull(dogRepository);

        Mockito.when(dogRepository.save(TEST_DOG)).thenReturn(TEST_DOG);
        assertEquals(TEST_DOG_NAME, TEST_DOG.getDogName());
        assertEquals(TEST_BREED, TEST_DOG.getBreed());
        assertEquals(TEST_YEAR_OF_BIRTH, TEST_DOG.getYearOfBirth());
        assertEquals(TEST_DESCRIPTION, TEST_DOG.getDescription());
        Assertions.assertThat(TEST_DOG).isEqualTo(dogService.addDog(TEST_DOG));
    }

    @Test
    public void updateCatTest() {
        assertNotNull(dogRepository);

        Mockito.when(dogRepository.findById(TEST_DOG.getId())).thenReturn(Optional.ofNullable(TEST_DOG));
        Mockito.when(dogRepository.save(TEST_DOG)).thenReturn(TEST_DOG);
        Dog actual = dogService.updateDog(TEST_DOG);
        Assertions.assertThat(actual).isEqualTo(TEST_DOG);
    }

    @Test
    public void findCatTest() {
        assertNotNull(dogRepository);

        Mockito.when(dogRepository.findById(TEST_ID)).thenReturn(Optional.of(TEST_DOG));
        Assertions.assertThat(TEST_DOG).isEqualTo(dogService.findDog(TEST_ID));
        Dog actual = dogService.findDog(TEST_ID);

        Assertions.assertThat(actual.getId()).isEqualTo(TEST_DOG.getId());
        Assertions.assertThat(actual.getDogName()).isEqualTo(TEST_DOG.getDogName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(TEST_DOG.getBreed());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(TEST_DOG.getYearOfBirth());
        Assertions.assertThat(actual.getDescription()).isEqualTo(TEST_DOG.getDescription());
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> dogService.findDog(TEST_ID_2));
    }

    @Test
    public void deleteCatTest() {
        assertNotNull(dogRepository);

        Mockito.when(dogRepository.findById(TEST_ID)).thenReturn(Optional.of(TEST_DOG));
        Dog expected = dogService.deleteDog(TEST_ID);
        Dog actual = dogService.deleteDog(TEST_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
        when(dogRepository.findById(TEST_ID)).thenReturn(Optional.of(TEST_DOG));
        expected = TEST_DOG;
        actual = dogService.deleteDog(TEST_ID);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

}