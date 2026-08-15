package com.example.proyecto2p.modelo;

/***
 * Representa las fases del torneo del Mundial 2026.
 *
 * <p>Cada constante guarda además un nombre legible ({@link #getNombreVisible()})
 * para mostrarlo directamente en Spinners, encabezados y tarjetas de la
 * interfaz, sin tener que mantener arreglos de texto duplicados en cada
 * Activity.</p>
 */
public enum FaseTorneo {
    FASE_GRUPOS("Fase de grupos"),
    DIECISEISAVOS("Dieciseisavos"),
    OCTAVOS("Octavos"),
    CUARTOS("Cuartos de final"),
    SEMIFINALES("Semifinales"),
    TERCER_LUGAR("Tercer lugar"),
    FINAL("Final");

    private final String nombreVisible;

    FaseTorneo(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    /***
     * Obtiene el nombre de la fase tal como debe mostrarse al usuario.
     *
     * @return nombre legible de la fase, por ejemplo "Cuartos de final".
     */
    public String getNombreVisible() {
        return nombreVisible;
    }
}
