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

import com.mycompany.tallerexpress.domain.models.Repuesto;

import com.mycompany.tallerexpress.repository.RepuestoRepository;

/**
 *
 * @author Coder
 */
public class RepuestoRepositoryJdbc implements RepuestoRepository {

    private final Connection conexion;

    public RepuestoRepositoryJdbc(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public Repuesto guardar(Repuesto repuesto) {
        String sql = "INSERT INTO repuestos (codigo_referencia, nombre, categoria, proveedor, stock_total, stock_disponible, precio_unitario, is_activo, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, repuesto.getCodigoReferencia());
            stmt.setString(2, repuesto.getNombre());
            stmt.setString(3, repuesto.getCategoria());
            stmt.setString(4, repuesto.getProveedor());
            stmt.setLong(5, repuesto.getStockTotal());
            stmt.setLong(6, repuesto.getStockDisponible());
            stmt.setBigDecimal(7, repuesto.getPrecioUnitario());
            stmt.setBoolean(8, repuesto.isIsActivo()); 
            stmt.setObject(9, repuesto.getCreatedAt());

            stmt.executeUpdate();

            // Recuperamos el ID autogenerado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    repuesto.setId(generatedKeys.getLong(1));
                }
            }
            return repuesto;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el repuesto en la base de datos", e);
        }
    }

    @Override
    public Optional<Repuesto> buscarPorId(Long id) {
        String sql = "SELECT * FROM repuestos WHERE id_repuesto = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearRepuesto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar repuesto por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Repuesto> buscarPorCategoria(String categoria) {
        String sql = "SELECT * FROM repuestos WHERE categoria = ?";
        return ejecutarConsultaConFiltroTexto(sql, categoria);
    }

    @Override
    public List<Repuesto> buscarPorProveedor(String proveedor) {
        String sql = "SELECT * FROM repuestos WHERE proveedor = ?";
        return ejecutarConsultaConFiltroTexto(sql, proveedor);
    }

    @Override
    public List<Repuesto> listarTodos() {
        String sql = "SELECT * FROM repuestos";
        List<Repuesto> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearRepuesto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los repuestos", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Repuesto repuesto) {
        String sql = "UPDATE repuestos SET codigo_referencia = ?, nombre = ?, categoria = ?, proveedor = ?, stock_total = ?, stock_disponible = ?, precio_unitario = ?, is_activo = ? WHERE id_repuesto = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, repuesto.getCodigoReferencia());
            stmt.setString(2, repuesto.getNombre());
            stmt.setString(3, repuesto.getCategoria());
            stmt.setString(4, repuesto.getProveedor());
            stmt.setLong(5, repuesto.getStockTotal());
            stmt.setLong(6, repuesto.getStockDisponible());
            stmt.setBigDecimal(7, repuesto.getPrecioUnitario());
            stmt.setBoolean(8, repuesto.isIsActivo());
            stmt.setLong(9, repuesto.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el repuesto", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM repuestos WHERE id_repuesto = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el repuesto", e);
        }
    }

    // Helper reutilizable para consultas de texto
    private List<Repuesto> ejecutarConsultaConFiltroTexto(String sql, String filtro) {
        List<Repuesto> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, filtro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearRepuesto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar consulta con filtro en repuestos", e);
        }
        return lista;
    }

    // Método auxiliar para transformar filas SQL al objeto Repuesto
    private Repuesto mapearRepuesto(ResultSet rs) throws SQLException {
        Repuesto repuesto = new Repuesto();
        repuesto.setId(rs.getLong("id_repuesto"));
        repuesto.setCodigoReferencia(rs.getString("codigo_referencia"));
        repuesto.setNombre(rs.getString("nombre"));
        repuesto.setCategoria(rs.getString("categoria"));
        repuesto.setProveedor(rs.getString("proveedor"));
        repuesto.setStockTotal(rs.getLong("stock_total"));
        repuesto.setStockDisponible(rs.getLong("stock_disponible"));
        repuesto.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        repuesto.setIsActivo(rs.getBoolean("is_activo"));
        repuesto.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
        return repuesto;
    }
}