package pro.sky.telegrambotteamwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambotteamwork.model.Image;

import java.util.Optional;

/**
 * Класс-репозиторий для работы с изображениями
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
    /**
     * Метод для поиска собаки по идентификатору
     *
     * @param dogId идентификатор искомой собаки
     * @return Возвращает найденную собаку
     */
    Optional<Image> findByDogId(Long dogId);

    /**
     * Метод для поиска кошки по идентификатору
     *
     * @param catId идентификатор искомой кошки
     * @return Возвращает найденную кошку
     */
    Optional<Image> findByCatId(Long catId);

    /**
     * Метод для поиска отчета по идентификатору
     *
     * @param reportDataId идентификатор искомого отчета
     * @return Возвращает найденный отчет
     */
    Optional<Image> findByReportDataId(Long reportDataId);
}
