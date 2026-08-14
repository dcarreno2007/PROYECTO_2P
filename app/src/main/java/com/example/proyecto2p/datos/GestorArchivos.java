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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de la persistencia de datos del sistema.
 * Lee archivos de solo lectura desde assets/ y escribe/lee
 * archivos editables desde el almacenamiento interno de la app.
 */
public class GestorArchivos {

    private Context context;

    public GestorArchivos(Context context) {
        this.context = context;
    }

    // ================= Usuarios (combinados) =================

    /**
     * Carga todos los usuarios del sistema (participantes + administradores)
     * como una lista polimórfica de Usuario.
     */
    public List<Usuario> cargarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.addAll(cargarParticipantes());
        usuarios.addAll(cargarAdministradores());
        return usuarios;
    }

    // ================= Participantes =================

    /**
     * Formato esperado de participantes.txt:
     * idUsuario|nombreUsuario|contrasena|nombreCompleto|puntajeAcumulado
     */
    public List<Participante> cargarParticipantes() {
        List<Participante> lista = new ArrayList<>();

        try (InputStream is = context.getAssets().open("participantes.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split("\\|");

                String idUsuario = datos[0];
                String nombreUsuario = datos[1];
                String contrasena = datos[2];
                String nombreCompleto = datos[3];
                int puntajeAcumulado = Integer.parseInt(datos[4]);

                Participante p = new Participante(idUsuario, nombreUsuario, contrasena, nombreCompleto, TipoUsuario.PARTICIPANTE, puntajeAcumulado);
                lista.add(p);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Sobrescribe participantes.txt en almacenamiento interno con la lista actualizada.
     * (No se puede escribir directamente en assets/, solo leer).
     */
    public void guardarParticipantes(List<Participante> participantes) {
        try (FileWriter writer = new FileWriter(context.getFilesDir() + "/participantes.txt")) {

            writer.write("idUsuario|nombreUsuario|contrasena|nombreCompleto|puntajeAcumulado\n");

            for (Participante p : participantes) {
                writer.write(p.getIdUsuario() + "|" +
                        p.getNombreUsuario() + "|" +
                        p.getContrasena() + "|" +
                        p.getNombreCompleto() + "|" +
                        p.getPuntajeAcumulado() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= ADMINISTRADORES =================

    /**
     * Formato esperado de administradores.txt:
     * idUsuario|nombreUsuario|contrasena|nombreCompleto|cargo
     */
    public List<Administrador> cargarAdministradores() {
        List<Administrador> lista = new ArrayList<>();

        try (InputStream is = context.getAssets().open("administradores.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split("\\|");

                String idUsuario = datos[0];
                String nombreUsuario = datos[1];
                String contrasena = datos[2];
                String nombreCompleto = datos[3];
                String cargo = datos[4];

                Administrador a = new Administrador(idUsuario, nombreUsuario, contrasena, nombreCompleto, TipoUsuario.ADMINISTRADOR, cargo);
                lista.add(a);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ================= PARTIDOS =================

    /**
     * Formato esperado de partidos.txt:
     * idPartido|fecha|hora|estadio|seleccion1|seleccion2|fase|estado
     */
    public List<Partido> cargarPartidos() {
        List<Partido> lista = new ArrayList<>();

        try (InputStream is = context.getAssets().open("partidos.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split("\\|");

                String idPartido = datos[0];
                String fecha = datos[1];
                String hora = datos[2];
                String estadio = datos[3];
                String seleccion1 = datos[4];
                String seleccion2 = datos[5];
                FaseTorneo fase = FaseTorneo.valueOf(datos[6]);
                EstadoPartido estado = EstadoPartido.valueOf(datos[7]);

                Partido partido = new Partido(idPartido, fecha, hora, estadio, seleccion1, seleccion2, fase, estado);
                lista.add(partido);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void guardarPartidos(List<Partido> partidos) {
        try (FileWriter writer = new FileWriter(context.getFilesDir() + "/partidos.txt")) {

            writer.write("idPartido|fecha|hora|estadio|seleccion1|seleccion2|fase|estado\n");

            for (Partido p : partidos) {
                writer.write(p.getIdPartido() + "|" +
                        p.getFecha() + "|" +
                        p.getHora() + "|" +
                        p.getEstadio() + "|" +
                        p.getSeleccion1() + "|" +
                        p.getSeleccion2() + "|" +
                        p.getFase() + "|" +
                        p.getEstado() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= Resultados =================

    /**
     * Formato esperado de resultados.txt:
     * idResultado|idPartido|golesSeleccion1|golesSeleccion2
     */
    public List<Resultado> cargarResultados() {
        List<Resultado> lista = new ArrayList<>();

        try (InputStream is = context.getAssets().open("resultados.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split("\\|");

                String idResultado = datos[0];
                String idPartido = datos[1];
                int golesSeleccion1 = Integer.parseInt(datos[2]);
                int golesSeleccion2 = Integer.parseInt(datos[3]);

                Resultado r = new Resultado(idResultado, idPartido, golesSeleccion1, golesSeleccion2);
                lista.add(r);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void guardarResultados(List<Resultado> resultados) {
        try (FileWriter writer = new FileWriter(context.getFilesDir() + "/resultados.txt")) {

            writer.write("idResultado|idPartido|golesSeleccion1|golesSeleccion2\n");

            for (Resultado r : resultados) {
                writer.write(r.getIdResultado() + "|" +
                        r.getIdPartido() + "|" +
                        r.getGolesSeleccion1() + "|" +
                        r.getGolesSeleccion2() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= PRONÓSTICOS =================

    /**
     * Formato esperado de pronosticos.txt:
     * idPronostico|idUsuario|idPartido|golesSeleccion1|golesSeleccion2|puntosObtenidos|fase
     * Filtra solo los pronósticos del usuario y fase indicados.
     */
    public List<Pronostico> cargarPronosticos(String idUsuario, FaseTorneo fase) {
        List<Pronostico> lista = new ArrayList<>();

        try (InputStream is = context.getAssets().open("pronosticos.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split("\\|");

                String idPronostico = datos[0];
                String idUsuarioLinea = datos[1];
                String idPartido = datos[2];
                int golesSeleccion1 = Integer.parseInt(datos[3]);
                int golesSeleccion2 = Integer.parseInt(datos[4]);
                int puntosObtenidos = Integer.parseInt(datos[5]);
                FaseTorneo faseLinea = FaseTorneo.valueOf(datos[6]);

                if (idUsuarioLinea.equals(idUsuario) && faseLinea == fase) {
                    Pronostico pr = new Pronostico(idPronostico, idUsuarioLinea, idPartido, golesSeleccion1, golesSeleccion2);
                    pr.setPuntosObtenidos(puntosObtenidos);
                    lista.add(pr);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void guardarPronosticos(List<Pronostico> pronosticos, String idUsuario, FaseTorneo fase) {
        try (FileWriter writer = new FileWriter(context.getFilesDir() + "/pronosticos.txt")) {

            writer.write("idPronostico|idUsuario|idPartido|golesSeleccion1|golesSeleccion2|puntosObtenidos|fase\n");

            for (Pronostico p : pronosticos) {
                writer.write(p.getIdPronostico() + "|" +
                        p.getIdUsuario() + "|" +
                        p.getIdPartido() + "|" +
                        p.getGolesSeleccion1() + "|" +
                        p.getGolesSeleccion2() + "|" +
                        p.getPuntosObtenidos() + "|" +
                        fase + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
