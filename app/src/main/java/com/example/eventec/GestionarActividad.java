package com.example.eventec;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GestionarActividad extends AppCompatActivity {

    private EditText editTextFecha;
    private Spinner spinnerHorasI;
    private Spinner spinnerHorasF;
    private Spinner spinnerEventos;
    private EditText editTextUbicacion;
    private EditText editTextDescripcion;
    private EditText editTextRecursos;
    private TextView textViewActividad;
    private Button buttonGuardar;

    private FirebaseFirestore db;
    private ArrayAdapter<String> adapter;
    private List<String> eventosList = new ArrayList<>();
    private String actividadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_actividad);

        editTextFecha = findViewById(R.id.editTextFecha);
        spinnerHorasI = findViewById(R.id.spinnerHoraI);
        spinnerHorasF = findViewById(R.id.spinnerHoraF);
        spinnerEventos = findViewById(R.id.spinnerEventos);
        editTextUbicacion = findViewById(R.id.editTextUbicacion);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        editTextRecursos = findViewById(R.id.editTextRecursos);
        textViewActividad = findViewById(R.id.textViewActividad);
        buttonGuardar = findViewById(R.id.buttonGuardar);
        Button buttonEliminar = findViewById(R.id.buttonEliminar);

        db = FirebaseFirestore.getInstance();

        // Crear una lista de horas (puedes personalizarla según tus necesidades)
        String[] horas = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, horas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerHorasI.setAdapter(adapter);
        spinnerHorasF.setAdapter(adapter);

        // Obtener datos de eventos y cargar en el spinnerEventos
        obtenerEventosParaSpinner();

        // Retrieve the activity name passed from the previous screen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String actividadNombre = extras.getString("actividad");

            // Use the activity name to query Firestore for the matching activity
            obtenerActividadPorNombre(actividadNombre);
        }

        // Configura el onClickListener para el botón Guardar
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llama al método para actualizar los datos en Firestore
                actualizarDatosEnFirestore();
                // Abre la pantalla LobbyAsociaciones
                abrirLobbyAsociaciones();
            }
        });
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llama al método para eliminar la actividad de Firestore
                eliminarActividadDeFirestore();
                // Abre la pantalla LobbyAsociaciones
                abrirLobbyAsociaciones();
            }
        });
    }

    private void obtenerEventosParaSpinner() {
        db.collection("evento")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String nombreEvento = document.getString("nombre");
                            eventosList.add(nombreEvento);
                        }

                        // Llenar el spinnerEventos con la lista de nombres de eventos
                        ArrayAdapter<String> eventosAdapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                eventosList
                        );

                        eventosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerEventos.setAdapter(eventosAdapter);
                    } else {
                        // Error al obtener los documentos
                    }
                });
    }

    private void obtenerActividadPorNombre(String nombreActividad) {
        db.collection("actividad")
                .whereEqualTo("descripcion", nombreActividad)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            actividadId = document.getId(); // Guarda el ID del documento
                            String fecha = document.getString("fecha");
                            String horaInicio = document.getString("horaInicio");
                            String horaCierre = document.getString("horaFin");
                            String evento = document.getString("idEvento");
                            String ubicacion = document.getString("ubicacion");
                            String descripcion = document.getString("descripcion");
                            String recursos = document.getString("recursos");

                            editTextFecha.setText(fecha);
                            spinnerHorasI.setSelection(adapter.getPosition(horaInicio));
                            spinnerHorasF.setSelection(adapter.getPosition(horaCierre));
                            spinnerEventos.setSelection(eventosList.indexOf(evento));
                            textViewActividad.setText(descripcion);
                            editTextUbicacion.setText(ubicacion);
                            editTextDescripcion.setText(descripcion);
                            editTextRecursos.setText(recursos);

                            break; // Sal del bucle después de procesar el primer documento
                        }
                    } else {
                        // Error al obtener los documentos
                    }
                });
    }

    private void actualizarDatosEnFirestore() {
        // Obtén los valores actuales de las vistas
        String fecha = editTextFecha.getText().toString();
        String horaInicio = spinnerHorasI.getSelectedItem().toString();
        String horaCierre = spinnerHorasF.getSelectedItem().toString();
        String idEvento = spinnerEventos.getSelectedItem().toString();
        String ubicacion = editTextUbicacion.getText().toString();
        String descripcion = editTextDescripcion.getText().toString();
        String recursos = editTextRecursos.getText().toString();

        // Actualiza los datos en Firestore utilizando la variable actividadId
        DocumentReference actividadRef = db.collection("actividad").document(actividadId); // Utiliza la variable actividadId
        actividadRef.update("fecha", fecha,
                        "horaInicio", horaInicio,
                        "horaFin", horaCierre,
                        "idEvento", idEvento,
                        "ubicacion", ubicacion,
                        "descripcion", descripcion,
                        "recursos", recursos)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(GestionarActividad.this, "Actividad modificada", Toast.LENGTH_SHORT).show();
                        } else {
                            // Error al actualizar los datos
                            // Maneja el error según tus necesidades
                        }
                    }
                });
    }

    private void eliminarActividadDeFirestore() {
        // Verifica que tengas el ID de la actividad antes de intentar eliminarla
        if (actividadId != null) {
            // Referencia al documento de la actividad que deseas eliminar
            DocumentReference actividadRef = db.collection("actividad").document(actividadId);

            // Elimina el documento
            actividadRef.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GestionarActividad.this, "Actividad eliminada", Toast.LENGTH_SHORT).show();
                            } else {
                                // Error al eliminar la actividad
                                // Maneja el error según tus necesidades
                            }
                        }
                    });
        } else {
            // No se encontró el ID de la actividad, muestra un mensaje de error o realiza la acción adecuada
        }
    }

    public void showDatePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        editTextFecha.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
    private void abrirLobbyAsociaciones() {
        Intent intent = new Intent(this, LobbyAsociaciones.class);
        startActivity(intent);
    }
}
