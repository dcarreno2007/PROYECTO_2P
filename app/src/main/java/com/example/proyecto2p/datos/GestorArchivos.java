package com.example.proyecto2p.datos;

import android.content.Context;

import com.example.proyecto2p.modelo.Administrador;
import com.example.proyecto2p.modelo.EstadoPartido;
import com.example.proyecto2p.modelo.FaseTorneo;
import com.example.proyecto2p.modelo.Participante;
import com.example.proyecto2p.modelo.Partido;
import com.example.proyecto2p.modelo.Pronostico;
import com.example.proyecto2p.modelo.Resultado;
import com.example.proyecto2p.modelo.Usuario;
import com.example.proyecto2p.modelo.TipoUsuario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase encargada de la persistencia de datos del sistema.
 * Gestiona la lectura estática desde la carpeta assets y la escritura dinámica en el almacenamiento interno.
 */
public class GestorArchivos {

    private Context context;

    /**
     * Constructor de la clase GestorArchivos.
     * Al instanciar, verifica y copia los archivos modificables (participantes.txt,
     * partidos.txt y resultados.txt) desde la carpeta assets hacia el almacenamiento interno
     * para permitir su posterior escritura y actualización.
     *
     * @param context Contexto de la aplicación necesario para acceder a los recursos del sistema.
     */
    public GestorArchivos(Context context) {
        this.context = context;
        copiarArchivoInicial("participantes.txt");
        copiarArchivoInicial("partidos.txt");
        copiarArchivoInicial("resultados.txt");
    }

