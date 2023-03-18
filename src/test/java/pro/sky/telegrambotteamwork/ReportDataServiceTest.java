package pro.sky.telegrambotteamwork;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambotteamwork.exception.ReportDataNotFoundException;
import pro.sky.telegrambotteamwork.model.ReportData;
import pro.sky.telegrambotteamwork.repository.ReportDataRepository;
import pro.sky.telegrambotteamwork.service.ReportDataService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class ReportDataServiceTest {

    @Mock
    private ReportDataRepository reportDataRepositoryMock;

    @InjectMocks
    private ReportDataService reportDataService;


    @Test
    public void findByIdTest() {
        ReportData expected = new ReportData(1L, 123456789L, "Рацион питомца", "Здоровье питомца", "Привычки питомца", 1);
        expected.setChatId(1L);
        expected.setRation("testРацион питомца");
        expected.setHealth("testЗдоровье питомца");
        expected.setHabits("testПривычки питомца");


        Mockito.when(reportDataRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(expected));

        ReportData actual = reportDataService.findReportData(1L);

        Assertions.assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        Assertions.assertThat(actual.getRation()).isEqualTo(expected.getRation());
        Assertions.assertThat(actual.getHealth()).isEqualTo(expected.getHealth());
        Assertions.assertThat(actual.getHabits()).isEqualTo(expected.getHabits());

    }


    @Test
    public void findByIdExceptionTest() {
        Mockito.when(reportDataRepositoryMock.findById(any(Long.class))).thenThrow(ReportDataNotFoundException.class);

        org.junit.jupiter.api.Assertions.assertThrows(ReportDataNotFoundException.class, () -> reportDataService.findReportData(1L));
    }


    @Test
    public void findListByChatIdTest() {
        Set<ReportData> expected = new HashSet<>();

        ReportData testReport1 = new ReportData();
        testReport1.setChatId(1L);
        testReport1.setRation("testРацион питомца1");
        testReport1.setHealth("testЗдоровье питомца1");
        testReport1.setHabits("testПривычки питомца1");
        expected.add(testReport1);

        ReportData testReport2 = new ReportData();
        testReport2.setChatId(1L);
        testReport2.setRation("testРацион питомца2");
        testReport2.setHealth("testЗдоровье питомца2");
        testReport2.setHabits("testПривычки питомца2");
        expected.add(testReport2);

        ReportData testReport3 = new ReportData();
        testReport3.setChatId(1L);
        testReport3.setRation("testРацион питомца3");
        testReport3.setHealth("testЗдоровье питомца3");
        testReport3.setHabits("testПривычки питомца3");
        expected.add(testReport3);

        Mockito.when(reportDataRepositoryMock.findListByChatId(any(Long.class))).thenReturn(expected);
        Collection<ReportData> actual = Collections.singleton(reportDataService.findReportData(1L));
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        Assertions.assertThat(actual).isEqualTo(expected);
    }


    @Test
    public void findAllTest() {
        List<ReportData> expected = new ArrayList<>();

        ReportData testReport1 = new ReportData();
        testReport1.setChatId(1L);
        testReport1.setRation("testРацион питомца1");
        testReport1.setHealth("testЗдоровье питомца1");
        testReport1.setHabits("testПривычки питомца1");
        expected.add(testReport1);

        ReportData testReport2 = new ReportData();
        testReport1.setChatId(1L);
        testReport1.setRation("testРацион питомца2");
        testReport1.setHealth("testЗдоровье питомца2");
        testReport1.setHabits("testПривычки питомца2");
        expected.add(testReport2);

        ReportData testReport3 = new ReportData();
        testReport1.setChatId(1L);
        testReport1.setRation("testРацион питомца3");
        testReport1.setHealth("testЗдоровье питомца3");
        testReport1.setHabits("testПривычки питомца3");
        expected.add(testReport3);

        Mockito.when(reportDataRepositoryMock.findAll()).thenReturn(expected);
        Collection<ReportData> actual = Collections.singleton(reportDataService.findReportData(1L));
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
