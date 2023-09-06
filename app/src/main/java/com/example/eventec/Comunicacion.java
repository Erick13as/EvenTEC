package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Comunicacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicacion);
        Button buttonForo = (Button) findViewById(R.id.btn_Foro);
        Button buttonFormulario = (Button) findViewById(R.id.btn_FormularioPropuestas);

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
    }
    public void OpenForo() {
        Intent intent = new Intent(this, Foro.class);
        startActivity(intent);
    }
    public void OpenFormulario() {
        Intent intent = new Intent(this, Propuesta.class);
        startActivity(intent);
    }
}