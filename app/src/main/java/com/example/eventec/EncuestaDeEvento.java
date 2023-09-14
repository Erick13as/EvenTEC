package com.example.eventec;

import java.io.Serializable;

public class EncuestaDeEvento implements Serializable {
    private String nombreEvento;
    private String respuesta1;
    private String respuesta2;
    private String respuesta3;
    private String respuesta4;

    public EncuestaDeEvento() {
        // Default constructor required for Firebase
    }
    public EncuestaDeEvento(String nombre,String pregunta1, String pregunta2, String pregunta3, String pregunta4) {
        this.nombreEvento = nombre;
        this.respuesta1 = pregunta1;
        this.respuesta2 = pregunta2;
        this.respuesta3 = pregunta3;
        this.respuesta4 = pregunta4;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getPregunta4() {
        return respuesta4;
    }

    public void setPregunta4(String pregunta4) {
        this.respuesta4 = pregunta4;
    }

    public String getPregunta3() {
        return respuesta3;
    }

    public void setPregunta3(String pregunta3) {
        this.respuesta3 = pregunta3;
    }

    public String getPregunta2() {
        return respuesta2;
    }

    public void setPregunta2(String pregunta2) {
        this.respuesta2 = pregunta2;
    }

    public String getPregunta1() {
        return respuesta1;
    }

    public void setPregunta1(String pregunta1) {
        this.respuesta1 = pregunta1;
    }
}
