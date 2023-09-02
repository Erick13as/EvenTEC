package com.example.eventec;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class Eventos extends AppCompatActivity {

    private Spinner spinnerEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        spinnerEventos = findViewById(R.id.spinnerEventos); // Cambia a spinnerHorasF

        // Crear una lista de horas (puedes personalizarla según tus necesidades)
        String[] eventos = {"Evento 1", "Evento 2", "Evento 3"};

        // Crear un adaptador para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eventos);

        // Especificar el diseño para la lista de opciones (opcional)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Configurar el adaptador en el Spinner
        spinnerEventos.setAdapter(adapter);
    }
}