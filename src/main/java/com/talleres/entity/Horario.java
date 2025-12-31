package com.talleres.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "horario")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nivel;
    private String horaInicio;
    private String horaFin;
    private String fecha;
    private String docenteNombre;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private Usuario usuario;
}