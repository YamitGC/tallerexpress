/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.controller;

import java.util.List;
import com.mycompany.tallerexpress.domain.models.Repuesto;
import com.mycompany.tallerexpress.domain.exceptions.*;
import com.mycompany.tallerexpress.service.RepuestoService;
/**
 *
 * @author Coder
 */
public class RepuestoController {

    private final RepuestoService repuestoService;

    public RepuestoController(RepuestoService repuestoService) {
        this.repuestoService = repuestoService;
    }

    public Respuesta<Repuesto> registrar(Repuesto repuesto) {
        try {
            Repuesto registrado = repuestoService.registrar(repuesto);
            return new Respuesta<>(201, "Repuesto creado exitosamente", registrado);
        } catch (EntidadDuplicadaException | ReglaNegocioException | DatosInvalidosException e) {
            return new Respuesta<>(400, e.getMessage(), null);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error interno del servidor", null);
        }
    }

    public Respuesta<String> actualizar(Repuesto repuesto) {
        try {
            repuestoService.actualizar(repuesto);
            return new Respuesta<>(200, "Repuesto actualizado correctamente", null);
        } catch (ReglaNegocioException | DatosInvalidosException e) {
            return new Respuesta<>(400, e.getMessage(), null);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error interno al actualizar", null);
        }
    }

    public Respuesta<List<Repuesto>> listarTodos() {
        try {
            List<Repuesto> lista = repuestoService.listarTodos();
            return new Respuesta<>(200, "Listado obtenido", lista);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al listar repuestos", null);
        }
    }

    public Respuesta<List<Repuesto>> filtrarPorCategoria(String categoria) {
        try {
            List<Repuesto> lista = repuestoService.buscarPorCategoria(categoria);
            return new Respuesta<>(200, "Filtro aplicado", lista);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al filtrar", null);
        }
    }

    public Respuesta<List<Repuesto>> filtrarPorProveedor(String proveedor) {
        try {
            List<Repuesto> lista = repuestoService.buscarPorProveedor(proveedor);
            return new Respuesta<>(200, "Filtro aplicado", lista);
        } catch (Exception e) {
            return new Respuesta<>(500, "Error al filtrar", null);
        }
    }
}