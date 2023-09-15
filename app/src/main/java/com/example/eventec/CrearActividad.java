package com.example.eventec;

        import android.app.DatePickerDialog;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Button;
        import android.widget.Toast;

        import androidx.appcompat.app.AppCompatActivity;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;

public class CrearActividad extends AppCompatActivity {

    private EditText editTextFecha;
    private Spinner spinnerHorasI;
    private Spinner spinnerHorasF;
    private Spinner spinnerEventos;
    private EditText editTextUbicacion;
    private EditText editTextDescripcion;
    private EditText editTextRecursos;
    private TextView textViewActividad;
    private Button buttonGuardar;

    private FirebaseFirestore db;
    private ArrayAdapter<String> adapter;
    private List<String> eventosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_actividad);

        editTextFecha = findViewById(R.id.editTextFecha);
        spinnerHorasI = findViewById(R.id.spinnerHoraI);
        spinnerHorasF = findViewById(R.id.spinnerHoraF);
        spinnerEventos = findViewById(R.id.spinnerEventos);
        editTextUbicacion = findViewById(R.id.editTextUbicacion);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        editTextRecursos = findViewById(R.id.editTextRecursos);
        textViewActividad = findViewById(R.id.textViewActividad);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        db = FirebaseFirestore.getInstance();

        // Crear una lista de horas (puedes personalizarla según tus necesidades)
        String[] horas = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, horas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerHorasI.setAdapter(adapter);
        spinnerHorasF.setAdapter(adapter);

        // Obtener datos de eventos y cargar en el spinnerEventos
        obtenerEventosParaSpinner();

        // Configura el onClickListener para el botón Guardar
        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llama al método para crear una nueva actividad en Firestore
                crearNuevaActividadEnFirestore();
                // Abre la pantalla LobbyAsociaciones
                abrirLobbyAsociaciones();
            }
        });
    }

    private void obtenerEventosParaSpinner() {
        db.collection("evento")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String nombreEvento = document.getString("nombre");
                            eventosList.add(nombreEvento);
                        }

                        // Llenar el spinnerEventos con la lista de nombres de eventos
                        ArrayAdapter<String> eventosAdapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                eventosList
                        );

                        eventosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerEventos.setAdapter(eventosAdapter);
                    } else {
                        // Error al obtener los documentos
                    }
                });
    }

    private void crearNuevaActividadEnFirestore() {
        // Obtén los valores actuales de las vistas
        String fecha = editTextFecha.getText().toString();
        String horaInicio = spinnerHorasI.getSelectedItem().toString();
        String horaCierre = spinnerHorasF.getSelectedItem().toString();
        String idEvento = spinnerEventos.getSelectedItem().toString();
        String ubicacion = editTextUbicacion.getText().toString();
        String descripcion = editTextDescripcion.getText().toString();
        String recursos = editTextRecursos.getText().toString();

        // Crea un nuevo documento en Firestore para la actividad
        db.collection("actividad")
                .add(new Actividad(fecha, horaInicio, horaCierre, idEvento, ubicacion, descripcion, recursos))
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Datos de la actividad creados correctamente
                            // Puedes mostrar un mensaje de éxito o realizar otra acción
                            Toast.makeText(CrearActividad.this, "Actividad creada exitosamente", Toast.LENGTH_SHORT).show();
                            abrirLobbyAsociaciones();
                        } else {
                            // Error al crear la actividad
                            // Maneja el error según tus necesidades
                        }
                    }
                });
    }

    public void showDatePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        editTextFecha.setText(selectedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void abrirLobbyAsociaciones() {
        Intent intent = new Intent(this, LobbyAsociaciones.class);
        startActivity(intent);
    }
}
