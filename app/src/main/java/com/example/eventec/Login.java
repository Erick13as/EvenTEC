package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // Importa la clase EditText
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private EditText correoEditText;
    private EditText contraseñaEditText;
    private Button ingresarButton;
    private FirebaseFirestore db;
    private List<String> infoEstudiante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correoEditText = findViewById(R.id.editTextEmail);
        contraseñaEditText = findViewById(R.id.editTextPassword);
        ingresarButton = findViewById(R.id.button2);

        db = FirebaseFirestore.getInstance();



        ingresarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateUsername() && validatePassword()) {
                    String correo = correoEditText.getText().toString();
                    String contraseña = contraseñaEditText.getText().toString();
                    checkUser(correo, contraseña);
                }
            }
        });
    }

    public boolean validateUsername() {
        String val = correoEditText.getText().toString();
        if (val.isEmpty()) {
            correoEditText.setError("Debe ingresar su correo");
            return false;
        } else {
            correoEditText.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String val = contraseñaEditText.getText().toString();
        if (val.isEmpty()) {
            contraseñaEditText.setError("Debe ingresar su contraseña");
            return false;
        } else {
            contraseñaEditText.setError(null);
            return true;
        }
    }


    private void checkUser(String correo, String contraseña) {
        Query query = db.collection("usuario");
        if (correo != null && !correo.isEmpty()) {
            query = query.whereEqualTo("correo", correo);
            if (contraseña != null && !contraseña.isEmpty()) {
                query = query.whereEqualTo("contraseña", contraseña);
            } else {
                contraseñaEditText.setError("Datos Inválidos");
                contraseñaEditText.requestFocus();
                return;
            }
        } else {
            correoEditText.setError("Contraseña o Correo Invalido");
            correoEditText.requestFocus();
            return;
        }

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                infoEstudiante = new ArrayList<>();
                String idTipo = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    infoEstudiante.add(documentSnapshot.getString("carnet"));
                    idTipo = documentSnapshot.getString("idTipo");

                    // Retrieve other user information
                    String nombre = documentSnapshot.getString("nombre");
                    String apellido = documentSnapshot.getString("apellido");
                    String apellido2 = documentSnapshot.getString("apellido2");
                    String carnet = documentSnapshot.getString("carnet");
                    String fechaNac = documentSnapshot.getString("fechaNac");
                    String correo = documentSnapshot.getString("correo");
                    String contraseña = documentSnapshot.getString("contraseña");
                    String idEstado = documentSnapshot.getString("idEstado");

                    // Create User object with retrieved data
                    User user = new User(nombre, apellido, apellido2, carnet, fechaNac, correo, contraseña, idTipo, idEstado);

                    if (idTipo.equals("Estudiante")) {
                        correoEditText.setError("El usuario existe");
                        correoEditText.requestFocus();
                        /*Intent intent = new Intent(Login.this, MainEstudiante.class);
                        intent.putExtra("user", user); // Pass the user object
                        startActivity(intent);*/
                    } else {
                        correoEditText.setError("El usuario no existe");
                        correoEditText.requestFocus();
                        /*Intent intent = new Intent(Login.this, MainAdministrador.class);
                        startActivity(intent);*/
                    }
                }

                if (infoEstudiante.isEmpty()) {
                    correoEditText.setError("Contraseña o Correo Invalido");
                    correoEditText.requestFocus();
                }
            }
        });
    }
}