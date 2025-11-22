package com.santamaria.tienda.Continua3.mapper;

import com.santamaria.tienda.Continua3.dto.ColeccionesDto;
import com.santamaria.tienda.Continua3.entity.ColeccionesEntity;

public class ColeccionesMapper {

    // Convierte DTO a Entity usando Builder
    public static ColeccionesEntity toEntity(ColeccionesDto dto) {
        if (dto == null) {
            return null;
        }

        return ColeccionesEntity.builder()
                .idColeccion(dto.getIdColeccion())
                .nombre(dto.getNombre())
                .temporada(dto.getTemporada())
                .anio(dto.getAnio())
                .estado(dto.getEstado() != null ? dto.getEstado() : true)
                .build();
    }

    
    // Convierte Entity a DTO usando Builder
    public static ColeccionesDto toDto(ColeccionesEntity entity) {
        if (entity == null) {
            return null;
        }

        return ColeccionesDto.builder()
                .idColeccion(entity.getIdColeccion())
                .nombre(entity.getNombre())
                .temporada(entity.getTemporada())
                .anio(entity.getAnio())
                .estado(entity.getEstado())
                .build();
    }

    // Actualiza una entidad existente con datos del DTO
    public static ColeccionesEntity updateEntity(ColeccionesDto dto, ColeccionesEntity entity) {
        if (dto == null || entity == null) {
            return entity;
        }

        entity.setNombre(dto.getNombre());
        entity.setTemporada(dto.getTemporada());
        entity.setAnio(dto.getAnio());
        if (dto.getEstado() != null) {
            entity.setEstado(dto.getEstado());
        }

        return entity;
    }
}