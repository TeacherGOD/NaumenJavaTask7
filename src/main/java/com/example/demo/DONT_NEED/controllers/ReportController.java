package com.example.demo.DONT_NEED.controllers;

import com.example.demo.DONT_NEED.entities.Report;
import com.example.demo.DONT_NEED.entities.ReportStatus;
import com.example.demo.DONT_NEED.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;


    @PostMapping
    public ResponseEntity<Long> createReport() {
        Long reportId = reportService.createReport();
        reportService.createReportAsync(reportId);
        return ResponseEntity.ok(reportId);
    }


    @GetMapping("/{id}")
    public ResponseEntity<String> getReportContent(@PathVariable Long id) {
        Report report;
        try {

            report = reportService.findReport(id);
        }
        catch (IllegalStateException e)
        {
            return ResponseEntity.badRequest().header("Error", e.getMessage()).body("Error:"+e.getMessage());
        }


        if (report.getStatus() == ReportStatus.CREATED) {
            return ResponseEntity.badRequest().body("Отчет еще не был сформирован.");
        } else if (report.getStatus() == ReportStatus.ERROR) {
            return ResponseEntity.badRequest().body("Отчет завершился с ошибкой.");
        }

        return ResponseEntity.ok(report.getContent());
    }
}