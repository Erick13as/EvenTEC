package com.example.eventec;

public class Actividad {
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String idEvento;
    private String ubicacion;
    private String descripcion;
    private String recursos;

    // Constructor vacío requerido para Firestore
    public Actividad() {
    }

    public Actividad(String fecha, String horaInicio, String horaFin, String idEvento, String ubicacion, String descripcion, String recursos) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.idEvento = idEvento;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.recursos = recursos;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRecursos() {
        return recursos;
    }
}

