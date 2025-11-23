package com.santamaria.tienda.Continua3.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.santamaria.tienda.Continua3.entity.ColeccionesEntity;
import com.santamaria.tienda.Continua3.repository.ColeccionesRepository;
import com.santamaria.tienda.Continua3.service.ColeccionesService;

@Service
public class ColeccionesServiceImpl implements ColeccionesService {
    
    private final ColeccionesRepository coleccionesRepository;

    public ColeccionesServiceImpl(ColeccionesRepository coleccionesRepository) {
        this.coleccionesRepository = coleccionesRepository;
    }

    @Override
    public Optional<ColeccionesEntity> buscarPorId(Long id) {
        return coleccionesRepository.findById(id);
    }

    @Override
    public void inactivar(Long id){
        coleccionesRepository.findById(id).ifPresent(coleccion -> {
            coleccion.setEstado(false);
            coleccionesRepository.save(coleccion);
        });
    }

    @Override
    public List<ColeccionesEntity> listado(){
        return coleccionesRepository.findAll();
    }

    @Override
    public List<ColeccionesEntity> listadoActivos(){
        return coleccionesRepository.findByEstado(true);
    }

    @Override
    public ColeccionesEntity guardar(ColeccionesEntity entidad) {
        // Validación de entidad nula
        if (entidad == null) {
            throw new IllegalArgumentException("Los datos de la colección son requeridos");
        }
        
        // Validar que el nombre de la colección sea obligatorio
        if (entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()) {
            throw new com.santamaria.tienda.Continua3.exception.DatosInvalidosException(
                "nombre", "el nombre de la colección es obligatorio"
            );
        }
        
        // Validar que el año sea numérico y razonable
        if (entidad.getAnio() == null || entidad.getAnio() < 1900 || entidad.getAnio() > 2100) {
            throw new com.santamaria.tienda.Continua3.exception.DatosInvalidosException(
                "año", "el año debe estar entre 1900 y 2100. Año recibido: " + entidad.getAnio()
            );
        }
       
        if (entidad.getIdColeccion() == null && entidad.getEstado() == null) {
            entidad.setEstado(true);
        }
        return coleccionesRepository.save(entidad);
    }

    @Override
    public ColeccionesEntity actualizar(ColeccionesEntity entidad){
        if(entidad == null || entidad.getIdColeccion() == null){
            throw new IllegalArgumentException("Id de colección requerido para actualizar.");
        }
        
        if(!coleccionesRepository.existsById(entidad.getIdColeccion())){
            throw new IllegalArgumentException("Colección no encontrada con id: " + entidad.getIdColeccion());
        }
        return coleccionesRepository.save(entidad);
    }
}
