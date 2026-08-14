package com.example.proyecto2p.util;
import com.example.proyecto2p.datos.GestorArchivos;
import com.example.proyecto2p.modelo.Participante;
import java.util.List;
import com.example.proyecto2p.modelo.Pronostico;
import com.example.proyecto2p.modelo.Resultado;

/***
 * Gestiona el cálculo y la actualización de los puntajes acumulados de los
 * participantes del torneo, en base a sus pronósticos y a los resultados
 * oficiales registrados.
 *
 * <p>Esta clase implementa el flujo descrito en la opcion "Actualizar
 * puntajes" del administrador: reinicia el puntaje acumulado de todos los
 * participantes, recorre los pronosticos comparandolos contra los
 * resultados oficiales de los partidos ya finalizados, y persiste los
 * puntajes actualizados mediante {@link GestorArchivos}.</p>
 */

public class GestorPuntaje {
    /***
     * Reinicia a cero el puntaje acumulado de todos los participantes.
     *
     * <p>Debe invocarse antes de
     * para evitar que los puntajes se dupliquen si la opcion "Actualizar
     * puntajes" se ejecuta mas de una vez.</p>
     *
     * @param participantes lista de participantes cuyo puntaje sera reiniciado
     */
    public void reiniciarPuntajes(List<Participante> participantes) {
        if (participantes == null) {
            return;
        }

        for (Participante participante : participantes) {
            participante.setPuntajeAcumulado(0);
        }
    }
}
