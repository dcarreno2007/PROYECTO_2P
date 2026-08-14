package com.example.proyecto2p.excepciones;

    public class CredencialesInvalidasException extends Exception {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}