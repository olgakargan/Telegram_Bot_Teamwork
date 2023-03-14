package pro.sky.telegrambotteamwork.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.model.ReportData;
import pro.sky.telegrambotteamwork.repository.ReportDataRepository;

@Service
@AllArgsConstructor
public class ReportDataService {
    private final Logger logger = LoggerFactory.getLogger(ReportDataService.class);
    private final ReportDataRepository reportDataRepository;

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
    public void deleteReportData(Long id) {
        logger.info("Вызван метод удаления отчета по id: {}", id);
        reportDataRepository.deleteById(id);
    }
}
