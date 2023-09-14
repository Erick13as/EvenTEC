package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LobbyEstudiantesAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_estudiantes_admin);
        Button buttonRegistrar = (Button) findViewById(R.id.btn_RegistrarEstudiante);
        Button buttonInscripcion = (Button) findViewById(R.id.btn_InscripciónEventos);
        Button buttonCalendario = (Button) findViewById(R.id.btn_CalendarioEventos);
        Button buttonGestionar = (Button) findViewById(R.id.btn_GestionarEstudiantes);
        Button buttonAsoc = (Button) findViewById(R.id.btn_Asociaciones);
        Button buttonColab = (Button) findViewById(R.id.btn_Colaboradores);
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenRegistrar();
            }
        });

        buttonInscripcion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenInscripcion();
            }
        });
        buttonCalendario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenCalendario();
            }
        });

        buttonGestionar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenGestionar();
            }
        });
        buttonAsoc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LobbyEstudiantesAdmin.this, LobbyAsociaciones.class);
            startActivity(intent);
            }
        });
        buttonColab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent intent = new Intent(LobbyEstudiantesAdmin.this, LobbyColaboradores.class);
            startActivity(intent);
            }
        });
    }
    public void OpenRegistrar() {
        Intent intent = new Intent(this, RegistrarEstudiantes.class);
        startActivity(intent);
    }
    public void OpenInscripcion() {
        Intent intent = new Intent(this, Inscripcion.class);
        startActivity(intent);
    }
    public void OpenCalendario() {
        Intent intent = new Intent(this, CalendarioEventos.class);
        startActivity(intent);
    }
    public void OpenGestionar() {
        Intent intent = new Intent(this, GestionarEstudiantes.class);
        startActivity(intent);
    }
}