package pro.sky.telegrambotteamwork.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс с сущностью питомца - собаки
 */
@Data
@Table(name = "dogs")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Dog {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "dog_name")
    private String dogName;
    @Column(name = "breed")
    private String breed;
    @Column(name = "year_of_birth")
    private int yearOfBirth;
    @Column(name = "description")
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image imageDog;

}
