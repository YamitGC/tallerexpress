/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.presentation;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import com.mycompany.tallerexpress.controller.RepuestoController;
import com.mycompany.tallerexpress.controller.Respuesta;
import com.mycompany.tallerexpress.domain.models.Repuesto;
/**
 *
 * @author Coder
 */
public class RepuestoView {

    private final RepuestoController controller;

    public RepuestoView(RepuestoController controller) {
        this.controller = controller;
    }

    public void mostrarMenu() {
        String[] opciones = {"Registrar Repuesto", "Actualizar Repuesto", "Listar Todos", "Filtrar por Categoría", "Filtrar por Proveedor", "Volver"};
        int seleccion;
        do {
            seleccion = JOptionPane.showOptionDialog(null, "Gestión de Repuestos", "Menú Repuestos",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones);

            switch (seleccion) {
                case 0 -> registrar();
                case 1 -> actualizar();
                case 2 -> {
                    Respuesta<List<Repuesto>> resp = controller.listarTodos();
                    procesarRespuestaLista(resp, "Todos los Repuestos");
                }
                case 3 -> {
                    String cat = JOptionPane.showInputDialog("Ingrese la categoría a buscar:");
                    if (cat != null && !cat.trim().isEmpty()) {
                        Respuesta<List<Repuesto>> resp = controller.filtrarPorCategoria(cat);
                        procesarRespuestaLista(resp, "Categoría: " + cat);
                    }
                }
                case 4 -> {
                    String prov = JOptionPane.showInputDialog("Ingrese el proveedor a buscar:");
                    if (prov != null && !prov.trim().isEmpty()) {
                        Respuesta<List<Repuesto>> resp = controller.filtrarPorProveedor(prov);
                        procesarRespuestaLista(resp, "Proveedor: " + prov);
                    }
                }
            }
        } while (seleccion != 5 && seleccion != -1);
    }

    private void registrar() {
        try {
            Repuesto r = new Repuesto();
            r.setCodigoReferencia(JOptionPane.showInputDialog("Código de Referencia:"));
            r.setNombre(JOptionPane.showInputDialog("Nombre del Repuesto:"));
            r.setCategoria(JOptionPane.showInputDialog("Categoría:"));
            r.setProveedor(JOptionPane.showInputDialog("Proveedor:"));
            r.setStockTotal(Long.parseLong(JOptionPane.showInputDialog("Stock Total:")));
            r.setStockDisponible(Long.parseLong(JOptionPane.showInputDialog("Stock Disponible:")));
            r.setPrecioUnitario(new BigDecimal(JOptionPane.showInputDialog("Precio Unitario:")));
            r.setIsActivo(true);
            r.setCreatedAt(OffsetDateTime.now());

            Respuesta<Repuesto> resp = controller.registrar(r);
            if (resp.getStatus() == 201) {
                JOptionPane.showMessageDialog(null, resp.getMensaje(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, resp.getMensaje(), "Error " + resp.getStatus(), JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Datos de entrada inválidos o formato incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        try {
            Repuesto r = new Repuesto();
            r.setId(Long.parseLong(JOptionPane.showInputDialog("ID del Repuesto a editar:")));
            r.setCodigoReferencia(JOptionPane.showInputDialog("Nuevo Código de Referencia:"));
            r.setNombre(JOptionPane.showInputDialog("Nuevo Nombre:"));
            r.setCategoria(JOptionPane.showInputDialog("Nueva Categoría:"));
            r.setProveedor(JOptionPane.showInputDialog("Nuevo Proveedor:"));
            r.setStockTotal(Long.parseLong(JOptionPane.showInputDialog("Nuevo Stock Total:")));
            r.setStockDisponible(Long.parseLong(JOptionPane.showInputDialog("Nuevo Stock Disponible:")));
            r.setPrecioUnitario(new BigDecimal(JOptionPane.showInputDialog("Nuevo Precio Unitario:")));
            r.setIsActivo(JOptionPane.showConfirmDialog(null, "¿El repuesto está activo?", "Estado", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);

            Respuesta<String> resp = controller.actualizar(r);
            if (resp.getStatus() == 200) {
                JOptionPane.showMessageDialog(null, resp.getMensaje(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, resp.getMensaje(), "Error " + resp.getStatus(), JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en el formato de los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarRespuestaLista(Respuesta<List<Repuesto>> resp, String titulo) {
        if (resp.getStatus() != 200) {
            JOptionPane.showMessageDialog(null, resp.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Repuesto> lista = resp.getDatos();
        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay registros para mostrar.", titulo, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Bloque HTML preformateado para garantizar alineación perfecta tipo tabla de texto
        StringBuilder sb = new StringBuilder("<html><pre>");
        sb.append(String.format("%-5s | %-12s | %-20s | %-12s | %-6s | %-8s\n", "ID", "Código", "Nombre", "Categoría", "Stock", "Precio"));
        sb.append("-------------------------------------------------------------------------------\n");
        for (Repuesto r : lista) {
            sb.append(String.format("%-5d | %-12s | %-20s | %-12s | %-6d | $%-8.2f\n",
                    r.getId(), r.getCodigoReferencia(), r.getNombre(), r.getCategoria(), r.getStockDisponible(), r.getPrecioUnitario()));
        }
        sb.append("</pre></html>");
        JOptionPane.showMessageDialog(null, sb.toString(), titulo, JOptionPane.PLAIN_MESSAGE);
    }
}