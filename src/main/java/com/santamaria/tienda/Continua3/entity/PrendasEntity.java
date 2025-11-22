package com.santamaria.tienda.Continua3.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prendas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrendasEntity {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_prenda")
	private Long idPrenda;
    
	
	@Column(nullable = false, length = 100)
	private String nombre;
  
    @Column(length = 10)
    private String talla;
    
    @Column(length = 50)
    private String color;
    
	@Column(nullable = false, precision = 10, scale = 2)
	
	private BigDecimal precio;
    
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String estado = "disponible";
    
	@ManyToOne(optional = false)
	@JoinColumn(name = "id_coleccion", nullable = false)
	private ColeccionesEntity coleccion;
}