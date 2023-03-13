package pro.sky.telegrambotteamwork.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.exception.NoEntityException;
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

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * Создание записи о животном в БД
     * @param pet Данные о животном
     */
    public void addNew(Pet pet) {
        logger.info("Was invoked method for add new pet");
        petRepository.save(pet);
    }

    /**
     * Метод для изменения информации о животном
     * @param petId Идентификатор животного
     * @param pet Новые данные
     */
    public void updatePet(Long petId, Pet pet) {
        logger.info("Was invoked method for update pet");
        Pet updatedPet = findPetById(petId);
        updatedPet.setPet_name(pet.getPet_name());
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
    public Pet findPetById(Long petId) {
        logger.info("Was invoked method for find pet by id");
        return petRepository.findById(petId).orElseThrow(() -> new NoEntityException("Животное с id" + petId + " не найдено"));
    }

    /**
     * Выводит список всех животных
     * @return возвращает {@link Collection<Pet>}  из всех животных приюта
     */
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
