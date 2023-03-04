package pro.sky.telegrambotteamwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambotteamwork.model.Ui;

@Repository
public interface UiRepository extends JpaRepository<Ui, Long> {

}

