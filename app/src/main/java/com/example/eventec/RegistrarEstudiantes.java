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

public class RegistrarEstudiantes extends AppCompatActivity {
    private FirebaseFirestore mFirestore;

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

        // Create a new User object
        User user = new User(nombre, apellido, apellido2, carnet, contraseña, correo, carrera,sede,descripcion,idTipo);

        // Get a reference to the "usuario" collection in Firestore
        CollectionReference usersCollection = mFirestore.collection("usuario");

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
                            Toast.makeText(RegistrarEstudiantes.this, "Registro de usuario fallida", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //OpenMainE();
    }
}