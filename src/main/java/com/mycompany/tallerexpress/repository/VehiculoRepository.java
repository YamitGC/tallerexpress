/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.domain.models.Vehiculo;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Coder
 */
public interface VehiculoRepository {
    Vehiculo guardar(Vehiculo vehiculo);
    Optional<Vehiculo>buscarPorId(Long id);
    List<Vehiculo>buscarPorPlaca(String placa);
    List<Vehiculo> listarTodos();
    void actualizar(Vehiculo vehiculo);
    void eliminar(Long id);
    
}
