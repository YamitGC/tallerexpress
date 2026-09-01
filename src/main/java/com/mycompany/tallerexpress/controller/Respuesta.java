/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.controller;

/**
 *
 * @author Coder
 * @param <T>
 */
public class Respuesta<T> {
    private final int status; // Simula código HTTP (200, 201, 400, 404, 500)
    private final String mensaje;
    private final T datos;

    public Respuesta(int status, String mensaje, T datos) {
        this.status = status;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    public int getStatus() { return status; }
    public String getMensaje() { return mensaje; }
    public T getDatos() { return datos; }
}