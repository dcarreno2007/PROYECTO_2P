package com.example.proyecto2p;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto2p.datos.GestorArchivos;
import com.example.proyecto2p.excepciones.DatosIncompletosException;
import com.example.proyecto2p.excepciones.PronosticoFueraDeTiempoException;
import com.example.proyecto2p.modelo.EstadoPartido;
import com.example.proyecto2p.modelo.FaseTorneo;
import com.example.proyecto2p.modelo.Partido;
import com.example.proyecto2p.modelo.Pronostico;
import com.example.proyecto2p.modelo.Usuario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.material.button.MaterialButton;

/**
 * Actividad que permite a un participante registrar sus pronósticos para
 * los partidos del Mundial 2026 (punto 4 del enunciado).
 *
 * <p>Muestra un {@link Spinner} para elegir la fase del torneo y, debajo,
 * una tarjeta por cada partido de esa fase con dos campos para ingresar
 * los goles pronosticados. Mientras el partido esté {@code ABIERTO} el
 * participante puede guardar o modificar su pronóstico; si está
 * {@code CERRADO} o {@code FINALIZADO}, los campos quedan deshabilitados.</p>
 *
 * @author Tu Nombre/Grupo
 * @version 1.0
 */
public class PronosticosActivity extends AppCompatActivity {

    private Usuario usuarioActivo;
    private GestorArchivos gestorArchivos;
    private LinearLayout contenedorPartidos;
    private FaseTorneo faseSeleccionada = FaseTorneo.FASE_GRUPOS;

