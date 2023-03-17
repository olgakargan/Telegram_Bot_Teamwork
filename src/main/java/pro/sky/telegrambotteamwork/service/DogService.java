package pro.sky.telegrambotteamwork.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.model.Dog;
import pro.sky.telegrambotteamwork.repository.DogRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сервис-класс для манипуляций с питомцем - собакой
 */
@Service
@AllArgsConstructor
public class DogService {
    private final Logger logger = LoggerFactory.getLogger(DogService.class);
    private final DogRepository dogRepository;
    private static final Pattern PATTERN = Pattern.compile("([\\W+]+)(\\/)([\\W+]+)(\\/)([0-9]{4})(\\/)([\\W+]+)(\\/)([\\W+]+)");

    /**
     * Метод добавления собаки в базу данных
     *
     * @param dog сущность собаки
     * @return Возвращает сохраненную в базу данных собаку
     */
    public Dog addDog(Dog dog) {
        logger.info("Вызов метода добавление собаки: {}", dog);
        return dogRepository.save(dog);
    }

    /**
     * Метод редактирования собаки в базе данных
     *
     * @param dog сущность собаки
     * @return Возвращает отредактированную в базе данных собаку
     */
    @CachePut(value = "dogs", key = "#dog.id")
    public Dog updateDog(Dog dog) {
        logger.info("Вызван метод редактирования собаки: {}", dog);
        if (dogRepository.findById(dog.getId()).orElse(null) == null) {
            return null;
        }
        return dogRepository.save(dog);
    }

    /**
     * Метод поиска собаки в базе данных
     *
     * @param id идентификатор искомой собаки
     * @return Возвращает найденную собаку
     */
    @Cacheable("dogs")
    public Dog findDog(Long id) {
        logger.info("Вызван метод поиска собаки по id {}", id);
        Dog dog = dogRepository.findById(id).orElse(null);
        if (dog == null) {
            throw new NullPointerException();
        }
        return dog;
    }

    /**
     * Метод удаления собаки из базы данных
     *
     * @param id идентификатор собаки
     */
    @CacheEvict("dogs")
    public void deleteDog(Long id) {
        logger.info("Вызван метод удаления собаки по id: {}", id);
        dogRepository.deleteById(id);
    }

    /**
     * Метод сохранения нового профиля собаки в базу данных.
     * Волонтер вводит имя, породу, год рождения и описание,
     * с помощью регулярного выражения эти данные расчленяются и записываются в переменные,
     * а затем в базу данных.
     *
     * @param message текстовое сообщение от волонтера
     */
    public void saveDog(String message) {
        Matcher matcher = PATTERN.matcher(message);

        if (matcher.matches()) {
            Dog dog = new Dog();
            String dogName = matcher.group(1);
            String breed = matcher.group(3);
            String yourOfBirthString = matcher.group(5);
            String floor = matcher.group(7);
            String description = matcher.group(9);
            int yourOfBirth = Integer.parseInt(yourOfBirthString);
            dog.setDogName(dogName);
            dog.setBreed(breed);
            dog.setYearOfBirth(yourOfBirth);
            dog.setFloor(floor);
            dog.setDescription(description);
            dogRepository.save(dog);
            logger.info("Новый профиль собаки сохранен: " + dog);
        }
    }
}
