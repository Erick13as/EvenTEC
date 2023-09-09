package com.example.eventec;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Eventos extends AppCompatActivity {

    private Spinner spinnerEventos;
    private User user;
    private static final String TAG = "Eventos"; // Etiqueta de registro

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        // Inicializa el Spinner
        spinnerEventos = findViewById(R.id.spinnerEventos);

        // Recibe el objeto User de la actividad de inicio de sesión
        user = (User) getIntent().getSerializableExtra("user");

        // Inicializa Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Realiza una consulta a Firestore para obtener los eventos de inscripciones con idEstado "Interés"
        db.collection("inscripcion")
                .whereEqualTo("carnet", user.getCarnet()) // Filtra por el carnet del usuario
                .whereEqualTo("idEstado", "Inscrito") // Filtra por el estado "Interés"
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> nombresEventos = new ArrayList<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String nombreEvento = document.getString("idEvento"); // Asegúrate de que el campo se llama "idEvento" en Firestore

                            if (nombreEvento != null) {
                                nombresEventos.add(nombreEvento);
                            }
                        }

                        // Actualiza el adaptador del Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Eventos.this, android.R.layout.simple_spinner_item, nombresEventos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerEventos.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejar errores si la consulta falla
                    }
                });
    }
}