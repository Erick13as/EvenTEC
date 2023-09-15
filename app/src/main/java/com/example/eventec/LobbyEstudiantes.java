package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LobbyEstudiantes extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_estudiantes);
        user = (User) getIntent().getSerializableExtra("user");

        Button btn_InscripcionEventos = findViewById(R.id.btn_InscripcionEventos);
        btn_InscripcionEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyEstudiantes.this, MisEventos.class);
                intent.putExtra("user", user); // Pass the user object
                startActivity(intent);


            }
        });
        Button btn_CalendarioDeEventos2 = findViewById(R.id.btn_CalendarioDeEventos2);
        btn_CalendarioDeEventos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyEstudiantes.this, CalendarioEventos.class);
                intent.putExtra("user", user); // Pass the user object
                startActivity(intent);
            }
        });
        Button btn_MisEventos = findViewById(R.id.btn_MisEventos);
        btn_MisEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyEstudiantes.this, Eventos.class);
                intent.putExtra("user", user); // Pass the user object
                startActivity(intent);
            }
        });
        Button btn_Comunicaciones = findViewById(R.id.btn_Comunicaciones);
        btn_Comunicaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyEstudiantes.this, Comunicacion.class);
                intent.putExtra("user", user); // Pass the user object
                startActivity(intent);
            }
        });
    }
}
