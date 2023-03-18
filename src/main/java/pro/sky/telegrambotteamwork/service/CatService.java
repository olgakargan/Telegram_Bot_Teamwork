package pro.sky.telegrambotteamwork.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.model.Cat;
import pro.sky.telegrambotteamwork.repository.CatRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Сервис-класс для манипуляций с питомцем - кошкой
 */
@Service
@AllArgsConstructor
public class CatService {
    private final Logger logger = LoggerFactory.getLogger(CatService.class);
    private final CatRepository catRepository;
    private static final Pattern PATTERN = Pattern.compile("([\\W+]+)(\\*)([\\W+]+)(\\*)([0-9]{4})(\\*)([\\W+]+)");

    /**
     * Метод добавления кошки в базу данных
     *
     * @param cat сущность кошки
     * @return Возвращает сохраненную в базу данных кошку
     */
    public Cat addCat(Cat cat) {
        logger.info("Вызов метода добавление кошки: {}", cat);
        return catRepository.save(cat);
    }

    /**
     * Метод редактирования кошки в базе данных
     *
     * @param cat сущность кошки
     * @return Возвращает отредактированную в базе данных кошку
     */
    @CachePut(value = "cats", key = "#cat.id")
    public Cat updateCat(Cat cat) {
        logger.info("Вызван метод редактирования кошки: {}", cat);
        if (catRepository.findById(cat.getId()).orElse(null) == null) {
            return null;
        }
        return catRepository.save(cat);
    }

    /**
     * Метод поиска кошки в базе данных
     *
     * @param id идентификатор искомой кошки
     * @return Возвращает найденную кошку
     */
    @Cacheable("cats")
    public Cat findCat(Long id) {
        logger.info("Вызван метод поиска кошки по id {}", id);
        Cat cat = catRepository.findById(id).orElse(null);
        if (cat == null) {
            throw new NullPointerException();
        }
        return cat;
    }

    /**
     * Метод удаления кошки из базы данных
     *
     * @param id идентификатор кошки
     */
    @CacheEvict("cats")
    public void deleteCat(Long id) {
        logger.info("Вызван метод удаления кошки по id: {}", id);
        catRepository.deleteById(id);
    }

    /**
     * Метод сохранения нового профиля кота/кошки в базу данных.
     * Волонтер вводит имя, породу, год рождения и описание,
     * с помощью регулярного выражения эти данные расчленяются и записываются в переменные,
     * а затем в базу данных.
     *
     * @param message текстовое сообщение от волонтера
     */
    public void saveCat(String message) {
        Matcher matcher = PATTERN.matcher(message);

        if (matcher.matches()) {
            Cat cat = new Cat();
            String catName = matcher.group(1);
            String breed = matcher.group(3);
            String yourOfBirthString = matcher.group(5);
            String description = matcher.group(7);
            int yourOfBirth = Integer.parseInt(yourOfBirthString);
            cat.setCatName(catName);
            cat.setBreed(breed);
            cat.setYearOfBirth(yourOfBirth);
            cat.setDescription(description);
            catRepository.save(cat);
            logger.info("Новый профиль кошки/кота сохранен: " + cat);
        }
    }
}
