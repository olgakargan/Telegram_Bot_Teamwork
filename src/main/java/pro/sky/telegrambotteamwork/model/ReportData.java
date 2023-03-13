package pro.sky.telegrambotteamwork.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Date;


@Data
@Entity
public class ReportData {
    @Id
    @GeneratedValue
    private long id;
    private Long chatId;
    private String ration;
    private String health;
    private String habits;
    private long days;
    private String filePath;
    private long fileSize;
    @Lob
    private byte[] data;
    private String caption;
    private Date lastMessage;
    private Long lastMessageMs;


}