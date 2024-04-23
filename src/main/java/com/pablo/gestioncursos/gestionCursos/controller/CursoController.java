package com.pablo.gestioncursos.gestionCursos.controller;

import com.pablo.gestioncursos.gestionCursos.entity.Curso;
import com.pablo.gestioncursos.gestionCursos.reports.CursoExporterExcel;
import com.pablo.gestioncursos.gestionCursos.reports.CursoExporterPDF;
import com.pablo.gestioncursos.gestionCursos.repository.CursoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    public String listarCursos(Model model) {
        List<Curso> cursos = cursoRepository.findAll();
        model.addAttribute("cursos", cursos);
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
