/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.service.impl;

import java.time.OffsetDateTime;
import java.util.List;
import com.mycompany.tallerexpress.domain.models.User;
import com.mycompany.tallerexpress.domain.enums.Roles;
import com.mycompany.tallerexpress.service.UserService;
/**
 *
 * @author Coder
 */
public class UserRegistrationDecorator implements UserService {
    
    private final UserService serviceDecorado;

    public UserRegistrationDecorator(UserService serviceDecorado) {
        this.serviceDecorado = serviceDecorado;
    }

    @Override
    public User registrar(User user) {
        System.out.println("[DECORATOR] Inyectando propiedades por defecto al usuario...");
        user.setRol(Roles.RECEPCIONISTA);
        user.setIsActivo(true);
        user.setCreatedAt(OffsetDateTime.now());
        
        return serviceDecorado.registrar(user);
    }

    @Override
    public User login(String username, String password) {
        return serviceDecorado.login(username, password);
    }

    @Override
    public List<User> listarTodos() {
        return serviceDecorado.listarTodos();
    }
}