package com.santamaria.tienda.Continua3.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrendasDto {

    private Long idPrenda;

    @NotBlank(message = "El nombre de la prenda es obligatorio")
    private String nombre;

    private String talla;

    private String color;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
    private BigDecimal precio;

    @Builder.Default
    private String estado = "disponible";

    @NotNull(message = "La prenda debe estar asociada a una colección")
    private Long idColeccion;
}