package com.example.proyecto2p.modelo;

public class Partido {
    private int idPartido;
    private String fecha;
    private String hora;
    private String estadio;
    private String seleccion1;
    private String seleccion2;
    private FaseTorneo fase;
    private EstadoPartido estado;


    public Partido(int idPartido, String fecha, String hora,
                   String estadio, String seleccion1,
                   String seleccion2, FaseTorneo fase,
                   EstadoPartido estado) {
        this.idPartido = idPartido;
        this.fecha = fecha;
        this.hora = hora;
        this.estadio = estadio;
        this.seleccion1 = seleccion1;
        this.seleccion2 = seleccion2;
        this.fase = fase;
        this.estado = estado;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstadio() {
        return estadio;
    }

    public void setEstadio(String estadio) {
        this.estadio = estadio;
    }

    public String getSeleccion1() {
        return seleccion1;
    }

    public void setSeleccion1(String seleccion1) {
        this.seleccion1 = seleccion1;
    }

    public String getSeleccion2() {
        return seleccion2;
    }

    public void setSeleccion2(String seleccion2) {
        this.seleccion2 = seleccion2;
    }

    public FaseTorneo getFase() {
        return fase;
    }

    public void setFase(FaseTorneo fase) {
        this.fase = fase;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }


    @Override
    public String toString() {
        return "Partido{" +
                "idPartido=" + idPartido +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", estadio='" + estadio + '\'' +
                ", seleccion1='" + seleccion1 + '\'' +
                ", seleccion2='" + seleccion2 + '\'' +
                ", fase=" + fase +
                ", estado=" + estado +
                '}';
    }

    public void cerrarPronosticos() {
        this.estado = EstadoPartido.CERRADO;
    }

    public void finalizarPartido() {
        this.estado = EstadoPartido.FINALIZADO;
    }

    public boolean permitePronosticos() {
        return estaAbierto();
    }

    public boolean estaAbierto() {
        return this.estado == EstadoPartido.ABIERTO;
    }

    public boolean estaCerrado() {
        return this.estado == EstadoPartido.CERRADO;
    }

    public boolean estaFinalizado() {
        return this.estado == EstadoPartido.FINALIZADO;
    }
}
