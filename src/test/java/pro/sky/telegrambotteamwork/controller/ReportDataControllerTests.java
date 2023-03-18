package pro.sky.telegrambotteamwork.controller;

import com.pengrad.telegrambot.TelegramBot;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;
import pro.sky.telegrambotteamwork.model.ReportData;

import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReportDataControllerTests {
    @LocalServerPort
    private int port;
    @MockBean
    private TelegramBot telegramBot;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addReportDataTest() {
        ReportData reportData = new ReportData(1L, 123456789L, "Рацион питомца", "Здоровье питомца", "Привычки питомца", 1);
        ResponseEntity<ReportData> response = formingUrl(constructionUriBuilderCreation().build().toUri(), reportData);
        checkingTheReportsDataForCreation(reportData, response);
    }

    @Test
    public void findReportDataTest() {
        ReportData reportData = new ReportData(1L, 123456789L, "Рацион питомца", "Здоровье питомца", "Привычки питомца", 1);
        ResponseEntity<ReportData> response = formingUrl(constructionUriBuilderCreation().build().toUri(), reportData);
        checkingTheReportsDataForCreation(reportData, response);
        ReportData findReportData = response.getBody();
        ResponseEntity<ReportData> findResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/report-data/" + findReportData.getId(), ReportData.class);
        Assertions.assertThat(findResponse.getBody()).isEqualTo(findReportData);
        Assertions.assertThat(findResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private ResponseEntity<ReportData> formingUrl(URI uri, ReportData reportData) {
        return restTemplate.postForEntity(uri, reportData, ReportData.class);
    }

    private UriComponentsBuilder constructionUriBuilderCreation() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/api/report-data");
    }

    private void checkingTheReportsDataForCreation(ReportData reportData, ResponseEntity<ReportData> response) {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isEqualTo(reportData.getId());
    }

}