    /**
     * Copia un archivo desde la carpeta assets al almacenamiento interno privado de la aplicación
     * si este aún no existe en dicho directorio, preparando el entorno para archivos de salida.
     *
     * @param nombreArchivo Nombre del archivo de texto con su extensión (ej. "partidos.txt").
     */
    private void copiarArchivoInicial(String nombreArchivo) {
        File archivo = new File(context.getFilesDir(), nombreArchivo);
        if (!archivo.exists()) {
            try (InputStream entrada = context.getAssets().open(nombreArchivo);
                 OutputStream salida = context.openFileOutput(nombreArchivo, Context.MODE_PRIVATE)) {

                byte[] buffer = new byte[1024];
                int cantidadBytes;
                while ((cantidadBytes = entrada.read(buffer)) != -1) {
                    salida.write(buffer, 0, cantidadBytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Carga todos los usuarios del sistema leyendo el archivo principal usuarios.txt desde assets
     * (de solo lectura) y combinándolo con los datos específicos de participantes.txt y
     * administradores.txt.
     * Utiliza el punto y coma (;) como separador y valida que existan al menos 5 datos por línea para evitar excepciones.
     *
     * @return Lista polimórfica de objetos de tipo Usuario (Participantes y Administradores).
     */
    public List<Usuario> cargarUsuarios() {
        List<Usuario> listaUsuarios = new ArrayList<>();
        Map<String, Integer> puntajes = cargarPuntajesParticipantes();
        Map<String, String> cargos = cargarCargosAdministradores();

        try (InputStream is = context.getAssets().open("usuarios.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                // Se omite la cabecera del archivo de texto
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(";");

                if (datos.length >= 5) {
                    String idUsuario = datos[0].trim();
                    String nombreUsuario = datos[1].trim();
                    String contrasena = datos[2].trim();
                    String nombreCompleto = datos[3].trim();

                    TipoUsuario tipo = TipoUsuario.valueOf(datos[4].trim().toUpperCase());

                    if (tipo == TipoUsuario.PARTICIPANTE) {
                        int puntaje = puntajes.containsKey(idUsuario) ? puntajes.get(idUsuario) : 0;
                        listaUsuarios.add(new Participante(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipo, puntaje));
                    } else if (tipo == TipoUsuario.ADMINISTRADOR) {
                        String cargo = cargos.containsKey(idUsuario) ? cargos.get(idUsuario) : "Desconocido";
                        listaUsuarios.add(new Administrador(idUsuario, nombreUsuario, contrasena, nombreCompleto, tipo, cargo));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return listaUsuarios;
    }

    /**
     * Lee los puntajes de los participantes desde el almacenamiento interno, ya que
     * este archivo se modifica constantemente durante la ejecución del torneo.
     * Emplea el separador original de punto y coma.
     *
     * @return Mapa que asocia el ID del participante con su respectivo puntaje acumulado.
     */
    private Map<String, Integer> cargarPuntajesParticipantes() {
        Map<String, Integer> mapaPuntajes = new HashMap<>();

        try (FileInputStream fis = context.openFileInput("participantes.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(";");
                if (datos.length >= 2) {
                    mapaPuntajes.put(datos[0].trim(), Integer.parseInt(datos[1].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapaPuntajes;
    }

    /**
     * Lee los cargos asignados a los administradores desde el archivo estático en assets,
     * ya que esta información no se modifica por la aplicación.
     *
     * @return Mapa que asocia el ID del administrador con su cargo asignado.
     */
    private Map<String, String> cargarCargosAdministradores() {
        Map<String, String> mapaCargos = new HashMap<>();

        try (InputStream is = context.getAssets().open("administradores.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(";");
                if (datos.length >= 2) {
                    mapaCargos.put(datos[0].trim(), datos[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapaCargos;
    }

    /**
     * Persiste la lista actualizada de participantes en el almacenamiento interno de la app.
     * Mantiene intacta la cabecera original del archivo y separa los datos mediante punto y coma.
     *
     * @param participantes Lista de objetos Participante cuyos puntajes han sido recalculados.
     */
    public void guardarParticipantes(List<Participante> participantes) {
        try (FileOutputStream fos = context.openFileOutput("participantes.txt", Context.MODE_PRIVATE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {

            writer.write("idUsuario;puntajeAcumulado\n");

            for (Participante p : participantes) {
                writer.write(p.getIdUsuario() + ";" + p.getPuntajeAcumulado() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga la información del fixture de partidos leyendo desde el almacenamiento interno privado.
     * Instancia objetos de la clase Partido interpretando el estado y la fase del torneo.
     *
     * @return Lista completa de objetos Partido registrados en el sistema.
     */
    public List<Partido> cargarPartidos() {
        List<Partido> lista = new ArrayList<>();

        try (FileInputStream fis = context.openFileInput("partidos.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(";");

                if(datos.length >= 8) {
                    String idPartido = datos[0].trim();
                    FaseTorneo fase = FaseTorneo.valueOf(datos[1].trim().toUpperCase());
                    String fecha = datos[2].trim();
                    String hora = datos[3].trim();
                    String estadio = datos[4].trim();
                    String seleccion1 = datos[5].trim();
                    String seleccion2 = datos[6].trim();
                    EstadoPartido estado = EstadoPartido.valueOf(datos[7].trim().toUpperCase());

                    Partido partido = new Partido(idPartido, fecha, hora, estadio, seleccion1, seleccion2, fase, estado);
                    lista.add(partido);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Actualiza y sobrescribe el archivo de partidos en el almacenamiento interno para reflejar
     * cambios en sus estados (ABIERTO, CERRADO, FINALIZADO).
     *
     * @param partidos Lista actualizada de objetos Partido.
     */
    public void guardarPartidos(List<Partido> partidos) {
        try (FileOutputStream fos = context.openFileOutput("partidos.txt", Context.MODE_PRIVATE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {

            writer.write("idPartido;fase;fecha;horaUTC;estadio;seleccion1;seleccion2;estado\n");

            for (Partido p : partidos) {
                writer.write(p.getIdPartido() + ";" +
                        p.getFase() + ";" +
                        p.getFecha() + ";" +
                        p.getHora() + ";" +
                        p.getEstadio() + ";" +
                        p.getSeleccion1() + ";" +
                        p.getSeleccion2() + ";" +
                        p.getEstado() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Consulta los resultados oficiales de los partidos que ya han finalizado desde
     * el archivo alojado en el almacenamiento interno.
     *
     * @return Lista de objetos Resultado conteniendo marcadores exactos.
     */
    public List<Resultado> cargarResultados() {
        List<Resultado> lista = new ArrayList<>();

        try (FileInputStream fis = context.openFileInput("resultados.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(";");

                if(datos.length >= 4) {
                    String idResultado = datos[0].trim();
                    String idPartido = datos[1].trim();
                    int golesSeleccion1 = Integer.parseInt(datos[2].trim());
                    int golesSeleccion2 = Integer.parseInt(datos[3].trim());

                    Resultado r = new Resultado(idResultado, idPartido, golesSeleccion1, golesSeleccion2);
                    lista.add(r);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Almacena de manera permanente los marcadores oficiales de los partidos finalizados.
     * Consolida los datos en el almacenamiento interno bajo el formato original.
     *
     * @param resultados Lista de marcadores que se grabarán en el archivo resultados.txt.
     */
    public void guardarResultados(List<Resultado> resultados) {
        try (FileOutputStream fos = context.openFileOutput("resultados.txt", Context.MODE_PRIVATE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {

            writer.write("idResultado;idPartido;golesSeleccion1;golesSeleccion2\n");

            for (Resultado r : resultados) {
                writer.write(r.getIdResultado() + ";" +
                        r.getIdPartido() + ";" +
                        r.getGolesSeleccion1() + ";" +
                        r.getGolesSeleccion2() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}