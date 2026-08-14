package com.example.proyecto2p.modelo;
import java.io.Serializable;

/***
 * Representa el resultado oficial de un partido del torneo, una vez que
 * este ha finalizado.
 *
 * <p>Esta clase implementa {@link Serializable} para permitir que
 * {@code GestorArchivos} la almacene y recupere desde archivo
 * (por ejemplo, {@code resultados.txt}). Se utiliza como base de comparacion
 * dentro de {@link Pronostico#calcularPuntos(Resultado)} para determinar
 * los puntos obtenidos por cada participante.</p>
 */

public class Resultado {
    private int idResultado;
    private int idPartido;
    private int golesSeleccion1;
    private int golesSeleccion2;

    /***
     * Crea un nuevo resultado oficial para un partido.
     *
     * @param idResultado     identificador unico del resultado
     * @param idPartido       identificador del partido al que corresponde el resultado
     * @param golesSeleccion1 goles anotados por la seleccion 1
     * @param golesSeleccion2 goles anotados por la seleccion 2
     */
    public Resultado(int idResultado, int idPartido, int golesSeleccion1, int golesSeleccion2) {
        this.idResultado = idResultado;
        this.idPartido = idPartido;
        this.golesSeleccion1 = golesSeleccion1;
        this.golesSeleccion2 = golesSeleccion2;
    }

    public int getIdResultado() {
        return idResultado;
    }

    public int getIdPartido() {
        return idPartido;
    }
    /***
     * Obtiene los goles anotados por la seleccion 1.
     *
     * @return goles reales de la seleccion 1
     */
    public int getGolesSeleccion1() {
        return golesSeleccion1;
    }

    /***
     * Obtiene los goles anotados por la seleccion 2.
     *
     * @return goles reales de la seleccion 2
     */
    public int getGolesSeleccion2() {
        return golesSeleccion2;
    }

    @Override
    public String toString() {
        return "Resultado{" +
                "idResultado=" + idResultado +
                ", idPartido=" + idPartido +
                ", golesSeleccion1=" + golesSeleccion1 +
                ", golesSeleccion2=" + golesSeleccion2 +
                '}';
    }

}
