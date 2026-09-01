/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.domain.models;

import com.mycompany.tallerexpress.domain.enums.Estado;
import com.mycompany.tallerexpress.domain.exceptions.DatosInvalidosException;
import java.time.OffsetDateTime;

/**
 *
 * @author Coder
 */
public class Cliente {
    private Long id;
    private String numeroIdentificacion;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String direccion;
    private Estado estado;
    private OffsetDateTime fechaRegistro;
    
    public Cliente(){}

    public Cliente(String numeroIdentificacion, String nombreCompleto, String telefono, String correo, String direccion, Estado estado, OffsetDateTime fechaRegistro) {
        validarNumeroIdentificacion(numeroIdentificacion);
        validarNombreCompleto(nombreCompleto);
        validarTelefono(telefono);
        validarCorreo(correo);
        validarDireccion(direccion);
        validarEstado(estado);
        validarFechaRegistro(fechaRegistro);
        
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public Cliente(Long id, String numeroIdentificacion, String nombreCompleto, String telefono, String correo, String direccion, Estado estado, OffsetDateTime fechaRegistro) {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("El ID debe ser un número positivo válido.");
        }
        validarNumeroIdentificacion(numeroIdentificacion);
        validarNombreCompleto(nombreCompleto);
        validarTelefono(telefono);
        validarCorreo(correo);
        validarDireccion(direccion);
        validarEstado(estado);
        validarFechaRegistro(fechaRegistro);
        
        this.id = id;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    
    public void validarNumeroIdentificacion(String numeroIdentificacion){
        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            throw new DatosInvalidosException("El numero de identificacion es obligatorio.");
        }
    }
    
    public void validarNombreCompleto(String nombreCompleto){
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre completo es obligatorio.");
        }
    }
    
    public void validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new DatosInvalidosException("El teléfono es obligatorio.");
        }
    }

    public void validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new DatosInvalidosException("El correo electrónico es obligatorio.");
        }
    }

    public void validarDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new DatosInvalidosException("La dirección es obligatoria.");
        }
    }
    
    public void validarEstado(Estado estado) {
        if (estado == null) {
            throw new DatosInvalidosException("El estado es obligatorio.");
        }
    }

    public void validarFechaRegistro(OffsetDateTime fechaRegistro) {
        if (fechaRegistro == null) {
            throw new DatosInvalidosException("La fecha de registro es obligatoria.");
        }
        if (fechaRegistro.isAfter(OffsetDateTime.now())) {
            throw new DatosInvalidosException("La fecha de registro no puede ser en el futuro.");
        }
    }


    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        validarNumeroIdentificacion(numeroIdentificacion);
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        validarNombreCompleto(nombreCompleto);
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        validarTelefono(telefono);
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        validarCorreo(correo);
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        validarDireccion(direccion);
        this.direccion = direccion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        validarEstado(estado);
        this.estado = estado;
    }

    public OffsetDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(OffsetDateTime fechaRegistro) {
        validarFechaRegistro(fechaRegistro);
        this.fechaRegistro = fechaRegistro;
    }
    

}
