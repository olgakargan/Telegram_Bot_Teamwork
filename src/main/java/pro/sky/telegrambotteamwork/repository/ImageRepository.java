package pro.sky.telegrambotteamwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambotteamwork.model.Image;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByDogId(Long dogId);

    Optional<Image> findByCatId(Long catId);

    Optional<Image> findByPetId(Long petId);
}
