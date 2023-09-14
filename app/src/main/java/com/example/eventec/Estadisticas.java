package com.example.eventec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Estadisticas extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextInputEditText numparticipantes;
    private TextInputEditText diaEvento;
    private TextInputEditText horaEvento;
    private TextInputEditText evaluacionEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        Intent intent = getIntent();
        String evento = (String) intent.getSerializableExtra("evento");

        numparticipantes = findViewById(R.id.idNumeroParticipantes);
        diaEvento = findViewById(R.id.idDiaEstadisticas);
        horaEvento = findViewById(R.id.idHoraEstadisticas);
        evaluacionEvento = findViewById(R.id.idEvaluacionEstadistica);

        /*Button button = findViewById(R.id.btn_GenerarInforme);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Estadisticas.this, evento, Toast.LENGTH_SHORT).show();
            }
        });*/

        // Inicializa Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Busca el evento con el mismo nombre en Firestore
        db.collection("evento")
                .whereEqualTo("nombre", evento)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Se encontró el evento, asumiendo que solo hay uno con el mismo nombre
                            DocumentSnapshot eventoDocument = queryDocumentSnapshots.getDocuments().get(0);
                            String diaInicio = eventoDocument.getString("fechaInicio");
                            String horaInicio = eventoDocument.getString("horaInicio");

                            // Establece los valores en los TextInputEditText
                            diaEvento.setText(diaInicio);
                            horaEvento.setText(horaInicio);

                            // Busca todas las inscripciones para el evento con estado "inscrito"
                            db.collection("inscripcion")
                                    .whereEqualTo("idEvento", evento)
                                    .whereEqualTo("idEstado", "inscrito")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot inscripcionQueryDocumentSnapshots) {
                                            long totalCalificacion = 0;
                                            int totalCoincidencias = inscripcionQueryDocumentSnapshots.size();

                                            for (DocumentSnapshot inscripcionDocument : inscripcionQueryDocumentSnapshots.getDocuments()) {
                                                Long calificacion = inscripcionDocument.getLong("calificacion");

                                                if (calificacion != null) {
                                                    totalCalificacion += calificacion;
                                                }
                                            }

                                            if (totalCoincidencias > 0) {
                                                // Calcula el promedio de calificaciones
                                                double promedioCalificacion = (double) totalCalificacion / totalCoincidencias;
                                                evaluacionEvento.setText(String.format("%.2f/10", promedioCalificacion));
                                                numparticipantes.setText(String.valueOf(totalCoincidencias));
                                            } else {
                                                // No se encontraron inscripciones con estado "inscrito"
                                                evaluacionEvento.setText("Sin evaluaciones");
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Maneja el error si no se puede obtener la inscripción
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Maneja el error si no se puede obtener el evento
                    }
                });

        }
    }
