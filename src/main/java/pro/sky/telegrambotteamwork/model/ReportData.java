package pro.sky.telegrambotteamwork.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "reports_data")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ReportData {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "ration")
    private String ration;
    @Column(name = "health")
    private String health;
    @Column(name = "habits")
    private String habits;
    @Column(name = "days")
    private Integer day;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_data_id")
    private User user;

    public ReportData(Long id, Long chatId, String ration, String health, String habits, Integer day) {
        this.id = id;
        this.chatId = chatId;
        this.ration = ration;
        this.health = health;
        this.habits = habits;
        this.day = day;
    }
}