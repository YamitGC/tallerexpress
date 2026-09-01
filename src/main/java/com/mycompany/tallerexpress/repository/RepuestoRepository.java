/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.domain.models.Repuesto;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author Coder
 */
public interface RepuestoRepository {
    Repuesto guardar(Repuesto repuesto);
    Optional<Repuesto>buscarPorId(Long id);
    List<Repuesto>buscarPorCategoria(String categoria);
    List<Repuesto>buscarPorProveedor(String proveedor);
    List<Repuesto> listarTodos();
    void actualizar(Repuesto repuesto);
    void eliminar(Long id);
    
}
