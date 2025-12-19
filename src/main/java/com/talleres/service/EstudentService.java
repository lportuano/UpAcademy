package com.talleres.service;

import com.talleres.entity.Estudent;
import com.talleres.repository.EstudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EstudentService {

    @Autowired
    private EstudentRepository estudentRepository;

    //LEER
    public List<Estudent> mostrarEstudiante(){
        return estudentRepository.findAll();
    }

    //Buscar por id
    public Optional<Estudent> buscarEstudianteById(Long id){
        return estudentRepository.findById(id);
    }

    //Buscar por nombre
    public List<Estudent> buscarEstudiantePorNombre(String buscarEstudiante){
        if (buscarEstudiante==null || buscarEstudiante.isEmpty()){
            return estudentRepository.findAll();
        }else{
            return estudentRepository.findByNombreContainingIgnoreCase(buscarEstudiante);
        }
    }

    //GUARDAR
    public Estudent guardarEstudiante(Estudent estudent){
        estudentRepository.save(estudent);
        return estudent;
    }

    //ACTUALIZAR
    public Estudent actualizarEstudiante(Long id, Estudent estudent){
        Estudent estudianteExistente = buscarEstudianteById(id)
                .orElseThrow(()-> new RuntimeException("Estudiante no encontrado"));
        estudianteExistente.setNombre(estudent.getNombre());
        estudianteExistente.setApellido(estudent.getApellido());
        estudianteExistente.setEmail(estudent.getEmail());
        estudianteExistente.setTelefono(estudent.getTelefono());
        estudianteExistente.setFechaNacimiento(estudent.getFechaNacimiento());
        estudianteExistente.setDireccion(estudent.getDireccion());
        estudianteExistente.setFechaInscripcion(estudent.getFechaInscripcion());
        estudianteExistente.setNivelIngles(estudent.getNivelIngles());
        return estudentRepository.save(estudianteExistente);
    }

    //ELIMINAR ESTUDIANTE
    public void eliminarEstudiante(Long id){
        Estudent estudent = buscarEstudianteById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Estudiante no EXISTE"));
        estudentRepository.delete(estudent);
    }

}
