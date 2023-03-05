package pro.sky.telegrambotteamwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambotteamwork.model.Cat;

/**
 * Класс-репозиторий для работы с методами питомца - кошки
 */
@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {

}


