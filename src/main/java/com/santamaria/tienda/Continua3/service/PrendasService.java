package com.santamaria.tienda.Continua3.service;

import java.util.List;
import java.util.Optional;
import com.santamaria.tienda.Continua3.entity.PrendasEntity;

public interface PrendasService {
    
    List<PrendasEntity> listarTodas();
    
    Optional<PrendasEntity> obtenerPorId(Long id);
    
    List<PrendasEntity> listarPorColeccion(Long coleccionId);
    
    List<PrendasEntity> buscarPorNombreOTalla(String termino);
    
    List<PrendasEntity> filtrarPorEstado(String estado);
    
    PrendasEntity guardar(PrendasEntity prenda);
    
    PrendasEntity actualizar(PrendasEntity prenda);
    
    void inactivar(Long id);
}
