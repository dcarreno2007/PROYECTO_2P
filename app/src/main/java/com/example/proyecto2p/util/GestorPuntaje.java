package com.example.proyecto2p.util;
import com.example.proyecto2p.datos.GestorArchivos;
import com.example.proyecto2p.modelo.Participante;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.example.proyecto2p.modelo.Pronostico;
import com.example.proyecto2p.modelo.Resultado;
import android.content.Context;

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
 /***
 * Recalcula y actualiza el puntaje acumulado de cada participante,
 * comparando sus pronosticos contra los resultados oficiales
 * registrados.
 * @param participantes lista de participantes a actualizar
 * @param pronosticos   lista de todos los pronosticos registrados
 * @param resultados    lista de resultados oficiales de los partidos finalizados
 */

 public void actualizarPuntajes(List<Participante> participantes, List<Pronostico> pronosticos,List<Resultado> resultados) {
     if (participantes == null || pronosticos == null || resultados == null) {
         return;
     }

     Map<String, Resultado> resultadosPorPartido = indexarResultadosPorPartido(resultados);
     Map<String, Participante> participantesPorId = indexarParticipantesPorId(participantes);

     for (Pronostico pronostico : pronosticos) {
         Resultado resultadoOficial = resultadosPorPartido.get(pronostico.getIdPartido());

         if (resultadoOficial == null) {
             continue;
         }

         int puntosObtenidos = pronostico.calcularPuntos(resultadoOficial);
         Participante participante = participantesPorId.get(pronostico.getIdUsuario());

         if (participante != null) {
             int nuevoPuntaje = participante.getPuntajeAcumulado() + puntosObtenidos;
             participante.setPuntajeAcumulado(nuevoPuntaje);
         }
     }
 }
    /***
     * Persiste los puntajes acumulados actualizados de los participantes,
     * delegando el guardado en {@link GestorArchivos}.
     *
     * @param participantes lista de participantes con los puntajes ya actualizados
     */
    public void guardarPuntajes(List<Participante> participantes, Context context) {
        if (participantes == null) {
            return;
        }

        GestorArchivos gestorArchivos = new GestorArchivos(context);
        gestorArchivos.guardarParticipantes(participantes);
    }

    /**
     * Construye un mapa que asocia el id de cada partido con su resultado
     * oficial, para permitir una busqueda eficiente durante el calculo de
     * puntajes.
     *
     * @param resultados lista de resultados oficiales
     * @return mapa de {@code idPartido} a {@link Resultado}
     */
    private Map<String, Resultado> indexarResultadosPorPartido(List<Resultado> resultados) {
        Map<String, Resultado> mapa = new HashMap<>();
        for (Resultado resultado : resultados) {
            mapa.put(resultado.getIdPartido(), resultado);
        }
        return mapa;
    }
    private Map<String, Participante> indexarParticipantesPorId(List<Participante> participantes) {
        Map<String, Participante> mapa = new HashMap<>();
        for (Participante participante : participantes) {
            mapa.put(participante.getIdUsuario(), participante);
        }
        return mapa;
    }


}
