package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
public class CreacionEvento extends AppCompatActivity {

    private EditText editTextTitulo;
    private Spinner spinnerHoraInicio;
    private Spinner spinnerHoraFin;
    private Spinner spinnerFechaInicio;
    private Spinner spinnerFechaFin;
    private Spinner spinnerCategorias;
    private EditText editTextDescripcion;
    private EditText editTextRequisitos;
    private EditText editTextUbicacion;
    private EditText editTextCapacidad;
    private Button btnGuardar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_evento);
        db = FirebaseFirestore.getInstance();

        // Inicializar vistas
        editTextTitulo = findViewById(R.id.editTextTitulo);
        spinnerHoraInicio = findViewById(R.id.spinnerHoraI);
        spinnerHoraFin = findViewById(R.id.spinnerHoraF);
        spinnerFechaInicio = findViewById(R.id.spinnerFechaInicio);
        spinnerFechaFin = findViewById(R.id.spinnerFechaFin);
        spinnerCategorias = findViewById(R.id.spinnerCategorias);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        editTextRequisitos = findViewById(R.id.editTextRequisitos);
        editTextUbicacion = findViewById(R.id.editTextUbicacion);
        btnGuardar = findViewById(R.id.btn_Guardar);
        editTextCapacidad= findViewById(R.id.editTextCapacidad);
        // Crear una lista de horas (puedes personalizarla según tus necesidades)
        String[] horasArray = {
                "08:00", "09:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"
        };

        // Crear un adaptador para el Spinner
        ArrayAdapter<String> horasAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, horasArray
        );

        // Establecer el diseño del dropdown
        horasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Establecer el adaptador en el Spinner
        spinnerHoraInicio.setAdapter(horasAdapter);
        spinnerHoraFin.setAdapter(horasAdapter);
// Crear una lista de fechas hasta el final del año
        List<String> fechasList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR); // Obtener el año actual

        while (calendar.get(Calendar.YEAR) == year) {
            fechasList.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, 1); // Avanzar un día
        }

// Crear un adaptador para el Spinner
        ArrayAdapter<String> fechasAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, fechasList
        );

// Establecer el diseño del dropdown
        fechasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Establecer el adaptador en el Spinner
        spinnerFechaInicio.setAdapter(fechasAdapter);
        spinnerFechaFin.setAdapter(fechasAdapter);


        ArrayAdapter<CharSequence> categoriasAdapter = ArrayAdapter.createFromResource(this, R.array.categorias_eventos, android.R.layout.simple_spinner_item);
        categoriasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(categoriasAdapter);

        // Configurar el botón para guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener los valores ingresados por el usuario
                String titulo = editTextTitulo.getText().toString();
                String horaInicio = spinnerHoraInicio.getSelectedItem().toString();
                String horaFin = spinnerHoraFin.getSelectedItem().toString();
                String fechaInicio = spinnerFechaInicio.getSelectedItem().toString();
                String fechaFin = spinnerFechaFin.getSelectedItem().toString();
                String categoria = spinnerCategorias.getSelectedItem().toString();
                String descripcion = editTextDescripcion.getText().toString();
                String requisitos = editTextRequisitos.getText().toString();
                String ubicacion = editTextUbicacion.getText().toString();
                String capacidad = editTextCapacidad.getText().toString();

                // Crear un nuevo documento en la colección "eventos" de Firestore
                Map<String, Object> evento = new HashMap<>();
                evento.put("nombre", titulo);
                evento.put("horaInicio", horaInicio);
                evento.put("horaFin", horaFin);
                evento.put("fechaInicio", fechaInicio);
                evento.put("fechaFin", fechaFin);
                evento.put("categoria", categoria);
                evento.put("descripcion", descripcion);
                evento.put("requisitosEspeciales", requisitos);
                evento.put("capacidad", capacidad);
                evento.put("lugar", ubicacion);

                // Agregar el evento a Firestore
                db.collection("evento")
                        .add(evento)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    // El evento se ha agregado exitosamente
                                    Toast.makeText(CreacionEvento.this, "Evento creado exitosamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Error al agregar el evento
                                    Toast.makeText(CreacionEvento.this, "Error al crear el evento", Toast.LENGTH_SHORT).show();
                                }
                                Intent intent = new Intent(CreacionEvento.this, LobbyAsociaciones.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });
    }
}