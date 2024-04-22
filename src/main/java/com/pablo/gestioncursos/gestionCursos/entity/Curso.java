package com.pablo.gestioncursos.gestionCursos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "cursos")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 150)
    private String titulo;
    @Column(length = 250)
    private String descripcion;
    @Column(nullable = false)
    private int nivel;
    @Column(name = "estadoPublicacion")
    private boolean isPublicado;
}
