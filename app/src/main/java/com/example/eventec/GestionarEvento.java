package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GestionarEvento extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private Spinner spinnerFechaInicioEvento;
    private EditText editTextTituloGestionEvento;
    private EditText editTextDescripcionGestionEvento;
    private  EditText editTextLugarGestionEvento;
    private Spinner spinnerFechaFinEvento;
    private Spinner spinnerHoraInicioEvento;
    private Spinner spinnerHoraFinEvento;
    private Spinner spinnerCategoriaEvento;
    private EditText editTextRequisitosEspecialesEvento;
    private Button buttonGuardar;
    private Button buttonEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_evento);

        mFirestore = FirebaseFirestore.getInstance();

        //Variables
        spinnerFechaInicioEvento = findViewById(R.id.spinnerFechaInicioEvento);
        editTextTituloGestionEvento = findViewById(R.id.editTextTituloGestionEvento);
        editTextDescripcionGestionEvento = findViewById(R.id.editTextDescripcionGestionEvento);
        editTextLugarGestionEvento = findViewById(R.id.editTextLugarGestionEvento);
        spinnerFechaFinEvento = findViewById(R.id.spinnerFechaFinEvento);
        spinnerHoraInicioEvento = findViewById(R.id.spinnerHoraInicioEvento);
        spinnerHoraFinEvento = findViewById(R.id.spinnerHoraFinEvento);
        spinnerCategoriaEvento = findViewById(R.id.spinnerCategoriaEvento);
        editTextRequisitosEspecialesEvento = findViewById(R.id.editTextRequisitosEspecialesEvento);
        buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonEliminar = findViewById(R.id.buttonEliminar);


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

        spinnerHoraInicioEvento.setAdapter(horasAdapter);
        spinnerHoraFinEvento.setAdapter(horasAdapter);

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
        spinnerFechaInicioEvento.setAdapter(fechasAdapter);
        spinnerFechaFinEvento.setAdapter(fechasAdapter);

        //Establecer eventos
        ArrayAdapter<CharSequence> categoriasAdapter = ArrayAdapter.createFromResource(this, R.array.categorias_eventos, android.R.layout.simple_spinner_item);
        categoriasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoriaEvento.setAdapter(categoriasAdapter);
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                actualizarEvento();
                abrirLobbyAsociaciones();

            }
        });

        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView carnetEditText = findViewById(R.id.editTextDescripcionGestionEvento);
                String descripcion = carnetEditText.getText().toString();

                eliminarEvento(descripcion);
                abrirLobbyAsociaciones();

            }
        });





        };

    private void eliminarEvento(String descripcion) {

        // Obtiene una referencia a la colección "usuarios" en Firestore
        CollectionReference descripcionT = mFirestore.collection("evento");

        // Crea una consulta para buscar al usuario por el carnet
        Query query = descripcionT.whereEqualTo("descripcion", descripcion);

        // Ejecuta la consulta
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Verifica si se encontraron resultados
                    if (!task.getResult().isEmpty()) {
                        // Si se encontró un usuario, elimínalo
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            descripcionT.document(document.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(GestionarEvento.this, "Colaborador eliminado correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(GestionarEvento.this, "Error al eliminar el colaborador", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Si no se encontraron resultados, muestra un mensaje
                        Toast.makeText(GestionarEvento.this, "No se encontraron colaboradores con ese carnet.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si ocurre un error, muestra un mensaje de error
                    Toast.makeText(GestionarEvento.this, "Error al buscar colaborador", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void actualizarEvento() {
        // Recopila los valores introducidos por el usuario en los campos de edición
        String fechaInicio = spinnerFechaInicioEvento.getSelectedItem().toString();
        String titulo = editTextTituloGestionEvento.getText().toString();
        String descripcion = editTextDescripcionGestionEvento.getText().toString();
        String lugar = editTextLugarGestionEvento.getText().toString();
        String fechaFin = spinnerFechaFinEvento.getSelectedItem().toString();
        String horaInicio = spinnerHoraInicioEvento.getSelectedItem().toString();
        String horaFin = spinnerHoraFinEvento.getSelectedItem().toString();
        String categoria = spinnerCategoriaEvento.getSelectedItem().toString();
        String requisitos = editTextRequisitosEspecialesEvento.getText().toString();

        // Verifica si algún campo obligatorio está vacío (puedes agregar más validaciones según tus necesidades)
        if (requisitos.isEmpty() || lugar.isEmpty() || titulo.isEmpty() || fechaInicio.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(GestionarEvento.this, "¡Error: Campos vacíos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consulta Firestore para encontrar el documento del colaborador por su número de carné
        mFirestore.collection("evento")
                .whereEqualTo("descripcion", descripcion)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            DocumentReference colaboradorRef = queryDocumentSnapshots.getDocuments().get(0).getReference();
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("categoria", categoria);
                            updates.put("descripcion", descripcion);
                            updates.put("fechaFin", fechaFin);
                            updates.put("fechaInicio", fechaInicio);
                            updates.put("horaFin", horaFin);
                            updates.put("horaInicio", descripcion);
                            updates.put("lugar", lugar);
                            updates.put("nombre", titulo);
                            updates.put("requisitosEspeciales", requisitos);

                            // Aplica las actualizaciones al documento del colaborador
                            colaboradorRef.update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Actualización exitosa
                                            Toast.makeText(GestionarEvento.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Error al actualizar
                                            Toast.makeText(GestionarEvento.this, "Error al actualizar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // No se encontró un colaborador con el carné especificado
                            Toast.makeText(GestionarEvento.this, "No se encontraron datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error al realizar la consulta
                        Toast.makeText(GestionarEvento.this, "Error al obtener datos del evento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void abrirLobbyAsociaciones() {
        Intent intent = new Intent(this, LobbyAsociaciones.class);
        startActivity(intent);
    }



}
