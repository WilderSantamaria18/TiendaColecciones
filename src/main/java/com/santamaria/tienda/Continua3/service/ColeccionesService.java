package com.santamaria.tienda.Continua3.service;

import java.util.List;
import java.util.Optional;
import com.santamaria.tienda.Continua3.entity.ColeccionesEntity;

public interface ColeccionesService {
    
    Optional<ColeccionesEntity> buscarPorId(Long id);
    
    void inactivar(Long id);
    
    List<ColeccionesEntity> listado();
    
    List<ColeccionesEntity> listadoActivos();
    
    ColeccionesEntity guardar(ColeccionesEntity entidad);
    
    ColeccionesEntity actualizar(ColeccionesEntity entidad);
}
