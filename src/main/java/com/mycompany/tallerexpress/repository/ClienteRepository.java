/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.domain.models.Cliente;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author Coder
 */
public interface ClienteRepository {
    Cliente guardar(Cliente cliente);
    Optional<Cliente>buscarPorId(Long id);
    List<Cliente>buscarPorNumeroIdentificacion(String numeroIdentificacion);
    List<Cliente> listarTodos();
    void actualizar(Cliente cliente);
    void eliminar(Long id);
    
}
