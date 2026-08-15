package com.example.proyecto2p;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyecto2p.modelo.Usuario;

/**
 * Actividad que muestra el menú principal del administrador una vez que
 * ha iniciado sesión correctamente.
 *
 * <p>Presenta el nombre del administrador autenticado en la cabecera y las
 * opciones disponibles para su rol: administrar los partidos del torneo,
 * actualizar los puntajes de los participantes, y salir de la
 * aplicación.</p>
 *
 * @author Tu Nombre/Grupo
 * @version 1.0
 */
public class MenuAdministradorActivity extends AppCompatActivity {

    /** Usuario que inició sesión, recibido desde {@link MainActivity}. */
    private Usuario usuarioActivo;

    /**
     * Método invocado cuando se crea la actividad.
     * Recupera al administrador autenticado enviado por {@link MainActivity}
     * a través del extra {@code "usuarioActivo"}, muestra su nombre completo
     * en la cabecera, y configura los listeners de cada opción del menú.
     *
     * @param savedInstanceState Estado de la actividad previamente guardado (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_administrador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recuperamos el usuario que envió MainActivity al iniciar sesión
        usuarioActivo = (Usuario) getIntent().getSerializableExtra("usuarioActivo");

        // Mostramos el nombre del administrador en la cabecera
        TextView tvNombre = findViewById(R.id.tvNombreUsuario);
        if (usuarioActivo != null) {
            tvNombre.setText(usuarioActivo.getNombreCompleto());
        }

        configurarOpcionesMenu();
    }

    /**
     * Asocia el listener de clic a cada una de las opciones del menú del
     * administrador: Administrar partidos, Actualizar puntajes y Salir.
     *
     * <p>Las opciones que todavía no tienen una pantalla asociada (partes
     * 6 y 7 del proyecto) muestran un mensaje temporal mediante
     * {@link Toast#makeText}; cuando se construya cada actividad, basta con
     * reemplazar ese Toast por el {@code Intent} correspondiente (ya
     * dejado comentado como referencia).</p>
     */
    private void configurarOpcionesMenu() {

        // Administrar partidos (se conecta en la parte 6)
        findViewById(R.id.btnAdministrarPartidos).setOnClickListener(v -> {
            Toast.makeText(this, "Próximamente: Administrar partidos", Toast.LENGTH_SHORT).show();
            // TODO parte 6:
            // Intent intent = new Intent(this, AdministrarPartidosActivity.class);
            // startActivity(intent);
        });

        // Actualizar puntajes (se conecta en la parte 7)
        findViewById(R.id.btnActualizarPuntajes).setOnClickListener(v -> {
            Toast.makeText(this, "Próximamente: Actualizar puntajes", Toast.LENGTH_SHORT).show();
            // TODO parte 7:
            // Intent intent = new Intent(this, ActualizarPuntajesActivity.class);
            // startActivity(intent);
        });

        // Salir: termina la aplicación por completo, como pide el enunciado
        findViewById(R.id.btnSalir).setOnClickListener(v -> finishAffinity());
    }
}
