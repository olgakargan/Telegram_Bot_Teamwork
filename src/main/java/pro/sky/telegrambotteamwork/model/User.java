package pro.sky.telegrambotteamwork.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс с сущностью пользователя ботом
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "date_time")
    private LocalDateTime dateTime;

}