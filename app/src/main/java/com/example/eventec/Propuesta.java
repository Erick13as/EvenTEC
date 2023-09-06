package com.example.eventec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Propuesta extends AppCompatActivity {


    private FirebaseFirestore mFirestore;

    private TextInputEditText Tematica;
    private TextInputEditText Objetivo;
    private TextInputEditText Actividades;
    private TextInputEditText FechaPropuesta;
    private TextInputEditText CantidadPropuesta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propuesta);
        mFirestore = FirebaseFirestore.getInstance();

        Tematica = findViewById(R.id.idTematica);
        Objetivo = findViewById(R.id.idObjetivo);
        Actividades = findViewById(R.id.idActividades);
        FechaPropuesta = findViewById(R.id.idFechaPropuesta);
        CantidadPropuesta = findViewById(R.id.idCantidadPropuesta);

        Button button = findViewById(R.id.btn_EnviarPropuesta);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadDataToFirestore();
            }
        });

    }
    private void uploadDataToFirestore() {
        String PropuestaTematica = Tematica.getText().toString();
        String PropuestaObjetivo = Objetivo.getText().toString();
        String PropuestaActividades = Actividades.getText().toString();
        String PropuestaFecha = FechaPropuesta.getText().toString();
        String PropuestaCantidad = CantidadPropuesta.getText().toString();

        String idTipo = "Estudiante";

        // Create a new User object
        PropuestaEvento datosPropuesta = new PropuestaEvento(PropuestaTematica, PropuestaObjetivo, PropuestaActividades, PropuestaFecha,PropuestaCantidad);

        // Get a reference to the "usuario" collection in Firestore
        CollectionReference usersCollection = mFirestore.collection("propuesta");

        // Upload the user data to Firestore
        usersCollection.add(datosPropuesta)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Data successfully uploaded to Firestore
                            // You can perform any desired actions here
                            // For example, display a success message
                            Toast.makeText(Propuesta.this, "Propuesta registrada exitosamente", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to upload data to Firestore
                            // You can handle the error here
                            Toast.makeText(Propuesta.this, "Registro de propuesta fallida", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //OpenMainE();
    }
}