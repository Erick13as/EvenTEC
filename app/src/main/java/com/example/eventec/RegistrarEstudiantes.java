package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;


//*********************************************************
import androidx.annotation.NonNull;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class RegistrarEstudiantes extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    private FirebaseFirestore db;
    private TextInputEditText nombreEditText;
    private TextInputEditText apellidoEditText;
    private TextInputEditText apellido2EditText;
    private TextInputEditText carnetEditText;
    private TextInputEditText correoEditText;
    private TextInputEditText contraseñaEditText;
    private TextInputEditText carreraEditText;
    private TextInputEditText sedeEditText;
    private TextInputEditText descripcionEditText;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_estudiantes);
        // Initialize Firestore instance
        mFirestore = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();
        Button button = findViewById(R.id.btn_CrearCuenta);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadDataToFirestore();
            }
        });
        nombreEditText = findViewById(R.id.idNombreEstudiante);
        apellidoEditText = findViewById(R.id.idPrimerApellidoEstudiante);
        apellido2EditText = findViewById(R.id.idSegundoApellidoEstudiante);
        carnetEditText = findViewById(R.id.idCarnetEstudiante);
        contraseñaEditText = findViewById(R.id.idContraseñaEstudiante);
        correoEditText = findViewById(R.id.idCorreoEstudiante);
        carreraEditText = findViewById(R.id.idCarreraEstudiante);
        sedeEditText = findViewById(R.id.idSedeEstudiante);
        descripcionEditText = findViewById(R.id.idPrimerApellidoEstudiante);

    }
    private void uploadDataToFirestore() {
        String nombre = nombreEditText.getText().toString();
        String apellido = apellidoEditText.getText().toString();
        String apellido2 = apellido2EditText.getText().toString();
        String carnet = carnetEditText.getText().toString();
        String contraseña = contraseñaEditText.getText().toString();
        String correo = correoEditText.getText().toString();
        String carrera = carreraEditText.getText().toString();
        String sede = sedeEditText.getText().toString();
        String descripcion = descripcionEditText.getText().toString();

        String idTipo = "Estudiante";

        // Get a reference to the "usuario" collection in Firestore
        CollectionReference usersCollection = mFirestore.collection("usuario");

        if (nombre.isEmpty() || apellido.isEmpty() || apellido2.isEmpty() || carnet.isEmpty() || contraseña.isEmpty() || correo.isEmpty() || carrera.isEmpty() || sede.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(RegistrarEstudiantes.this, "¡Error: Campos vacíos!", Toast.LENGTH_SHORT).show();
        } else {
            // Check if a user with the same email already exists
            usersCollection.whereEqualTo("correo", correo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // A user with the same email already exists
                            Toast.makeText(RegistrarEstudiantes.this, "¡Error: Usuario ya existente!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Check if a user with the same carnet already exists
                            usersCollection.whereEqualTo("carnet", carnet).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().isEmpty()) {
                                            // A user with the same carnet already exists
                                            Toast.makeText(RegistrarEstudiantes.this, "¡Error: Carnet ya registrado!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Create a new User object
                                            User user = new User(nombre, apellido, apellido2, carnet, contraseña, correo, carrera, sede, descripcion, idTipo);

                                            // Upload the user data to Firestore
                                            usersCollection.add(user)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (task.isSuccessful()) {
                                                                // Data successfully uploaded to Firestore
                                                                // You can perform any desired actions here
                                                                // For example, display a success message
                                                                Toast.makeText(RegistrarEstudiantes.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                // Failed to upload data to Firestore
                                                                // You can handle the error here
                                                                Toast.makeText(RegistrarEstudiantes.this, "Registro de usuario fallido", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        // Handle the error here
                                        Toast.makeText(RegistrarEstudiantes.this, "Error al verificar el carnet del usuario", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        // Handle the error here
                        Toast.makeText(RegistrarEstudiantes.this, "Error al verificar el correo del usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



}