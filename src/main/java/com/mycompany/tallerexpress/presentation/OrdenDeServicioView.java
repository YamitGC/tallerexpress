/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.presentation;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.tallerexpress.controller.OrdenDeServicioController;
import com.mycompany.tallerexpress.controller.Respuesta;
import com.mycompany.tallerexpress.domain.enums.Estado;
import com.mycompany.tallerexpress.domain.models.*;

/**
 *
 * @author Coder
 */
public class OrdenDeServicioView {

    private final OrdenDeServicioController controller;

    public OrdenDeServicioView(OrdenDeServicioController controller) {
        this.controller = controller;
    }

    public void mostrarMenu() {
        String[] opciones = {"Crear Orden", "Cambiar Estado", "Historial por Placa", "Calcular Total Cobro", "Volver"};
        int seleccion;
        do {
            seleccion = JOptionPane.showOptionDialog(null, "Gestión de Órdenes de Servicio", "Menú Órdenes",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones);

            switch (seleccion) {
                case 0 -> crearOrden();
                case 1 -> cambiarEstado();
                case 2 -> verHistorialPorPlaca();
                case 3 -> calcularTotal();
            }
        } while (seleccion != 4 && seleccion != -1);
    }

    private void crearOrden() {
        try {
            OrdenDeServicio orden = new OrdenDeServicio();
            
            Cliente c = new Cliente(); c.setId(Long.parseLong(JOptionPane.showInputDialog("ID del Cliente:")));
            Vehiculo v = new Vehiculo(); v.setId(Long.parseLong(JOptionPane.showInputDialog("ID del Vehículo:")));
            User mecanico = new User(); mecanico.setId(Long.parseLong(JOptionPane.showInputDialog("ID del Mecánico Responsable:")));
            
            orden.setCliente(c);
            orden.setVehiculo(v);
            orden.setUser(mecanico);
            orden.setDescripcionFalla(JOptionPane.showInputDialog("Descripción del Problema:"));
            orden.setDiagnostico(JOptionPane.showInputDialog("Diagnóstico Inicial:"));
            orden.setTotalManoObra(new BigDecimal(JOptionPane.showInputDialog("Costo Mano de Obra ($):")));
            orden.setTotalRepuestos(BigDecimal.ZERO);
            orden.setTotalPagar(BigDecimal.ZERO);
            orden.setEstado(Estado.ACTIVO);
            orden.setFechaRegistro(OffsetDateTime.now());

            List<DetalleOrdenRepuesto> repuestosUsados = new ArrayList<>();
            int agregarMas;
            do {
                agregarMas = JOptionPane.showConfirmDialog(null, "¿Desea asociar un repuesto consumido a esta orden?", "Asignar Repuestos", JOptionPane.YES_NO_OPTION);
                if (agregarMas == JOptionPane.YES_OPTION) {
                    DetalleOrdenRepuesto detalle = new DetalleOrdenRepuesto();
                    Repuesto rep = new Repuesto();
                    rep.setId(Long.parseLong(JOptionPane.showInputDialog("ID del Repuesto Utilizado:")));
                    detalle.setRepuesto(rep);
                    detalle.setPrecioUnitarioHistorico(new BigDecimal(JOptionPane.showInputDialog("Precio Unitario Histórico ($):")));
                    repuestosUsados.add(detalle);
                }
            } while (agregarMas == JOptionPane.YES_OPTION);

            Respuesta<OrdenDeServicio> resp = controller.registrarOrden(orden, repuestosUsados);
            JOptionPane.showMessageDialog(null, resp.getMensaje(), "Operación terminada", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en los formatos de entrada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstado() {
        try {
            Long ordenId = Long.parseLong(JOptionPane.showInputDialog("ID de la Orden de Servicio:"));
            Estado[] estados = Estado.values();
            Estado seleccion = (Estado) JOptionPane.showInputDialog(null, "Seleccione el nuevo estado:", "Cambio de Estado",
                    JOptionPane.QUESTION_MESSAGE, null, estados, estados);
            
            if (seleccion != null) {
                Respuesta<String> resp = controller.actualizarEstado(ordenId, seleccion);
                JOptionPane.showMessageDialog(null, resp.getMensaje(), "Actualizado", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verHistorialPorPlaca() {
        String placa = JOptionPane.showInputDialog("Ingrese la Placa del vehículo:");
        if (placa == null || placa.trim().isEmpty()) return;

        Respuesta<List<OrdenDeServicio>> resp = controller.consultarHistorialPorVehiculo(placa);
        if (resp.getStatus() != 200) {
            JOptionPane.showMessageDialog(null, resp.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<OrdenDeServicio> historial = resp.getDatos();
        if (historial == null || historial.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se registran antecedentes técnicos para este vehículo.");
            return;
        }

        StringBuilder sb = new StringBuilder("<html><pre>");
        sb.append(String.format("%-5s | %-12s | %-30s | %-12s\n", "ID", "Fecha", "Falla Reportada", "Estado"));
        sb.append("----------------------------------------------------------------------\n");
        for (OrdenDeServicio o : historial) {
            String fallaCorta = o.getDescripcionFalla().length() > 27 ? o.getDescripcionFalla().substring(0, 24) + "..." : o.getDescripcionFalla();
            sb.append(String.format("%-5d | %-12s | %-30s | %-12s\n",
                    o.getId(), o.getFechaRegistro().toLocalDate().toString(), fallaCorta, o.getEstado().name()));
        }
        sb.append("</pre></html>");
        JOptionPane.showMessageDialog(null, sb.toString(), "Historial Clínico del Vehículo", JOptionPane.PLAIN_MESSAGE);
    }

    private void calcularTotal() {
        try {
            Long ordenId = Long.parseLong(JOptionPane.showInputDialog("ID de la Orden a liquidar:"));
            Respuesta<BigDecimal> resp = controller.calcularCostoTotal(ordenId);
            if (resp.getStatus() == 200) {
                JOptionPane.showMessageDialog(null, resp.getMensaje() + "\nTotal a cobrar (Mano Obra + Repuestos): $" + resp.getDatos(),
                        "Liquidación Financiera", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, resp.getMensaje(), "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ID Inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}