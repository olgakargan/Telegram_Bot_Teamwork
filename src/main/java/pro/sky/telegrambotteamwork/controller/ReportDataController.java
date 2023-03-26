package pro.sky.telegrambotteamwork.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.ReportData;
import pro.sky.telegrambotteamwork.service.ReportDataService;

/**
 * Класс-контроллер для работы с отчетами от пользователя
 */
@RestController
@RequestMapping("/api/report-data")
@AllArgsConstructor
@Tag(name = "Работа с отчетами", description = "Позволяет управлять методами по работе с отчетами от пользователей")
public class ReportDataController {
    private final ReportDataService reportDataService;

    /**
     * Метод добавления деталей отчета в базу данных
     *
     * @param reportData детали отчета
     * @return Возвращает сохраненный в базу данных отчет
     */
    @Operation(summary = "Метод добавления отчета в базу данных", description = "Позваляет добавлять отчет в базу данных")
    @PostMapping
    public ReportData addReportData(@RequestBody ReportData reportData) {
        return reportDataService.addReportData(reportData);
    }

    /**
     * Метод поиска отчета в базе данных
     *
     * @param id идентификатор отчета
     * @return Возвращает найденный отчет
     */
    @Operation(summary = "Метод, чтобы найти отчет в базе данных", description = "Позваляет найти отчет в базе данных")
    @GetMapping("/{id}")
    public ReportData findReportData(@Parameter(description = "Идентификатор искомого отчета") @PathVariable Long id) {
        return reportDataService.findReportData(id);
    }

    /**
     * Метод удаления отчета из базы данных
     *
     * @param id идентификатор отчета
     * @return Возвращает ответ 200, если удаление отчета успешно произошло
     */
    @Operation(summary = "Метод, чтобы удалить отчет из базы данных", description = "Позваляет удалить отчет из базы данных")
    @DeleteMapping("/{id}")
    public ResponseEntity<ReportData> deleteReportData(@Parameter(description = "Идентификатор удаляемого отчета") @PathVariable Long id) {
        reportDataService.deleteReportData(id);
        return ResponseEntity.ok().build();
    }

}