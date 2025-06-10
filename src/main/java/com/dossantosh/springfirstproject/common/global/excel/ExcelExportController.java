package com.dossantosh.springfirstproject.common.global.excel;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExcelExportController {

    private final ExcelExportService excelExportService;

    // Users
    @GetMapping("/user/users/export/excel")
    public void exportUsers(HttpServletResponse response) throws Exception {
        excelExportService.exportUsuarios(response);
    }

    @GetMapping("/user/users/export/excel/{id}")
    public void exportUserById(@PathVariable Long id, HttpServletResponse response) throws IOException {
        excelExportService.exportarUsuarioPorId(id, response);
    }

    // Perfumes
    @GetMapping("/objects/perfume/export/excel")
    public void exportPerfumes(HttpServletResponse response) throws IOException {
        excelExportService.exportarTodosLosPerfumes(response);
    }

    @GetMapping("/objects/perfume/export/excel/{id}")
    public void exportarPerfumesById(@PathVariable Long id, HttpServletResponse response) throws IOException {
        excelExportService.exportarPerfumePorId(id, response);
    }
}