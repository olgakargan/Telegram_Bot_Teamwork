package pro.sky.telegrambotteamwork.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.pengrad.telegrambot.model.File;
import pro.sky.telegrambotteamwork.exception.ReportDataNotFoundException;
import pro.sky.telegrambotteamwork.model.ReportData;
import pro.sky.telegrambotteamwork.repository.ReportDataRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author OKargan
 * @date 28.02.2023 20:00
 */

@Service
@RequiredArgsConstructor

public class ReportDataServiceImpl implements ReportDataService {
    private final ReportDataRepository reportRepository;

    private final Logger logger = LoggerFactory.getLogger(ReportDataServiceImpl.class);
    @Override
    public void uploadReportData(Long personId, byte[] pictureFile, File file, String ration, String health,
                                 String habits, String filePath, Date dateSendMessage, Long timeDate, long daysOfReports) {
        logger.info("Загрузка отчета");
        ReportData report = new ReportData();
        report.setLastMessage(dateSendMessage);
        report.setDays(daysOfReports);
        report.setFilePath(filePath);
        report.setFileSize(file.fileSize());
        report.setLastMessageMs(timeDate);
        report.setChatId(personId);
        report.setData(pictureFile);
        report.setRation(ration);
        report.setHealth(health);
        report.setHabits(habits);
        reportRepository.save(report);
    }

    @Override
    public ReportData findById(Long id) {
        logger.info("Получение отчета с помощью id={}", id);
        return reportRepository.findById(id).orElseThrow(ReportDataNotFoundException::new);
    }

    @Override
    public ReportData findByChatId(Long chatId) {
        logger.info("Получение отчета с помощью chatId={}", chatId);
        return reportRepository.findByChatId(chatId);
    }
    @Override
    public Collection<ReportData> findListByChatId(Long chatId) {
        logger.info("Поиск по id={}", chatId);
        return reportRepository.findListByChatId(chatId);
    }
    @Override
    public ReportData save(ReportData report) {
        logger.info("Сохранение отчета");
        return reportRepository.save(report);
    }
    @Override
    public void remove(Long id) {
        logger.info("Удаление отчета с помощью id={}", id);
        reportRepository.deleteById(id);
    }
    @Override
    public List<ReportData> getAll() {
        logger.info("Получение всех отчетов");
        return reportRepository.findAll();
    }
    @Override
    public List<ReportData> getAllReports(Integer pageNumber, Integer pageSize) {
        logger.info("Получение всех данных по отчету");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return reportRepository.findAll(pageRequest).getContent();
    }
    @Override
    public String getExtensions(String fileName) {
        logger.info("Was invoked method to getExtensions");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public void uploadReportData(Long id, byte[] fileContent, File file, String caption, String fullPathPhoto, String habits, String pathPhoto, Date dateSendMessage, long timeDate, long daysOfReports) {

    }
}