package com.example.eventec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BuscarColaborador extends AppCompatActivity {

    private TextView resultadosTextView; // El TextView donde mostrarás los resultados
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_colaborador);

        resultadosTextView = findViewById(R.id.textView); // Asigna el TextView desde el archivo XML
        resultadosTextView.setTextColor(getResources().getColor(android.R.color.white)); // Cambia el color del texto a blanco

        linearLayout = findViewById(R.id.linearLayout); // Asigna el LinearLayout desde el archivo XML

        Button buscarButton = findViewById(R.id.btn_Buscar);
        Button eliminarButton = findViewById(R.id.btn_Eliminar);
        Button btn_Editar = findViewById(R.id.btn_Editar);
        eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView carnetEditText = findViewById(R.id.idCarnetColab);
                String carnet = carnetEditText.getText().toString();

                if (!carnet.isEmpty()) {
                    eliminarColaborador(carnet);
                } else {
                    Toast.makeText(BuscarColaborador.this, "Ingrese un carnet válido", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(com.example.eventec.BuscarColaborador.this, LobbyColaboradores.class);
                startActivity(intent);

                // Cierra la actividad actual (opcional, si deseas volver atrás)
                finish();
            }
        });
        btn_Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TextView carnetEditText = findViewById(R.id.idCarnetColab);
                String carnet = carnetEditText.getText().toString();

                if (!carnet.isEmpty()) {
                    editarColaborador(carnet);
                } else {
                    Toast.makeText(BuscarColaborador.this, "Ingrese un carnet válido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buscarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView carnetEditText = findViewById(R.id.idCarnetColab);
                String carnet = carnetEditText.getText().toString();

                if (!carnet.isEmpty()) {
                    buscarColaborador(carnet);
                } else {
                    Toast.makeText(BuscarColaborador.this, "Ingrese un carnet válido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void eliminarColaborador(String carnet) {
        // Obtiene una referencia a la base de datos Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtiene una referencia a la colección "usuarios" en Firestore
        CollectionReference usuariosRef = db.collection("colaborador");

        // Crea una consulta para buscar al usuario por el carnet
        Query query = usuariosRef.whereEqualTo("carnet", carnet)
                .whereEqualTo("idTipo", "Colaborador");

        // Ejecuta la consulta
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Verifica si se encontraron resultados
                    if (!task.getResult().isEmpty()) {
                        // Si se encontró un usuario, elimínalo
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            usuariosRef.document(document.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(BuscarColaborador.this, "Colaborador eliminado correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(BuscarColaborador.this, "Error al eliminar el colaborador", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Si no se encontraron resultados, muestra un mensaje
                        Toast.makeText(BuscarColaborador.this, "No se encontraron colaboradores con ese carnet.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si ocurre un error, muestra un mensaje de error
                    Toast.makeText(BuscarColaborador.this, "Error al buscar colaborador", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void editarColaborador(String carnet) {
        // Obtiene una referencia a la base de datos Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtiene una referencia a la colección "usuarios" en Firestore
        CollectionReference usuariosRef = db.collection("colaborador");

        // Crea una consulta para buscar por carnet e idtipo igual a "colaborador"
        Query query = usuariosRef.whereEqualTo("carnet", carnet)
                .whereEqualTo("idTipo", "Colaborador");

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
                        String carnet = document.getString("carnet");
                        String sede = document.getString("sede");

                        // Crea un Intent para iniciar la actividad EditarColaborador
                        Intent intentEditar = new Intent(BuscarColaborador.this, EditarColaborador.class);

                        // Agrega los datos como extras en el Intent
                        intentEditar.putExtra("nombre", nombre);
                        intentEditar.putExtra("carnet", carnet);
                        intentEditar.putExtra("correo", correo);
                        intentEditar.putExtra("apellido", apellido);
                        intentEditar.putExtra("apellido2", apellido2);
                        intentEditar.putExtra("carrera", carrera);
                        intentEditar.putExtra("descripcion", descripcion);
                        intentEditar.putExtra("sede", sede);
                        startActivity(intentEditar);
                    }

                    if (resultadosTextView.getText().toString().isEmpty()) {
                        // Si no se encontraron resultados, muestra un mensaje
                        resultadosTextView.setText("No se encontraron colaboradores con ese carnet.");
                    }
                } else {
                    // Si ocurre un error, muestra un mensaje de error
                    Toast.makeText(BuscarColaborador.this, "Error al buscar colaborador", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void buscarColaborador(String carnet) {
        // Obtiene una referencia a la base de datos Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtiene una referencia a la colección "usuarios" en Firestore
        CollectionReference usuariosRef = db.collection("colaborador");

        // Crea una consulta para buscar por carnet e idtipo igual a "colaborador"
        Query query = usuariosRef.whereEqualTo("carnet", carnet)
                .whereEqualTo("idTipo", "Colaborador");

        // Ejecuta la consulta
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Elimina el texto anterior de resultadosTextView
                    resultadosTextView.setText("");

                    // Procesa los resultados de la consulta
                    boolean encontrados = false; // Variable para verificar si se encontraron resultados

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String nombre = document.getString("nombre");
                        String correo = document.getString("correo");
                        String apellido = document.getString("apellido");
                        String apellido2 = document.getString("apellido2");
                        String carrera = document.getString("carrera");
                        String descripcion = document.getString("descripcion");
                        String carnet = document.getString("carnet");
                        String sede = document.getString("sede");

                        // Concatena los nuevos resultados con el contenido previo
                        String nuevoContenido = resultadosTextView.getText().toString() +
                                "Nombre: " + nombre + " " + apellido + " " + apellido2 +
                                "\nCorreo: " + correo +
                                "\nCarrera: " + carrera +
                                "\nDescripcion: " + descripcion +
                                "\nSede: " + sede + "\n\n";

                        // Actualiza el contenido del TextView con el nuevo contenido
                        resultadosTextView.setText(nuevoContenido);
                        encontrados = true; // Se encontraron resultados
                    }

                    if (!encontrados) {
                        // Si no se encontraron resultados, muestra un mensaje
                        resultadosTextView.setText("No se encontraron colaboradores con ese carnet.");
                    }
                } else {
                    // Si ocurre un error, muestra un mensaje de error
                    Toast.makeText(BuscarColaborador.this, "Error al buscar colaborador", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
