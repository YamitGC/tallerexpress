/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.service;

import java.util.List;
import com.mycompany.tallerexpress.domain.models.Repuesto;
/**
 *
 * @author Coder
 */
public interface RepuestoService {
    Repuesto registrar(Repuesto repuesto);
    void actualizar(Repuesto repuesto);
    List<Repuesto> listarTodos();
    List<Repuesto> buscarPorCategoria(String categoria);
    List<Repuesto> buscarPorProveedor(String proveedor);
}