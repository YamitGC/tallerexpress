/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.presentation;

import javax.swing.JOptionPane;
import java.time.OffsetDateTime;
import java.util.List;
import com.mycompany.tallerexpress.controller.Cliente_VehiculoController;
import com.mycompany.tallerexpress.controller.Respuesta;
import com.mycompany.tallerexpress.domain.models.Cliente;
import com.mycompany.tallerexpress.domain.models.Vehiculo;
import com.mycompany.tallerexpress.domain.enums.Estado;
/**
 *
 * @author Coder
 */
public class Cliente_VehiculoView {

    private final Cliente_VehiculoController controller;

    public Cliente_VehiculoView(Cliente_VehiculoController controller) {
        this.controller = controller;
    }

    public void mostrarMenu() {
        String[] opciones = {"Registrar Cliente", "Registrar Vehículo", "Historial de Vehículos por Cliente", "Volver"};
        int seleccion;
        do {
            seleccion = JOptionPane.showOptionDialog(null, "Gestión de Clientes y Vehículos", "Menú Clientes/Vehículos",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones);

            switch (seleccion) {
                case 0 -> registrarCliente();
                case 1 -> registrarVehiculo();
                case 2 -> consultarHistorial();
            }
        } while (seleccion != 3 && seleccion != -1);
    }

    private void registrarCliente() {
        try {
            Cliente c = new Cliente();
            c.setNumeroIdentificacion(JOptionPane.showInputDialog("Número de Identificación:"));
            c.setNombreCompleto(JOptionPane.showInputDialog("Nombre Completo:"));
            c.setTelefono(JOptionPane.showInputDialog("Teléfono:"));
            c.setCorreo(JOptionPane.showInputDialog("Correo Electrónico:"));
            c.setDireccion(JOptionPane.showInputDialog("Dirección:"));
            c.setEstado(Estado.ACTIVO);
            c.setFechaRegistro(OffsetDateTime.now());

            Respuesta<Cliente> resp = controller.registrarCliente(c);
            JOptionPane.showMessageDialog(null, resp.getMensaje(), resp.getStatus() == 201 ? "Éxito" : "Aviso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al procesar los datos de entrada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarVehiculo() {
        try {
            Vehiculo v = new Vehiculo();
            Cliente c = new Cliente();
            c.setId(Long.parseLong(JOptionPane.showInputDialog("ID del Cliente Propietario:")));
            v.setCliente(c);
            
            v.setPlaca(JOptionPane.showInputDialog("Placa:"));
            v.setMarca(JOptionPane.showInputDialog("Marca:"));
            v.setModelo(JOptionPane.showInputDialog("Modelo:"));
            v.setCategoria(JOptionPane.showInputDialog("Categoría:"));
            v.setAnioModelo(Integer.parseInt(JOptionPane.showInputDialog("Año del Modelo:")));

            Respuesta<Vehiculo> resp = controller.registrarVehiculo(v);
            if (resp.getStatus() == 201) {
                JOptionPane.showMessageDialog(null, resp.getMensaje(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, resp.getMensaje(), "Error " + resp.getStatus(), JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Verifique los datos numéricos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultarHistorial() {
        try {
            Long clienteId = Long.parseLong(JOptionPane.showInputDialog("Ingrese el ID del Cliente:"));
            Respuesta<List<Vehiculo>> resp = controller.consultarHistorialPorCliente(clienteId);
            
            if (resp.getStatus() != 200) {
                JOptionPane.showMessageDialog(null, resp.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<Vehiculo> historial = resp.getDatos();
            if (historial == null || historial.isEmpty()) {
                JOptionPane.showMessageDialog(null, "El cliente no posee vehículos asignados.");
                return;
            }

            StringBuilder sb = new StringBuilder("<html><pre>");
            sb.append(String.format("%-5s | %-8s | %-12s | %-12s | %-5s\n", "ID", "Placa", "Marca", "Modelo", "Año"));
            sb.append("-----------------------------------------------------------\n");
            for (Vehiculo v : historial) {
                sb.append(String.format("%-5d | %-8s | %-12s | %-12s | %-5d\n",
                        v.getId(), v.getPlaca(), v.getMarca(), v.getModelo(), v.getAnioModelo()));
            }
            sb.append("</pre></html>");
            JOptionPane.showMessageDialog(null, sb.toString(), "Historial Vehicular", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ID Inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}