/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.domain.models;

import com.mycompany.tallerexpress.domain.enums.Estado;
import com.mycompany.tallerexpress.domain.exceptions.DatosInvalidosException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 *
 * @author Coder
 */
public class OrdenDeServicio {
    private Long id;
    private Vehiculo vehiculo;
    private Cliente cliente;
    private User user;
    private String descripcionFalla;
    private String diagnostico;
    private BigDecimal totalManoObra;
    private BigDecimal totalRepuestos;
    private BigDecimal totalPagar;
    private Estado estado;
    private OffsetDateTime fechaRegistro;
    

    public OrdenDeServicio(Vehiculo vehiculo, Cliente cliente, User user, String descripcionFalla, String diagnostico, BigDecimal totalManoObra, BigDecimal totalRepuestos, BigDecimal totalPagar, Estado estado, OffsetDateTime fechaRegistro) {
        validarVehiculo(vehiculo);
        validarCliente(cliente);
        validarUser(user);
        validarDescripcionFalla(descripcionFalla);
        validarDiagnostico(diagnostico);
        validarTotalManoObra(totalManoObra);
        validarTotalRepuestos(totalRepuestos);
        validarTotalPagar(totalPagar);
        validarEstado(estado);
        validarFechaRegistro(fechaRegistro);
        
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.user = user;
        this.descripcionFalla = descripcionFalla;
        this.diagnostico = diagnostico;
        this.totalManoObra = totalManoObra;
        this.totalRepuestos = totalRepuestos;
        this.totalPagar = totalPagar;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public OrdenDeServicio(Long id, Vehiculo vehiculo, Cliente cliente, User user, String descripcionFalla, String diagnostico, BigDecimal totalManoObra, BigDecimal totalRepuestos, BigDecimal totalPagar, Estado estado, OffsetDateTime fechaRegistro) {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("El ID debe ser un número positivo válido.");
        }
        this.id = id;
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.user = user;
        this.descripcionFalla = descripcionFalla;
        this.diagnostico = diagnostico;
        this.totalManoObra = totalManoObra;
        this.totalRepuestos = totalRepuestos;
        this.totalPagar = totalPagar;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public OrdenDeServicio(){}
    
    public void validarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new DatosInvalidosException("El vehículo asignado es obligatorio.");
        }
    }

    public void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new DatosInvalidosException("El cliente es obligatorio.");
        }
    }

    public void validarUser(User user) {
        if (user == null) {
            throw new DatosInvalidosException("El usuario que registra es obligatorio.");
        }
    }

    public void validarDescripcionFalla(String descripcionFalla) {
        if (descripcionFalla == null || descripcionFalla.trim().isEmpty()) {
            throw new DatosInvalidosException("La descripción de la falla es obligatoria.");
        }
    }

    public void validarDiagnostico(String diagnostico) {
        // Si el diagnóstico puede ser opcional al abrir la orden, quita la validación isEmpty()
        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            throw new DatosInvalidosException("El diagnóstico es obligatorio.");
        }
    }

    public void validarTotalManoObra(BigDecimal totalManoObra) {
        if (totalManoObra == null) {
            throw new DatosInvalidosException("El total de mano de obra es obligatorio.");
        }
        if (totalManoObra.compareTo(BigDecimal.ZERO) < 0) {
            throw new DatosInvalidosException("El total de mano de obra no puede ser negativo.");
        }
    }

    public void validarTotalRepuestos(BigDecimal totalRepuestos) {
        if (totalRepuestos == null) {
            throw new DatosInvalidosException("El total de repuestos es obligatorio.");
        }
        if (totalRepuestos.compareTo(BigDecimal.ZERO) < 0) {
            throw new DatosInvalidosException("El total de repuestos no puede ser negativo.");
        }
    }

    public void validarTotalPagar(BigDecimal totalPagar) {
        if (totalPagar == null) {
            throw new DatosInvalidosException("El total a pagar es obligatorio.");
        }
        if (totalPagar.compareTo(BigDecimal.ZERO) < 0) {
            throw new DatosInvalidosException("El total a pagar no puede ser negativo.");
        }
    }

    public void validarEstado(Estado estado) {
        if (estado == null) {
            throw new DatosInvalidosException("El estado de la orden es obligatorio.");
        }
    }

    public void validarFechaRegistro(OffsetDateTime fechaRegistro) {
        if (fechaRegistro == null) {
            throw new DatosInvalidosException("La fecha de registro es obligatoria.");
        }
        if (fechaRegistro.isAfter(OffsetDateTime.now())) {
            throw new DatosInvalidosException("La fecha de registro no puede ser en el futuro.");
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        validarVehiculo(vehiculo);
        this.vehiculo = vehiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        validarCliente(cliente);
        this.cliente = cliente;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        validarUser(user);
        this.user = user;
    }

    public String getDescripcionFalla() {
        return descripcionFalla;
    }

    public void setDescripcionFalla(String descripcionFalla) {
        validarDescripcionFalla(descripcionFalla);
        this.descripcionFalla = descripcionFalla;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        validarDiagnostico(diagnostico);
        this.diagnostico = diagnostico;
    }

    public BigDecimal getTotalManoObra() {
        return totalManoObra;
    }

    public void setTotalManoObra(BigDecimal totalManoObra) {
        validarTotalManoObra(totalManoObra);
        this.totalManoObra = totalManoObra;
    }

    public BigDecimal getTotalRepuestos() {
        return totalRepuestos;
    }

    public void setTotalRepuestos(BigDecimal totalRepuestos) {
        validarTotalRepuestos(totalRepuestos);
        this.totalRepuestos = totalRepuestos;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        validarTotalPagar(totalPagar);
        this.totalPagar = totalPagar;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        validarEstado(estado);
        this.estado = estado;
    }

    public OffsetDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(OffsetDateTime fechaRegistro) {
        validarFechaRegistro(fechaRegistro);
        this.fechaRegistro = fechaRegistro;
    }
    

}
