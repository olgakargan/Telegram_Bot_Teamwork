package pro.sky.telegrambotteamwork.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс с сущностью питомца - кошки
 */
@Data
@Table(name = "cats")
@Entity
@NoArgsConstructor
public class Cat {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "cat_name")
    private String catName;
    @Column(name = "breed")
    private String breed;
    @Column(name = "year_of_birth")
    private int yearOfBirth;
    @Column(name = "description")
    private String description;

    public Cat(String catName, String breed, int yearOfBirth, String description) {
        this.catName = catName;
        this.breed = breed;
        this.yearOfBirth = yearOfBirth;
        this.description = description;
    }
}