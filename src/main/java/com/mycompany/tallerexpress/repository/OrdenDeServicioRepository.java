/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.domain.enums.Estado;
import com.mycompany.tallerexpress.domain.models.OrdenDeServicio;
import java.time.OffsetDateTime;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface OrdenDeServicioRepository {
    void guardar(OrdenDeServicio orden);
    OrdenDeServicio buscarPorId(Long id);
    List<OrdenDeServicio> buscarTodas();
    void actualizar(OrdenDeServicio orden);
    
    // Filtros específicos del negocio
    List<OrdenDeServicio> buscarPorClienteId(Long clienteId);
    List<OrdenDeServicio> buscarPorVehiculoPlaca(String placa);
    List<OrdenDeServicio> buscarPorEstado(Estado estado); // Ejemplo: BUSCAR TODAS LAS ORDENES 'EN_PROCESO'
    List<OrdenDeServicio> buscarPorFecha(OffsetDateTime inicio, OffsetDateTime fin);
}