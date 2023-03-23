package pro.sky.telegrambotteamwork.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    @Scheduled(cron = "* * * * * *")
    public void checkingScheduleReports() {

    }
}
