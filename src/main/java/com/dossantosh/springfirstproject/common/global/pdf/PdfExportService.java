package com.dossantosh.springfirstproject.common.global.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dossantosh.springfirstproject.perfume.models.Perfumes;
import com.dossantosh.springfirstproject.perfume.repository.PerfumeRepository;
import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.permissions.Modules;
import com.dossantosh.springfirstproject.user.models.permissions.Roles;
import com.dossantosh.springfirstproject.user.models.permissions.Submodules;
import com.dossantosh.springfirstproject.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PdfExportService {

    private final PerfumeRepository perfumeRepository;

    private final UserRepository userRepository;

    // Users
    public void exportarTodosLosUsuarios(HttpServletResponse response) throws IOException {
        List<User> usuarios = userRepository.findAll();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.pdf");

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.DARK_GRAY);
        Paragraph title = new Paragraph("Listado de Usuarios", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        String[] columnas = { "ID", "Username", "Email", "Habilitado", "Roles", "Módulos", "Submódulos" };
        Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);

        // Agregar encabezados
        for (String col : columnas) {
            PdfPCell cell = new PdfPCell(new Phrase(col, headFont));
            cell.setBackgroundColor(Color.BLUE);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        Font cellFont = new Font(Font.HELVETICA, 10);

        // Agregar filas con datos
        for (User user : usuarios) {
            addCellToTable(table, String.valueOf(user.getId()), cellFont);
            addCellToTable(table, user.getUsername(), cellFont);
            addCellToTable(table, user.getEmail(), cellFont);
            addCellToTable(table, (user.getEnabled() != null && user.getEnabled()) ? "Sí" : "No", cellFont);

            String roles = user.getRoles().stream().map(Roles::getName).reduce((a, b) -> a + ", " + b).orElse("");
            String modulos = user.getModules().stream().map(Modules::getName).reduce((a, b) -> a + ", " + b).orElse("");
            String submodulos = user.getSubmodules().stream().map(Submodules::getName).reduce((a, b) -> a + ", " + b)
                    .orElse("");

            addCellToTable(table, roles, cellFont);
            addCellToTable(table, modulos, cellFont);
            addCellToTable(table, submodulos, cellFont);
        }

        document.add(table);
        document.close();
    }

    public void exportarUsuario(Long id, HttpServletResponse response) throws IOException {
        Optional<User> userOpt = userRepository.findById(id);

        if (!userOpt.isPresent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
            return;
        }

        User user = userOpt.get();

        response.setContentType("application/pdf");
        String nombreArchivo = "usuario-" + user.getId() + ".pdf";
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.DARK_GRAY);
        Paragraph title = new Paragraph("Detalles del Usuario", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        String[] columnas = { "ID", "Username", "Email", "Habilitado", "Roles", "Módulos", "Submódulos" };
        Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);

        // Agregar encabezados (idéntico al método de todos los usuarios)
        for (String col : columnas) {
            PdfPCell cell = new PdfPCell(new Phrase(col, headFont));
            cell.setBackgroundColor(Color.BLUE);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        Font cellFont = new Font(Font.HELVETICA, 10);

        // Agregar fila con los datos del usuario
        addCellToTable(table, String.valueOf(user.getId()), cellFont);
        addCellToTable(table, user.getUsername(), cellFont);
        addCellToTable(table, user.getEmail(), cellFont);
        addCellToTable(table, (user.getEnabled() != null && user.getEnabled()) ? "Sí" : "No", cellFont);

        // Formatear listas de relaciones
        String roles = user.getRoles().stream()
                .map(Roles::getName)
                .collect(Collectors.joining(", "));

        String modulos = user.getModules().stream()
                .map(Modules::getName)
                .collect(Collectors.joining(", "));

        String submodulos = user.getSubmodules().stream()
                .map(Submodules::getName)
                .collect(Collectors.joining(", "));

        addCellToTable(table, roles, cellFont);
        addCellToTable(table, modulos, cellFont);
        addCellToTable(table, submodulos, cellFont);

        document.add(table);
        document.close();
    }

    // Perfumes
    public void exportarTodosLosPerfumes(HttpServletResponse response) throws IOException {
        List<Perfumes> perfumes = perfumeRepository.findAll();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=perfumes.pdf");

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.DARK_GRAY);
        Paragraph title = new Paragraph("Listado de Perfumes", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        String[] columnas = { "ID", "Nombre", "Marca", "Tipo", "Precio", "Volumen", "Temporada", "Descripción", "Año" };
        Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);

        // Agregar encabezados con estilo
        for (String col : columnas) {
            PdfPCell cell = new PdfPCell(new Phrase(col, headFont));
            cell.setBackgroundColor(Color.BLUE);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        Font cellFont = new Font(Font.HELVETICA, 10);

        // Agregar filas con datos usando cellFont y alineación centrada
        for (Perfumes p : perfumes) {
            addCellToTable(table, String.valueOf(p.getId()), cellFont);
            addCellToTable(table, p.getName(), cellFont);
            addCellToTable(table, p.getBrand() != null ? p.getBrand().getName() : "Sin marca", cellFont);
            addCellToTable(table, p.getTipo() != null ? p.getTipo().getName() : "Sin tipo", cellFont);
            addCellToTable(table, String.format("$%.2f", p.getPrice()), cellFont);
            addCellToTable(table, p.getVolume() != null ? p.getVolume().toString() : "", cellFont);
            addCellToTable(table, p.getSeason() != null ? p.getSeason() : "", cellFont);
            addCellToTable(table, p.getDescription() != null ? p.getDescription() : "", cellFont);
            addCellToTable(table, p.getFecha() != null ? p.getFecha().toString() : "", cellFont);
        }

        document.add(table);
        document.close();
    }

    public void exportarPerfume(Long id, HttpServletResponse response) throws IOException {
        Optional<Perfumes> perfumeOpt = perfumeRepository.findById(id);

        if (!perfumeOpt.isPresent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Perfume no encontrado");
            return;
        }

        Perfumes perfume = perfumeOpt.get();

        response.setContentType("application/pdf");
        String nombreArchivo = "perfume-" + perfume.getId() + ".pdf";
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.DARK_GRAY);
        Paragraph title = new Paragraph("Detalles del Perfume", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        String[] columnas = { "ID", "Nombre", "Marca", "Tipo", "Precio", "Volumen", "Temporada", "Descripción", "Año" };
        Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);

        for (String col : columnas) {
            PdfPCell cell = new PdfPCell(new Phrase(col, headFont));
            cell.setBackgroundColor(Color.BLUE);
            cell.setPadding(5);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        Font cellFont = new Font(Font.HELVETICA, 10);

        // Agregar fila con los datos del perfume (mismo formato que en la lista)
        addCellToTable(table, String.valueOf(perfume.getId()), cellFont);
        addCellToTable(table, perfume.getName(), cellFont);
        addCellToTable(table, perfume.getBrand() != null ? perfume.getBrand().getName() : "Sin marca", cellFont);
        addCellToTable(table, perfume.getTipo() != null ? perfume.getTipo().getName() : "Sin tipo", cellFont);
        addCellToTable(table, String.format("$%.2f", perfume.getPrice()), cellFont);
        addCellToTable(table, perfume.getVolume() != null ? perfume.getVolume().toString() : "", cellFont);
        addCellToTable(table, perfume.getSeason() != null ? perfume.getSeason() : "", cellFont);
        addCellToTable(table, perfume.getDescription() != null ? perfume.getDescription() : "", cellFont);
        addCellToTable(table, perfume.getFecha() != null ? perfume.getFecha().toString() : "", cellFont);

        document.add(table);
        document.close();
    }

    // Utils
    private void addCellToTable(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
