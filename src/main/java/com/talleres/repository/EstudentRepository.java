package com.talleres.repository;

import com.talleres.entity.Estudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudentRepository extends JpaRepository<Estudent, Long> {

List<Estudent> findByNombreContainingIgnoreCase(String nombre);

}
