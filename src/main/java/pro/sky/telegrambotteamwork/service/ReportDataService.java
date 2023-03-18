package pro.sky.telegrambotteamwork.service;

import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.model.ReportData;
import pro.sky.telegrambotteamwork.repository.ReportDataRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сервис-класс для манипуляций с отчетами от пользователя
 */
@Service
@AllArgsConstructor
public class ReportDataService {
    private final Logger logger = LoggerFactory.getLogger(ReportDataService.class);
    private final ReportDataRepository reportDataRepository;
    private static final Pattern PATTERN = Pattern.compile("([\\W+]+)(\\#)([\\W+]+)(\\#)([\\W+]+)(\\#)([0-9]{1,})");

    /**
     * Метод добавления деталей отчета в базу данных
     *
     * @param reportData детали отчета
     * @return Возвращает сохраненный в базу данных отчет
     */
    public ReportData addReportData(ReportData reportData) {
        logger.info("Вызов метода добавление отчета: {}", reportData);
        return reportDataRepository.save(reportData);
    }

    /**
     * Метод поиска отчета в базе данных
     *
     * @param id идентификатор отчета
     * @return Возвращает найденный отчет
     */
    @Cacheable("reports")
    public ReportData findReportData(Long id) {
        logger.info("Вызов метода поиска отчета по идентификатору: {}", id);
        ReportData reportData = reportDataRepository.findById(id).orElse(null);
        if (reportData == null) {
            throw new NullPointerException();
        }
        return reportData;
    }

    /**
     * Метод удаления отчета из базы данных
     *
     * @param id идентификатор отчета
     */
    @CacheEvict("reports")
    public void deleteReportData(Long id) {
        logger.info("Вызван метод удаления отчета по id: {}", id);
        reportDataRepository.deleteById(id);
    }

    /**
     * Метод сохранения нового отчета о питомце в базу данных.
     * Усыновитель вводит рацион животного, общее самочувствие, изменения в поведении и номер дня,
     * с помощью регулярного выражения эти данные расчленяются и записываются в переменные,
     * а затем в базу данных.
     *
     * @param update  входящее обновление
     * @param message текстовое сообщение от усыновителя
     */
    public void saveReportData(Update update, String message) {
        Matcher matcher = PATTERN.matcher(message);

        if (matcher.matches()) {
            ReportData reportData = new ReportData();
            Long chatId = update.message().chat().id();
            String ration = matcher.group(1);
            String health = matcher.group(3);
            String habits = matcher.group(5);
            String dayString = matcher.group(7);
            Integer day = Integer.parseInt(dayString);
            reportData.setChatId(chatId);
            reportData.setRation(ration);
            reportData.setHealth(health);
            reportData.setHabits(habits);
            reportData.setDay(day);
            reportDataRepository.save(reportData);
            logger.info("Отчет о питомце сохранен в базу: " + reportData);
        }
    }
}
