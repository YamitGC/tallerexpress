/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.service;

import java.util.List;
import com.mycompany.tallerexpress.domain.models.User;

/**
 *
 * @author Coder
 */
public interface UserService {
    User login(String username, String password);
    User registrar(User user);
    List<User> listarTodos();
}
