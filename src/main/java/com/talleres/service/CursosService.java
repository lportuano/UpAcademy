package com.talleres.service;

import com.talleres.entity.Cursos;
import com.talleres.repository.CursosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursosService {

    @Autowired
    private CursosRepository cursosRepository;

    public List<Cursos> listarTodos() {
        return cursosRepository.findAll();
    }

    public Optional<Cursos> buscarPorId(Long id) {
        return cursosRepository.findById(id);
    }

    public Optional<Cursos> buscarPorNivel(String nivel) {
        return cursosRepository.findByNivelIgnoreCase(nivel);
    }

    public void guardarCurso(Cursos curso) {
        cursosRepository.save(curso);
    }

    public void eliminarCurso(Long id) {
        cursosRepository.deleteById(id);
    }
}