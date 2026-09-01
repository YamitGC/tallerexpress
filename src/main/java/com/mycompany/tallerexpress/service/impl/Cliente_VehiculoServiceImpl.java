/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.service.impl;

import java.util.List;
import com.mycompany.tallerexpress.domain.models.Cliente;
import com.mycompany.tallerexpress.domain.models.Vehiculo;
import com.mycompany.tallerexpress.domain.exceptions.EntidadDuplicadaException;
import com.mycompany.tallerexpress.domain.exceptions.EntidadNoEncontradaException;
import com.mycompany.tallerexpress.repository.ClienteRepository;
import com.mycompany.tallerexpress.repository.VehiculoRepository;
import com.mycompany.tallerexpress.service.Cliente_VehiculoService;
/**
 *
 * @author Coder
 */
public class Cliente_VehiculoServiceImpl implements Cliente_VehiculoService {

    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;

    public Cliente_VehiculoServiceImpl(ClienteRepository clienteRepository, VehiculoRepository vehiculoRepository) {
        this.clienteRepository = clienteRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    public Cliente registrarCliente(Cliente cliente) {
        System.out.println("[POST] /api/clientes - Registrando nuevo cliente");
        return clienteRepository.guardar(cliente);
    }

    @Override
    public Vehiculo registrarVehiculo(Vehiculo vehiculo) {
        System.out.println("[POST] /api/vehiculos - Registrando vehículo con placa: " + vehiculo.getPlaca());
        
        // Regla de negocio: Validar que la placa sea única
        if (!vehiculoRepository.buscarPorPlaca(vehiculo.getPlaca()).isEmpty()) {
            throw new EntidadDuplicadaException("La placa del vehículo ya se encuentra registrada.");
        }
        
        // Validar que el cliente asociado exista
        clienteRepository.buscarPorId(vehiculo.getCliente().getId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El cliente asociado al vehículo no existe."));

        return vehiculoRepository.guardar(vehiculo);
    }

    @Override
    public List<Vehiculo> consultarHistorialVehiculosPorCliente(Long clienteId) {
        System.out.println("[GET] /api/clientes/" + clienteId + "/vehiculos - Obteniendo historial de vehículos");
        // Filtramos en memoria o extendiendo el repositorio si fuera necesario
        return vehiculoRepository.listarTodos().stream()
                .filter(v -> v.getCliente().getId().equals(clienteId))
                .toList();
    }
}