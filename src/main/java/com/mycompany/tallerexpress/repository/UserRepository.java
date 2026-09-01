/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.domain.models.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Coder
 */
public interface UserRepository {
    User guardar(User user);
    Optional<User> buscarPorId(Long id);
    Optional<User> buscarPorCorreo(String correo);
    List<User> listarTodos();
    void actualizar(User user);
    void eliminar(Long id);
    
}
