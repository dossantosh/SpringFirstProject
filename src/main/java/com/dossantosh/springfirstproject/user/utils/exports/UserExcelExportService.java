package com.dossantosh.springfirstproject.user.utils.exports;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.permissions.Modules;
import com.dossantosh.springfirstproject.user.models.permissions.Roles;
import com.dossantosh.springfirstproject.user.models.permissions.Submodules;
import com.dossantosh.springfirstproject.user.service.UserService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserExcelExportService {

    private final UserService userService;

    private static final String CONTENTTYPEOPENXMLOFFICEDOCUMENTS = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    // Users
    public void exportUsuarios(HttpServletResponse response) throws IOException {
        List<User> usuarios = userService.findAll();
        String[] columnas = { "ID", "Username", "Email", "Habilitado", "Roles", "Módulos", "Submódulos" };

        response.setContentType(CONTENTTYPEOPENXMLOFFICEDOCUMENTS);
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.xlsx");

        try (Workbook workbook = new XSSFWorkbook(); ServletOutputStream out = response.getOutputStream()) {
            Sheet sheet = workbook.createSheet("Usuarios");

            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle cellStyle = crearEstiloCelda(workbook);

            // Crear fila encabezado
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos de usuarios
            int fila = 1;
            for (User user : usuarios) {
                Row row = sheet.createRow(fila++);

                crearCelda(row, 0, user.getId(), cellStyle);
                crearCelda(row, 1, user.getUsername(), cellStyle);
                crearCelda(row, 2, user.getEmail(), cellStyle);
                crearCelda(row, 3, user.getEnabled() != null && user.getEnabled() ? "Sí" : "No", cellStyle);
                crearCelda(row, 4,
                        user.getRoles().stream().map(Roles::getName).collect(Collectors.joining(", ")), cellStyle);
                crearCelda(row, 5,
                        user.getModules().stream().map(Modules::getName).collect(Collectors.joining(", ")), cellStyle);
                crearCelda(row, 6,
                        user.getSubmodules().stream().map(Submodules::getName).collect(Collectors.joining(", ")),
                        cellStyle);
            }

            autoSizeColumns(sheet, columnas.length);
            workbook.write(out);
        }
    }

    public void exportarUsuarioPorId(Long id, HttpServletResponse response) throws IOException {
        User user = userService.findById(id);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String[] campos = { "ID", "Username", "Email", "Activo", "Roles", "Módulos", "Submódulos" };
        String[] valores = {
                user.getId().toString(),
                user.getUsername(),
                user.getEmail(),
                (user.getEnabled() != null && user.getEnabled() ? "Sí" : "No"),
                user.getRoles().stream().map(Roles::getName).collect(Collectors.joining(", ")),
                user.getModules().stream().map(Modules::getName).collect(Collectors.joining(", ")),
                user.getSubmodules().stream().map(Submodules::getName).collect(Collectors.joining(", "))
        };

        response.setContentType(CONTENTTYPEOPENXMLOFFICEDOCUMENTS);
        response.setHeader("Content-Disposition", "attachment; filename=usuario_" + user.getId() + ".xlsx");

        try (Workbook workbook = new XSSFWorkbook(); ServletOutputStream out = response.getOutputStream()) {
            Sheet sheet = workbook.createSheet("Usuario");

            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle cellStyle = crearEstiloCelda(workbook);

            Row headerRow = sheet.createRow(0);
            Row dataRow = sheet.createRow(1);

            for (int i = 0; i < campos.length; i++) {
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue(campos[i]);
                headerCell.setCellStyle(headerStyle);

                Cell dataCell = dataRow.createCell(i);
                dataCell.setCellValue(valores[i]);
                dataCell.setCellStyle(cellStyle);
            }

            autoSizeColumns(sheet, campos.length);
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

    private void autoSizeColumns(Sheet sheet, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}