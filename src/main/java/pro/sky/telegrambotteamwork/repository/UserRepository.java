package pro.sky.telegrambotteamwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambotteamwork.enums.Role;
import pro.sky.telegrambotteamwork.model.User;

import java.util.Collection;


/**
 * Класс-репозиторий для работы с методами всех пользователей
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Этот метод ищет всех пользователей по идентификатору
     *
     * @param userId идентификатор искомого пользователя
     * @return Возвращает найденного пользователя
     */
    @Query(value = "SELECT * FROM users WHERE user_id = :userId", nativeQuery = true)
    Collection<User> findUserByUserId(@Param("userId") Long userId);


    /**
     * Этот метод ищет всех пользователей разделенных по ролям
     *
     * @param role роль пользователя (роль задается в соответствующем классе enum)
     * @return Возвращает всех пользователей с соответствующей ролью
     */
    Collection<User> findUserByRole(Role role);

    Collection<User> findAllByPhone(String phone);



}
