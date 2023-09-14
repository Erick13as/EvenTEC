package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LobbyAsociaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_asociaciones);
        Button btnRegistrarAsociacion = findViewById(R.id.btn_RegistrarAsociacion);
        Button btnCreacionEventos = findViewById(R.id.btn_CreacionEventos);
        Button btnConsultarEventos = findViewById(R.id.btn_ConsultarEventos);
        btnRegistrarAsociacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyAsociaciones.this, RegistrarAsociaciones.class);
                startActivity(intent);
                finish();
            }
        });
        btnCreacionEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyAsociaciones.this, LobbyAsociaciones.class);
                startActivity(intent);
                finish();
            }
        });
        btnConsultarEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyAsociaciones.this, ConsultarEventos.class);
                startActivity(intent);
                finish();
            }
        });
    }
}