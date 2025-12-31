package com.talleres.service;

import com.talleres.entity.Horario;
import com.talleres.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorariosService {

    @Autowired
    private HorarioRepository horarioRepository;

    /* Lista para mostrar todos los horarios registrados en la base de datos */
    public List<Horario> listarTodos() {
        return horarioRepository.findAll();
    }

    /* Guarda o actualiza un horario */
    public void guardar(Horario horario) {
        horarioRepository.save(horario);
    }

    /* Elimina un horario por su ID */
    public void eliminar(Long id) {
        horarioRepository.deleteById(id);
    }

    /* Busca un horario específico por ID */
    public Horario buscarPorId(Long id) {
        return horarioRepository.findById(id).orElse(null);
    }

    /* Muestra los horarios según el nivel registrado */
    public List<Horario> obtenerHorariosPorNivel(String nivel) {
        return horarioRepository.findAll().stream()
                .filter(h -> h.getNivel().equalsIgnoreCase(nivel))
                .collect(Collectors.toList());
    }
}