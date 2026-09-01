/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.domain.models;

import com.mycompany.tallerexpress.domain.exceptions.DatosInvalidosException;

/**
 *
 * @author Coder
 */
public class Vehiculo {
    private Long id;
    private Cliente cliente;
    private String placa;
    private String marca;
    private String modelo;
    private String categoria;
    private int anioModelo;
    
    public Vehiculo(){}

    public Vehiculo(Cliente cliente, String placa, String marca, String modelo, String categoria, int anioModelo) {
        validarCliente(cliente);
        validarPlaca(placa);
        validarMarca(marca);
        validarModelo(modelo);
        validarCategoria(categoria);
        validarAnioModelo(anioModelo);
        
        this.cliente = cliente;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.anioModelo = anioModelo;
    }

    public Vehiculo(Long id, Cliente cliente, String placa, String marca, String modelo, String categoria, int anioModelo) {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("El ID debe ser un número positivo válido.");
        }
        validarCliente(cliente);
        validarPlaca(placa);
        validarMarca(marca);
        validarModelo(modelo);
        validarCategoria(categoria);
        validarAnioModelo(anioModelo);
        
        this.id = id;
        this.cliente = cliente;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.anioModelo = anioModelo;
    }

    
    public void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new DatosInvalidosException("El cliente asignado es obligatorio.");
        }
    }

    public void validarPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new DatosInvalidosException("La placa es obligatoria.");
        }
    }

    public void validarMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new DatosInvalidosException("La marca es obligatoria.");
        }
    }

    public void validarModelo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new DatosInvalidosException("El modelo es obligatorio.");
        }
    }

    public void validarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new DatosInvalidosException("La categoría es obligatoria.");
        }
    }

    public void validarAnioModelo(Integer anioModelo) {
        if (anioModelo == null) {
            throw new DatosInvalidosException("El año del modelo es obligatorio.");
        }
        if (anioModelo < 1900) {
            throw new DatosInvalidosException("El año del modelo no es válido.");
        }
        // Evita que registren vehículos con un año exageradamente futurista
        int anioLimite = java.time.Year.now().getValue() + 1;
            if (anioModelo > anioLimite) {
            throw new DatosInvalidosException("El año del modelo no puede ser mayor a " + anioLimite + ".");
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        validarCliente(cliente);
        this.cliente = cliente;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        validarPlaca(placa);
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        validarMarca(marca);
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        validarModelo(modelo);
        this.modelo = modelo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        validarCategoria(categoria);
        this.categoria = categoria;
    }

    public int getAnioModelo() {
        return anioModelo;
    }

    public void setAnioModelo(int anioModelo) {
        validarAnioModelo(anioModelo);
        this.anioModelo = anioModelo;
    }
    
    
}
