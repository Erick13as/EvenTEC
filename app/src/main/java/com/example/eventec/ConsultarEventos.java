package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConsultarEventos extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private Spinner spinnerEventos;
    private List<String> nombresEventos;
    public boolean checkEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_eventos);

        mFirestore = FirebaseFirestore.getInstance();
        spinnerEventos = findViewById(R.id.spinnerlListarEventos);
        nombresEventos = new ArrayList<>();
        Button btnAgregarColaboradores = findViewById(R.id.btn_GestionarEvento);
        String ActividadesEventos = spinnerEventos.getSelectedItem().toString();
        obtenerEvento();
        btnAgregarColaboradores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultarEventos.this, RegistrarAsociaciones.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void obtenerEvento() {
        mFirestore.collection("evento")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombreEventos = document.getString("descripcion");

                                nombresEventos.add(nombreEventos);
                            }
                            if (nombresEventos.isEmpty()){
                                checkEvento=false;
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
