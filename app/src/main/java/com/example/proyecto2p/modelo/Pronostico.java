package com.example.proyecto2p.modelo;

package com.example.proyecto2p.modelo;

/***
 * Representa el pronostico que un participante realiza sobre el resultado
 * de un partido especifico del torneo.
 */
public class Pronostico {

    private int idPronostico;
    private int idUsuario;
    private int idPartido;
    private int golesSeleccion1;
    private int golesSeleccion2;
    private int puntosObtenidos;

    public Pronostico(int idPronostico, int idUsuario, int idPartido,
                      int golesSeleccion1, int golesSeleccion2) {
        this.idPronostico = idPronostico;
        this.idUsuario = idUsuario;
        this.idPartido = idPartido;
        this.golesSeleccion1 = golesSeleccion1;
        this.golesSeleccion2 = golesSeleccion2;
        this.puntosObtenidos = 0;
    }

    public int getIdPronostico() {
        return idPronostico;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdPartido() {
        return idPartido;
    }

    public int getGolesSeleccion1() {
        return golesSeleccion1;
    }

    public int getGolesSeleccion2() {
        return golesSeleccion2;
    }

    public int getPuntosObtenidos() {
        return puntosObtenidos;
    }

    public void setGolesSeleccion1(int goles) {
        this.golesSeleccion1 = goles;
    }

    public void setGolesSeleccion2(int goles) {
        this.golesSeleccion2 = goles;
    }

    public void setPuntosObtenidos(int puntos) {
        this.puntosObtenidos = puntos;
    }

    /***
     *Calcula los puntos obtenidos comparando este pronóstico contra el
     *resultado oficial del partido, y actualiza puntosObtenidos.
     * @param resultadoOficial resultadoOficial resultado real del partido, ya cargado
     * @return los puntos obtenidos por este pronóstico
     */
    public int calcularPuntos(Resultado resultadoOficial) {
        if (resultadoOficial == null || resultadoOficial.getIdPartido() != this.idPartido) {
            this.puntosObtenidos = 0;
            return this.puntosObtenidos;
        }

        int golesRealesSeleccion1 = resultadoOficial.getGolesSeleccion1();
        int golesRealesSeleccion2 = resultadoOficial.getGolesSeleccion2();

        boolean marcadorExacto = (this.golesSeleccion1 == golesRealesSeleccion1) && (this.golesSeleccion2 == golesRealesSeleccion2);

        if (marcadorExacto) {
            this.puntosObtenidos = 3;
            return this.puntosObtenidos;
        }

        boolean empatePronosticado = (this.golesSeleccion1 == this.golesSeleccion2);
        boolean empateReal = (golesRealesSeleccion1 == golesRealesSeleccion2);

        if (empatePronosticado && empateReal) {
            this.puntosObtenidos = 2;
            return this.puntosObtenidos;
        }

        String ganadorPronosticado = obtenerGanador(this.golesSeleccion1, this.golesSeleccion2);
        String ganadorReal = obtenerGanador(golesRealesSeleccion1, golesRealesSeleccion2);
        boolean aciertaGanador = !empatePronosticado && !empateReal
                && ganadorPronosticado.equals(ganadorReal);

        if (aciertaGanador) {
            int diferenciaPronosticada = this.golesSeleccion1 - this.golesSeleccion2;
            int diferenciaReal = golesRealesSeleccion1 - golesRealesSeleccion2;

            if (diferenciaPronosticada == diferenciaReal) {
                this.puntosObtenidos = 2;
            } else {
                this.puntosObtenidos = 1;
            }
        } else {
            this.puntosObtenidos = 0;
        }

        return this.puntosObtenidos;
    }

    /***
     * Determina el ganador según los goles: seleccion1, seleccion2, o empate.
     * Se usa como apoyo dentro de {@link #calcularPuntos(Resultado)}.
     *
     * @param goles1 goles de la seleccion 1
     * @param goles2 goles de la seleccion 2
     * @return {@code "GANA_1"} si gana la seleccion 1, {@code "GANA_2"} si gana
     *         la seleccion 2, o {@code "EMPATE"} si el marcador es igual
     */
    private String obtenerGanador(int goles1, int goles2) {
        if (goles1 > goles2) {
            return "GANA_1";
        } else if (goles2 > goles1) {
            return "GANA_2";
        } else {
            return "EMPATE";
        }
    }

    @Override
    public String toString() {
        return "Pronostico{" +
                "idPronostico=" + idPronostico +
                ", idUsuario=" + idUsuario +
                ", idPartido=" + idPartido +
                ", golesSeleccion1=" + golesSeleccion1 +
                ", golesSeleccion2=" + golesSeleccion2 +
                ", puntosObtenidos=" + puntosObtenidos +
                '}';
    }
}
