/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.service;

import java.util.List;
import com.mycompany.tallerexpress.domain.models.Cliente;
import com.mycompany.tallerexpress.domain.models.Vehiculo;
/**
 *
 * @author Coder
 */
public interface Cliente_VehiculoService {
    Cliente registrarCliente(Cliente cliente);
    Vehiculo registrarVehiculo(Vehiculo vehiculo);
    List<Vehiculo> consultarHistorialVehiculosPorCliente(Long clienteId);
}
