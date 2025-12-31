package com.talleres.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    // CORRECCIÓN 1: Eliminado @Enumerated porque tu atributo es String
    @Column(name = "role")
    private String role;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]+$", message = "El apellido solo puede contener letras y espacios")
    private String apellido;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 10, max = 10, message = "La cédula debe tener exactamente 10 dígitos")
    @Pattern(regexp = "^[0-9]+$", message = "La cédula solo puede contener números")
    private String cedula;

    // CORRECCIÓN 2: Unificado en una sola variable.
    // Eliminado @Temporal (no se usa con LocalDate) y la variable duplicada.
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener exactamente 10 dígitos numéricos")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(min = 5, max = 200, message = "La dirección debe tener entre 5 y 200 caracteres")
    @Pattern(regexp = "^[^0-9]*$", message = "La dirección no debe contener números")
    private String direccion;

    @NotBlank(message = "El nivel de inglés es obligatorio")
    @Pattern(regexp = "^(A1|A2|B1|B2|C1|C2|DOCENTE)$",
            message = "Nivel de inglés inválido. Valores permitidos: A1, A2, B1, B2, C1, C2")
    private String nivelIngles;


}