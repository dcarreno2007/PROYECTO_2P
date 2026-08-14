package com.example.proyecto2p.modelo;

public class Usuario {
    private String idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private String nombreCompleto;
    private tipoUsuario tipoUsuario;

    public Usuario(String idUsuario,String nombreUsuario, String contrasena, String nombreCompleto, tipoUsuario tipoUsuario ){
        this.idUsuario=idUsuario;
        this.nombreUsuario=nombreUsuario;
        this.contrasena=contrasena;
        this.nombreCompleto=nombreCompleto;
        this.tipoUsuario=tipoUsuario;
    }

    public tipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
    public void setTipoUsuario(tipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public String getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", tipoUsuario=" + tipoUsuario +
                '}';
    }
}
