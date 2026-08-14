package com.example.proyecto2p.modelo;

public class Partido {

    private String idPartido;
    private String fecha;
    private String hora;
    private String estadio;
    private String seleccion1;
    private String seleccion2;
    private FaseTorneo fase;
    private EstadoPartido estado;

    /***
     * Constructor de la clase Partido
     */
    public Partido(String idPartido, String fecha, String hora,
                   String estadio, String seleccion1,
                   String seleccion2, FaseTorneo fase,
                   EstadoPartido estado) {
        this.idPartido = idPartido;
        this.fecha = fecha;
        this.hora = hora;
        this.estadio = estadio;
        this.seleccion1 = seleccion1;
        this.seleccion2 = seleccion2;
        this.fase = fase;
        this.estado = estado;
    }

    /***
     * Getters y Setters de los atributos de la clase Partido
     */

    public String getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(String idPartido) {
        this.idPartido = idPartido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstadio() {
        return estadio;
    }

    public void setEstadio(String estadio) {
        this.estadio = estadio;
    }

    public String getSeleccion1() {
        return seleccion1;
    }

    public void setSeleccion1(String seleccion1) {
        this.seleccion1 = seleccion1;
    }

    public String getSeleccion2() {
        return seleccion2;
    }

    public void setSeleccion2(String seleccion2) {
        this.seleccion2 = seleccion2;
    }

    public FaseTorneo getFase() {
        return fase;
    }

    public void setFase(FaseTorneo fase) {
        this.fase = fase;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }


    /***
     * Devuelve una representación en texto del partido,
     * incluyendo todos sus atributos. Útil para depuración
     * y para mostrar información rápida en consola.
     *
     * @return cadena con los datos del partido.
     */
    @Override
    public String toString() {
        return "Partido{" +
                "idPartido=" + idPartido +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", estadio='" + estadio + '\'' +
                ", seleccion1='" + seleccion1 + '\'' +
                ", seleccion2='" + seleccion2 + '\'' +
                ", fase=" + fase +
                ", estado=" + estado +
                '}';
    }

    /***
     * Cierra la ventana de pronósticos para este partido.
     * Cambia el estado del partido a CERRADO, impidiendo que los
     * participantes sigan registrando o modificando predicciones
     */
    public void cerrarPronosticos() {
        this.estado = EstadoPartido.CERRADO;
    }

    /***
     * Marca el partido como finalizado una vez que ya se jugó
     * y se conoce el resultado real. Cambia el estado a FINALIZADO.
     */
    public void finalizarPartido() {
        this.estado = EstadoPartido.FINALIZADO;
    }

    /***
     * Indica si actualmente se pueden registrar pronósticos
     * para este partido.
     *
     * @return true si el partido está en estado ABIERTO, false en caso contrario.
     */
    public boolean permitePronosticos() {
        return estaAbierto();
    }

    /***
     * Verifica si el partido se encuentra en estado ABIERTO,
     * es decir, si aún no ha comenzado y acepta pronósticos.
     *
     * @return true si el estado actual es ABIERTO.
     */
    public boolean estaAbierto() {
        return this.estado == EstadoPartido.ABIERTO;
    }

    /***
     * Verifica si el partido se encuentra en estado CERRADO,
     * es decir, ya no acepta nuevos pronósticos, pero aún no finaliza.
     *
     * @return true si el estado actual es CERRADO.
     */
    public boolean estaCerrado() {
        return this.estado == EstadoPartido.CERRADO;
    }

    /***
     * Verifica si el partido se encuentra en estado FINALIZADO,
     * es decir, ya se jugó y se conoce el resultado.
     *
     * @return true si el estado actual es FINALIZADO.
     */
    public boolean estaFinalizado() {
        return this.estado == EstadoPartido.FINALIZADO;
    }
}
