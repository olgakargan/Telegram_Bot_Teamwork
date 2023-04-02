package pro.sky.telegrambotteamwork.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.enums.Role;
import pro.sky.telegrambotteamwork.model.Image;
import pro.sky.telegrambotteamwork.model.ReportData;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.repository.ImageRepository;
import pro.sky.telegrambotteamwork.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final TelegramBot telegramBot;

    @Scheduled(cron = "* * 21 * * *")
    public void checkingScheduleReports() {
        Collection<User> users = userRepository.findUserByRole(Role.ROLE_USER);
        Collection<User> volunteers = userRepository.findUserByRole(Role.ROLE_VOLUNTEER);
        List<Image> images = imageRepository.findAll();
        for (int i = 0; i < images.size(); i++) {
            ReportData reportData = images.get(i).getReportData();
            LocalDateTime dateTime = LocalDateTime.now();
            Long differenceDate = Duration.between(reportData.getDateTime(), dateTime).toDays();
            if (differenceDate >= 2 && differenceDate <= 4) {
                telegramBot.execute(new SendMessage(reportData.getChatId(), "Дорогой усыновитель, вы забыли отправить отчет о вашем питомце! Пожалуйста, подойдите ответственней к этому!"));
            } else if (differenceDate >= 3) {
                User user = users.stream()
                        .max(Comparator.comparing(User::getRole))
                        .get();
                User volunteer = volunteers.stream()
                        .max(Comparator.comparing(User::getRole))
                        .get();
                telegramBot.execute(new SendMessage(volunteer.getChatId(), "Дорогой волонтер, усыновитель " + user.getUserName() + " уже три и более дней не присылал отчет! Свяжитесь с ним по номеру телефона: " + user.getPhone()));
            }
        }
    }
}
