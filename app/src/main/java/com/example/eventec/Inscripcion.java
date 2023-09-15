package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Inscripcion extends AppCompatActivity {

    private TextView textViewActividad;
    private TextView textViewEventoFecha;
    private TextView textViewHoraI;
    private TextView textViewHoraF;
    private TextView textViewCantidadEve;
    private User user;
    private Button buttonInscrip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscripcion);

        // Inicializa los TextView para mostrar los detalles del evento
        textViewActividad = findViewById(R.id.textViewActividad);
        textViewEventoFecha = findViewById(R.id.textViewFechaEve);
        textViewHoraI = findViewById(R.id.textViewHoraIEve);
        textViewHoraF = findViewById(R.id.textViewHoraFEve);
        textViewCantidadEve = findViewById(R.id.textViewCantidadEve); // Inicializa el TextView para la cantidad de actividades
        user = (User) getIntent().getSerializableExtra("user");

        // Inicializa el botón
        buttonInscrip = findViewById(R.id.buttonInscrip);

        // Establece un OnClickListener para el botón
        buttonInscrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Realiza la comparación con la tabla inscripcion en Firestore
                compararYActualizarInscripcion(user.getCarnet());
            }
        });

        // Recibe el evento enviado desde MisEventos
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("eventoSeleccionado")) {
            Evento evento = (Evento) intent.getSerializableExtra("eventoSeleccionado");

            // Muestra los detalles del evento en los TextView
            if (evento != null) {
                textViewActividad.setText(evento.getNombre());
                textViewEventoFecha.setText(evento.getFechaInicio());
                textViewHoraI.setText(evento.getHoraInicio());
                textViewHoraF.setText(evento.getHoraFin());

                // Obtén el nombre del evento
                String nombreEvento = evento.getNombre();

                // Referencia a la colección de eventos en Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference eventosRef = db.collection("evento"); // Cambia "actividad" a "evento"

                // Consulta el evento con el mismo nombre
                eventosRef.whereEqualTo("nombre", nombreEvento).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int capacidadEvento = 0; // Inicializa la capacidad en 0
                                    for (DocumentSnapshot document : task.getResult()) {
                                        // Obtiene la capacidad del evento
                                        capacidadEvento = document.getLong("capacidad").intValue();
                                    }

                                    // Muestra la capacidad en el textViewCantidadEve
                                    textViewCantidadEve.setText(String.valueOf(capacidadEvento));
                                } else {
                                    // Maneja el error si la consulta falla
                                    // Por ejemplo, puedes mostrar un mensaje de error o dejar el textViewCantidadEve en un valor predeterminado
                                }
                            }
                        });
            }
        }
    }

    private void compararYActualizarInscripcion(String carnetUsuario) {
        String nombreEvento = textViewActividad.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference inscripcionesRef = db.collection("inscripcion");
        CollectionReference eventosRef = db.collection("evento"); // Agrega referencia a la colección de eventos

        Query query = inscripcionesRef.whereEqualTo("carnet", carnetUsuario)
                .whereEqualTo("idEvento", nombreEvento);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        // Actualiza el valor de idEstado a "Inscrito"
                        inscripcionesRef.document(document.getId())
                                .update("idEstado", "Inscrito")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> updateTask) {
                                        if (updateTask.isSuccessful()) {
                                            // Actualización exitosa
                                            // Puedes mostrar un mensaje de éxito o realizar otras acciones necesarias

                                            // Resta 1 a la capacidad del evento en la base de datos
                                            eventosRef.whereEqualTo("nombre", nombreEvento)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> eventTask) {
                                                            if (eventTask.isSuccessful()) {
                                                                for (DocumentSnapshot eventDocument : eventTask.getResult()) {
                                                                    int capacidadEvento = eventDocument.getLong("capacidad").intValue();
                                                                    if (capacidadEvento > 0) {
                                                                        capacidadEvento--; // Resta 1 a la capacidad
                                                                        // Actualiza la capacidad en la base de datos
                                                                        eventosRef.document(eventDocument.getId())
                                                                                .update("capacidad", capacidadEvento)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> capacityUpdateTask) {
                                                                                        if (capacityUpdateTask.isSuccessful()) {
                                                                                            // Capacidad actualizada con éxito
                                                                                            // Puedes mostrar un mensaje de éxito o realizar otras acciones necesarias
                                                                                        } else {
                                                                                            // Maneja el error si la actualización de la capacidad falla
                                                                                            // Puedes mostrar un mensaje de error
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            } else {
                                                                // Maneja el error si la consulta de eventos falla
                                                                // Puedes mostrar un mensaje de error
                                                            }
                                                        }
                                                    });

                                            // Crea un objeto Inscrip con los datos
                                            Inscrip inscripcion = document.toObject(Inscrip.class);

                                            // Abre la pantalla ConfirmarInscrip y envía los datos
                                            Intent intent = new Intent(Inscripcion.this, ConfirmarInscrip.class);
                                            intent.putExtra("inscripcion", inscripcion);
                                            intent.putExtra("user", user); // Pass the user object
                                            startActivity(intent);
                                        } else {
                                            // Maneja el error si la actualización de la inscripción falla
                                            // Puedes mostrar un mensaje de error
                                        }
                                    }
                                });
                    }
                } else {
                    // Maneja el error si la consulta de inscripciones falla
                    // Puedes mostrar un mensaje de error
                }
            }
        });
    }
}



