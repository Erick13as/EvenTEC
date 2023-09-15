package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class GestionarEstudiantes extends AppCompatActivity {

    private TextInputEditText idNombreEstudiante;
    private TextInputEditText idPrimerApellidoEstudiante;
    private TextInputEditText idSegundoApellidoEstudiante;
    private TextInputEditText idCarnetEstudiante;
    private TextInputEditText idContraseñaEstudiante;
    private TextInputEditText idCorreoEstudiante;
    private TextInputEditText idCarreraEstudiante;
    private TextInputEditText idSedeEstudiante;
    private TextInputEditText idDescripcionEstudiante;

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_estudiantes);

        mFirestore = FirebaseFirestore.getInstance();

        idNombreEstudiante = findViewById(R.id.idNombreEstudiante);
        idPrimerApellidoEstudiante = findViewById(R.id.idPrimerApellidoEstudiante);
        idSegundoApellidoEstudiante = findViewById(R.id.idSegundoApellidoEstudiante);
        idCarnetEstudiante = findViewById(R.id.idCarnetEstudiante);
        idCorreoEstudiante = findViewById(R.id.idCorreoEstudiante);
        idContraseñaEstudiante = findViewById(R.id.idContraseñaEstudiante);
        idCarreraEstudiante = findViewById(R.id.idCarreraEstudiante);
        idSedeEstudiante = findViewById(R.id.idSedeEstudiante);
        idDescripcionEstudiante = findViewById(R.id.idDescripcionEstudiante);

        // Recibe los datos del colaborador desde el Intent
        Intent intent = getIntent();
        if (intent != null) {
            String nombre = intent.getStringExtra("nombre");
            String apellido = intent.getStringExtra("apellido");
            String apellido2 = intent.getStringExtra("apellido2");
            String carnet = intent.getStringExtra("carnet");
            String contrasena = intent.getStringExtra("contrasena");
            String correo = intent.getStringExtra("correo");
            String carrera = intent.getStringExtra("carrera");
            String sede = intent.getStringExtra("sede");
            String descripcion = intent.getStringExtra("descripcion");


            // Establece los datos en los elementos de la interfaz de usuario
            idNombreEstudiante.setText(nombre);
            idPrimerApellidoEstudiante.setText(apellido);
            idSegundoApellidoEstudiante.setText(apellido2);
            idCarnetEstudiante.setText(carnet);
            idCorreoEstudiante.setText(correo);
            idContraseñaEstudiante.setText(contrasena);
            idCarreraEstudiante.setText(carrera);
            idSedeEstudiante.setText(sede);
            idDescripcionEstudiante.setText(descripcion);
        }

        Button btnGuardar = findViewById(R.id.btn_GuardarGestionarEstudiantes);
        Button btnEliminar = findViewById(R.id.btn_EliminarGestionarEstudiantes);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llama al método para crear una nueva actividad en Firestore
                actualizarDatosEstudiante();
                abrirLobbyEstudiantes();

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView carnetEditText = findViewById(R.id.idCarnetEstudiante);
                String carnet = carnetEditText.getText().toString();


                // Llama al método para crear una nueva actividad en Firestore
                eliminarEstudiante(carnet);
                abrirLobbyEstudiantes();

            }
        });



    }
    private void actualizarDatosEstudiante() {
        // Recopila los valores introducidos por el usuario en los campos de edición
        String nombre = idNombreEstudiante.getText().toString();
        String apellido1 = idPrimerApellidoEstudiante.getText().toString();
        String apellido2 = idSegundoApellidoEstudiante.getText().toString();
        String carnet = idCarnetEstudiante.getText().toString();
        String correo = idCorreoEstudiante.getText().toString();
        String contrasena = idContraseñaEstudiante.getText().toString();
        String carrera = idCarreraEstudiante.getText().toString();
        String sede = idSedeEstudiante.getText().toString();
        String descripcion = idDescripcionEstudiante.getText().toString();

        // Verifica si algún campo obligatorio está vacío (puedes agregar más validaciones según tus necesidades)
        if (nombre.isEmpty() || carnet.isEmpty() || correo.isEmpty() || sede.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(GestionarEstudiantes.this, "¡Error: Campos vacíos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consulta Firestore para encontrar el documento del colaborador por su número de carné
        mFirestore.collection("usuario")
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
                            updates.put("contrasena", contrasena);
                            updates.put("descripcion", descripcion);
                            updates.put("carrera", carrera);
                            updates.put("sede", sede);
                            updates.put("carnet", carnet);

                            // Aplica las actualizaciones al documento del colaborador
                            colaboradorRef.update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Actualización exitosa
                                            Toast.makeText(GestionarEstudiantes.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Error al actualizar
                                            Toast.makeText(GestionarEstudiantes.this, "Error al actualizar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // No se encontró un colaborador con el carné especificado
                            Toast.makeText(GestionarEstudiantes.this, "No se encontraron datos para el carnet proporcionado", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error al realizar la consulta
                        Toast.makeText(GestionarEstudiantes.this, "Error al obtener datos del colaborador: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminarEstudiante(String carnet) {

        // Obtiene una referencia a la colección "usuarios" en Firestore
        CollectionReference Estudent = mFirestore.collection("usuario");

        // Crea una consulta para buscar al usuario por el carnet
        Query query = Estudent.whereEqualTo("carnet", carnet)
                .whereEqualTo("idTipo", "Estudiante");

        // Ejecuta la consulta
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Verifica si se encontraron resultados
                    if (!task.getResult().isEmpty()) {
                        // Si se encontró un usuario, elimínalo
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Estudent.document(document.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(GestionarEstudiantes.this, "Colaborador eliminado correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(GestionarEstudiantes.this, "Error al eliminar el colaborador", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Si no se encontraron resultados, muestra un mensaje
                        Toast.makeText(GestionarEstudiantes.this, "No se encontraron colaboradores con ese carnet.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si ocurre un error, muestra un mensaje de error
                    Toast.makeText(GestionarEstudiantes.this, "Error al buscar colaborador", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirLobbyEstudiantes() {
        Intent intent = new Intent(this, LobbyEstudiantesAdmin.class);
        startActivity(intent);
    }



}
