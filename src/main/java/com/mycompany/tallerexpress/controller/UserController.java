/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.controller;

import java.util.List;
import com.mycompany.tallerexpress.domain.models.User;
import com.mycompany.tallerexpress.domain.exceptions.DatosInvalidosException;
import com.mycompany.tallerexpress.service.UserService;
/**
 *
 * @author Coder
 */
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Respuesta<User> login(String username, String password) {
    try {
        User usuario = userService.login(username, password);
        return new Respuesta<>(200, "Autenticación exitosa", usuario);
    } catch (DatosInvalidosException e) {
        return new Respuesta<>(401, e.getMessage(), null);
    } catch (Exception e) {
        return new Respuesta<>(500, "Error en el proceso de login", null);
    }
    }

    public Respuesta<User> registrarRecepcionista(User user) {
        try {
            // Invoca al servicio que tiene aplicado el Decorador estructural
            User registrado = userService.registrar(user);
            return new Respuesta<>(201, "Usuario asignado con propiedades por defecto con éxito", registrado);
        } catch (DatosInvalidosException e) {
            return new Respuesta<>(400, e.getMessage(), null);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al registrar usuario", null);
        }
    }

    public Respuesta<List<User>> listarTodos() {
        try {
            List<User> usuarios = userService.listarTodos();
            return new Respuesta<>(200, "Listado de usuarios obtenido", usuarios);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al obtener usuarios", null);
        }
    }
}
