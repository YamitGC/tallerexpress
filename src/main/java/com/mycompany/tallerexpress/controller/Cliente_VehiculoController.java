/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.controller;

import java.util.List;
import com.mycompany.tallerexpress.domain.models.Cliente;
import com.mycompany.tallerexpress.domain.models.Vehiculo;
import com.mycompany.tallerexpress.domain.exceptions.*;
import com.mycompany.tallerexpress.service.Cliente_VehiculoService;
/**
 *
 * @author Coder
 */
public class Cliente_VehiculoController {

    private final Cliente_VehiculoService service;

    public Cliente_VehiculoController(Cliente_VehiculoService service) {
        this.service = service;
    }

    public Respuesta<Cliente> registrarCliente(Cliente cliente) {
        try {
            Cliente registrado = service.registrarCliente(cliente);
            return new Respuesta<>(201, "Cliente registrado exitosamente", registrado);
        } catch (DatosInvalidosException e) {
            return new Respuesta<>(400, e.getMessage(), null);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al registrar cliente", null);
        }
    }

    public Respuesta<Vehiculo> registrarVehiculo(Vehiculo vehiculo) {
        try {
            Vehiculo registrado = service.registrarVehiculo(vehiculo);
            return new Respuesta<>(201, "Vehículo registrado y asociado correctamente", registrado);
        } catch (EntidadDuplicadaException | DatosInvalidosException e) {
            return new Respuesta<>(400, e.getMessage(), null);
        } catch (EntidadNoEncontradaException e) {
            return new Respuesta<>(404, e.getMessage(), null);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al registrar vehículo", null);
        }
    }

    public Respuesta<List<Vehiculo>> consultarHistorialPorCliente(Long clienteId) {
        try {
            List<Vehiculo> historial = service.consultarHistorialVehiculosPorCliente(clienteId);
            return new Respuesta<>(200, "Historial vehicular obtenido", historial);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al consultar historial", null);
        }
    }
}