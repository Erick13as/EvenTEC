package com.example.eventec;
import java.io.Serializable;

public class User implements Serializable {
    private String nombre;
    private String apellido;
    private String apellido2;
    private String carnet;
    private String contraseña;
    private String correo;
    private String carrera;
    private String sede;
    private String descripcion;

    private String idTipo;


    public User() {
        // Default constructor required for Firebase
    }


    public User(String nombre, String apellido, String apellido2, String carnet, String contraseña, String correo, String carrera, String sede, String descripcion, String idTipo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.apellido2 = apellido2;
        this.carnet = carnet;
        this.contraseña = contraseña;
        this.correo = correo;
        this.carrera = carrera;
        this.sede = sede;
        this.descripcion = descripcion;
        this.idTipo = idTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getCarnet() {
        return carnet;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getCorreo() {
        return correo;
    }
    public String getCarrera() {
        return carrera;
    }
    public String getSede() {
        return sede;
    }
    public String getDescripcion() {
        return descripcion;
    }

    public String getIdTipo() {
        return idTipo;
    }
}


