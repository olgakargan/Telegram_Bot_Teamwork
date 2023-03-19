package pro.sky.telegrambotteamwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambotteamwork.model.Pet;

/**
 * Класс-репозиторий для работы с методами питомца
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

}
