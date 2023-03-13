package pro.sky.telegrambotteamwork.service;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.model.Pet;
import pro.sky.telegrambotteamwork.repository.PetRepository;

/**
 * Сервис-класс для манипуляций с питомцем
 */
@Service
@AllArgsConstructor
public class PetService {
    private final Logger logger = LoggerFactory.getLogger(PetService.class);
    private final PetRepository petRepository;

    /**
     * Метод добавления питомца в базу данных
     *
     * @param pet сущность питомца
     * @return Возвращает сохраненного в базе данных питомца
     */
    public Pet addPet(Pet pet) {
        logger.info("Вызов метода добавление питомца: {}", pet);
        return petRepository.save(pet);
    }

    /**
     * Метод редактирования питомца в базе данных
     *
     * @param pet сущность питомца
     * @return Возвращает отредактированного в базе данных питомца
     */
    public Pet updatePet(Pet pet) {
        logger.info("Вызван метод редактирования питомца: {}", pet);
        return petRepository.save(pet);
    }

    /**
     * Метод поиска питомца в базе данных
     *
     * @param id идентификатор искомого питомца
     * @return Возвращает найденого питомца
     */
    public Pet findPet(Long id) {
        logger.info("Вызван метод поиска питомца по id {}", id);
        Pet pet = petRepository.findById(id).orElse(null);
        if (pet == null) {
            throw new NullPointerException();
        }
        return pet;
    }
}