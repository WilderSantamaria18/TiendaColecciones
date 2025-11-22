package com.santamaria.tienda.Continua3.entity;



import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "colecciones")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColeccionesEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coleccion")
    private Long idColeccion;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 50)
    private String temporada;
    
    @Column(nullable = false)
    private Integer anio;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean estado = true;
    
    @OneToMany(mappedBy = "coleccion")
    private List<PrendasEntity> prendas;
}