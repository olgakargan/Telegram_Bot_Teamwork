package pro.sky.telegrambotteamwork.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.model.Dog;
import pro.sky.telegrambotteamwork.model.Image;
import pro.sky.telegrambotteamwork.repository.DogRepository;
import pro.sky.telegrambotteamwork.repository.ImageRepository;
import pro.sky.telegrambotteamwork.repository.UserRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pro.sky.telegrambotteamwork.constants.KeyboardMessageUserConstant.DOG_CATALOG;
import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.MESSAGE_AFTER_ADDING_TEXT_DOG;
import static pro.sky.telegrambotteamwork.constants.TextMessageUserConstant.MESSAGE_AFTER_ADDING_TEXT_DOG_2;

/**
 * Сервис-класс для манипуляций с питомцем - собакой
 */
@Service
@AllArgsConstructor
public class DogService {
    private final Logger logger = LoggerFactory.getLogger(DogService.class);
    private final DogRepository dogRepository;
    private final ImageRepository imageRepository;
    private final MenuService menuService;
    private final UserRepository userRepository;
    private final TelegramBot telegramBot;
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
    public Dog deleteDog(Long id) {
        logger.info("Вызван метод удаления собаки по id: {}", id);
        Dog dog = findDog(id);
        if (dog != null) {
            dogRepository.delete(dog);
        }
        return dog;
    }

    /**
     * Метод сохранения нового профиля собаки в базу данных.
     * Волонтер вводит имя, породу, год рождения и описание,
     * с помощью регулярного выражения эти данные расчленяются и записываются в переменные,
     * а затем в базу данных.
     *
     * @param update входящее обновление
     */
    public void saveDog(Update update) {
        Matcher matcher = PATTERN.matcher(update.message().text());

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
            telegramBot.execute(new SendMessage(update.message().chat().id(), MESSAGE_AFTER_ADDING_TEXT_DOG + dog.getId() + MESSAGE_AFTER_ADDING_TEXT_DOG_2));
            logger.info("Новый профиль собаки сохранен: " + dog);
        }
    }

    /**
     * Этот метод выводит список имеющихся в базе собак, которых можно приютить
     *
     * @param update входящее обновление
     */
    public void findAllImagesAndDescriptionDogs(Update update) {
        List<Image> images = imageRepository.findAll();

        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).getDog() != null && DOG_CATALOG.equals(update.callbackQuery().data())) {
                telegramBot.execute(new SendPhoto(update.callbackQuery().message().chat().id(), images.get(i).getBytes())
                        .caption("Кличка: " + images.get(i).getDog().getDogName() +
                                "\nПорода: " + images.get(i).getDog().getBreed() +
                                "\nГод рождения: " + images.get(i).getDog().getYearOfBirth() +
                                "\nПол: " + images.get(i).getDog().getFloor() +
                                "\nКраткое описание: " + images.get(i).getDog().getDescription()));
            }
        }
    }
}
