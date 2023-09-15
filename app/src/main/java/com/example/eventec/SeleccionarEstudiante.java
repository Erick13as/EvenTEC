package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SeleccionarEstudiante extends AppCompatActivity {

    private EditText editTextCarnetEstudiante;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_estudiante);

        // Inicializa la referencia a Firestore
        db = FirebaseFirestore.getInstance();

        editTextCarnetEstudiante = findViewById(R.id.editTextCarnetEstudiante);

        Button buttonVerEve = findViewById(R.id.buttonVerEve);

        buttonVerEve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí se ejecutará el código cuando se haga clic en el botón

                // Obtener el texto ingresado en el EditText
                String carnetEstudiante = editTextCarnetEstudiante.getText().toString();

                // Llamar al método para buscar el usuario en Firestore
                buscarUsuarioEnFirestore(carnetEstudiante);
            }
        });
    }

    // Método para buscar un usuario en Firestore
    private void buscarUsuarioEnFirestore(String carnetEstudiante) {
        // Realiza una consulta en Firestore para buscar usuarios con el carnet especificado
        Query query = db.collection("usuario").whereEqualTo("carnet", carnetEstudiante);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Se encontraron resultados, asume que solo hay un resultado y obtén ese documento
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                    // Obtén los datos del documento y crea una instancia de la clase User
                    String nombre = document.getString("nombre");
                    String apellido = document.getString("apellido");
                    String apellido2 = document.getString("apellido2");
                    String carnet = document.getString("carnet");
                    String contraseña = document.getString("contraseña");
                    String correo = document.getString("correo");
                    String carrera = document.getString("carrera");
                    String sede = document.getString("sede");
                    String descripcion = document.getString("descripcion");
                    String idTipo = document.getString("idTipo");

                    User user = new User(nombre, apellido, apellido2, carnet, contraseña,correo,carrera, sede,descripcion, idTipo);

                    // Crear un Intent para abrir la pantalla GestionarEstudiantes
                    Intent intent = new Intent(this, GestionarEstudiantes.class);

                    // Pasar el objeto user como un extra en el Intent
                    intent.putExtra("user", user);

                    // Iniciar la actividad GestionarEstudiantes
                    startActivity(intent);
                    // Ahora puedes utilizar la instancia de User según tus necesidades
                } else {
                    // No se encontraron resultados, maneja este caso apropiadamente
                }
            } else {
                // Maneja el caso de error
            }
        });
    }
}
