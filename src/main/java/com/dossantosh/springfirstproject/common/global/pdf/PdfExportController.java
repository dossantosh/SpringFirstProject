package com.dossantosh.springfirstproject.common.global.pdf;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PdfExportController {

    private final PdfExportService pdfExportService;

    // Users
    @GetMapping("/user/users/export/pdf")
    public void exportUsers(HttpServletResponse response) throws IOException {
        pdfExportService.exportarTodosLosUsuarios(response);
    }

    @GetMapping("/user/users/export/pdf/{id}")
    public void exportUserById(@PathVariable Long id, HttpServletResponse response) throws IOException {
        pdfExportService.exportarUsuario(id, response);
    }

    // Perfumes
    @GetMapping("/objects/perfume/export/pdf")
    public void exportPerfumes(HttpServletResponse response) throws IOException {
        pdfExportService.exportarTodosLosPerfumes(response);
    }

    @GetMapping("/objects/perfume/export/pdf/{id}")
    public void exportPerfumeById(@PathVariable Long id, HttpServletResponse response) throws IOException {
        pdfExportService.exportarPerfume(id, response);
    }
}
