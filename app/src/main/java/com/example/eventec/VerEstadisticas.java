package com.example.eventec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VerEstadisticas extends AppCompatActivity {
    private Spinner spinnerEventos;
    private FirebaseFirestore mFirestore;
    private List<String> nombresEventos;
    public boolean Flag_eventos;//Variable para saber si si hay eventos disponibles
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_estadisticas);
        mFirestore = FirebaseFirestore.getInstance();
        nombresEventos = new ArrayList<>();
        spinnerEventos = findViewById(R.id.spinnerEventosEstadisticas);
        Flag_eventos= true;
        String EventoSoli = spinnerEventos.getSelectedItem().toString();
        obtenerNombresEventos();
        Button button = (Button) findViewById(R.id.btn_VerEstadisticas);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String EventoSoli = spinnerEventos.getSelectedItem().toString();
                //Condicional para saber si existen eventos disponibles
                if (EventoSoli.equals("Sin eventos")){
                    Toast.makeText(VerEstadisticas.this, "Sin eventos disponibles", Toast.LENGTH_SHORT).show();
                }
                else {

                    OpenEstadisticas(EventoSoli);
                }
            }
        });

    }
    public void OpenEstadisticas(String EventoSoli) {
        Intent intent = new Intent(VerEstadisticas.this, Estadisticas.class);
        intent.putExtra("evento", EventoSoli); // Pasa el nombre del evento seleccionado
        startActivity(intent);
    }
    private void obtenerNombresEventos() {
        mFirestore.collection("evento")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombreEvento = document.getString("nombre");
                                //String idEstadoCubiculo = document.getString("idEstadoC");
                                /*if (idEstadoCubiculo.equals("Libre")) {
                                    nombresCubiculos.add(nombreCubiculo);
                                }*/
                                nombresEventos.add(nombreEvento);
                            }
                            if (nombresEventos.isEmpty()){
                                Flag_eventos=false;
                                return;
                            }
                            else{
                                configurarSpinner();
                            }

                        } else {
                            Log.e("Firestore", "Error al obtener los datos", task.getException());
                        }
                        return;
                    }
                });
    }

    private void configurarSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresEventos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventos.setAdapter(adapter);
    }
}