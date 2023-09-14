package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

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

public class Encuesta extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    private TextInputEditText pregunta1;
    private TextInputEditText pregunta2;
    private TextInputEditText pregunta3;
    private TextInputEditText pregunta4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        mFirestore = FirebaseFirestore.getInstance();

        Button button = findViewById(R.id.btn_EnviarEncuesta);
        Button buttonSalir = findViewById(R.id.btn_SalirEncuesta);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadDataToFirestore();
            }
        });

        buttonSalir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openLobbyEstudiantes();
            }
        });
        pregunta1 = findViewById(R.id.idPregunta1);
        pregunta2 = findViewById(R.id.idPregunta2);
        pregunta3 = findViewById(R.id.idPregunta3);
        pregunta4 = findViewById(R.id.idPregunta4);

    }
    public void openLobbyEstudiantes() {
        Intent intent = new Intent(this, LobbyEstudiantes.class);
        startActivity(intent);
    }
    private void uploadDataToFirestore() {
        String RespuestaPregunta1 = pregunta1.getText().toString();
        String RespuestaPregunta2 = pregunta2.getText().toString();
        String RespuestaPregunta3 = pregunta3.getText().toString();
        String RespuestaPregunta4 = pregunta4.getText().toString();

        String idTipo = "Estudiante";

        // Create a new User object
        EncuestaDeEvento datosEncuesta = new EncuestaDeEvento(RespuestaPregunta1, RespuestaPregunta2, RespuestaPregunta3, RespuestaPregunta4);

        // Get a reference to the "usuario" collection in Firestore
        CollectionReference usersCollection = mFirestore.collection("encuesta");

        // Upload the user data to Firestore
        usersCollection.add(datosEncuesta)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Data successfully uploaded to Firestore
                            // You can perform any desired actions here
                            // For example, display a success message
                            Toast.makeText(Encuesta.this, "Encuesta registrada exitosamente", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to upload data to Firestore
                            // You can handle the error here
                            Toast.makeText(Encuesta.this, "Registro de encuesta fallida", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //OpenMainE();
    }
}