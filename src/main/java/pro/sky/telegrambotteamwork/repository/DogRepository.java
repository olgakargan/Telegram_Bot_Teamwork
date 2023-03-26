package pro.sky.telegrambotteamwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambotteamwork.model.Dog;

/**
 * Класс-репозиторий для работы с методами питомца - собаки
 */
@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
}