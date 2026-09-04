/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.repository.jdbc;

import com.mycompany.tallerexpress.domain.models.DetalleOrdenRepuesto;
import com.mycompany.tallerexpress.domain.models.OrdenDeServicio;
import com.mycompany.tallerexpress.domain.models.Repuesto;
import com.mycompany.tallerexpress.repository.DetalleOrdenRepuestoRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Coder
 */
public class DetalleOrdenRepuestoRepositoryJdbc implements DetalleOrdenRepuestoRepository {

    private final Connection conexion;

    public DetalleOrdenRepuestoRepositoryJdbc(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void guardar(DetalleOrdenRepuesto detalle) {
        String sql = "INSERT INTO detalle_orden_repuestos (orden_servicio_id, repuesto_id, precio_unitario_historico) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, detalle.getOrdenDeServicio().getId());
            stmt.setLong(2, detalle.getRepuesto().getId());
            stmt.setBigDecimal(3, detalle.getPrecioUnitarioHistorico());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    detalle.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el detalle de repuesto", e);
        }
    }

    @Override
    public void guardarLista(List<DetalleOrdenRepuesto> detalles) {
        String sql = "INSERT INTO detalle_orden_repuestos (id_detalle, id_repuesto, precio_unitario_historico) VALUES (?, ?, ?)";
        // Optimizamos usando Batch Updates para insertar todos los repuestos en un solo viaje a la base de datos
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            for (DetalleOrdenRepuesto detalle : detalles) {
                stmt.setLong(1, detalle.getOrdenDeServicio().getId());
                stmt.setLong(2, detalle.getRepuesto().getId());
                stmt.setBigDecimal(3, detalle.getPrecioUnitarioHistorico());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la lista de detalles en lote", e);
        }
    }

    @Override
    public List<DetalleOrdenRepuesto> buscarPorOrdenId(Long ordenId) {
        String sql = "SELECT * FROM detalle_orden_repuestos WHERE id_detalle = ?";
        List<DetalleOrdenRepuesto> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, ordenId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetalleOrdenRepuesto detalle = new DetalleOrdenRepuesto();
                    detalle.setId(rs.getLong("id"));
                    
                    OrdenDeServicio o = new OrdenDeServicio(); o.setId(rs.getLong("id_detalle"));
                    detalle.setOrdenDeServicio(o);
                    
                    Repuesto r = new Repuesto(); r.setId(rs.getLong("id_repuesto"));
                    detalle.setRepuesto(r);
                    
                    detalle.setPrecioUnitarioHistorico(rs.getBigDecimal("precio_unitario_historico"));
                    lista.add(detalle);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los detalles de la orden", e);
        }
        return lista;
    }

    @Override
    public void eliminarDetalle(Long id) {
        String sql = "DELETE FROM detalle_orden_repuestos WHERE id_detalle = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el detalle de repuesto", e);
        }
    }

    @Override
    public void eliminarPorOrdenId(Long ordenId) {
        String sql = "DELETE FROM detalle_orden_repuestos WHERE id_detalle = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, ordenId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al limpiar los repuestos de la orden", e);
        }
    }
}