package pro.sky.telegrambotteamwork.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @Schema(description = "Имя", example = "Алиса")
    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_id")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Телефон", example = "+79180001122")
    @Column(name = "phone")
    private String phone;
}