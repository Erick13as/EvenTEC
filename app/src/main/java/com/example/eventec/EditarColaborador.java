package com.example.eventec;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditarColaborador extends AppCompatActivity {

    private TextInputEditText nombreColab;
    private TextInputEditText Apellido1;
    private TextInputEditText Apellido2;
    private TextInputEditText CarnetColab;
    private TextInputEditText CorreoColab;
    private Spinner spinnerSedeColab;
    private TextInputEditText DescripcionColab;
    private TextInputEditText CarreraColab;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_colaborador);

        // Inicializa Firestore
        db = FirebaseFirestore.getInstance();

        Button btnEditar = findViewById(R.id.btn_Editar);

        // Configura el evento de clic para el botón "Editar"
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarDatosColaborador();
                Intent intent = new Intent(com.example.eventec.EditarColaborador.this, LobbyColaboradores.class);
                startActivity(intent);
                finish();
            }
        });

        // Inicializa los elementos de la interfaz de usuario y carga los datos del colaborador existente
        // Puedes agregar código aquí para cargar los datos actuales del colaborador si es necesario
        // (por ejemplo, cuando el usuario llega a esta actividad para editar un colaborador existente).
        // Luego, llena los campos de edición con esos datos si es necesario.
    }

    private void actualizarDatosColaborador() {
        // Recopila los valores introducidos por el usuario en los campos de edición
        String nombre = nombreColab.getText().toString();
        String apellido1 = Apellido1.getText().toString();
        String apellido2 = Apellido2.getText().toString();
        String carnet = CarnetColab.getText().toString();
        String correo = CorreoColab.getText().toString();
        String descripcion = DescripcionColab.getText().toString();
        String carrera = CarreraColab.getText().toString();
        String sede = spinnerSedeColab.getSelectedItem().toString();

        // Verifica si algún campo obligatorio está vacío (puedes agregar más validaciones según tus necesidades)
        if (nombre.isEmpty() || carnet.isEmpty() || correo.isEmpty() || sede.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(EditarColaborador.this, "¡Error: Campos vacíos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consulta Firestore para encontrar el documento del colaborador por su número de carné
        db.collection("usuario")
                .whereEqualTo("carnet", carnet)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Si se encuentra un colaborador con el carné especificado, actualiza los datos
                            DocumentReference colaboradorRef = queryDocumentSnapshots.getDocuments().get(0).getReference();
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("nombre", nombre);
                            updates.put("apellido", apellido1);
                            updates.put("apellido2", apellido2);
                            updates.put("correo", correo);
                            updates.put("descripcion", descripcion);
                            updates.put("carrera", carrera);
                            updates.put("sede", sede);

                            // Aplica las actualizaciones al documento del colaborador
                            colaboradorRef.update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Actualización exitosa
                                            Toast.makeText(EditarColaborador.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Error al actualizar
                                            Toast.makeText(EditarColaborador.this, "Error al actualizar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // No se encontró un colaborador con el carné especificado
                            Toast.makeText(EditarColaborador.this, "No se encontraron datos para el carnet proporcionado", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error al realizar la consulta
                        Toast.makeText(EditarColaborador.this, "Error al obtener datos del colaborador: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
