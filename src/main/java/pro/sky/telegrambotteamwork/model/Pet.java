package pro.sky.telegrambotteamwork.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;

/**
 * Класс с сущностью домашнего питомца
 */
@Data
@Table(name = "pet")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue
    @Column(name = "id")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Кличка", example = "Бобик")
    @Column(name = "pet_name")
    private String petName;
    @Schema(description = "Порода", example = "Колли")
    @Column(name = "breed")
    private String breed;
    @Schema(description = "Год рождения", example = "2010")
    @Column(name = "year_of_birth")
    private int yearOfBirth;
    @Schema(description = "Описание", example = "Добрый, любит детей")
    @Column(name = "description")
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image imagePet;
}