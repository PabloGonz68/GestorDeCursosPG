package com.pablo.gestioncursos.gestionCursos.controller;

import com.pablo.gestioncursos.gestionCursos.entity.Curso;
import com.pablo.gestioncursos.gestionCursos.reports.CursoExporterExcel;
import com.pablo.gestioncursos.gestionCursos.reports.CursoExporterPDF;
import com.pablo.gestioncursos.gestionCursos.repository.CursoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CursoController {
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public String home() {
        return "redirect:/cursos";
    }


    @GetMapping("/cursos")
    public String listarCursos(Model model, @Param("keyword") String keyword, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        try{
            // Inicializa una lista de cursos
            List<Curso> cursos = new ArrayList<>();

            // Configura la paginación
            Pageable paging = PageRequest.of(page-1,size);

            // Inicializa una página de cursos
            Page<Curso> pageCursos = null;

            // Verifica si se proporcionó una palabra clave (keyword) para buscar cursos
            if(keyword == null){
                // Si no hay palabra clave, busca todos los cursos paginados
                pageCursos = cursoRepository.findAll(paging);
            } else {
                // Si se proporcionó una palabra clave, busca los cursos que contengan esa palabra clave en el título
                pageCursos = cursoRepository.findByTituloContainingIgnoreCase(keyword, paging);
                // Añade la palabra clave al modelo para mostrarla en la vista
                model.addAttribute("keyword", keyword);
            }

            // Obtiene la lista de cursos de la página actual
            cursos = pageCursos.getContent();

            // Añade los atributos necesarios al modelo para su visualización en la vista
            model.addAttribute("cursos", cursos); // Lista de cursos
            model.addAttribute("currentPage", pageCursos.getNumber() + 1); // Página actual
            model.addAttribute("totalItems", pageCursos.getTotalElements()); // Total de elementos
            model.addAttribute("totalPages", pageCursos.getTotalPages()); // Total de páginas
            model.addAttribute("pageSize", size); // Tamaño de la página
        } catch (Exception e) {
            // Si ocurre algún error, añade un mensaje de error al modelo
            model.addAttribute("message", e.getMessage());
        }

        // Devuelve el nombre de la vista a la que se redirigirá
        return "cursos";
    }

    @GetMapping("/cursos/nuevo")
    public String mostrarFormulario(Model model) {
        Curso curso = new Curso();
        curso.setPublicado(true);
        model.addAttribute("curso", curso);
        model.addAttribute("pageTitle", "Nuevo Curso");
        return "cursoForm";

    }

    @PostMapping("/cursos/save")
    public String guardarCurso(Curso curso, RedirectAttributes redirectAttributes) {
        try {
            cursoRepository.save(curso);
            redirectAttributes.addFlashAttribute("message", "El curso ha sido guardado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/cursos";

    }

    @GetMapping("/cursos/{id}")
    public String editarCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Curso curso= cursoRepository.findById(id).get();
            redirectAttributes.addFlashAttribute("message", "El curso ha sido actualizado con exito");
            model.addAttribute("curso", curso);
            model.addAttribute("pageTitle", "Editar Curso: "+id);
            return "cursoForm";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/cursos";
    }

    @GetMapping("cursos/delete/{id}")
    public String eliminarCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes){
    try {
        cursoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "El curso ha sido eliminado con exito");
    }catch (Exception e){
        redirectAttributes.addFlashAttribute("message", e.getMessage());
    }
    return "redirect:/cursos";
    }

    @GetMapping("/export/pdf")
    public void generarReportePDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue="attachment; filename=cursos" + currentDateTime+".pdf";
        response.setHeader(headerKey, headerValue);

        List<Curso> cursos = cursoRepository.findAll();
        CursoExporterPDF exporterPDF = new CursoExporterPDF(cursos);
        exporterPDF.export(response);


    }
    @GetMapping("/export/excel")
    public void generarReporteExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey ="Content-Disposition";
        String headerValue = "attachment; filename=cursos"+currentDateTime+".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Curso> cursos = cursoRepository.findAll();
        CursoExporterExcel exporterExcel = new CursoExporterExcel(cursos);
        exporterExcel.export(response);



    }
}
