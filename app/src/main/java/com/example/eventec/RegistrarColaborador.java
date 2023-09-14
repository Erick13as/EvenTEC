package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.Spinner;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.widget.Toast;
import androidx.annotation.NonNull;

public class RegistrarColaborador extends AppCompatActivity {

    private TextInputEditText nombreColab;
    private TextInputEditText Apellido1;
    private TextInputEditText Apellido2;
    private TextInputEditText CarnetColab;
    private TextInputEditText CorreoColab;
    private Spinner spinnerSedeColab;
    private TextInputEditText DescripcionColab;
    private TextInputEditText CarreraColab;
    private FirebaseFirestore mFirestore;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_colaborador);

        // Inicializa Firestore
        mFirestore = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();

        // Obtén el Spinner y configura su adaptador
         spinnerSedeColab = findViewById(R.id.spinnerSedeColab);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sedes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSedeColab.setAdapter(adapter);

        // Inicializa los elementos de la interfaz
        nombreColab = findViewById(R.id.nombreColab);
        Apellido1 = findViewById(R.id.Apellido1);
        Apellido2 = findViewById(R.id.Apellido2);
        CarnetColab = findViewById(R.id.CarnetColab);
        CorreoColab = findViewById(R.id.CorreoColab);
        DescripcionColab = findViewById(R.id.DescripcionColab);
        CarreraColab = findViewById(R.id.CarreraColab);


        Button btnGuardar = findViewById(R.id.btn_Guardar);
        Button btnCancelar = findViewById(R.id.btn_Cancelar);

        // Configura el evento de clic para el botón "Guardar"
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDataToFirestore();
                Intent intent = new Intent(RegistrarColaborador.this, LobbyColaboradores.class);
                startActivity(intent);

                // Cierra la actividad actual (opcional, si deseas volver atrás)
                finish();
            }
        });

        // Configura el evento de clic para el botón "Cancelar"
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrarColaborador.this, LobbyColaboradores.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public class Colaborador {
        private String nombre;

        private String apellido;

        private String apellido2;
        private String carnet;
        private String contraseña;
        private String correo;
        private String carrera;
        private String sede;
        private String idTipo;
        private String descripcion;


        public Colaborador(String apellido,String apellido2, String carnet, String carrera, String correo, String descripcion, String idTipo, String nombre, String sede) {
            this.nombre = nombre;
            this.apellido= apellido;
            this.apellido2=apellido2;
            this.contraseña = contraseña;
            this.carnet = carnet;
            this.correo = correo;
            this.carrera= carrera;
            this.sede = sede;
            this.descripcion = descripcion;
            this.idTipo = idTipo;
        }

        public String getNombre() {
            return nombre;
        }
        public String getApellido() {
            return apellido;
        }
        public String getApellido2() {
            return apellido2;
        }
        public String getContraseña() {
            return contraseña;
        }

        public String getCarnet() {
            return carnet;
        }

        public String getCorreo() {
            return correo;
        }

        public String getSede() {
            return sede;
        }

        public String getDescripcion() {
            return descripcion;
        }
        public String getIdTipo() { return idTipo;}
        public String getCarrera() {
            return carrera;
        }
    }
    private void uploadDataToFirestore() {
        String nombre = nombreColab.getText().toString();
        String apellido = Apellido1.getText().toString();
        String apellido2 = Apellido2.getText().toString();
        String carnet = CarnetColab.getText().toString();
        String correo = CorreoColab.getText().toString();
        String sede = spinnerSedeColab.getSelectedItem().toString(); // Obtener valor del Spinner
        String descripcion = DescripcionColab.getText().toString();
        String carrera = CarreraColab.getText().toString();
        String idTipo = "Colaborador";
        String contraseña = "contraseña";

        CollectionReference usersCollection = mFirestore.collection("colaborador");
        // Comprueba si los campos están vacíos
        if (nombre.isEmpty() || carnet.isEmpty() || correo.isEmpty() || sede.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(RegistrarColaborador.this, "¡Error: Campos vacíos!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            usersCollection.whereEqualTo("correo", correo).whereEqualTo("idTipo", "Colaborador").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // A user with the same email already exists
                            Toast.makeText(RegistrarColaborador.this, "¡Error: Colaborador ya registrado!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Check if a user with the same carnet already exists
                            usersCollection.whereEqualTo("carnet", carnet).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().isEmpty()) {
                                            // A user with the same carnet already exists
                                            Toast.makeText(RegistrarColaborador.this, "¡Error: Colaborador ya registrado!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Create a new User object
                                            Colaborador colaborador = new Colaborador(apellido, apellido2,  carnet,  carrera, correo,  descripcion,  idTipo, nombre, sede);

                                            // Upload the user data to Firestore
                                            usersCollection.add(colaborador)
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (task.isSuccessful()) {
                                                                // Data successfully uploaded to Firestore3
                                                                // You can perform any desired actions here
                                                                // For example, display a success message
                                                                Toast.makeText(RegistrarColaborador.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                // Failed to upload data to Firestore
                                                                // You can handle the error here
                                                                Toast.makeText(RegistrarColaborador.this, "Registro de usuario fallido", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        // Handle the error here
                                        Toast.makeText(RegistrarColaborador.this, "Error al verificar el carnet del usuario", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        // Handle the error here
                        Toast.makeText(RegistrarColaborador.this, "Error al verificar el correo del usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}