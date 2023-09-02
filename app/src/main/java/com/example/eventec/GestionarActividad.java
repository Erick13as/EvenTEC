package com.example.eventec;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class GestionarActividad extends AppCompatActivity {

    private EditText editTextFecha;
    private Spinner spinnerHorasI;
    private Spinner spinnerHorasF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_actividad);

        editTextFecha = findViewById(R.id.editTextFecha);
        spinnerHorasI = findViewById(R.id.spinnerHoraI); // Cambia a spinnerHorasF
        spinnerHorasF = findViewById(R.id.spinnerHoraF); // Asegúrate de que el ID sea correcto

        // Crear una lista de horas (puedes personalizarla según tus necesidades)
        String[] horas = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

        // Crear un adaptador para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, horas);

        // Especificar el diseño para la lista de opciones (opcional)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Configurar el adaptador en el Spinner
        spinnerHorasI.setAdapter(adapter);
        spinnerHorasF.setAdapter(adapter); // Configura el adaptador para spinnerHorasF
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
}
