package com.example.proyecto2p; // Confirma que este sea el nombre correcto de tu paquete

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Asegúrate de que las rutas a tus clases sean correctas
import com.example.proyecto2p.excepciones.CredencialesInvalidasException;
import com.example.proyecto2p.modelo.Usuario;
import com.example.proyecto2p.modelo.TipoUsuario; // O tipoUsuario en minúsculas, dependiendo de cómo lo llamaste

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etContrasena;
    private Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Enlazamos con los IDs exactos de tu diseño XML
        etUsuario = findViewById(R.id.editText_usuario);
        etContrasena = findViewById(R.id.editText_contrasenia);
        btnIniciarSesion = findViewById(R.id.btn_iniciarSesion);

        // 2. Lógica para mostrar/ocultar contraseña con el "ojito"
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

        // 3. Acción del botón de iniciar sesión
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    private void iniciarSesion() {
        String usuarioIngresado = etUsuario.getText().toString().trim();
        String contrasenaIngresada = etContrasena.getText().toString().trim();

        if (usuarioIngresado.isEmpty() || contrasenaIngresada.isEmpty()) {
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Buscamos al usuario en la base de datos
            Usuario usuarioAutenticado = validarCredenciales(usuarioIngresado, contrasenaIngresada);

            // Si lo encuentra, evalúa el tipo de usuario y lo envía a su menú[cite: 7]
            if (usuarioAutenticado.getTipoUsuario() == TipoUsuario.PARTICIPANTE) {
                Intent intent = new Intent(MainActivity.this, MenuParticipanteActivity.class);
                intent.putExtra("usuarioActivo", usuarioAutenticado);
                startActivity(intent);
            } else if (usuarioAutenticado.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                Intent intent = new Intent(MainActivity.this, MenuAdministradorActivity.class);
                intent.putExtra("usuarioActivo", usuarioAutenticado);
                startActivity(intent);
            }

            finish(); // Destruye esta pantalla para que no puedan volver con la flecha atrás

        } catch (CredencialesInvalidasException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Usuario validarCredenciales(String user, String pass) throws CredencialesInvalidasException {
        // Leemos el archivo almacenado en la carpeta assets[cite: 3]
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("usuarios.txt")))) {
            String linea;

            // Saltamos la primera línea porque es la cabecera (idUsuario;nombreUsuario...)[cite: 7]
            reader.readLine();

            while ((linea = reader.readLine()) != null) {
                // Separamos los datos por punto y coma[cite: 7]
                String[] datos = linea.split(";");

                if (datos.length >= 5) {
                    String archivoUsuario = datos[1].trim();
                    String archivoContrasena = datos[2].trim();

                    // Si el usuario y contraseña coinciden, construimos el objeto
                    if (archivoUsuario.equals(user) && archivoContrasena.equals(pass)) {
                        String id = datos[0].trim();
                        String nombreCompleto = datos[3].trim();
                        TipoUsuario tipo = TipoUsuario.valueOf(datos[4].trim().toUpperCase());

                        return new Usuario(id, archivoUsuario, archivoContrasena, nombreCompleto, tipo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al leer la base de datos de usuarios", Toast.LENGTH_SHORT).show();
        }

        // Si recorrió todo el archivo y no encontró al usuario, lanza la excepción
        throw new CredencialesInvalidasException("El usuario o la contraseña son incorrectos.");
    }
}