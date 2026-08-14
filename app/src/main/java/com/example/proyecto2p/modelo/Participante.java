package com.example.proyecto2p.modelo;

public class Participante extends Usuario implements Comparable<Participante> {
    private int puntajeAcumulado;

    public Participante(String idUsuario, String nombreUsuario, String contrasena, String nombreCompleto, TipoUsuario tipoUsuario, int puntajeAcumulado) {
        // Llama al constructor de la clase padre (Usuario)
        super(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipoUsuario);
        this.puntajeAcumulado = puntajeAcumulado;
    }

    public int getPuntajeAcumulado() {
        return puntajeAcumulado;
    }

    public void setPuntajeAcumulado(int puntajeAcumulado) {
        this.puntajeAcumulado = puntajeAcumulado;
    }

    @Override
    public int compareTo(Participante otro) {
        // 1. Ordenar de mayor a menor puntaje (descendente)
        if (this.puntajeAcumulado != otro.getPuntajeAcumulado()) {
            return Integer.compare(otro.getPuntajeAcumulado(), this.puntajeAcumulado);
        }
        // 2. Si hay empate de puntos, ordenar alfabéticamente por nombre de usuario
        return this.getNombreUsuario().compareToIgnoreCase(otro.getNombreUsuario());
    }

    @Override
    public String toString() {
        return "Participante{" +
                "puntajeAcumulado=" + puntajeAcumulado +
                "} " + super.toString();
    }
}