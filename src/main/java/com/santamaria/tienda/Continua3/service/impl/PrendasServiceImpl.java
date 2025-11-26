package com.santamaria.tienda.Continua3.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.santamaria.tienda.Continua3.entity.PrendasEntity;
import com.santamaria.tienda.Continua3.repository.PrendasRepository;
import com.santamaria.tienda.Continua3.service.PrendasService;
import com.santamaria.tienda.Continua3.util.ConstantesApp;

@Service
public class PrendasServiceImpl implements PrendasService {

    private final PrendasRepository prendasRepository;

    public PrendasServiceImpl(PrendasRepository prendasRepository) {
        this.prendasRepository = prendasRepository;
    }

    @Override
    public List<PrendasEntity> listarTodas() {
        return prendasRepository.findAll();
    }

    @Override
    public Optional<PrendasEntity> obtenerPorId(Long id) {
        return prendasRepository.findById(id);
    }

    @Override
    public List<PrendasEntity> listarPorColeccion(Long coleccionId) {
        // RF19.6: Listar todas las prendas de una coleccion, sin filtrar por estado
        return prendasRepository.findByColeccion_IdColeccion(coleccionId);
    }

    @Override
    public List<PrendasEntity> buscarPorNombreOTalla(String termino) {
        if (termino == null) {
            return List.of();
        }
        return prendasRepository.findByNombreContainingIgnoreCaseOrTallaContainingIgnoreCase(termino, termino);
    }

    @Override
    public List<PrendasEntity> filtrarPorEstado(String estado) {
        return prendasRepository.findByEstado(estado);
    }

    @Override
    public PrendasEntity guardar(PrendasEntity prenda) {
        // Validación de prenda nula
        if (prenda == null) {
            throw new IllegalArgumentException("Los datos de la prenda son requeridos");
        }
        
        // Validar que el precio sea mayor que cero
        if (prenda.getPrecio() == null || prenda.getPrecio().doubleValue() <= 0) {
            throw new com.santamaria.tienda.Continua3.exception.PrecioInvalidoException(
                "El precio debe ser un número mayor que cero. Precio recibido: " + 
                (prenda.getPrecio() != null ? prenda.getPrecio() : "null")
            );
        }
        
        //  No permitir registrar prenda sin colección asociada
        if (prenda.getColeccion() == null || prenda.getColeccion().getIdColeccion() == null) {
            throw new com.santamaria.tienda.Continua3.exception.PrendaSinColeccionException(
                "No se puede registrar una prenda sin una colección asociada"
            );
        }
        
        if (prenda.getEstado() == null) {
            prenda.setEstado(ConstantesApp.ESTADO_DISPONIBLE);
        }
        
        return prendasRepository.save(prenda);
    }

    @Override
    public PrendasEntity actualizar(PrendasEntity prenda) {
        if (prenda == null || prenda.getIdPrenda() == null) {
            throw new IllegalArgumentException("El ID de prenda es requerido");
        }
        
        PrendasEntity existente = prendasRepository.findById(prenda.getIdPrenda())
                .orElseThrow(() -> new IllegalArgumentException("Prenda no encontrada"));
        
        existente.setNombre(prenda.getNombre());
        existente.setPrecio(prenda.getPrecio());
        existente.setTalla(prenda.getTalla());
        existente.setColor(prenda.getColor());
        existente.setColeccion(prenda.getColeccion());
        existente.setEstado(prenda.getEstado());
        
        return prendasRepository.save(existente);
    }

    @Override
    public void inactivar(Long id) {
        // RF19.7: Cambiar estado a descontinuada para marcar la prenda como inactiva
        PrendasEntity prenda = prendasRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prenda no encontrada"));
        
        prenda.setEstado("descontinuada");
        prendasRepository.save(prenda);
    }
}
