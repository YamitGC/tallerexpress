/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.presentation;

import javax.swing.JOptionPane;
import java.util.List;
import com.mycompany.tallerexpress.controller.UserController;
import com.mycompany.tallerexpress.controller.Respuesta;
import com.mycompany.tallerexpress.domain.models.User;
/**
 *
 * @author Coder
 */
public class UserViews {

    private final UserController controller;
    private User usuarioSesion;

    public UserViews(UserController controller) {
        this.controller = controller;
    }

    public boolean ejecutarLogin() {
        String user = JOptionPane.showInputDialog("Username:");
        String pass = JOptionPane.showInputDialog("Password:");
        if (user == null || pass == null) return false;

        Respuesta<User> resp = controller.login(user, pass);
        if (resp.getStatus() == 200) {
            this.usuarioSesion = resp.getDatos();
            JOptionPane.showMessageDialog(null, "¡Bienvenido, " + usuarioSesion.getNombreCompleto() + "!\nRol: " + usuarioSesion.getRol());
            return true;
        } else {
            JOptionPane.showMessageDialog(null, resp.getMensaje(), "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void mostrarMenu() {
        String[] opciones = {"Registrar Recepcionista (Decorador)", "Listar Todos", "Volver"};
        int seleccion;
        do {
            seleccion = JOptionPane.showOptionDialog(null, "Gestión de Usuarios\nSesión activa: " + usuarioSesion.getUsername(), "Menú Usuarios",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones);

            switch (seleccion) {
                case 0 -> registrarConDecorador();
                case 1 -> listarUsuarios();
            }
        } while (seleccion != 2 && seleccion != -1);
    }

    private void registrarConDecorador() {
        User u = new User();
        u.setUsername(JOptionPane.showInputDialog("Nuevo Username:"));
        u.setPassword(JOptionPane.showInputDialog("Password:"));
        u.setNombreCompleto(JOptionPane.showInputDialog("Nombre Completo:"));
        u.setCorreo(JOptionPane.showInputDialog("Correo:"));
        
        Respuesta<User> resp = controller.registrarRecepcionista(u);
        JOptionPane.showMessageDialog(null, resp.getMensaje(), "Respuesta del Sistema", JOptionPane.INFORMATION_MESSAGE);
    }

    private void listarUsuarios() {
        Respuesta<List<User>> resp = controller.listarTodos();
        if (resp.getStatus() != 200) {
            JOptionPane.showMessageDialog(null, resp.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<User> lista = resp.getDatos();
        StringBuilder sb = new StringBuilder("<html><pre>");
        sb.append(String.format("%-5s | %-12s | %-25s | %-15s | %-8s\n", "ID", "Username", "Nombre", "Rol", "Activo"));
        sb.append("--------------------------------------------------------------------------------\n");
        for (User u : lista) {
            sb.append(String.format("%-5d | %-12s | %-25s | %-15s | %-8s\n",
                    u.getId(), u.getUsername(), u.getNombreCompleto(), u.getRol().name(), u.isIsActivo() ? "SI" : "NO"));
        }
        sb.append("</pre></html>");
        JOptionPane.showMessageDialog(null, sb.toString(), "Usuarios Registrados", JOptionPane.PLAIN_MESSAGE);
    }
}