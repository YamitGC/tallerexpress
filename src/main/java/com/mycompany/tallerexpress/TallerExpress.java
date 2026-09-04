/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tallerexpress;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

import com.mycompany.tallerexpress.repository.jdbc.*;
import com.mycompany.tallerexpress.service.impl.*;
import com.mycompany.tallerexpress.service.UserService;
import com.mycompany.tallerexpress.controller.*;
import com.mycompany.tallerexpress.presentation.Cliente_VehiculoView;
import com.mycompany.tallerexpress.presentation.OrdenDeServicioView;
import com.mycompany.tallerexpress.presentation.RepuestoView;
import com.mycompany.tallerexpress.presentation.*;
import java.sql.SQLException;

/**
 *
 * @author Coder
 */
public class TallerExpress {
    public static void main(String[] args) {
        // Tu conexión actual a PostgreSQL
        String url = "jdbc:postgresql://localhost:5432/taller_db";
        String userDB = "postgres";
        String passDB = "yamitgc01";

        try {
            // Levantamos tu conexión
            Connection conexion = DriverManager.getConnection(url, userDB, passDB);
            System.out.println("[CONFIG] Conexión establecida con éxito a PostgreSQL.");
            
            // 1. Repositorios JDBC
            var repuestoRepo = new RepuestoRepositoryJdbc(conexion);
            var clienteRepo = new ClienteRepositoryJdbc(conexion);
            var vehiculoRepo = new VehiculoRepositoryJdbc(conexion);
            var userRepo = new UserRepositoryJdbc(conexion);
            var ordenRepo = new OrdenDeServicioRepositoryJdbc(conexion);
            var detalleRepo = new DetalleOrdenRepuestoRepositoryJdbc(conexion);

            // 2. Servicios
            var repuestoService = new RepuestoServiceImpl(repuestoRepo);
            var clienteVehiculoService = new Cliente_VehiculoServiceImpl(clienteRepo, vehiculoRepo);
            var ordenService = new OrdenDeServicioServiceImpl(ordenRepo, detalleRepo, clienteRepo);
            
            UserService baseUserService = new UserServiceImpl(userRepo);
            UserService decoratedUserService = new UserRegistrationDecorator(baseUserService);

            // 3. Controladores Puros
            var repuestoCtrl = new RepuestoController(repuestoService);
            var clienteVehiculoCtrl = new Cliente_VehiculoController(clienteVehiculoService);
            var userCtrl = new UserController(decoratedUserService);
            var ordenCtrl = new OrdenDeServicioController(ordenService);

            // 4. Vistas (Capa presentation)
            var repuestoView = new RepuestoView(repuestoCtrl);
            var clienteVehiculoView = new Cliente_VehiculoView(clienteVehiculoCtrl);
            var userView = new UserViews(userCtrl);
            var ordenView = new OrdenDeServicioView(ordenCtrl);

            // 5. Orquestación del Flujo con JOptionPane
            JOptionPane.showMessageDialog(null, "--- Sistema Taller Express ---\nControl de Seguridad del Sistema");
            if (!userView.ejecutarLogin()) {
                JOptionPane.showMessageDialog(null, "Acceso cancelado. Finalizando ejecución.");
                conexion.close();
                return;
            }

            String[] modulos = {"Repuestos", "Clientes y Vehículos", "Usuarios", "Órdenes de Servicio", "Salir"};
            int seleccion;
            do {
                seleccion = JOptionPane.showOptionDialog(null, "Seleccione el módulo a gestionar", "Panel de Control",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, modulos, modulos);

                switch (seleccion) {
                    case 0 -> repuestoView.mostrarMenu();
                    case 1 -> clienteVehiculoView.mostrarMenu();
                    case 2 -> userView.mostrarMenu();
                    case 3 -> ordenView.mostrarMenu();
                }
            } while (seleccion != 4 && seleccion != -1);

            // Cerramos de forma segura
            conexion.close();
            JOptionPane.showMessageDialog(null, "Conexión a PostgreSQL cerrada. ¡Hasta pronto!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de base de datos:\n" + e.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error general del sistema:\n" + e.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }
}