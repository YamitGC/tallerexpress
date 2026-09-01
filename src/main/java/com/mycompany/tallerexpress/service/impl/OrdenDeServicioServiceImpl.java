/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.mycompany.tallerexpress.domain.models.OrdenDeServicio;
import com.mycompany.tallerexpress.domain.models.DetalleOrdenRepuesto;
import com.mycompany.tallerexpress.domain.enums.Estado;
import com.mycompany.tallerexpress.domain.models.Cliente;
import com.mycompany.tallerexpress.domain.exceptions.ReglaNegocioException;
import com.mycompany.tallerexpress.domain.exceptions.EntidadNoEncontradaException;
import com.mycompany.tallerexpress.repository.OrdenDeServicioRepository;
import com.mycompany.tallerexpress.repository.DetalleOrdenRepuestoRepository;
import com.mycompany.tallerexpress.repository.ClienteRepository;
import com.mycompany.tallerexpress.service.OrdenDeServicioService;
/**
 *
 * @author Coder
 */
public class OrdenDeServicioServiceImpl implements OrdenDeServicioService {

    private final OrdenDeServicioRepository ordenRepository;
    private final DetalleOrdenRepuestoRepository detalleRepository;
    private final ClienteRepository clienteRepository;

    public OrdenDeServicioServiceImpl(OrdenDeServicioRepository ordenRepository, 
                                      DetalleOrdenRepuestoRepository detalleRepository,
                                      ClienteRepository clienteRepository) {
        this.ordenRepository = ordenRepository;
        this.detalleRepository = detalleRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public OrdenDeServicio registrarOrden(OrdenDeServicio orden, List<DetalleOrdenRepuesto> repuestosUsados) {
        System.out.println("[POST] /api/ordenes - Registrando nueva orden de servicio");

        // Regla de negocio: Validar que el cliente esté activo
        Cliente cliente = clienteRepository.buscarPorId(orden.getCliente().getId())
                .orElseThrow(() -> new EntidadNoEncontradaException("Cliente no encontrado."));
        
        // Al usar el mismo Enum Estado, comparamos directamente con Estado.ACTIVO
        if (cliente.getEstado() == null || cliente.getEstado() != Estado.ACTIVO) {
            throw new ReglaNegocioException("El cliente debe estar en estado ACTIVO para abrir una orden.");
        }

        // Guardamos la orden maestro primero para generar su ID
        ordenRepository.guardar(orden);

        // Enlazamos los repuestos al ID de la orden generada y guardamos el detalle
        for (DetalleOrdenRepuesto detalle : repuestosUsados) {
            detalle.setOrdenDeServicio(orden);
            detalleRepository.guardar(detalle);
        }

        return orden;
    }


    @Override
    public void actualizarEstado(Long ordenId, Estado nuevoEstado) {
        System.out.println("[PATCH] /api/ordenes/" + ordenId + "/estado - Cambiando estado a: " + nuevoEstado);
        OrdenDeServicio orden = ordenRepository.buscarPorId(ordenId);
        if (orden == null) {
            throw new EntidadNoEncontradaException("La orden de servicio especificada no existe.");
        }
        orden.setEstado(nuevoEstado);
        ordenRepository.actualizar(orden);
    }

    @Override
    public List<OrdenDeServicio> consultarHistorialPorVehiculo(String placa) {
        System.out.println("[GET] /api/ordenes?placa=" + placa + " - Consultando historial por vehículo");
        return ordenRepository.buscarPorVehiculoPlaca(placa);
    }

    @Override
    public BigDecimal calcularCostoTotal(Long ordenId) {
        System.out.println("[GET] /api/ordenes/" + ordenId + "/total - Calculando costos totales de reparación");
        OrdenDeServicio orden = ordenRepository.buscarPorId(ordenId);
        if (orden == null) {
            throw new EntidadNoEncontradaException("Orden no encontrada.");
        }

        // Sumamos los precios históricos de los repuestos asignados a esta orden
        List<DetalleOrdenRepuesto> detalles = detalleRepository.buscarPorOrdenId(ordenId);
        BigDecimal totalRepuestos = detalles.stream()
                .map(DetalleOrdenRepuesto::getPrecioUnitarioHistorico)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Costo Total = Mano de Obra + Repuestos
        return orden.getTotalManoObra().add(totalRepuestos);
    }
}