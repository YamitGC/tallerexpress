/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tallerexpress.domain.exceptions;

/**
 *
 * @author Coder
 */
public class DatosInvalidosException extends RuntimeException {
    public DatosInvalidosException(String mensaje){
        super(mensaje);
    }
    
}
