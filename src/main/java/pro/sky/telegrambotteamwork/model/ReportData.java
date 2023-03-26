package pro.sky.telegrambotteamwork.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "reports_data")
@AllArgsConstructor
@Entity
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
    @Column(name = "date_and_time")
    private LocalDateTime dateTime;
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