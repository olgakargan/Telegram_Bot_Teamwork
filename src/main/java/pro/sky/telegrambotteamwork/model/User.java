package pro.sky.telegrambotteamwork.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
<<<<<<< HEAD

    @Schema(description = "Имя", example = "Алиса")
    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_id")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Телефон", example = "+79180001122")
    @Column(name = "phone")
    private String phone;
=======
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

>>>>>>> origin/dev
}