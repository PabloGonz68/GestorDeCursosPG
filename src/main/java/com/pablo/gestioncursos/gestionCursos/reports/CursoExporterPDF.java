package com.pablo.gestioncursos.gestionCursos.reports;

// Importaciones necesarias para trabajar con PDF y servlets HTTP
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.pablo.gestioncursos.gestionCursos.entity.Curso;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

// Clase encargada de exportar datos de cursos a un archivo PDF
public class CursoExporterPDF {
    // Lista de cursos que se exportarán
    private List<Curso> listaCursos;

    // Constructor que recibe la lista de cursos a exportar
    public CursoExporterPDF(List<Curso> listaCursos) {
        this.listaCursos = listaCursos;
    }

    // Método para escribir la cabecera de la tabla en el PDF
    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.red); // Configura el color de fondo de las celdas
        cell.setPadding(5); // Configura el espacio interior de las celdas

        Font font = FontFactory.getFont(FontFactory.HELVETICA); // Obtiene una fuente
        font.setColor(Color.WHITE); // Configura el color de la fuente

        // Agrega celdas con los encabezados de las columnas de la tabla
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Título", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Descripción", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Nivel", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Publicado", font));
        table.addCell(cell);
    }

    // Método para escribir los datos de los cursos en la tabla del PDF
    private void writeTableData(PdfPTable table) {
        // Itera sobre la lista de cursos y agrega una fila por cada curso
        for (Curso curso : listaCursos) {
            table.addCell(String.valueOf(curso.getId())); // Agrega el ID del curso
            table.addCell(curso.getTitulo()); // Agrega el título del curso
            table.addCell(curso.getDescripcion()); // Agrega la descripción del curso
            table.addCell(String.valueOf(curso.getNivel())); // Agrega el nivel del curso
            table.addCell(String.valueOf(curso.isPublicado())); // Agrega el estado de publicación del curso
        }
    }

    // Método para exportar los datos de los cursos a un archivo PDF
    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4); // Crea un documento PDF de tamaño A4
        PdfWriter.getInstance(document, response.getOutputStream()); // Obtiene una instancia de PdfWriter

        document.open(); // Abre el documento PDF para escribir en él

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD); // Obtiene una fuente
        font.setSize(18); // Configura el tamaño de la fuente
        font.setColor(Color.RED); // Configura el color de la fuente

        Paragraph p = new Paragraph("Lista de cursos", font); // Crea un párrafo con el título
        p.setAlignment(Paragraph.ALIGN_CENTER); // Alinea el título al centro

        document.add(p); // Agrega el título al documento PDF

        PdfPTable table = new PdfPTable(5); // Crea una tabla con 5 columnas
        table.setWidthPercentage(100f); // Configura el ancho de la tabla al 100% del ancho de la página
        table.setWidths(new float[]{1.3f, 3.5f, 3.5f, 2.0f, 1.5f}); // Configura los anchos de las columnas
        table.setSpacingBefore(10); // Configura el espacio antes de la tabla

        writeTableHeader(table); // Escribe la cabecera de la tabla
        writeTableData(table); // Escribe los datos de los cursos en la tabla

        document.add(table); // Agrega la tabla al documento PDF
        document.close(); // Cierra el documento PDF
    }
}
