package com.talleres.repository;

import com.talleres.entity.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CursosRepository extends JpaRepository<Cursos, Long> {
    Optional<Cursos> findByNivelIgnoreCase(String nivel);
}