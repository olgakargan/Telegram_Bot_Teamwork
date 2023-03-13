package pro.sky.telegrambotteamwork.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.model.Dog;
import pro.sky.telegrambotteamwork.repository.DogRepository;

/**
 * Сервис-класс для манипуляций с питомцем - собакой
 */
@Service
@AllArgsConstructor
public class DogService {
    private final Logger logger = LoggerFactory.getLogger(DogService.class);
    private final DogRepository dogRepository;

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
     * @return Возвращает найденую собаку
     */
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
    public void deleteDog(Long id) {
        logger.info("Вызван метод удаления собаки по id: {}", id);
        dogRepository.deleteById(id);
    }
}