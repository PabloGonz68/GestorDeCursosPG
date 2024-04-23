package com.pablo.gestioncursos.gestionCursos.reports;

import com.pablo.gestioncursos.gestionCursos.entity.Curso;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CursoExporterExcel {

    private XSSFWorkbook workbook;//libro
    private XSSFSheet sheet;//hoja
    private List<Curso> cursos;

    public CursoExporterExcel(List<Curso> cursos) {
        this.cursos = cursos;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine(){
        sheet = workbook.createSheet("Cursos");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "Título", style);
        createCell(row, 2, "Descripción", style);
        createCell(row, 3, "Nivel", style);
        createCell(row, 4, "Estado de publicación", style);
    }

    private void createCell(Row row, int columCount, Object value, CellStyle style){

        sheet.autoSizeColumn(columCount);
        Cell cell = row.createCell(columCount);

        if (value instanceof Integer){
            cell.setCellValue((Integer)value);
        }else if (value instanceof Boolean){
            cell.setCellValue((Boolean)value);
        }else{
            cell.setCellValue((String)value);
        }
        cell.setCellStyle(style);

    }

    private  void writeDataLines(){
    int rowCount = 1;

    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();

    font.setFontHeight(14);
    style.setFont(font);
    for (Curso curso:cursos){
        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;

        createCell(row, columnCount++, curso.getId(), style);
        createCell(row, columnCount++, curso.getTitulo(), style);
        createCell(row, columnCount++, curso.getDescripcion(), style);
        createCell(row, columnCount++, curso.getNivel(), style);
        createCell(row, columnCount++, curso.isPublicado(), style);
    }


    }

    public void export(HttpServletResponse response) throws IOException {
    writeHeaderLine();
    writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
