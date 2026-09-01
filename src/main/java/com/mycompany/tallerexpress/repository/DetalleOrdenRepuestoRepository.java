/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.domain.models.DetalleOrdenRepuesto;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface DetalleOrdenRepuestoRepository {
    void guardar(DetalleOrdenRepuesto detalle); 
    void guardarLista(List<DetalleOrdenRepuesto> detalles);
    List<DetalleOrdenRepuesto> buscarPorOrdenId(Long ordenId); 
    void eliminarDetalle(Long id);
    void eliminarPorOrdenId(Long ordenId);
}

