package com.santamaria.tienda.Continua3.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.santamaria.tienda.Continua3.entity.ColeccionesEntity;

public interface ColeccionesRepository extends JpaRepository<ColeccionesEntity, Long> {

   List<ColeccionesEntity> findByEstado(Boolean estado);

   List<ColeccionesEntity> findByNombre(String nombre);

   List<ColeccionesEntity> findByAnio(Integer anio);

   Page<ColeccionesEntity> findByEstado(Boolean estado, Pageable pageable);
}
