package com.example.proyecto2p;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto2p.datos.GestorArchivos;
import com.example.proyecto2p.modelo.Participante;
import com.example.proyecto2p.modelo.Usuario;

import java.util.Collections;
import java.util.List;

public class TablaPosicionesActivity extends AppCompatActivity {

    private Usuario usuarioActivo;
    private LinearLayout contenedorParticipantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_posiciones);

        // ==========================================
        // RECUPERAR USUARIO QUE INICIÓ SESIÓN
        // ==========================================

        usuarioActivo =
                (Usuario) getIntent().getSerializableExtra("usuarioActivo");

        TextView tvNombreUsuario =
                findViewById(R.id.tvNombreUsuario);

        if (usuarioActivo != null) {
            tvNombreUsuario.setText(
                    usuarioActivo.getNombreCompleto()
            );
        }


        // ==========================================
        // CONTENEDOR DE LA TABLA
        // ==========================================

        contenedorParticipantes =
                findViewById(R.id.contenedorParticipantes);


        // ==========================================
        // CARGAR PARTICIPANTES
        // ==========================================

        cargarTablaPosiciones();


        // ==========================================
        // BOTÓN VOLVER
        // ==========================================

        findViewById(R.id.btnVolver)
                .setOnClickListener(v -> finish());
    }


    /**
     * Carga los participantes y los muestra
     * ordenados en la tabla de posiciones.
     */
    private void cargarTablaPosiciones() {

        GestorArchivos gestorArchivos = new GestorArchivos(this);

        List<Usuario> usuarios = gestorArchivos.cargarUsuarios();

        List<Participante> participantes = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            if (usuario instanceof Participante) {
                participantes.add((Participante) usuario);
            }
        }

        Collections.sort(participantes);

        int posicion = 1;

        for (Participante participante : participantes) {
            agregarFila(
                    posicion,
                    participante.getNombreCompleto(),
                    participante.getPuntajeAcumulado()
            );

            posicion++;
        }
    }


    /**
     * Agrega una fila a la tabla.
     *
     * @param posicion posición del participante
     * @param nombre nombre del participante
     * @param puntos puntaje acumulado
     */
    private void agregarFila(int posicion, String nombre, int puntos) {

        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setGravity(Gravity.CENTER_VERTICAL);
        fila.setPadding(dp(10), dp(12), dp(10), dp(12));

        // Posición
        TextView tvPosicion = new TextView(this);
        tvPosicion.setText(String.valueOf(posicion));
        tvPosicion.setTextSize(13);
        tvPosicion.setTextColor(Color.BLACK);
        tvPosicion.setGravity(Gravity.CENTER);

        tvPosicion.setLayoutParams(
                new LinearLayout.LayoutParams(
                        dp(45),
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        int colorTabla = Color.parseColor("#082B5B");

        // Nombre
        TextView tvNombre = new TextView(this);
        tvNombre.setText(nombre);
        tvNombre.setTextSize(13);
        tvNombre.setTextColor(colorTabla);

        tvNombre.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        // Puntos
        TextView tvPuntos = new TextView(this);
        tvPuntos.setText(String.valueOf(puntos));
        tvPuntos.setTextSize(13);
        tvPuntos.setTextColor(Color.BLACK);
        tvPuntos.setGravity(Gravity.END);

        tvPuntos.setLayoutParams(
                new LinearLayout.LayoutParams(dp(70), LinearLayout.LayoutParams.WRAP_CONTENT)
        );

        tvPosicion.setTypeface(null, Typeface.BOLD);
        tvNombre.setTypeface(null, Typeface.BOLD);
        tvPuntos.setTypeface(null, Typeface.BOLD);

        tvPosicion.setPadding(dp(0), 0, dp(18), 0);
        tvPuntos.setPadding(dp(0), 0, dp(18), 0);

        fila.addView(tvPosicion);
        fila.addView(tvNombre);
        fila.addView(tvPuntos);

        contenedorParticipantes.addView(fila);

        View separador = new View(this);

        separador.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(1)
                )
        );

        separador.setBackgroundColor(
                Color.parseColor("#EEEEEE")
        );

        contenedorParticipantes.addView(separador);
    }


    /**
     * Convierte dp a píxeles.
     */
    private int dp(int valor) {

        return (int) (valor * getResources().getDisplayMetrics().density);
    }
}