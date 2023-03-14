package pro.sky.telegrambotteamwork.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.ReportData;
import pro.sky.telegrambotteamwork.service.ReportDataService;

@RestController
@RequestMapping("/api/report-data")
@AllArgsConstructor
public class ReportDataController {
    private final ReportDataService reportDataService;

    @PostMapping
    public ReportData addReportData(@RequestBody ReportData reportData) {
        return reportDataService.addReportData(reportData);
    }

    @GetMapping("/{id}")
    public ReportData findReportData(@PathVariable Long id) {
        return reportDataService.findReportData(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReportData> deleteReportData(@PathVariable Long id) {
        reportDataService.deleteReportData(id);
        return ResponseEntity.ok().build();
    }

}
