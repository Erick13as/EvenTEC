package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class MisEventos extends AppCompatActivity {

    private Spinner spinnerEventos;
    private User user;
    private Evento selectedEvento; // Clase modelo para representar los datos del evento seleccionado
    private static final String TAG = "MisEventos"; // Etiqueta de registro

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_eventos);

        // Inicializa el Spinner
        spinnerEventos = findViewById(R.id.spinnerEventos);

        // Recibe el objeto User de la actividad de inicio de sesión
        user = (User) getIntent().getSerializableExtra("user");

        // Inicializa Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Realiza una consulta a Firestore para obtener los eventos de inscripciones con idEstado "Interés"
        db.collection("inscripcion")
                .whereEqualTo("carnet", user.getCarnet()) // Filtra por el carnet del usuario
                .whereEqualTo("idEstado", "Interés") // Filtra por el estado "Interés"
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MisEventos.this, android.R.layout.simple_spinner_item, nombresEventos);
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

        // Agrega un listener al botón buttonVerEve para obtener y pasar los datos del evento
        Button buttonVerEve = findViewById(R.id.buttonVerEve);
        buttonVerEve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtiene el evento seleccionado del Spinner
                String eventoSeleccionado = spinnerEventos.getSelectedItem().toString();

                // Realiza una consulta adicional para obtener los detalles del evento
                db.collection("evento")
                        .whereEqualTo("nombre", eventoSeleccionado)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                // Suponiendo que solo haya un documento que coincida con el nombre del evento
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                                    selectedEvento = document.toObject(Evento.class); // Convierte el documento a una instancia de la clase Evento

                                    // Ahora, puedes pasar los datos del evento y el objeto User a la actividad de Inscripcion
                                    Intent intent = new Intent(MisEventos.this, Inscripcion.class);
                                    intent.putExtra("eventoSeleccionado", selectedEvento);
                                    intent.putExtra("user", user); // Agrega el objeto User como extra
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }
}
