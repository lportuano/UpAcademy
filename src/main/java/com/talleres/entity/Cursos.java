package com.talleres.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="curso")
public class Cursos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_curso")
    private String nombreCurso;

    private String nivel;
    private Double precio;

    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Matricula> matriculas = new ArrayList<>();
}