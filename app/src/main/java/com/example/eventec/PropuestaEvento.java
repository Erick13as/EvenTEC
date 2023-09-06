package com.example.eventec;

public class PropuestaEvento {
    private String PropuestaTematica;
    private String PropuestaObjetivo;
    private String PropuestaActividad;
    private String PropuestaFecha;
    private String PropuestaCantidad;

    public PropuestaEvento() {
        // Default constructor required for Firebase
    }
    public PropuestaEvento(String tematica, String objetivo, String actividad, String fecha, String cantidad) {
        this.PropuestaTematica=tematica;
        this.PropuestaObjetivo=objetivo;
        this.PropuestaActividad=actividad;
        this.PropuestaFecha=fecha;
        this.PropuestaCantidad=cantidad;
    }

    public String getPropuestaTematica() {
        return PropuestaTematica;
    }

    public void setPropuestaTematica(String propuestaTematica) {
        PropuestaTematica = propuestaTematica;
    }

    public String getPropuestaObjetivo() {
        return PropuestaObjetivo;
    }

    public void setPropuestaObjetivo(String propuestaObjetivo) {
        PropuestaObjetivo = propuestaObjetivo;
    }

    public String getPropuestaActividad() {
        return PropuestaActividad;
    }

    public void setPropuestaActividad(String propuestaActividad) {
        PropuestaActividad = propuestaActividad;
    }

    public String getPropuestaFecha() {
        return PropuestaFecha;
    }

    public void setPropuestaFecha(String propuestaFecha) {
        PropuestaFecha = propuestaFecha;
    }

    public String getPropuestaCantidad() {
        return PropuestaCantidad;
    }

    public void setPropuestaCantidad(String propuestaCantidad) {
        PropuestaCantidad = propuestaCantidad;
    }
}
