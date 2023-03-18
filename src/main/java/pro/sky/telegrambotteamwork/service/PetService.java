package pro.sky.telegrambotteamwork.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.exception.NoEntityException;
import pro.sky.telegrambotteamwork.model.Pet;
import pro.sky.telegrambotteamwork.repository.PetRepository;

import java.util.Collection;

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
     * Возвращает ок
     */
    public void updatePet(Long petId, Pet pet) {
        logger.info("Was invoked method for update pet");
        Pet updatedPet = findPetById(petId);
        updatedPet.setPetName(pet.getPetName());
        updatedPet.setBreed(pet.getBreed());
        updatedPet.setDescription(pet.getDescription());
        updatedPet.setYearOfBirth(pet.getYearOfBirth());
        petRepository.save(updatedPet);
    }

    /**
     * Поиск животного в БД по его id
     * @param petId Идентификатор животного
     * @return Объект {@link Pet} с указанным id, либо исключение {@link NoEntityException}
     */
    @CachePut(value = "pets", key = "#pet.id")
    public Pet updatePet(Pet pet) {
        logger.info("Вызван метод редактирования питомца: {}", pet);
        return petRepository.save(pet);
    public Pet findPetById(Long petId) {
        logger.info("Was invoked method for find pet by id");
        return petRepository.findById(petId).orElseThrow(() -> new NoEntityException("Животное с id" + petId + " не найдено"));
    }

    /**
     * Выводит список всех животных
     * @return возвращает {@link Collection<Pet>}  из всех животных приюта
     */
    @Cacheable("pets")
    public Pet findPet(Long id) {
        logger.info("Вызван метод поиска питомца по id {}", id);
        Pet pet = petRepository.findById(id).orElse(null);
        if (pet == null) {
            throw new NullPointerException();
        }
        return pet;
    public Collection<Pet> findAll() {
        logger.info("Was invoked method for find all pets");
        return petRepository.findAll();
    }

    /**
     * Удаление записи о животном из БД
     * @param petId Идентификатор животного
     */
    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }
}
