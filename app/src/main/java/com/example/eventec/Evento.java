package com.example.eventec;

import java.io.Serializable;

public class Evento implements Serializable {
    private String nombre;
    private String categoria;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String horaInicio;
    private String horaFin;
    private String lugar;
    private String requisitosEspeciales;

    public Evento() {
        // Constructor vacío requerido por Firebase
    }

    public Evento(String nombre, String categoria, String descripcion, String fechaInicio, String fechaFin, String horaInicio, String horaFin, String lugar, String requisitosEspeciales) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.lugar = lugar;
        this.requisitosEspeciales = requisitosEspeciales;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public String getLugar() {
        return lugar;
    }

    public String getRequisitosEspeciales() {
        return requisitosEspeciales;
    }

    // Agrega getters y setters adicionales si es necesario
}

class EventoCalificar extends Evento implements Serializable {
    private String capacidad;

    public EventoCalificar(String nombre, String fechaInicio, String horaInicio, String horaFin, String capacidad) {
        super(nombre, null, null, fechaInicio, null, horaInicio, horaFin, null, null);
        this.capacidad = capacidad;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }
}

