package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrarAsociaciones extends AppCompatActivity {

    private EditText nombreAsociacion, idCarnetMiembro, idCorreoAsociacion, idSedeAsociacion, idDescripcionAsociacion, idCodCarreraAsociacion;
    private TextView textViewMiembros;
    private List<String> miembrosList = new ArrayList<>(); // Lista para almacenar números de carné de miembros

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_asociaciones);

        nombreAsociacion = findViewById(R.id.nombreAsociacion);
        idCarnetMiembro = findViewById(R.id.idCarnetMiembro);
        idCorreoAsociacion = findViewById(R.id.idCorreoAsociacion);
        idSedeAsociacion = findViewById(R.id.idSedeAsociacion);
        idDescripcionAsociacion = findViewById(R.id.idDescripcionAsociacion);
        idCodCarreraAsociacion = findViewById(R.id.idCodCarreraAsociacion);
        textViewMiembros = findViewById(R.id.textViewMiembros);
        Button btnGuardar = findViewById(R.id.btn_Guardar);
        Button btnSumarMiembro = findViewById(R.id.btn_SumarMiembro);
        Button btnRestarMiembro = findViewById(R.id.btn_RestarMiembro);

        btnRestarMiembro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarCarnetIngresado();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatosEnFirestore();
                Intent intent = new Intent(RegistrarAsociaciones.this, LobbyAsociaciones.class);
                startActivity(intent);
                finish();
            }
        });

        btnSumarMiembro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarMiembro();
            }
        });
    }

    private void eliminarCarnetIngresado() {
        // Obtén el carné ingresado en el EditText
        String carnetIngresado = idCarnetMiembro.getText().toString().trim();

        // Comprueba si el carné ingresado está en la lista y elimínalo si existe
        if (miembrosList.contains(carnetIngresado)) {
            miembrosList.remove(carnetIngresado);

            // Actualiza el contenido del EditText con la lista de carnés actualizada
            actualizarTextViewMiembros();
        } else {
            Toast.makeText(this, "El número de carné no está en la lista", Toast.LENGTH_SHORT).show();
        }

        // Limpia el contenido del EditText
        idCarnetMiembro.getText().clear();
    }

    private void actualizarTextViewMiembros() {
        StringBuilder miembrosText = new StringBuilder("Miembros:\n");
        for (String miembro : miembrosList) {
            miembrosText.append(miembro).append("\n");
        }
        textViewMiembros.setText(miembrosText.toString());
    }

    private void agregarMiembro() {
        String carnetMiembro = idCarnetMiembro.getText().toString().trim();

        if (!carnetMiembro.isEmpty()) {
            if (!miembrosList.contains(carnetMiembro)) {
                miembrosList.add(carnetMiembro);
                actualizarTextViewMiembros();
            } else {
                Toast.makeText(this, "El número de carné ya está en la lista", Toast.LENGTH_SHORT).show();
            }
            idCarnetMiembro.getText().clear(); // Limpiar el campo de ingreso de carné
        } else {
            Toast.makeText(this, "Por favor, ingrese un número de carné", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarDatosEnFirestore() {
        String nombre = nombreAsociacion.getText().toString().trim();
        String correoAsociacion = idCorreoAsociacion.getText().toString().trim();
        String sedeAsociacion = idSedeAsociacion.getText().toString().trim();
        String descripcionAsociacion = idDescripcionAsociacion.getText().toString().trim();
        String codCarreraAsociacion = idCodCarreraAsociacion.getText().toString().trim();

        if (nombre.isEmpty() || correoAsociacion.isEmpty() || sedeAsociacion.isEmpty() || descripcionAsociacion.isEmpty() || codCarreraAsociacion.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un nuevo objeto Map para almacenar los datos que deseas agregar a Firestore
        Map<String, Object> data = new HashMap<>();
        data.put("nombre", nombre);
        data.put("miembros", miembrosList); // Aquí se guarda la lista de carnés de miembros
        data.put("correoAsociacion", correoAsociacion);
        data.put("sedeAsociacion", sedeAsociacion);
        data.put("descripcionAsociacion", descripcionAsociacion);
        data.put("codCarreraAsociacion", codCarreraAsociacion);

        // Obtener una instancia de Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Agregar los datos a la colección "asociaciones" en Firestore
        db.collection("asociacion")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    // Éxito al agregar los datos
                    Toast.makeText(this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show();
                    // Puedes realizar otras acciones aquí después de guardar los datos
                })
                .addOnFailureListener(e -> {
                    // Error al agregar los datos
                    Toast.makeText(this, "Error al guardar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
