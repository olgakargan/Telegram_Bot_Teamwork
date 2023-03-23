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
    @Column(name = "floor")
    private String floor;
    @Column(name = "description")
    private String description;

    public Dog(Long id, String dogName, String breed, int yearOfBirth, String description) {
        this.id = id;
        this.dogName = dogName;
        this.breed = breed;
        this.yearOfBirth = yearOfBirth;
        this.description = description;
    }
}
