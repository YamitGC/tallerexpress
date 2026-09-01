/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.domain.models;

import com.mycompany.tallerexpress.domain.enums.Roles;
import com.mycompany.tallerexpress.domain.exceptions.DatosInvalidosException;
import java.time.OffsetDateTime;

/**
 *
 * @author Coder
 */
public class User {
    private Long id;
    private String username;
    private String password;
    private String nombreCompleto;
    private String correo;
    private Roles rol;
    private boolean isActivo;
    private OffsetDateTime createdAt;
    

    public User(String username, String password, String nombreCompleto, String correo, Roles rol, boolean isActivo, OffsetDateTime createdAt) {
        validarUsername(username);
        validarPassword(password);
        validarNombreCompleto(nombreCompleto);
        validarCorreo(correo);
        validarRoles(rol);
        validarIsActivo(isActivo);
        validarCreatedAt(createdAt);
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.rol = rol;
        this.isActivo = isActivo;
        this.createdAt = createdAt;
    }

    public User(Long id, String username, String password, String nombreCompleto, String correo, Roles rol, boolean isActivo, OffsetDateTime createdAt) {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("El ID debe ser un número positivo válido.");
        }
        validarUsername(username);
        validarPassword(password);
        validarNombreCompleto(nombreCompleto);
        validarCorreo(correo);
        validarRoles(rol);
        validarIsActivo(isActivo);
        validarCreatedAt(createdAt);
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.rol = rol;
        this.isActivo = isActivo;
        this.createdAt = createdAt;
    }

    public User() {}
    
    public void validarUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre de usuario es obligatorio.");
        }
    }

    public void validarPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new DatosInvalidosException("La contraseña es obligatoria.");
        }
    }

    public void validarNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre completo es obligatorio.");
        }
    }

    public void validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new DatosInvalidosException("El correo electrónico es obligatorio.");
        }
    }

    public void validarRoles(Roles rol) {
        if (rol == null) {
            throw new DatosInvalidosException("El rol es obligatorio.");
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        validarUsername(username);
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        validarPassword(password);
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        validarNombreCompleto(nombreCompleto);
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        validarCorreo(correo);
        this.correo = correo;
    }

    public Roles getRol() {
        return rol;
    }

    public void setRol(Roles rol) {
        validarRoles(rol);
        this.rol = rol;
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
