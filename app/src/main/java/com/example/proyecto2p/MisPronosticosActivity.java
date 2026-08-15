package com.example.proyecto2p;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto2p.datos.GestorArchivos;
import com.example.proyecto2p.modelo.EstadoPartido;
import com.example.proyecto2p.modelo.FaseTorneo;
import com.example.proyecto2p.modelo.Partido;
import com.example.proyecto2p.modelo.Pronostico;
import com.example.proyecto2p.modelo.Resultado;
import com.example.proyecto2p.modelo.Usuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Actividad de solo lectura que muestra todos los pronósticos registrados
 * por el participante que inició sesión (punto 5 del enunciado).
 *
 * <p>Recorre las siete fases del torneo, deserializa los pronósticos
 * guardados en cada una mediante {@link GestorArchivos#cargarPronosticos},
 * y los agrupa visualmente por fase. Para cada pronóstico se muestra el
 * marcador que el participante predijo y, si el partido ya finalizó, el
 * resultado oficial junto con los puntos obtenidos calculados en el
 * momento con {@link Pronostico#calcularPuntos(Resultado)}; si el partido
 * todavía no finalizó, se indica que el resultado está pendiente.</p>
 *
 * @author Tu Nombre/Grupo
 * @version 1.0
 */
public class MisPronosticosActivity extends AppCompatActivity {

    private Usuario usuarioActivo;
    private GestorArchivos gestorArchivos;
    private LinearLayout contenedor;

    /**
     * Método invocado cuando se crea la actividad. Recupera al usuario
     * autenticado, carga sus pronósticos y configura el botón Volver.
     *
     * @param savedInstanceState Estado de la actividad previamente guardado (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_pronosticos);

        usuarioActivo = (Usuario) getIntent().getSerializableExtra("usuarioActivo");

        TextView tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        if (usuarioActivo != null) {
            tvNombreUsuario.setText(usuarioActivo.getNombreCompleto());
        }

        gestorArchivos = new GestorArchivos(this);
        contenedor = findViewById(R.id.contenedorMisPronosticos);

        cargarMisPronosticos();

        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
    }

    /**
     * Recorre las siete fases del torneo, agrega un encabezado por cada
     * fase que tenga pronósticos registrados, y una tarjeta de solo
     * lectura por cada pronóstico encontrado.
     */
    private void cargarMisPronosticos() {
        contenedor.removeAllViews();

        if (usuarioActivo == null) {
            return;
        }

        List<Partido> partidos = gestorArchivos.cargarPartidos();
        Map<String, Partido> partidosPorId = new HashMap<>();
        for (Partido p : partidos) {
            partidosPorId.put(p.getIdPartido(), p);
        }

        List<Resultado> resultados = gestorArchivos.cargarResultados();
        Map<String, Resultado> resultadosPorId = new HashMap<>();
        for (Resultado r : resultados) {
            resultadosPorId.put(r.getIdPartido(), r);
        }

        boolean hayAlgunPronostico = false;

        for (FaseTorneo fase : FaseTorneo.values()) {
            List<Pronostico> pronosticosDeFase =
                    gestorArchivos.cargarPronosticos(usuarioActivo.getIdUsuario(), fase);

            if (pronosticosDeFase.isEmpty()) {
                continue;
            }

            hayAlgunPronostico = true;
            agregarEncabezadoFase(fase.getNombreVisible());

            for (Pronostico pronostico : pronosticosDeFase) {
                Partido partido = partidosPorId.get(pronostico.getIdPartido());
                if (partido == null) {
                    // El partido ya no existe en partidos.txt; se omite para evitar un dato huérfano.
                    continue;
                }
                Resultado resultadoOficial = resultadosPorId.get(pronostico.getIdPartido());
                agregarTarjetaPronostico(partido, pronostico, resultadoOficial);
            }
        }

        if (!hayAlgunPronostico) {
            TextView tvVacio = new TextView(this);
            tvVacio.setText("Todavía no has registrado ningún pronóstico.\nVe a la opción \"Pronósticos\" para empezar.");
            tvVacio.setTextColor(Color.GRAY);
            tvVacio.setTextSize(13);
            tvVacio.setPadding(dp(8), dp(24), dp(8), dp(24));
            contenedor.addView(tvVacio);
        }
    }

    /**
     * Agrega un encabezado de sección con el nombre de la fase, para
     * separar visualmente los pronósticos agrupados por fase del torneo.
     *
     * @param nombreFase nombre visible de la fase (ej. "Cuartos de final").
     */
    private void agregarEncabezadoFase(String nombreFase) {
        TextView tvFase = new TextView(this);
        tvFase.setText(nombreFase);
        tvFase.setTextColor(Color.parseColor("#082B5B"));
        tvFase.setTextSize(13);
        tvFase.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = dp(14);
        params.bottomMargin = dp(6);
        tvFase.setLayoutParams(params);
        contenedor.addView(tvFase);
    }

    /**
     * Construye la tarjeta de solo lectura para un pronóstico: selecciones
     * enfrentadas, estado del partido, marcador pronosticado y, según
     * corresponda, el resultado oficial con los puntos obtenidos o un
     * mensaje indicando que aún está pendiente.
     *
     * @param partido        partido al que pertenece el pronóstico.
     * @param pronostico     pronóstico registrado por el participante.
     * @param resultadoOficial resultado oficial del partido, o {@code null} si aún no existe.
     */
    private void agregarTarjetaPronostico(Partido partido, Pronostico pronostico, Resultado resultadoOficial) {

        LinearLayout tarjeta = new LinearLayout(this);
        tarjeta.setOrientation(LinearLayout.VERTICAL);
        tarjeta.setBackgroundResource(R.drawable.fondo_boton_menu);
        tarjeta.setPadding(dp(14), dp(12), dp(14), dp(12));
        LinearLayout.LayoutParams paramsTarjeta = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsTarjeta.bottomMargin = dp(10);
        tarjeta.setLayoutParams(paramsTarjeta);

        // ---------- Fila 1: selecciones + estado ----------
        LinearLayout filaTitulo = new LinearLayout(this);
        filaTitulo.setOrientation(LinearLayout.HORIZONTAL);
        filaTitulo.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvPartido = new TextView(this);
        tvPartido.setText(partido.getSeleccion1() + "  vs  " + partido.getSeleccion2());
        tvPartido.setTextColor(Color.parseColor("#082B5B"));
        tvPartido.setTextSize(14);
        tvPartido.setTypeface(null, Typeface.BOLD);
        tvPartido.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView tvEstado = crearBadgeEstado(partido.getEstado());

        filaTitulo.addView(tvPartido);
        filaTitulo.addView(tvEstado);
        tarjeta.addView(filaTitulo);

        // ---------- Fila 2: tu pronóstico ----------
        TextView tvTuPronostico = new TextView(this);
        tvTuPronostico.setText("Tu pronóstico: " + pronostico.getGolesSeleccion1() + " - " + pronostico.getGolesSeleccion2());
        tvTuPronostico.setTextColor(Color.parseColor("#333333"));
        tvTuPronostico.setTextSize(13);
        LinearLayout.LayoutParams paramsPronostico = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsPronostico.topMargin = dp(6);
        tvTuPronostico.setLayoutParams(paramsPronostico);
        tarjeta.addView(tvTuPronostico);

        // ---------- Fila 3: resultado oficial / pendiente ----------
        TextView tvResultado = new TextView(this);
        LinearLayout.LayoutParams paramsResultado = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsResultado.topMargin = dp(4);
        tvResultado.setLayoutParams(paramsResultado);
        tvResultado.setTextSize(12);

        if (partido.estaFinalizado() && resultadoOficial != null) {
            int puntos = pronostico.calcularPuntos(resultadoOficial);
            tvResultado.setText("Resultado oficial: " + resultadoOficial.getGolesSeleccion1() + " - "
                    + resultadoOficial.getGolesSeleccion2() + "   ·   " + puntos + " pts obtenidos");
            tvResultado.setTextColor(Color.parseColor("#2E7D32"));
            tvResultado.setTypeface(null, Typeface.BOLD);
        } else {
            tvResultado.setText("Resultado y puntaje pendientes.");
            tvResultado.setTextColor(Color.parseColor("#9AA5B1"));
        }

        tarjeta.addView(tvResultado);

        contenedor.addView(tarjeta);
    }

    /**
     * Crea la pequeña etiqueta de color que muestra el estado actual del
     * partido (ABIERTO, CERRADO o FINALIZADO), igual que en la pantalla de
     * Pronósticos.
     *
     * @param estado estado del partido.
     * @return un {@link TextView} estilizado como "badge" con el color correspondiente.
     */
    private TextView crearBadgeEstado(EstadoPartido estado) {
        TextView tvEstado = new TextView(this);
        tvEstado.setText(estado.name());
        tvEstado.setTextColor(Color.WHITE);
        tvEstado.setTextSize(9);
        tvEstado.setTypeface(null, Typeface.BOLD);
        tvEstado.setPadding(dp(8), dp(3), dp(8), dp(3));

        String colorHex;
        switch (estado) {
            case ABIERTO:
                colorHex = "#2E7D32";
                break;
            case CERRADO:
                colorHex = "#C77700";
                break;
            default:
                colorHex = "#082B5B";
                break;
        }

        GradientDrawable fondo = new GradientDrawable();
        fondo.setColor(Color.parseColor(colorHex));
        fondo.setCornerRadius(dp(20));
        tvEstado.setBackground(fondo);

        return tvEstado;
    }

    /**
     * Convierte dp a píxeles, igual que en el resto de las pantallas de la app.
     */
    private int dp(int valor) {
        return (int) (valor * getResources().getDisplayMetrics().density);
    }
}
