package com.example.eventec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import android.widget.TextView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AsignarColaborador extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView nombreEvAsignarTextView;
    private Button btnConfirmar;
    private Button btn_Buscar;
    private String colaboradorCarnet;
    private TextView resultadosTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_colaborador);

        db = FirebaseFirestore.getInstance();
        nombreEvAsignarTextView = findViewById(R.id.nombreEvAsignar);
        btnConfirmar = findViewById(R.id.btn_Confirmar);
        btn_Buscar = findViewById(R.id.btn_Buscar);
        resultadosTextView = findViewById(R.id.textView);

        // Obtiene el valor de "actividad" del Intent
        String actividad = getIntent().getStringExtra("actividad");

        // Obtiene el valor actual del TextView
        String valorActual = nombreEvAsignarTextView.getText().toString();

        // Concatena el nombre del evento/actividad al valor actual del TextView
        nombreEvAsignarTextView.setText(valorActual + actividad);

        // Configura el listener para el botón de confirmación
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView carnetEditText = findViewById(R.id.idCarnetColab);
                String carnet = carnetEditText.getText().toString();
                asignarColaborador(carnet);
                Intent intent = new Intent(AsignarColaborador.this, LobbyAsociaciones.class);
                startActivity(intent);
            }
        });

        btn_Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView carnetEditText = findViewById(R.id.idCarnetColab);
                String carnet = carnetEditText.getText().toString();

                if (!carnet.isEmpty()) {
                    buscarColaborador(carnet);
                } else {
                    Toast.makeText(AsignarColaborador.this, "Ingrese un carnet válido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Obtiene el carnet del colaborador para buscar sus datos
        colaboradorCarnet = getIntent().getStringExtra("carnetColaborador");
    }

    private void buscarColaborador(String carnet) {
        // Obtiene una referencia a la base de datos Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtiene una referencia a la colección "colaborador" en Firestore
        CollectionReference colaboradorRef = db.collection("colaborador");

        // Crea una consulta para buscar por carnet e idtipo igual a "Colaborador"
        Query query = colaboradorRef.whereEqualTo("carnet", carnet);

        // Ejecuta la consulta
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Procesa los resultados de la consulta
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String nombre = document.getString("nombre");
                        String correo = document.getString("correo");
                        String apellido = document.getString("apellido");
                        String apellido2 = document.getString("apellido2");
                        String carrera = document.getString("carrera");
                        String descripcion = document.getString("descripcion");
                        String sede = document.getString("sede");

                        // Obtén el contenido actual del TextView previo
                        String contenidoPrevio = resultadosTextView.getText().toString();

                        // Concatena los nuevos resultados con el contenido previo
                        String nuevoContenido = contenidoPrevio + "\n" + "Nombre: " + nombre + " " + apellido + " " + apellido2 +
                                "\nCorreo: " + correo +
                                "\nCarrera: " + carrera +
                                "\nDescripción: " + descripcion +
                                "\nSede: " + sede;

                        // Actualiza el contenido del TextView con el nuevo contenido
                        resultadosTextView.setText(nuevoContenido);

                    }

                    if (resultadosTextView.getText().toString().isEmpty()) {
                        // Si no se encontraron resultados, muestra un mensaje
                        resultadosTextView.setText("No se encontraron colaboradores con ese carnet.");
                    }
                } else {
                    // Si ocurre un error, muestra un mensaje de error
                    Toast.makeText(AsignarColaborador.this, "Error al buscar colaborador", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void asignarColaborador(String carnet) {
        // Verifica si tienes la información necesaria para realizar la asignación.
        if (carnet != null) {
            // Supongo que quieres asignar el colaborador a un evento específico.
            String nombreEvento = nombreEvAsignarTextView.getText().toString();

            // Crea un objeto de asignación
            Map<String, Object> asignacion = new HashMap<>();
            asignacion.put("colaboradorCarnet", carnet);
            asignacion.put("eventoNombre", nombreEvento);

            // Obtiene una referencia a la colección "asignacion" en Firestore
            CollectionReference asignacionesRef = db.collection("asignaciones");

            // Agrega la asignación a Firestore
            asignacionesRef.add(asignacion)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // La asignación se ha agregado con éxito.
                            Toast.makeText(AsignarColaborador.this, "Colaborador asignado con éxito.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Ocurrió un error al agregar la asignación.
                            Toast.makeText(AsignarColaborador.this, "Error al asignar colaborador.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Si falta información, muestra un mensaje de advertencia.
            Toast.makeText(AsignarColaborador.this, "Falta información para realizar la asignación.", Toast.LENGTH_SHORT).show();
        }
    }
}