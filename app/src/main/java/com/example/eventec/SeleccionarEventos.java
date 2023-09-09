package com.example.eventec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class SeleccionarEventos extends AppCompatActivity {

    private FirebaseFirestore db;
    private String event_name;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_eventos);

        db = FirebaseFirestore.getInstance();

        // Obtén el event_name enviado desde CalendarioEventos
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event_name = extras.getString("event_name");
            user = (User) getIntent().getSerializableExtra("user");
        }

        // Obtén la referencia del LinearLayout
        LinearLayout linearLayout = findViewById(R.id.linearLayoutEventos);

        // Consulta Firestore para obtener la lista de eventos con idEvento igual a event_name
        db.collection("actividad")
                .whereEqualTo("idEvento", event_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Obtén el nombre del evento desde Firestore
                                String nombreEvento = document.getString("descripcion");

                                // Crea un nuevo TextView para el evento
                                TextView textView = new TextView(SeleccionarEventos.this);

                                // Establece el texto del TextView
                                textView.setText(nombreEvento);

                                // Establece el fondo del TextView como el recurso con bordes redondeados
                                textView.setBackgroundResource(R.drawable.rounded_background);

                                // Establece el tamaño de texto igual al textViewActividad
                                textView.setTextSize(24); // Tamaño en sp

                                // Agrega margen inferior entre los TextView
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                layoutParams.setMargins(0, 0, 0, 10); // Margen inferior en píxeles
                                textView.setLayoutParams(layoutParams);

                                linearLayout.addView(textView);
                            }
                            // Cambia el texto del TextView llamado textViewEvento
                            TextView textViewEvento = findViewById(R.id.textViewEvento);
                            textViewEvento.setText(event_name);

                            // Agregar OnClickListener al botón
                            Button botonAgregar = findViewById(R.id.botonAgregar);
                            botonAgregar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Lógica para verificar si ya existe una inscripción antes de agregarla
                                    verificarYAgregarInscripcion();
                                }
                            });
                        } else {
                            // Maneja errores de lectura desde Firestore
                        }
                    }
                });
    }

    private void verificarYAgregarInscripcion() {
        // Realiza una consulta para verificar si ya existe una inscripción con los mismos valores
        db.collection("inscripcion")
                .whereEqualTo("carnet", user.getCarnet())
                .whereEqualTo("idEvento", event_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                // No existe una inscripción con los mismos valores, puedes agregarla
                                agregarInscripcion();
                            } else {
                                // Ya existe una inscripción con los mismos valores, muestra un mensaje
                                Toast.makeText(SeleccionarEventos.this, "Evento ya agregado a eventos de interés", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Maneja errores de consulta en Firestore
                        }
                    }
                });
    }

    private void agregarInscripcion() {
        // Crear un mapa con los datos de la inscripción
        Map<String, Object> inscripcionData = new HashMap<>();
        inscripcionData.put("carnet", user.getCarnet());
        inscripcionData.put("idEvento", event_name);
        inscripcionData.put("idEstado", "Interés");
        inscripcionData.put("calificacion", 0);
        inscripcionData.put("idEncuesta", "");

        // Agregar la inscripción en Firestore
        db.collection("inscripcion")
                .add(inscripcionData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(SeleccionarEventos.this, CalendarioEventos.class);
                            intent.putExtra("user", user); // Agrega el objeto User como extra
                            startActivity(intent);
                        } else {
                            // Manejar errores al agregar la inscripción
                        }
                    }
                });
    }
}

