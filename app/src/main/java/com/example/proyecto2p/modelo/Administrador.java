package com.example.proyecto2p.modelo;

public class Administrador extends Usuario {
    private String cargo;

    public Administrador(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, TipoUsuario tipoUsuario, String cargo) {
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.cargo = cargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "cargo='" + cargo + '\'' +
                "} " + super.toString();
    }
}
