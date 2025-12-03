package com.santamaria.tienda.Continua3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.santamaria.tienda.Continua3.entity.ProductoEntity;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Long> {
    
}
