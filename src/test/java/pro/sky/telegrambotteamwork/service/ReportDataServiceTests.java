package pro.sky.telegrambotteamwork.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambotteamwork.model.ReportData;
import pro.sky.telegrambotteamwork.repository.ReportDataRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReportDataServiceTests {
    @Mock
    private ReportDataRepository reportDataRepository;
    @InjectMocks
    private ReportDataService reportDataService;

    private static final Long TEST_ID = 1L;
    private static final Long TEST_ID_2 = 2L;
    private static final Long TEST_CHAT_ID = 12345L;
    private static final String TEST_RATION = "Описание рациона животного";
    private static final String TEST_HEALTH = "Описание здоровья животного";
    private static final String TEST_HABITS = "Описание привычек животного";
    private static final Integer TEST_DAY = 2;

    private final ReportData reportData = new ReportData(TEST_ID, TEST_CHAT_ID, TEST_RATION, TEST_HEALTH, TEST_HABITS, TEST_DAY);

    @Test
    public void addReportDataTest() {
        assertNotNull(reportDataRepository);

        Mockito.when(reportDataRepository.save(reportData)).thenReturn(reportData);
        Assertions.assertThat(reportData).isEqualTo(reportDataService.addReportData(reportData));
    }

    @Test
    public void findReportDataTest() {
        assertNotNull(reportDataRepository);

        Mockito.when(reportDataRepository.findById(TEST_ID)).thenReturn(Optional.of(reportData));
        Assertions.assertThat(reportData).isEqualTo(reportDataService.findReportData(TEST_ID));
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> reportDataService.findReportData(TEST_ID_2));
    }

    @Test
    public void deleteReportDataTest() {
        assertNotNull(reportDataRepository);
        Mockito.when(reportDataRepository.save(reportData)).thenReturn(reportData);
        Mockito.when(reportDataRepository.findById(TEST_ID)).thenReturn(Optional.of(reportData));

        verify(reportDataRepository, atLeastOnce()).deleteById(TEST_ID);

    }
}
