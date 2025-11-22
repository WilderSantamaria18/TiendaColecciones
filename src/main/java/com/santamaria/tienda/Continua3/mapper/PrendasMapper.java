package com.santamaria.tienda.Continua3.mapper;

import com.santamaria.tienda.Continua3.dto.PrendasDto;
import com.santamaria.tienda.Continua3.entity.ColeccionesEntity;
import com.santamaria.tienda.Continua3.entity.PrendasEntity;

public class PrendasMapper {

    // Convierte DTO a Entity usando Builder
    public static PrendasEntity toEntity(PrendasDto dto, ColeccionesEntity coleccion) {
        if (dto == null) {
            return null;
        }

        return PrendasEntity.builder()
                .idPrenda(dto.getIdPrenda())
                .nombre(dto.getNombre())
                .talla(dto.getTalla())
                .color(dto.getColor())
                .precio(dto.getPrecio())
                .estado(dto.getEstado() != null ? dto.getEstado() : "disponible")
                .coleccion(coleccion)
                .build();
    }

    // Convierte Entity a DTO usando Builder
    public static PrendasDto toDto(PrendasEntity entity) {
        if (entity == null) {
            return null;
        }

        return PrendasDto.builder()
                .idPrenda(entity.getIdPrenda())
                .nombre(entity.getNombre())
                .talla(entity.getTalla())
                .color(entity.getColor())
                .precio(entity.getPrecio())
                .estado(entity.getEstado())
                .idColeccion(entity.getColeccion() != null ? entity.getColeccion().getIdColeccion() : null)
                .build();
    }

    // Actualiza una entidad existente con datos del DTO
    public static PrendasEntity updateEntity(PrendasDto dto, PrendasEntity entity, ColeccionesEntity coleccion) {
        if (dto == null || entity == null) {
            return entity;
        }

        entity.setNombre(dto.getNombre());
        entity.setTalla(dto.getTalla());
        entity.setColor(dto.getColor());
        entity.setPrecio(dto.getPrecio());
        
        if (dto.getEstado() != null) {
            entity.setEstado(dto.getEstado());
        }
        
        if (coleccion != null) {
            entity.setColeccion(coleccion);
        }

        return entity;
    }
}