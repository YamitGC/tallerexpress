/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.service.impl;

import java.util.List;
import com.mycompany.tallerexpress.domain.models.Repuesto;
import com.mycompany.tallerexpress.domain.exceptions.EntidadDuplicadaException;
import com.mycompany.tallerexpress.domain.exceptions.ReglaNegocioException;
import com.mycompany.tallerexpress.repository.RepuestoRepository;
import com.mycompany.tallerexpress.service.RepuestoService;
/**
 *
 * @author Coder
 */
public class RepuestoServiceImpl implements RepuestoService {

    private final RepuestoRepository repuestoRepository;

    public RepuestoServiceImpl(RepuestoRepository repuestoRepository) {
        this.repuestoRepository = repuestoRepository;
    }

    @Override
    public Repuesto registrar(Repuesto repuesto) {
        System.out.println("[POST] /api/repuestos - Iniciando registro de repuesto: " + repuesto.getCodigoReferencia());
        
        // Validación de código único (Regla de negocio)
        if (repuestoRepository.buscarPorId(repuesto.getId()).isPresent()) {
             throw new EntidadDuplicadaException("El código de referencia ya existe.");
        }
        
        // Validación de Stock
        if (repuesto.getStockTotal() < 0 || repuesto.getStockDisponible() < 0) {
            throw new ReglaNegocioException("El stock no puede ser menor a cero.");
        }

        return repuestoRepository.guardar(repuesto);
    }

    @Override
    public void actualizar(Repuesto repuesto) {
        System.out.println("[PATCH] /api/repuestos/" + repuesto.getId() + " - Actualizando información del repuesto");
        if (repuesto.getStockTotal() < 0 || repuesto.getStockDisponible() < 0) {
            throw new ReglaNegocioException("El stock no puede ser menor a cero.");
        }
        repuestoRepository.actualizar(repuesto);
    }

    @Override
    public List<Repuesto> listarTodos() {
        System.out.println("[GET] /api/repuestos - Listando todos los repuestos");
        return repuestoRepository.listarTodos();
    }

    @Override
    public List<Repuesto> buscarPorCategoria(String categoria) {
        System.out.println("[GET] /api/repuestos?categoria=" + categoria + " - Filtrando repuestos");
        return repuestoRepository.buscarPorCategoria(categoria);
    }

    @Override
    public List<Repuesto> buscarPorProveedor(String proveedor) {
        System.out.println("[GET] /api/repuestos?proveedor=" + proveedor + " - Filtrando repuestos");
        return repuestoRepository.buscarPorProveedor(proveedor);
    }
}
