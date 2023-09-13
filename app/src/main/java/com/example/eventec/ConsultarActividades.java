package com.example.eventec;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class ConsultarActividades extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private Spinner spinnerActividades;
    private List<String> nombresActividad;
    public boolean checkActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_actividades);

        mFirestore = FirebaseFirestore.getInstance();
        spinnerActividades = findViewById(R.id.spinnerlListaActividades);
        nombresActividad = new ArrayList<>();
        checkActividad = true;
        String ActividadesConsult = spinnerActividades.getSelectedItem().toString();
        obtenerActividades();

    }

    private void obtenerActividades() {
        mFirestore.collection("actividad")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombreActividad = document.getString("descripcion");

                                nombresActividad.add(nombreActividad);
                            }
                            if (nombresActividad.isEmpty()){
                                checkActividad=false;
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresActividad);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActividades.setAdapter(adapter);
    }
}
