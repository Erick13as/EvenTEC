package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LobbyEstudiantesAdmin extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_estudiantes_admin);
        user = (User) getIntent().getSerializableExtra("user");
        Button buttonGestionar = (Button) findViewById(R.id.btn_GestionarEstudiantes);
        Button buttonAsoc = (Button) findViewById(R.id.btn_Asociaciones);
        Button buttonColab = (Button) findViewById(R.id.btn_Colaboradores);
        Button buttonEstadisticas = (Button) findViewById(R.id.btn_VerEstadisticas2);

        buttonEstadisticas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenEstadisticas();
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

    public void OpenGestionar() {
        Intent intent = new Intent(this, SeleccionarEstudiante.class);
        startActivity(intent);
    }
    public void OpenEstadisticas() {
        Intent intent = new Intent(this, VerEstadisticas.class);
        startActivity(intent);
    }
}