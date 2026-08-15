package com.example.proyecto2p;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.annotation.SuppressLint;

import com.example.proyecto2p.datos.GestorArchivos;
import com.example.proyecto2p.excepciones.CredencialesInvalidasException;
import com.example.proyecto2p.modelo.Usuario;
import com.example.proyecto2p.modelo.TipoUsuario;

import java.util.List;

/**
 * Actividad principal de la aplicación que gestiona el inicio de sesión.
 * Permite a los usuarios.txt autenticarse y los redirige a su menú correspondiente
 * dependiendo de si son Participantes o Administradores.
 *
 */
public class MainActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etContrasena;
    private Button btnIniciarSesion;

    /**
     * Método invocado cuando se crea la actividad.
     * Se encarga de inicializar los componentes de la interfaz gráfica y
     * configurar los eventos de interacción (Listeners).
     *
     * @param savedInstanceState Estado de la actividad previamente guardado (si existe).
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etUsuario = findViewById(R.id.editText_usuario);
        etContrasena = findViewById(R.id.editText_contrasenia);
        btnIniciarSesion = findViewById(R.id.btn_iniciarSesion);

        etContrasena.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etContrasena.getRight() - etContrasena.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() - 50)) {

                        // Si la contraseña está oculta, la mostramos
                        if (etContrasena.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                            etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etContrasena.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_candado, 0, R.drawable.ic_ojo_cerrado, 0);
                        }
                        // Si la contraseña está visible, la ocultamos
                        else {
                            etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etContrasena.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_candado, 0, R.drawable.ic_ojo, 0);
                        }
                        // Mantiene el cursor al final del texto
                        etContrasena.setSelection(etContrasena.getText().length());
                        return true;
                    }
                }
                return false;
            }
        });


        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    /**
     * Extrae los datos ingresados en los campos de texto, realiza validaciones
     * de campos vacíos y gestiona el flujo de autenticación del usuario.
     * Dependiendo del rol del usuario autenticado, inicia la actividad correspondiente.
     */
    private void iniciarSesion() {
        String usuarioIngresado = etUsuario.getText().toString().trim();
        String contrasenaIngresada = etContrasena.getText().toString().trim();

        if (usuarioIngresado.isEmpty() || contrasenaIngresada.isEmpty()) {
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            Usuario usuarioAutenticado = validarCredenciales(usuarioIngresado, contrasenaIngresada);


            if (usuarioAutenticado.getTipoUsuario() == TipoUsuario.PARTICIPANTE) {
                Intent intent = new Intent(MainActivity.this, MenuParticipanteActivity.class);
                intent.putExtra("usuarioActivo", usuarioAutenticado);
                startActivity(intent);
            } else if (usuarioAutenticado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                Intent intent = new Intent(MainActivity.this, MenuAdministradorActivity.class);
                intent.putExtra("usuarioActivo", usuarioAutenticado);
                startActivity(intent);
            }

            finish();

        } catch (CredencialesInvalidasException e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Valida las credenciales ingresadas comparándolas con los registros cargados
     * por el GestorArchivos.
     *
     * @param user Nombre de usuario ingresado en la interfaz.
     * @param pass Contraseña ingresada en la interfaz.
     * @return El objeto {@link Usuario} que coincide con las credenciales ingresadas.
     * @throws CredencialesInvalidasException Si el usuario no existe o la contraseña no coincide.
     */
    private Usuario validarCredenciales(String user, String pass) throws CredencialesInvalidasException {
        GestorArchivos gestor = new GestorArchivos(this);
        List<Usuario> listaUsuarios = gestor.cargarUsuarios();

        for (Usuario u : listaUsuarios) {
            if (u.getNombreUsuario().equals(user) && u.getContrasena().equals(pass)) {
                return u;
            }
        }

        throw new CredencialesInvalidasException("El usuario o la contraseña son incorrectos.");
    }
}