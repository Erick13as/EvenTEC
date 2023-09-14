package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LobbyEstudiantes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_estudiantes);

        Button btn_InscripcionEventos = findViewById(R.id.btn_InscripcionEventos);
        btn_InscripcionEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyEstudiantes.this, Inscripcion.class);
                startActivity(intent);

                // Cierra la actividad actual (opcional, si deseas volver atrás)
                finish();
            }
        });
        Button btn_CalendarioDeEventos2 = findViewById(R.id.btn_CalendarioDeEventos2);
        btn_CalendarioDeEventos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyEstudiantes.this, CalendarioEventos.class);
                startActivity(intent);

                // Cierra la actividad actual (opcional, si deseas volver atrás)
                finish();
            }
        });
        Button btn_MisEventos = findViewById(R.id.btn_MisEventos);
        btn_MisEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyEstudiantes.this, MisEventos.class);
                startActivity(intent);

                // Cierra la actividad actual (opcional, si deseas volver atrás)
                finish();
            }
        });
    }
}
