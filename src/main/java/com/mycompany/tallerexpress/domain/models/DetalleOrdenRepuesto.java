/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.domain.models;

import com.mycompany.tallerexpress.domain.exceptions.DatosInvalidosException;
import java.math.BigDecimal;

/**
 *
 * @author Coder
 */
public class DetalleOrdenRepuesto {
    private Long id;
    private OrdenDeServicio ordenDeServicio;
    private Repuesto repuesto;
    private BigDecimal precioUnitarioHistorico;

    public DetalleOrdenRepuesto(OrdenDeServicio ordenDeServicio, Repuesto repuesto, BigDecimal precioUnitarioHistorico) {
        validarOrdenDeServicio(ordenDeServicio);
        validarRepuesto(repuesto);
        validarPrecioUnitarioHistorico(precioUnitarioHistorico);
        
        this.ordenDeServicio = ordenDeServicio;
        this.repuesto = repuesto;
        this.precioUnitarioHistorico = precioUnitarioHistorico;
    }

    public DetalleOrdenRepuesto(Long id, OrdenDeServicio ordenDeServicio, Repuesto repuesto, BigDecimal precioUnitarioHistorico) {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("El ID debe ser un número positivo válido.");
        }
        validarOrdenDeServicio(ordenDeServicio);
        validarRepuesto(repuesto);
        validarPrecioUnitarioHistorico(precioUnitarioHistorico);
        
        this.id = id;
        this.ordenDeServicio = ordenDeServicio;
        this.repuesto = repuesto;
        this.precioUnitarioHistorico = precioUnitarioHistorico;
    }

    public DetalleOrdenRepuesto() {}
    
    public void validarOrdenDeServicio(OrdenDeServicio ordenDeServicio) {
        if (ordenDeServicio == null) {
            throw new DatosInvalidosException("La orden de servicio es obligatoria.");
        }
    }

    public void validarRepuesto(Repuesto repuesto) {
        if (repuesto == null) {
            throw new DatosInvalidosException("El repuesto es obligatorio.");
        }
    }

    public void validarPrecioUnitarioHistorico(BigDecimal precioUnitarioHistorico) {
        if (precioUnitarioHistorico == null) {
            throw new DatosInvalidosException("El precio unitario histórico es obligatorio.");
        }
        if (precioUnitarioHistorico.compareTo(BigDecimal.ZERO) < 0) {
            throw new DatosInvalidosException("El precio unitario histórico no puede ser negativo.");
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrdenDeServicio getOrdenDeServicio() {
        return ordenDeServicio;
    }

    public void setOrdenDeServicio(OrdenDeServicio ordenDeServicio) {
        validarOrdenDeServicio(ordenDeServicio);
        this.ordenDeServicio = ordenDeServicio;
    }

    public Repuesto getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(Repuesto repuesto) {
        validarRepuesto(repuesto);
        this.repuesto = repuesto;
    }

    public BigDecimal getPrecioUnitarioHistorico() {
        return precioUnitarioHistorico;
    }

    public void setPrecioUnitarioHistorico(BigDecimal precioUnitarioHistorico) {
        validarPrecioUnitarioHistorico(precioUnitarioHistorico);
        this.precioUnitarioHistorico = precioUnitarioHistorico;
    }
    
    
    
}
