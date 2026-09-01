/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.service.impl;

import java.util.List;
import com.mycompany.tallerexpress.domain.models.User;
import com.mycompany.tallerexpress.domain.exceptions.DatosInvalidosException;
import com.mycompany.tallerexpress.repository.UserRepository;
import com.mycompany.tallerexpress.service.UserService;
/**
 *
 * @author Coder
 */
public class UserServiceImpl implements UserService {

    protected final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        System.out.println("[POST] /api/auth/login - Intentando autenticación para: " + username);
        return userRepository.listarTodos().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new DatosInvalidosException("Credenciales de acceso incorrectas."));
    }

    @Override
    public User registrar(User user) {
        System.out.println("[POST] /api/usuarios - Ejecutando registro base de usuario");
        return userRepository.guardar(user);
    }

    @Override
    public List<User> listarTodos() {
        System.out.println("[GET] /api/usuarios - Listando usuarios");
        return userRepository.listarTodos();
    }
}