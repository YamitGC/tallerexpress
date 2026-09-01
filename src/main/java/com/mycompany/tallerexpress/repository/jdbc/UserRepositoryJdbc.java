/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.repository.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mycompany.tallerexpress.domain.models.User;
import com.mycompany.tallerexpress.domain.enums.Roles;

import com.mycompany.tallerexpress.repository.UserRepository;
/**
 *
 * @author Coder
 */
public class UserRepositoryJdbc implements UserRepository {

    private final Connection conexion;

    public UserRepositoryJdbc(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public User guardar(User user) {
        String sql = "INSERT INTO users (username, password, nombre_completo, correo, rol, is_activo, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, new String[]{"id_user"})) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getNombreCompleto());
            stmt.setString(4, user.getCorreo());
            stmt.setString(5, user.getRol().name());
            stmt.setBoolean(6, user.isIsActivo());
            stmt.setObject(7, user.getCreatedAt());

            stmt.executeUpdate();

            // Recuperamos el ID autogenerado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el usuario en la base de datos", e);
        }
    }

    @Override
    public Optional<User> buscarPorId(Long id) {
        String sql = "SELECT * FROM \"users\" WHERE id_user = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> buscarPorCorreo(String correo) {
        String sql = "UPDATE \"users\" SET username = ?, password = ?, nombre_completo = ?, correo = ?, rol = ?, is_activo = ? WHERE id_user = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por correo", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> listarTodos() {
        String sql = "SELECT * FROM \"users\"";
        List<User> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            // ESTO NOS MOSTRARÁ EL ERROR REAL EN LA CONSOLA DE NETBEANS
            System.out.println("❌ ERROR CRÍTICO EN JDBC USUARIOS: " + e.getMessage());
            e.printStackTrace(); 
            throw new RuntimeException("Error al listar todos los usuarios", e);
        }
        return lista;
    }

    @Override
    public void actualizar(User user) {
        String sql = "UPDATE \"users\" SET username = ?, password = ?, nombre_completo = ?, correo = ?, rol = ?, is_activo = ? WHERE id_user = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getNombreCompleto());
            stmt.setString(4, user.getCorreo());
            stmt.setString(5, user.getRol().name());
            stmt.setBoolean(6, user.isIsActivo());
            stmt.setLong(7, user.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el usuario", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM \"users\" WHERE id_user = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el usuario", e);
        }
    }

    // Método auxiliar para transformar filas SQL al objeto User
    private User mapearUsuario(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id_user"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setNombreCompleto(rs.getString("nombre_completo"));
        user.setCorreo(rs.getString("correo"));
        user.setRol(Roles.valueOf(rs.getString("rol")));
        user.setIsActivo(rs.getBoolean("is_activo"));
        user.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
        return user;
    }
}