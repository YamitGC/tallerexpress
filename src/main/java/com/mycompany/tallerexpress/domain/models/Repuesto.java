/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.domain.models;

import com.mycompany.tallerexpress.domain.exceptions.DatosInvalidosException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 *
 * @author Coder
 */
public class Repuesto {
    private Long id;
    private String codigoReferencia;
    private String nombre;
    private String categoria;
    private String proveedor;
    private Long stockTotal;
    private Long stockDisponible;
    private BigDecimal precioUnitario;
    private boolean isActivo;
    private OffsetDateTime createdAt;

    public Repuesto(String codigoReferencia, String nombre, String categoria, String proveedor, Long stockTotal, Long stockDisponible, BigDecimal precioUnitario, boolean isActivo, OffsetDateTime createdAt) {
        validarCodigoReferencia(codigoReferencia);
        validarNombre(nombre);
        validarCategoria(categoria);
        validarProveedor(proveedor);
        validarStockTotal(stockTotal);
        validarStockDisponible(stockDisponible);
        validarPrecioUnitario(precioUnitario);
        validarIsActivo(isActivo);
        validarCreatedAt(createdAt);
        
        this.codigoReferencia = codigoReferencia;
        this.nombre = nombre;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.stockTotal = stockTotal;
        this.stockDisponible = stockDisponible;
        this.precioUnitario = precioUnitario;
        this.isActivo = isActivo;
        this.createdAt = createdAt;
    }

    public Repuesto(Long id, String codigoReferencia, String nombre, String categoria, String proveedor, Long stockTotal, Long stockDisponible, BigDecimal precioUnitario, boolean isActivo, OffsetDateTime createdAt) {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("El ID debe ser un número positivo válido.");
        }
        validarCodigoReferencia(codigoReferencia);
        validarNombre(nombre);
        validarCategoria(categoria);
        validarProveedor(proveedor);
        validarStockTotal(stockTotal);
        validarStockDisponible(stockDisponible);
        validarPrecioUnitario(precioUnitario);
        validarIsActivo(isActivo);
        validarCreatedAt(createdAt);
        
        this.id = id;
        this.codigoReferencia = codigoReferencia;
        this.nombre = nombre;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.stockTotal = stockTotal;
        this.stockDisponible = stockDisponible;
        this.precioUnitario = precioUnitario;
        this.isActivo = isActivo;
        this.createdAt = createdAt;
    }

    public Repuesto() {}
    
    public void validarCodigoReferencia(String codigoReferencia) {
        if (codigoReferencia == null || codigoReferencia.trim().isEmpty()) {
            throw new DatosInvalidosException("El código de referencia es obligatorio.");
        }
    }

    public void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre del repuesto es obligatorio.");
        }
    }

    public void validarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new DatosInvalidosException("La categoría es obligatoria.");
        }
    }

    public void validarProveedor(String proveedor) {
        if (proveedor == null || proveedor.trim().isEmpty()) {
            throw new DatosInvalidosException("El proveedor es obligatorio.");
        }
    }

    public void validarStockTotal(Long stockTotal) {
        if (stockTotal == null) {
            throw new DatosInvalidosException("El stock total es obligatorio.");
        }
        if (stockTotal < 0) {
            throw new DatosInvalidosException("El stock total no puede ser negativo.");
        }
    }

    public void validarStockDisponible(Long stockDisponible) {
        if (stockDisponible == null) {
            throw new DatosInvalidosException("El stock disponible es obligatorio.");
        }
        if (stockDisponible < 0) {
            throw new DatosInvalidosException("El stock disponible no puede ser negativo.");
        }
    }

    public void validarPrecioUnitario(BigDecimal precioUnitario) {
        if (precioUnitario == null) {
            throw new DatosInvalidosException("El precio unitario es obligatorio.");
        }
        if (precioUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new DatosInvalidosException("El precio unitario no puede ser negativo.");
        }
    }

    public void validarIsActivo(Boolean isActivo) {
        if (isActivo == null) {
            throw new DatosInvalidosException("El estado de actividad es obligatorio.");
        }
    }

    public void validarCreatedAt(OffsetDateTime createdAt) {
        if (createdAt == null) {
            throw new DatosInvalidosException("La fecha de creación es obligatoria.");
        }
        if (createdAt.isAfter(OffsetDateTime.now())) {
            throw new DatosInvalidosException("La fecha de creación no puede ser en el futuro.");
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoReferencia() {
        return codigoReferencia;
    }

    public void setCodigoReferencia(String codigoReferencia) {
        validarCodigoReferencia(codigoReferencia);
        this.codigoReferencia = codigoReferencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        validarNombre(nombre);
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        validarCategoria(categoria);
        this.categoria = categoria;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        validarProveedor(proveedor);
        this.proveedor = proveedor;
    }

    public Long getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(Long stockTotal) {
        validarStockTotal(stockTotal);
        this.stockTotal = stockTotal;
    }

    public Long getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(Long stockDisponible) {
        validarStockDisponible(stockDisponible);
        this.stockDisponible = stockDisponible;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        validarPrecioUnitario(precioUnitario);
        this.precioUnitario = precioUnitario;
    }

    public boolean isIsActivo() {
        return isActivo;
    }

    public void setIsActivo(boolean isActivo) {
        validarIsActivo(isActivo);
        this.isActivo = isActivo;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        validarCreatedAt(createdAt);
        this.createdAt = createdAt;
    }
    
    
}
