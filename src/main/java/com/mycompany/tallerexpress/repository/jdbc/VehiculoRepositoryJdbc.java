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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mycompany.tallerexpress.domain.models.Vehiculo;
import com.mycompany.tallerexpress.domain.models.Cliente;

import com.mycompany.tallerexpress.repository.VehiculoRepository;
/**
 *
 * @author Coder
 */
public class VehiculoRepositoryJdbc implements VehiculoRepository {

    private final Connection conexion;

    public VehiculoRepositoryJdbc(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public Vehiculo guardar(Vehiculo vehiculo) {
        String sql = "INSERT INTO vehiculos (cliente_id, placa, marca, modelo, categoria, anio_modelo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, vehiculo.getCliente().getId());
            stmt.setString(2, vehiculo.getPlaca());
            stmt.setString(3, vehiculo.getMarca());
            stmt.setString(4, vehiculo.getModelo());
            stmt.setString(5, vehiculo.getCategoria());
            stmt.setInt(6, vehiculo.getAnioModelo());

            stmt.executeUpdate();

            // Recuperamos el ID autogenerado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehiculo.setId(generatedKeys.getLong(1));
                }
            }
            return vehiculo;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el vehículo en la base de datos", e);
        }
    }

    @Override
    public Optional<Vehiculo> buscarPorId(Long id) {
        String sql = "SELECT * FROM vehiculos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearVehiculo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar vehículo por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Vehiculo> buscarPorPlaca(String placa) {
        String sql = "SELECT * FROM vehiculos WHERE placa = ?";
        List<Vehiculo> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, placa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearVehiculo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar vehículo por placa", e);
        }
        return lista;
    }

    @Override
    public List<Vehiculo> listarTodos() {
        String sql = "SELECT * FROM vehiculos";
        List<Vehiculo> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearVehiculo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los vehículos", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Vehiculo vehiculo) {
        String sql = "UPDATE vehiculos SET cliente_id = ?, placa = ?, marca = ?, modelo = ?, categoria = ?, anio_modelo = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, vehiculo.getCliente().getId());
            stmt.setString(2, vehiculo.getPlaca());
            stmt.setString(3, vehiculo.getMarca());
            stmt.setString(4, vehiculo.getModelo());
            stmt.setString(5, vehiculo.getCategoria());
            stmt.setInt(6, vehiculo.getAnioModelo());
            stmt.setLong(7, vehiculo.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el vehículo", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM vehiculos WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el vehículo", e);
        }
    }

    // Método auxiliar para transformar filas SQL al objeto Vehiculo
    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(rs.getLong("id"));
        
        // Creamos el cascarón de cliente con su ID correspondiente
        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("cliente_id"));
        vehiculo.setCliente(cliente);
        
        vehiculo.setPlaca(rs.getString("placa"));
        vehiculo.setMarca(rs.getString("marca"));
        vehiculo.setModelo(rs.getString("modelo"));
        vehiculo.setCategoria(rs.getString("categoria"));
        vehiculo.setAnioModelo(rs.getInt("anio_modelo"));
        
        return vehiculo;
    }
}