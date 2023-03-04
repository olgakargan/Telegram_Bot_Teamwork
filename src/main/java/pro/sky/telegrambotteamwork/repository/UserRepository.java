package pro.sky.telegrambotteamwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambotteamwork.model.User;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Collection<User> findAllByPhone(String phone);


}
