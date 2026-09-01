/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.service;

import java.math.BigDecimal;
import java.util.List;
import com.mycompany.tallerexpress.domain.models.OrdenDeServicio;
import com.mycompany.tallerexpress.domain.models.DetalleOrdenRepuesto;
import com.mycompany.tallerexpress.domain.enums.Estado;
/**
 *
 * @author Coder
 */
public interface OrdenDeServicioService {
    OrdenDeServicio registrarOrden(OrdenDeServicio orden, List<DetalleOrdenRepuesto> repuestosUsados);
    void actualizarEstado(Long ordenId, Estado nuevoEstado);
    List<OrdenDeServicio> consultarHistorialPorVehiculo(String placa);
    BigDecimal calcularCostoTotal(Long ordenId);
}