package pro.sky.telegrambotteamwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambotteamwork.model.User;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM users WHERE user_id = :userId", nativeQuery = true)
    Collection<User> findUserByUserId(@Param("userId") Long userId);
}