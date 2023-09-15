package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class VerEventoTerminado extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private TextView textViewFechaEve;
    private TextView textViewHoraIEve;
    private TextView textViewHoraFEve;
    private TextView textViewActividad;
    private User user;
    private Spinner spinnerCalificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_evento_terminado);
        user = (User) getIntent().getSerializableExtra("user");

        String nombreActividad = "";
        mFirestore = FirebaseFirestore.getInstance();
        if (getIntent().hasExtra("eventoDatos")) {
            EventoCalificar eventoDatos = (EventoCalificar) getIntent().getSerializableExtra("eventoDatos");

            // Now you can use the data as needed
            nombreActividad = eventoDatos.getNombre();
            String fechaEvento = eventoDatos.getFechaInicio();
            String horaInicio = eventoDatos.getHoraInicio();
            String horaFin = eventoDatos.getHoraFin();
            String cantidadEventos = eventoDatos.getCapacidad();

            // Find the TextViews by their IDs
            textViewFechaEve = findViewById(R.id.textViewFechaEve);
            textViewHoraIEve = findViewById(R.id.textViewHoraIEve);
            textViewHoraFEve = findViewById(R.id.textViewHoraFEve);
            textViewActividad = findViewById(R.id.textViewActividad);

            // Set the text of the TextViews
            textViewFechaEve.setText(fechaEvento);
            textViewHoraIEve.setText(horaInicio);
            textViewHoraFEve.setText(horaFin);
            textViewActividad.setText(nombreActividad);
        }

        Button buttonCalificar = findViewById(R.id.buttonCalificar);
        spinnerCalificacion = findViewById(R.id.spinnerVerEventoTerminado);

        // Crear un adaptador personalizado para el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.calificaciones_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Configurar el adaptador en el Spinner
        spinnerCalificacion.setAdapter(adapter);

        String finalNombreActividad = nombreActividad;
        buttonCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el valor seleccionado del Spinner (texto)
                String calificacionTexto = spinnerCalificacion.getSelectedItem().toString();

                // Convertir el texto en un número
                int calificacion = Integer.parseInt(calificacionTexto);

                // Tu lógica para buscar y actualizar Firestore con la calificación
                mFirestore.collection("inscripcion")
                        .whereEqualTo("carnet", user.getCarnet())
                        .whereEqualTo("idEvento", finalNombreActividad)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Actualizar el documento con la calificación
                                        document.getReference().update("calificacion", calificacion);
                                    }
                                } else {
                                    // Manejo de errores
                                }
                            }
                        });

                // Iniciar la actividad de encuesta
                Intent intent = new Intent(VerEventoTerminado.this, Encuesta.class);
                intent.putExtra("eventoEncuestar", finalNombreActividad); // Pasa el objeto de usuario
                intent.putExtra("user", user); // Pasa el objeto de usuario
                startActivity(intent);
                finish();
            }
        });
    }
}

