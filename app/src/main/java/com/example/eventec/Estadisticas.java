package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Estadisticas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        Intent intent = getIntent();
        String evento = (String) intent.getSerializableExtra("evento");
        Button button = (Button) findViewById(R.id.btn_GenerarInforme);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Estadisticas.this, evento, Toast.LENGTH_SHORT).show();
            }
        });

    }
}