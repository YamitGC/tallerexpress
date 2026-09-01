/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.controller;

import java.math.BigDecimal;
import java.util.List;
import com.mycompany.tallerexpress.domain.models.OrdenDeServicio;
import com.mycompany.tallerexpress.domain.models.DetalleOrdenRepuesto;
import com.mycompany.tallerexpress.domain.enums.Estado;
import com.mycompany.tallerexpress.domain.exceptions.*;
import com.mycompany.tallerexpress.service.OrdenDeServicioService;


/**
 *
 * @author Coder
 */
public class OrdenDeServicioController {

    private final OrdenDeServicioService ordenService;

    public OrdenDeServicioController(OrdenDeServicioService ordenService) {
        this.ordenService = ordenService;
    }

    public Respuesta<OrdenDeServicio> registrarOrden(OrdenDeServicio orden, List<DetalleOrdenRepuesto> repuestos) {
        try {
            OrdenDeServicio registrada = ordenService.registrarOrden(orden, repuestos);
            return new Respuesta<>(201, "Orden de Servicio abierta correctamente", registrada);
        } catch (ReglaNegocioException | DatosInvalidosException e) {
            return new Respuesta<>(400, e.getMessage(), null);
        } catch (EntidadNoEncontradaException e) {
            return new Respuesta<>(404, e.getMessage(), null);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al procesar la orden", null);
        }
    }

    public Respuesta<String> actualizarEstado(Long ordenId, Estado nuevoEstado) {
        try {
            ordenService.actualizarEstado(ordenId, nuevoEstado);
            return new Respuesta<>(200, "Estado modificado exitosamente", null);
        } catch (EntidadNoEncontradaException e) {
            return new Respuesta<>(404, e.getMessage(), null);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al actualizar estado", null);
        }
    }

    public Respuesta<List<OrdenDeServicio>> consultarHistorialPorVehiculo(String placa) {
        try {
            List<OrdenDeServicio> historial = ordenService.consultarHistorialPorVehiculo(placa);
            return new Respuesta<>(200, "Historial de servicios obtenido", historial);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al consultar historial técnico", null);
        }
    }

    public Respuesta<BigDecimal> calcularCostoTotal(Long ordenId) {
        try {
            BigDecimal total = ordenService.calcularCostoTotal(ordenId);
            return new Respuesta<>(200, "Cálculo financiero exitoso", total);
        } catch (EntidadNoEncontradaException e) {
            return new Respuesta<>(404, e.getMessage(), null);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al liquidar la orden", null);
        }
    }
}
