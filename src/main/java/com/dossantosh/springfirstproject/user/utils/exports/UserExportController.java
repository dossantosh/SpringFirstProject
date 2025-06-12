package com.dossantosh.springfirstproject.user.utils.exports;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserExportController {

    private final UserPdfExportService pdfExportService;

    private final UserExcelExportService excelExportService;

    // Users
    @GetMapping("/user/users/export/excel")
    public void exportUsersExcel(HttpServletResponse response) throws Exception {
        excelExportService.exportUsuarios(response);
    }

    @GetMapping("/user/users/export/excel/{id}")
    public void exportUserByIdExcel(@PathVariable Long id, HttpServletResponse response) throws IOException {
        excelExportService.exportarUsuarioPorId(id, response);
    }

    // Users
    @GetMapping("/user/users/export/pdf")
    public void exportUsersPdf(HttpServletResponse response) throws IOException {
        pdfExportService.exportarTodosLosUsuarios(response);
    }

    @GetMapping("/user/users/export/pdf/{id}")
    public void exportUserByIdPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        pdfExportService.exportarUsuario(id, response);
    }
}