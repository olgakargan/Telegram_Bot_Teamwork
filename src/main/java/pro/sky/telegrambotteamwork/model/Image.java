package pro.sky.telegrambotteamwork.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "images")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "file_size")
    private Long fileSize;
    @Column(name = "media_type")
    private String mediaType;
    @Lob
    private byte[] bytes;
    @OneToOne(mappedBy = "image")
    private ReportData reportData;
    @OneToOne(mappedBy = "imageCat")
    private Cat cat;
    @OneToOne(mappedBy = "imageDog")
    private Dog dog;
    @OneToOne(mappedBy = "imagePet")
    private Pet pet;
}