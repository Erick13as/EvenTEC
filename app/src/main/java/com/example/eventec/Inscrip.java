package com.example.eventec;

import java.io.Serializable;

public class Inscrip implements Serializable {
    private String carnet;
    private String idEvento;
    private String idEstado;

    public Inscrip() {
        // Constructor vacío requerido por Firebase
    }

    public Inscrip(String carnet, String idEvento, String idEstado) {
        this.carnet = carnet;
        this.idEvento = idEvento;
        this.idEstado = idEstado;
    }

    public String getCarnet() {
        return carnet;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public String getIdEstado() {
        return idEstado;
    }
}

