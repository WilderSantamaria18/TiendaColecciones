package com.santamaria.tienda.Continua3.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.santamaria.tienda.Continua3.entity.PrendasEntity;

public interface PrendasRepository extends JpaRepository<PrendasEntity, Long> {
    
    List<PrendasEntity> findByColeccion_IdColeccion(Long coleccionId);

    List<PrendasEntity> findByNombreContainingIgnoreCaseOrTallaContainingIgnoreCase(String nombre, String talla);

    List<PrendasEntity> findByEstado(String estado);

    List<PrendasEntity> findByColeccion_IdColeccionAndEstado(Long coleccionId, String estado);
}
