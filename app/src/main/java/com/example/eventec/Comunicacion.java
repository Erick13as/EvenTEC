package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Comunicacion extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicacion);
        user = (User) getIntent().getSerializableExtra("user");
        Button buttonForo = (Button) findViewById(R.id.btn_Foro);
        Button buttonFormulario = (Button) findViewById(R.id.btn_FormularioPropuestas);
        Button buttonVolver = (Button) findViewById(R.id.btn_Volver);

        buttonForo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    OpenForo();
            }
        });

        buttonFormulario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenFormulario();
            }
        });
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenLobby();
            }
        });
    }
    public void OpenForo() {
        Intent intent = new Intent(this, Foro.class);
        intent.putExtra("user", user); // Pass the user object
        startActivity(intent);
    }
    public void OpenFormulario() {
        Intent intent = new Intent(this, Propuesta.class);
        intent.putExtra("user", user); // Pass the user object
        startActivity(intent);
    }
    public void OpenLobby() {
        Intent intent = new Intent(this, LobbyEstudiantes.class);
        intent.putExtra("user", user); // Pass the user object
        startActivity(intent);
    }
}