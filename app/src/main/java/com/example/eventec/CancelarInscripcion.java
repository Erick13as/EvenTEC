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

public class CancelarInscripcion extends AppCompatActivity {

    private TextView textViewActividad;
    private TextView textViewEventoFecha;
    private TextView textViewHoraI;
    private TextView textViewHoraF;
    private TextView textViewCantidadEve;
    private User user;
    private Button buttonCancelarIns;
    private Button buttonVerQR;
    private Button buttonCalificarEvento;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_inscripcion);

        // Inicializa los TextView para mostrar los detalles del evento
        textViewActividad = findViewById(R.id.textViewActividad);
        textViewEventoFecha = findViewById(R.id.textViewFechaEve);
        textViewHoraI = findViewById(R.id.textViewHoraIEve);
        textViewHoraF = findViewById(R.id.textViewHoraFEve);
        textViewCantidadEve = findViewById(R.id.textViewCantidadEve); // Inicializa el TextView para la cantidad de actividades
        user = (User) getIntent().getSerializableExtra("user");

        // Inicializa el botón
        buttonCancelarIns = findViewById(R.id.buttonCancelarIns);


        // Establece un OnClickListener para el botón
        buttonCancelarIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Realiza la comparación con la tabla inscripción en Firestore y elimina la inscripción del usuario
                eliminarInscripcion(user.getCarnet());

                // Incrementa la capacidad del evento en la tabla evento en Firestore
                incrementarCapacidadEvento();
            }
        });

        buttonCalificarEvento = findViewById(R.id.buttonCalificar);
        buttonCalificarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CancelarInscripcion.this, VerEventoTerminado.class);
                startActivity(intent);
                finish();


            }
        });

        buttonVerQR = findViewById(R.id.buttonVerQR); // Inicializa el botón

        buttonVerQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el nombre del evento
                String nombreEvento = textViewActividad.getText().toString();

                // Crea un Intent para la actividad Entrada y agrega el nombre del evento como extra
                Intent intent = new Intent(CancelarInscripcion.this, Entrada.class);
                intent.putExtra("carnet", user.getCarnet());
                intent.putExtra("nombreEvento", nombreEvento); // Agrega el nombre del evento como extra
                startActivity(intent);
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
                CollectionReference eventosRef = db.collection("evento"); // Asegúrate de que sea la colección correcta

                // Consulta el evento con el mismo nombre
                Query query = eventosRef.whereEqualTo("nombre", nombreEvento); // Cambia "nombre" por el campo correcto en Firestore

                // Realiza la consulta
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            DocumentSnapshot eventDocument = task.getResult().getDocuments().get(0); // Obtén el primer documento
                            int capacidadEvento = eventDocument.getLong("capacidad").intValue();

                            // Muestra la capacidad del evento en el textViewCantidadEve
                            textViewCantidadEve.setText(String.valueOf(capacidadEvento));
                        } else {
                            // Maneja el error si la consulta no encuentra el evento
                            // Puedes mostrar un mensaje de error o dejar el textViewCantidadEve en un valor predeterminado
                        }
                    }
                });
            }
        }
    }

    private void eliminarInscripcion(String carnetUsuario) {
        String nombreEvento = textViewActividad.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference inscripcionesRef = db.collection("inscripcion");

        Query query = inscripcionesRef.whereEqualTo("carnet", carnetUsuario)
                .whereEqualTo("idEvento", nombreEvento);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        // Elimina el documento de inscripción encontrado
                        inscripcionesRef.document(document.getId())
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> deleteTask) {
                                        if (deleteTask.isSuccessful()) {
                                            // Eliminación exitosa
                                            // Puedes mostrar un mensaje de éxito o realizar otras acciones necesarias

                                            // Crea un objeto Inscrip con los datos (opcional si necesitas los datos antes de eliminar)

                                            // Abre la pantalla de inicio de sesión (Login)
                                            Intent loginIntent = new Intent(CancelarInscripcion.this, Login.class);
                                            startActivity(loginIntent);
                                            finish(); // Cierra la actividad actual si es necesario
                                        } else {
                                            // Maneja el error si la eliminación falla
                                            // Puedes mostrar un mensaje de error
                                        }
                                    }
                                });
                    }
                } else {
                    // Maneja el error si la consulta falla
                    // Puedes mostrar un mensaje de error
                }
            }
        });
    }
    private void incrementarCapacidadEvento() {
        String nombreEvento = textViewActividad.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventosRef = db.collection("evento");

        // Consulta el evento con el mismo nombre
        Query query = eventosRef.whereEqualTo("nombre", nombreEvento);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                    DocumentSnapshot eventDocument = task.getResult().getDocuments().get(0); // Obtén el primer documento
                    int capacidadEvento = eventDocument.getLong("capacidad").intValue();

                    // Incrementa la capacidad en 1
                    capacidadEvento++;

                    // Actualiza la capacidad en Firestore
                    eventosRef.document(eventDocument.getId()).update("capacidad", capacidadEvento)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> updateTask) {
                                    if (updateTask.isSuccessful()) {
                                        // Incremento exitoso
                                        // Puedes mostrar un mensaje de éxito o realizar otras acciones necesarias
                                    } else {
                                        // Maneja el error si la actualización falla
                                        // Puedes mostrar un mensaje de error
                                    }
                                }
                            });
                } else {
                    // Maneja el error si la consulta no encuentra el evento
                    // Puedes mostrar un mensaje de error
                }
            }
        });
    }

}