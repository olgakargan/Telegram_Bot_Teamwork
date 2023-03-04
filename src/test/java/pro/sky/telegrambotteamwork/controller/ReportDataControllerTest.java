package pro.sky.telegrambotteamwork.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import pro.sky.telegrambotteamwork.model.ReportData;
import pro.sky.telegrambotteamwork.serviceImpl.ReportDataService;

import java.io.*;
import java.util.Objects;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportDataController.class)
class ReportDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportDataService reportDataService;

    @Test //загрузить отчет
    void downloadReport() throws Exception {
        String ration = "good ration";
        String health = "health";
        ReportData reportData = new ReportData();
        reportData.setHealth(health);
        reportData.setRation(ration);
        when(reportDataService.findById(anyLong())).thenReturn(reportData);

        mockMvc.perform(
                        get("/photoReports/{id}/report", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.health").value(health))
                .andExpect(jsonPath("$.ration").value(ration));
        verify(reportDataService).findById(1L);
    }

    @Test //загрузить фотографию из Базы данных
    void downloadPhotoFromDataBase() throws Exception {
        String fileType = "image/png";
        ReportData reportData = new ReportData();
        InputStream is = getClass().getClassLoader().getResourceAsStream("pet.png");
        byte[] data = Objects.requireNonNull(is).readAllBytes();
        reportData.setData(data);

        when(reportDataService.findById(anyLong())).thenReturn(reportData);
        mockMvc.perform(
                        get("/photoReports/{id}/photo-from-db", 1L))
                .andExpect(status().isOk())
                .andExpect(content().bytes(data))
                .andExpect(content().contentType(fileType));
        verify(reportDataService).findById(1L);
    }

    @Test //тестовая загрузка Аватара из Файла
    void testDownloadAvatarFromFile() throws Exception {
        String fileType = "image/png";
        ReportData reportData = new ReportData();
        reportData.setFilePath("src/test/java/pro.sky.telegrambotteamwork.controller/pet.png");

        InputStream is = getClass().getClassLoader().getResourceAsStream("pet.png");
        byte[] data = Objects.requireNonNull(is).readAllBytes();

        when(reportDataService.findById(anyLong())).thenReturn(reportData);
        mockMvc.perform(
                        get("/photoReports/{id}/photo-from-file", 1L))
                .andExpect(status().isOk())
                .andExpect(content().bytes(data))
                .andExpect(content().contentType(fileType));
        verify(reportDataService).findById(1L);
    }
}