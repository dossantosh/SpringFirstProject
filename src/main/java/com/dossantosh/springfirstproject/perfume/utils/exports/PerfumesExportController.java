package com.dossantosh.springfirstproject.perfume.utils.exports;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PerfumesExportController {

    private final PerfumesExcelExportService excelExportService;

    private final PerfumesPdfExportService pdfExportService;

    @GetMapping("/objects/perfume/export/pdf")
    public void exportPerfumesPdf(HttpServletResponse response) throws IOException {
        pdfExportService.exportarTodosLosPerfumes(response);
    }

    @GetMapping("/objects/perfume/export/pdf/{id}")
    public void exportPerfumePdfById(@PathVariable Long id, HttpServletResponse response) throws IOException {
        pdfExportService.exportarPerfume(id, response);
    }

    @GetMapping("/objects/perfume/export/excel")
    public void exportPerfumesExcel(HttpServletResponse response) throws IOException {
        excelExportService.exportarTodosLosPerfumes(response);
    }

    @GetMapping("/objects/perfume/export/excel/{id}")
    public void exportPerfumesExcelById(@PathVariable Long id, HttpServletResponse response) throws IOException {
        excelExportService.exportarPerfumePorId(id, response);
    }
}
