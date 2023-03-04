package pro.sky.telegrambotteamwork.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "volunteer")
@Entity
public class Ui {

    @Id
    @GeneratedValue
    @Column(name = "id")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Имя", example = "Волонтер")
    @Column(name = "volunteer_name")
    private String volunteerName;
}
