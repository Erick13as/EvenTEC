package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LobbyColaboradores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_colaboradores);

        Button btn_RegistrarColaborador = findViewById(R.id.btn_RegistrarColaborador);
        Button btn_GestionarColaboradores = findViewById(R.id.btn_GestionarColaboradores);
        btn_RegistrarColaborador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyColaboradores.this, RegistrarColaborador.class);
                startActivity(intent);

                // Cierra la actividad actual (opcional, si deseas volver atrás)
                finish();
            }
        });
        btn_GestionarColaboradores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyColaboradores.this, BuscarColaborador.class);
                startActivity(intent);

                // Cierra la actividad actual (opcional, si deseas volver atrás)
                finish();
            }
        });
    }
}