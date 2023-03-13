package pro.sky.telegrambotteamwork.serviceImpl;


import com.pengrad.telegrambot.model.File;
import pro.sky.telegrambotteamwork.model.ReportData;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface ReportDataService {
    void uploadReportData(Long personId, byte[] pictureFile, File file, String ration, String health,
                          String habits, String filePath, Date dateSendMessage, Long timeDate, long daysOfReports) throws IOException;

    ReportData findById(Long id);

    ReportData findByChatId(Long chatId);

    Collection<ReportData> findListByChatId(Long chatId);

    ReportData save(ReportData report);

    void remove(Long id);

    List<ReportData> getAll();

    List<ReportData> getAllReports(Integer pageNumber, Integer pageSize);

    String getExtensions(String fileName);
}