    /**
     * Método invocado cuando se crea la actividad. Recupera al usuario
     * autenticado, prepara el {@link GestorArchivos}, configura el Spinner
     * de fases y el botón Volver.
     *
     * @param savedInstanceState Estado de la actividad previamente guardado (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronosticos);

        usuarioActivo = (Usuario) getIntent().getSerializableExtra("usuarioActivo");

        TextView tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        if (usuarioActivo != null) {
            tvNombreUsuario.setText(usuarioActivo.getNombreCompleto());
        }

        gestorArchivos = new GestorArchivos(this);
        contenedorPartidos = findViewById(R.id.contenedorPartidos);

        configurarSpinnerFase();

        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
    }

    /**
     * Configura el Spinner con las siete fases del torneo (usando el nombre
     * visible definido en {@link FaseTorneo}) y recarga los partidos cada
     * vez que el usuario cambia de fase.
     */
    private void configurarSpinnerFase() {
        Spinner spinner = findViewById(R.id.spinnerFase);

        FaseTorneo[] fases = FaseTorneo.values();
        String[] nombresFases = new String[fases.length];
        for (int i = 0; i < fases.length; i++) {
            nombresFases[i] = fases[i].getNombreVisible();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, nombresFases);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                faseSeleccionada = FaseTorneo.values()[position];
                cargarPartidosDeFase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se requiere ninguna acción.
            }
        });

        // Cargamos la primera fase inmediatamente, sin esperar al callback del Spinner.
        cargarPartidosDeFase();
    }

    /**
     * Carga los partidos correspondientes a {@link #faseSeleccionada} y
     * construye una tarjeta por cada uno, combinándolos con el pronóstico
     * que el participante ya tuviera guardado (si existe).
     */
    private void cargarPartidosDeFase() {
        contenedorPartidos.removeAllViews();

        List<Partido> todosLosPartidos = gestorArchivos.cargarPartidos();
        List<Pronostico> misPronosticos =
                gestorArchivos.cargarPronosticos(usuarioActivo.getIdUsuario(), faseSeleccionada);

        Map<String, Pronostico> pronosticosPorPartido = new HashMap<>();
        for (Pronostico p : misPronosticos) {
            pronosticosPorPartido.put(p.getIdPartido(), p);
        }

        boolean hayPartidos = false;
        for (Partido partido : todosLosPartidos) {
            if (partido.getFase() == faseSeleccionada) {
                hayPartidos = true;
                agregarPartido(partido, pronosticosPorPartido.get(partido.getIdPartido()));
            }
        }

        if (!hayPartidos) {
            TextView tvVacio = new TextView(this);
            tvVacio.setText("Todavía no hay partidos cargados para esta fase.");
            tvVacio.setTextColor(Color.GRAY);
            tvVacio.setTextSize(13);
            tvVacio.setPadding(dp(8), dp(16), dp(8), dp(16));
            contenedorPartidos.addView(tvVacio);
        }
    }

    /**
     * Construye y agrega al contenedor la tarjeta correspondiente a un
     * partido: información básica, estado, y los controles para
     * registrar/modificar el pronóstico (o el mensaje de que ya no se
     * puede modificar, según el estado del partido).
     *
     * @param partido            partido a mostrar.
     * @param pronosticoExistente pronóstico que el participante ya tenía
     *                            guardado para este partido, o {@code null}
     *                            si todavía no ha pronosticado.
     */
    private void agregarPartido(Partido partido, Pronostico pronosticoExistente) {

        LinearLayout tarjeta = new LinearLayout(this);
        tarjeta.setOrientation(LinearLayout.VERTICAL);
        tarjeta.setBackgroundResource(R.drawable.fondo_boton_menu);
        tarjeta.setPadding(dp(14), dp(14), dp(14), dp(14));
        LinearLayout.LayoutParams paramsTarjeta = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsTarjeta.bottomMargin = dp(14);
        tarjeta.setLayoutParams(paramsTarjeta);

        // ---------- Fila 1: fecha / hora / estadio + estado ----------
        LinearLayout filaInfo = new LinearLayout(this);
        filaInfo.setOrientation(LinearLayout.HORIZONTAL);
        filaInfo.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvInfo = new TextView(this);
        tvInfo.setText(partido.getFecha() + " · " + partido.getHora() + " · " + partido.getEstadio());
        tvInfo.setTextSize(11);
        tvInfo.setTextColor(Color.parseColor("#757575"));
        tvInfo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView tvEstado = crearBadgeEstado(partido.getEstado());

        filaInfo.addView(tvInfo);
        filaInfo.addView(tvEstado);
        tarjeta.addView(filaInfo);

        // ---------- Fila 2: seleccion1  [goles] - [goles]  seleccion2 ----------
        LinearLayout filaMarcador = new LinearLayout(this);
        filaMarcador.setOrientation(LinearLayout.HORIZONTAL);
        filaMarcador.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams paramsFilaMarcador = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsFilaMarcador.topMargin = dp(10);
        filaMarcador.setLayoutParams(paramsFilaMarcador);

        TextView tvSeleccion1 = new TextView(this);
        tvSeleccion1.setText(partido.getSeleccion1());
        tvSeleccion1.setTextColor(Color.parseColor("#082B5B"));
        tvSeleccion1.setTextSize(14);
        tvSeleccion1.setTypeface(null, Typeface.BOLD);
        tvSeleccion1.setGravity(Gravity.END);
        tvSeleccion1.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        boolean partidoAbierto = partido.estaAbierto();

        EditText etGoles1 = crearCampoGoles(pronosticoExistente != null ? pronosticoExistente.getGolesSeleccion1() : null);
        etGoles1.setEnabled(partidoAbierto);

        TextView tvGuion = new TextView(this);
        tvGuion.setText("-");
        tvGuion.setTextColor(Color.parseColor("#082B5B"));
        tvGuion.setTextSize(16);
        tvGuion.setTypeface(null, Typeface.BOLD);
        tvGuion.setPadding(dp(6), 0, dp(6), 0);

        EditText etGoles2 = crearCampoGoles(pronosticoExistente != null ? pronosticoExistente.getGolesSeleccion2() : null);
        etGoles2.setEnabled(partidoAbierto);

        TextView tvSeleccion2 = new TextView(this);
        tvSeleccion2.setText(partido.getSeleccion2());
        tvSeleccion2.setTextColor(Color.parseColor("#082B5B"));
        tvSeleccion2.setTextSize(14);
        tvSeleccion2.setTypeface(null, Typeface.BOLD);
        tvSeleccion2.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        filaMarcador.addView(tvSeleccion1);
        filaMarcador.addView(etGoles1);
        filaMarcador.addView(tvGuion);
        filaMarcador.addView(etGoles2);
        filaMarcador.addView(tvSeleccion2);
        tarjeta.addView(filaMarcador);

        // ---------- Fila 3: acción (guardar, o mensaje de bloqueo) ----------
        if (partidoAbierto) {
            MaterialButton btnGuardar = new MaterialButton(this);
            btnGuardar.setText(pronosticoExistente != null ? "Actualizar pronóstico" : "Guardar pronóstico");
            btnGuardar.setTextSize(13);
            btnGuardar.setAllCaps(false);
            btnGuardar.setCornerRadius(dp(8));
            btnGuardar.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#082B5B")));
            LinearLayout.LayoutParams paramsBoton = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsBoton.topMargin = dp(10);
            btnGuardar.setLayoutParams(paramsBoton);

            btnGuardar.setOnClickListener(v ->
                    guardarPronostico(partido, etGoles1, etGoles2));

            tarjeta.addView(btnGuardar);
        } else {
            TextView tvBloqueado = new TextView(this);
            tvBloqueado.setText(partido.estaCerrado()
                    ? "Los pronósticos para este partido están cerrados."
                    : "Este partido ya finalizó. Consulta el resultado en \"Mis pronósticos\".");
            tvBloqueado.setTextColor(Color.parseColor("#9AA5B1"));
            tvBloqueado.setTextSize(11);
            LinearLayout.LayoutParams paramsMsg = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsMsg.topMargin = dp(8);
            tvBloqueado.setLayoutParams(paramsMsg);
            tarjeta.addView(tvBloqueado);
        }

        contenedorPartidos.addView(tarjeta);
    }

    /**
     * Valida y guarda el pronóstico ingresado para un partido.
     *
     * <p>Lanza {@link PronosticoFueraDeTiempoException} si el partido ya no
     * está {@code ABIERTO}, y {@link DatosIncompletosException} si faltan
     * datos o los goles ingresados no son números enteros válidos (mayores
     * o iguales a cero). Ambos mensajes se muestran mediante
     * {@link Toast#makeText}, tal como exige el enunciado.</p>
     *
     * @param partido  partido al que corresponde el pronóstico.
     * @param etGoles1 campo con los goles pronosticados para la selección 1.
     * @param etGoles2 campo con los goles pronosticados para la selección 2.
     */
    private void guardarPronostico(Partido partido, EditText etGoles1, EditText etGoles2) {
        try {
            if (!partido.estaAbierto()) {
                throw new PronosticoFueraDeTiempoException(
                        "El período para registrar pronósticos de este partido ya ha finalizado.");
            }

            String texto1 = etGoles1.getText().toString().trim();
            String texto2 = etGoles2.getText().toString().trim();

            if (texto1.isEmpty() || texto2.isEmpty()) {
                throw new DatosIncompletosException(
                        "No se han ingresado todos los datos necesarios para registrar el pronóstico.");
            }

            int goles1;
            int goles2;
            try {
                goles1 = Integer.parseInt(texto1);
                goles2 = Integer.parseInt(texto2);
            } catch (NumberFormatException nfe) {
                throw new DatosIncompletosException(
                        "Los goles deben ser números enteros válidos.");
            }

            if (goles1 < 0 || goles2 < 0) {
                throw new DatosIncompletosException(
                        "Los goles deben ser números enteros mayores o iguales a cero.");
            }

            String idPronostico = usuarioActivo.getIdUsuario() + "_" + partido.getIdPartido();
            Pronostico pronostico = new Pronostico(
                    idPronostico, usuarioActivo.getIdUsuario(), partido.getIdPartido(), goles1, goles2);

            gestorArchivos.guardarPronostico(pronostico, faseSeleccionada);

            Toast.makeText(this, "Pronóstico guardado correctamente.", Toast.LENGTH_SHORT).show();

        } catch (DatosIncompletosException | PronosticoFueraDeTiempoException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Crea el campo de texto donde el participante ingresa los goles
     * pronosticados para una selección.
     *
     * @param valorInicial goles previamente guardados para precargar el campo,
     *                     o {@code null} si el participante todavía no ha pronosticado.
     * @return un {@link EditText} numérico listo para agregarse a la tarjeta.
     */
    private EditText crearCampoGoles(Integer valorInicial) {
        EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setGravity(Gravity.CENTER);
        editText.setBackgroundResource(R.drawable.fondo_input);
        editText.setTextColor(Color.parseColor("#082B5B"));
        editText.setTextSize(14);
        editText.setPadding(dp(4), dp(6), dp(4), dp(6));
        if (valorInicial != null) {
            editText.setText(String.valueOf(valorInicial));
        }
        editText.setLayoutParams(new LinearLayout.LayoutParams(dp(48), dp(40)));
        return editText;
    }

    /**
     * Crea la pequeña etiqueta de color que muestra el estado actual del
     * partido (ABIERTO, CERRADO o FINALIZADO).
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
