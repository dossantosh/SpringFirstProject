package com.dossantosh.springfirstproject.perfume.utils.exports;

import com.dossantosh.springfirstproject.perfume.models.Perfumes;
import com.dossantosh.springfirstproject.perfume.repository.PerfumeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerfumesExcelExportService {

    private final PerfumeRepository perfumeRepository;

    private static final String CONTENTTYPEOPENXMLOFFICEDOCUMENTS = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    // Perfumes
    public void exportarTodosLosPerfumes(HttpServletResponse response) throws IOException {
        List<Perfumes> perfumes = perfumeRepository.findAll();

        String[] columnas = { "ID", "Nombre", "Marca", "Tipo", "Precio", "Volumen", "Temporada", "Descripción", "Año" };

        response.setContentType(CONTENTTYPEOPENXMLOFFICEDOCUMENTS);
        response.setHeader("Content-Disposition", "attachment; filename=perfumes.xlsx");

        try (Workbook workbook = new XSSFWorkbook(); ServletOutputStream out = response.getOutputStream()) {
            Sheet sheet = workbook.createSheet("Perfumes");

            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle textCellStyle = crearEstiloCelda(workbook);
            CellStyle priceCellStyle = crearEstiloPrecio(workbook);
            CellStyle numberCellStyle = crearEstiloNumero(workbook);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            int fila = 1;
            for (Perfumes p : perfumes) {
                Row row = sheet.createRow(fila++);

                crearCelda(row, 0, p.getId(), numberCellStyle);
                crearCelda(row, 1, p.getName(), textCellStyle);
                crearCelda(row, 2, p.getBrand() != null ? p.getBrand().getName() : "Sin marca", textCellStyle);
                crearCelda(row, 3, p.getTipo() != null ? p.getTipo().getName() : "Sin tipo", textCellStyle);
                crearCelda(row, 4, p.getPrice(), priceCellStyle);
                crearCelda(row, 5, p.getVolume(), numberCellStyle);
                crearCelda(row, 6, p.getSeason(), textCellStyle);
                crearCelda(row, 7, p.getDescription(), textCellStyle);
                crearCelda(row, 8, p.getFecha() != null ? p.getFecha() : 0, numberCellStyle);
            }

            autoSizeColumns(sheet, columnas.length);
            workbook.write(out);
        }
    }

    public void exportarPerfumePorId(Long id, HttpServletResponse response) throws IOException {
        Optional<Perfumes> optionalPerfume = perfumeRepository.findById(id);
        if (optionalPerfume.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Perfumes p = optionalPerfume.get();

        String[] columnas = { "ID", "Nombre", "Marca", "Tipo", "Precio", "Volumen", "Temporada", "Descripción", "Año" };
        String[] valores = {
                String.valueOf(p.getId()),
                p.getName(),
                p.getBrand() != null ? p.getBrand().getName() : "Sin marca",
                p.getTipo() != null ? p.getTipo().getName() : "Sin tipo",
                String.valueOf(p.getPrice()),
                String.valueOf(p.getVolume()),
                p.getSeason(),
                p.getDescription(),
                p.getFecha() != null ? String.valueOf(p.getFecha()) : "0"
        };

        response.setContentType(CONTENTTYPEOPENXMLOFFICEDOCUMENTS);
        response.setHeader("Content-Disposition", "attachment; filename=perfume_" + id + ".xlsx");

        try (Workbook workbook = new XSSFWorkbook(); ServletOutputStream out = response.getOutputStream()) {
            Sheet sheet = workbook.createSheet("Perfume");

            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle textCellStyle = crearEstiloCelda(workbook);
            CellStyle priceCellStyle = crearEstiloPrecio(workbook);
            CellStyle numberCellStyle = crearEstiloNumero(workbook);

            Row headerRow = sheet.createRow(0);
            Row dataRow = sheet.createRow(1);

            for (int i = 0; i < columnas.length; i++) {
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue(columnas[i]);
                headerCell.setCellStyle(headerStyle);

                Cell dataCell = dataRow.createCell(i);

                // Selección del estilo según tipo de dato
                if (i == 4) { // Precio
                    dataCell.setCellValue(Double.parseDouble(valores[i]));
                    dataCell.setCellStyle(priceCellStyle);
                } else if (i == 0 || i == 5 || i == 8) { // ID, volumen, año
                    dataCell.setCellValue(Integer.parseInt(valores[i]));
                    dataCell.setCellStyle(numberCellStyle);
                } else {
                    dataCell.setCellValue(valores[i]);
                    dataCell.setCellStyle(textCellStyle);
                }
            }

            autoSizeColumns(sheet, columnas.length);
            workbook.write(out);
        }
    }

    // Utils
    private void crearCelda(Row row, int colIndex, Object valor, CellStyle style) {
        Cell cell = row.createCell(colIndex);

        if (valor == null) {
            cell.setCellValue("");
        } else if (valor instanceof String s) {
            cell.setCellValue(s);
        } else if (valor instanceof Integer i) {
            cell.setCellValue(i);
        } else if (valor instanceof Long l) {
            cell.setCellValue(l.doubleValue());
        } else if (valor instanceof Double d) {
            cell.setCellValue(d);
        } else if (valor instanceof Boolean b) {
            cell.setCellValue(b);
        } else {
            cell.setCellValue(valor.toString());
        }

        cell.setCellStyle(style);
    }

    private CellStyle crearEstiloEncabezado(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private CellStyle crearEstiloCelda(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        style.setWrapText(true);

        return style;
    }

    private CellStyle crearEstiloPrecio(Workbook workbook) {
        CellStyle style = crearEstiloCelda(workbook);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$#,##0.00"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    private CellStyle crearEstiloNumero(Workbook workbook) {
        CellStyle style = crearEstiloCelda(workbook);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private void autoSizeColumns(Sheet sheet, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
