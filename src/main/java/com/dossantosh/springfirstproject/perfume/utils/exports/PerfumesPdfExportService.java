package com.dossantosh.springfirstproject.perfume.utils.exports;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.dossantosh.springfirstproject.perfume.models.Perfumes;
import com.dossantosh.springfirstproject.perfume.service.PerfumeService;

@Service
@RequiredArgsConstructor
public class PerfumesPdfExportService {

    private final PerfumeService perfumeService;

    // Perfumes
    public void exportarTodosLosPerfumes(HttpServletResponse response) throws IOException {
        List<Perfumes> perfumes = perfumeService.findAll();

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
        Perfumes perfume = perfumeService.findById(id);

        if (perfume == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Perfume no encontrado");
            return;
        }

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
