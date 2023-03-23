package pro.sky.telegrambotteamwork.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "date_and_time")
    private LocalDateTime dateTime;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private ReportData reportData;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Cat cat;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Dog dog;
}
