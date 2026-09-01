/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mycompany.tallerexpress.domain.models.Cliente;
import com.mycompany.tallerexpress.domain.enums.Estado;

import com.mycompany.tallerexpress.repository.ClienteRepository;
/**
 *
 * @author Coder
 */
public class ClienteRepositoryJdbc implements ClienteRepository {

    private final Connection conexion;

    public ClienteRepositoryJdbc(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        String sql = "INSERT INTO clientes (numero_identificacion, nombre_completo, telefono, correo, direccion, estado, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNumeroIdentificacion());
            stmt.setString(2, cliente.getNombreCompleto());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getCorreo());
            stmt.setString(5, cliente.getDireccion());
            stmt.setString(6, cliente.getEstado().name());
            stmt.setObject(7, cliente.getFechaRegistro());

            stmt.executeUpdate();

            // Recuperamos el ID autogenerado y lo asignamos al objeto antes de retornarlo
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setId(generatedKeys.getLong(1));
                }
            }
            return cliente;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el cliente en la base de datos", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearCliente(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Cliente> buscarPorNumeroIdentificacion(String numeroIdentificacion) {
        String sql = "SELECT * FROM clientes WHERE numero_identificacion = ?";
        List<Cliente> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, numeroIdentificacion);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCliente(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por número de identificación", e);
        }
        return lista;
    }

    @Override
    public List<Cliente> listarTodos() {
        String sql = "SELECT * FROM clientes";
        List<Cliente> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los clientes", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET numero_identificacion = ?, nombre_completo = ?, telefono = ?, correo = ?, direccion = ?, estado = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNumeroIdentificacion());
            stmt.setString(2, cliente.getNombreCompleto());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getCorreo());
            stmt.setString(5, cliente.getDireccion());
            stmt.setString(6, cliente.getEstado().name());
            stmt.setLong(7, cliente.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el cliente", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el cliente", e);
        }
    }

    // Método auxiliar para transformar filas de SQL a la entidad Cliente
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNumeroIdentificacion(rs.getString("numero_identificacion"));
        cliente.setNombreCompleto(rs.getString("nombre_completo"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setCorreo(rs.getString("correo"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setEstado(Estado.valueOf(rs.getString("estado")));
        cliente.setFechaRegistro(rs.getObject("fecha_registro", OffsetDateTime.class));
        return cliente;
    }
}
