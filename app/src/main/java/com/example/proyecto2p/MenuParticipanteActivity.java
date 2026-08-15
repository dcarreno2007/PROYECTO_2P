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
 * Actividad que muestra el menú principal del participante una vez que
 * ha iniciado sesión correctamente.
 *
 * <p>Presenta el nombre del participante autenticado en la cabecera y las
 * opciones disponibles para su rol: consultar la tabla de posiciones,
 * registrar pronósticos, revisar sus propios pronósticos, y salir de la
 * aplicación.</p>
 *
 * @author Tu Nombre/Grupo
 * @version 1.0
 */
public class MenuParticipanteActivity extends AppCompatActivity {

    /** Usuario que inició sesión, recibido desde {@link MainActivity}. */
    private Usuario usuarioActivo;

    /**
     * Método invocado cuando se crea la actividad.
     * Recupera al usuario autenticado enviado por {@link MainActivity} a
     * través del extra {@code "usuarioActivo"}, muestra su nombre completo
     * en la cabecera, y configura los listeners de cada opción del menú.
     *
     * @param savedInstanceState Estado de la actividad previamente guardado (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_participante);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recuperamos el usuario que envió MainActivity al iniciar sesión
        usuarioActivo = (Usuario) getIntent().getSerializableExtra("usuarioActivo");

        // Mostramos el nombre del participante en la cabecera
        TextView tvNombre = findViewById(R.id.tvNombreUsuario);
        if (usuarioActivo != null) {
            tvNombre.setText(usuarioActivo.getNombreCompleto());
        }

        configurarOpcionesMenu();
    }

    /**
     * Asocia el listener de clic a cada una de las opciones del menú del
     * participante: Tabla de posiciones, Pronósticos, Mis pronósticos y
     * Salir.
     *
     * <p>Las opciones que todavía no tienen una pantalla asociada (partes
     * 3, 4 y 5 del proyecto) muestran un mensaje temporal mediante
     * {@link Toast#makeText}; cuando se construya cada actividad, basta con
     * reemplazar ese Toast por el {@code Intent} correspondiente (ya
     * dejado comentado como referencia).</p>
     */
    private void configurarOpcionesMenu() {

        // Tabla de posiciones (se conecta en la parte 3)
        findViewById(R.id.btnTablaPosiciones).setOnClickListener(v -> {
            Toast.makeText(this, "Próximamente: Tabla de posiciones", Toast.LENGTH_SHORT).show();
            // TODO parte 3:
            // Intent intent = new Intent(this, TablaPosicionesActivity.class);
            // startActivity(intent);
        });

        // Pronósticos (se conecta en la parte 4)
        findViewById(R.id.btnPronosticos).setOnClickListener(v -> {
            Toast.makeText(this, "Próximamente: Pronósticos", Toast.LENGTH_SHORT).show();
            // TODO parte 4:
            // Intent intent = new Intent(this, PronosticosActivity.class);
            // intent.putExtra("usuarioActivo", usuarioActivo);
            // startActivity(intent);
        });

        // Mis pronósticos (se conecta en la parte 5)
        findViewById(R.id.btnMisPronosticos).setOnClickListener(v -> {
            Toast.makeText(this, "Próximamente: Mis pronósticos", Toast.LENGTH_SHORT).show();
            // TODO parte 5:
            // Intent intent = new Intent(this, MisPronosticosActivity.class);
            // intent.putExtra("usuarioActivo", usuarioActivo);
            // startActivity(intent);
        });

        // Salir: termina la aplicación por completo, como pide el enunciado
        findViewById(R.id.btnSalir).setOnClickListener(v -> finishAffinity());
    }
}
