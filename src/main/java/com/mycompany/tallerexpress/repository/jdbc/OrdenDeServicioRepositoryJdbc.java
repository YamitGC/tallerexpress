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

import com.mycompany.tallerexpress.domain.models.OrdenDeServicio;
import com.mycompany.tallerexpress.domain.models.Vehiculo;
import com.mycompany.tallerexpress.domain.models.Cliente;
import com.mycompany.tallerexpress.domain.models.User;
import com.mycompany.tallerexpress.domain.enums.Estado;

import com.mycompany.tallerexpress.repository.OrdenDeServicioRepository;
/**
 *
 * @author Coder
 */
public class OrdenDeServicioRepositoryJdbc implements OrdenDeServicioRepository {

    private final Connection conexion;

    public OrdenDeServicioRepositoryJdbc(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public void guardar(OrdenDeServicio orden) {
        String sql = "INSERT INTO ordenes_servicio (id_vehiculo, id_cliente, user_id, descripcion_falla, diagnostico, total_mano_obra, total_repuestos, total_pagar, estado, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, orden.getVehiculo().getId());
            stmt.setLong(2, orden.getCliente().getId());
            stmt.setLong(3, orden.getUser().getId());
            stmt.setString(4, orden.getDescripcionFalla());
            stmt.setString(5, orden.getDiagnostico());
            stmt.setBigDecimal(6, orden.getTotalManoObra());
            stmt.setBigDecimal(7, orden.getTotalRepuestos());
            stmt.setBigDecimal(8, orden.getTotalPagar());
            stmt.setString(9, orden.getEstado().name());
            stmt.setObject(10, orden.getFechaRegistro());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orden.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la orden de servicio", e);
        }
    }

    @Override
    public OrdenDeServicio buscarPorId(Long id) {
        String sql = "SELECT * FROM ordenes_servicio WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearOrden(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar orden por ID", e);
        }
        return null;
    }

    @Override
    public List<OrdenDeServicio> buscarTodas() {
        String sql = "SELECT * FROM ordenes_servicio";
        List<OrdenDeServicio> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearOrden(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las órdenes", e);
        }
        return lista;
    }

    @Override
    public void actualizar(OrdenDeServicio orden) {
        String sql = "UPDATE ordenes_servicio SET id_vehiculo = ?, id_cliente = ?, id_user = ?, descripcion_falla = ?, diagnostico = ?, total_mano_obra = ?, total_repuestos = ?, total_pagar = ?, estado = ? WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, orden.getVehiculo().getId());
            stmt.setLong(2, orden.getCliente().getId());
            stmt.setLong(3, orden.getUser().getId());
            stmt.setString(4, orden.getDescripcionFalla());
            stmt.setString(5, orden.getDiagnostico());
            stmt.setBigDecimal(6, orden.getTotalManoObra());
            stmt.setBigDecimal(7, orden.getTotalRepuestos());
            stmt.setBigDecimal(8, orden.getTotalPagar());
            stmt.setString(9, orden.getEstado().name());
            stmt.setLong(10, orden.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la orden de servicio", e);
        }
    }

    @Override
    public List<OrdenDeServicio> buscarPorClienteId(Long clienteId) {
        String sql = "SELECT * FROM ordenes_servicio WHERE id_cliente = ?";
        return ejecutarConsultaConParametroLong(sql, clienteId);
    }

    @Override
    public List<OrdenDeServicio> buscarPorEstado(Estado estado) {
        String sql = "SELECT * FROM ordenes_servicio WHERE estado = ?";
        List<OrdenDeServicio> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, estado.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar órdenes por estado", e);
        }
        return lista;
    }

    private List<OrdenDeServicio> ejecutarConsultaConParametroLong(String sql, Long parametro) {
        List<OrdenDeServicio> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, parametro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en consulta de órdenes de servicio", e);
        }
        return lista;
    }

    private OrdenDeServicio mapearOrden(ResultSet rs) throws SQLException {
        OrdenDeServicio orden = new OrdenDeServicio();
        orden.setId(rs.getLong("id"));
        
        Vehiculo v = new Vehiculo(); 
        v.setId(rs.getLong("id_vehiculo")); 
        orden.setVehiculo(v);
        
        Cliente c = new Cliente(); 
        c.setId(rs.getLong("id_cliente")); 
        orden.setCliente(c);
        
        User u = new User(); 
        u.setId(rs.getLong("id_user")); 
        orden.setUser(u);
        
        orden.setDescripcionFalla(rs.getString("descripcion_falla"));
        orden.setDiagnostico(rs.getString("diagnostico"));
        orden.setTotalManoObra(rs.getBigDecimal("total_mano_obra"));
        orden.setTotalRepuestos(rs.getBigDecimal("total_repuestos"));
        orden.setTotalPagar(rs.getBigDecimal("total_pagar"));
        orden.setEstado(Estado.valueOf(rs.getString("estado")));
        orden.setFechaRegistro(rs.getObject("fecha_registro", OffsetDateTime.class));
        return orden;
    }
    
    @Override
    public List<OrdenDeServicio> buscarPorFecha(OffsetDateTime inicio, OffsetDateTime fin) {
        String sql = "SELECT * FROM ordenes_servicio WHERE fecha_registro BETWEEN ? AND ?";
        List<OrdenDeServicio> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setObject(1, inicio);
            stmt.setObject(2, fin);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar órdenes por rango de fechas", e);
        }
        return lista;
    }
    
    @Override
    public List<OrdenDeServicio> buscarPorVehiculoPlaca(String placa) {
        String sql = "SELECT o.* FROM ordenes_servicio o JOIN vehiculos v ON o.id_vehiculo = v.id WHERE v.placa = ?";
        List<OrdenDeServicio> lista = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, placa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar órdenes por placa de vehículo", e);
        }
        return lista;
    }


}
