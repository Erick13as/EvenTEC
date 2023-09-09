package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarioEventos extends AppCompatActivity {

    private Button[] monthButtons;
    private Button[] dayButtons;
    private Button[] hourButtons;
    private int selectedMonth = -1;
    private int selectedDay = -1;
    private int selectedHour = -1;
    private Button lastPressedEventButton = null;
    private User user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_eventos);

        user = (User) getIntent().getSerializableExtra("user");

        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Obtener el mes y el día actual
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Inicializar los botones de los meses
        monthButtons = new Button[]{
                findViewById(R.id.eneroButton),
                findViewById(R.id.febreroButton),
                findViewById(R.id.marzoButton),
                findViewById(R.id.abrilButton),
                findViewById(R.id.mayoButton),
                findViewById(R.id.junioButton),
                findViewById(R.id.julioButton),
                findViewById(R.id.agostoButton),
                findViewById(R.id.septiembreButton),
                findViewById(R.id.octubreButton),
                findViewById(R.id.noviembreButton),
                findViewById(R.id.diciembreButton)
        };

        // Inicializar los botones de los días
        LinearLayout dayLinearLayout = findViewById(R.id.dayLinearLayout);
        dayButtons = new Button[31];
        for (int i = 0; i < 31; i++) {
            Button dayButton = new Button(this);
            dayButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            dayButton.setText(String.valueOf(i + 1));
            dayButton.setBackgroundResource(R.drawable.button_day_default_background);
            dayButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            dayButton.setVisibility(View.GONE);
            final int day = i + 1;
            dayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectDay(day);
                }
            });
            dayLinearLayout.addView(dayButton);
            dayButtons[i] = dayButton;
        }

        // Inicializar los botones de las horas
        LinearLayout hourLinearLayout = findViewById(R.id.hourLinearLayout);
        hourButtons = new Button[13];
        for (int i = 0; i < 13; i++) {
            Button hourButton = new Button(this);
            hourButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            int hour = i + 8;
            hourButton.setText(String.format("%02d:00", hour));
            hourButton.setBackgroundResource(R.drawable.button_day_default_background);
            hourButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            hourButton.setVisibility(View.GONE);
            final int selectedHour = hour;
            hourButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectHour(selectedHour);
                }
            });
            hourLinearLayout.addView(hourButton);
            hourButtons[i] = hourButton;
        }

        // Configurar automáticamente el mes, día y hora seleccionados
        selectMonth(currentMonth);
        selectDay(currentDay);

        // Configura el ScrollView de meses
        setupMonthScrollView();
    }

    private void loadEventsFromFirestore() {
        if (selectedMonth != -1 && selectedDay != -1 && selectedHour != -1) {
            // Realiza la consulta en Firestore con los filtros adecuados
            Log.d("CalendarioEventos", "Consulta Firestore: Mes " + selectedMonth + ", Día " + selectedDay + ", Hora " + selectedHour);

            // Realiza la consulta en Firestore para obtener todos los eventos
            db.collection("evento")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> eventNames = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Obtener la fecha de inicio del evento en formato "dd-MM-yyyy"
                                    String fechaInicio = document.getString("fechaInicio"); // Reemplaza "fechaInicio" con el nombre del campo en tu documento
                                    String fechaFin = document.getString("fechaFin");
                                    String horaInicio = document.getString("horaInicio");
                                    String horaFin = document.getString("horaFin");

                                    // Dividir la fecha en partes usando el guión "-"
                                    String[] partesFechaI = fechaInicio.split("-");
                                    String[] partesFechaF = fechaFin.split("-");
                                    String[] partesHoraI = horaInicio.split(":");
                                    String[] partesHoraF = horaFin.split(":");

                                    // Obtener el mes y el día
                                    String mesEventoI = partesFechaI[1]; // Obtenemos el mes (índice 1 del arreglo)
                                    String mesEventoF = partesFechaF[1];
                                    String diaEventoI = partesFechaI[0]; // Obtenemos el día (índice 0 del arreglo)
                                    String diaEventoF = partesFechaF[0];
                                    String horaEventoI = partesHoraI[0];
                                    String horaEventoF = partesHoraF[0];

                                    // Verificar si la fecha coincide con la selección actual
                                    if ((Integer.parseInt(mesEventoI)-1 < selectedMonth + 1 && Integer.parseInt(mesEventoF)+1 > selectedMonth + 1)
                                            && (Integer.parseInt(diaEventoI)-1 < selectedDay && Integer.parseInt(diaEventoF)+1 > selectedDay)
                                            && (Integer.parseInt(horaEventoI)-1 < selectedHour && Integer.parseInt(horaEventoF)+1 > selectedHour)) {
                                        // Agregar el nombre del evento a la lista
                                        String eventName = document.getString("nombre"); // Reemplaza "nombre" con el nombre del campo en tu documento
                                        eventNames.add(eventName);
                                    }
                                }
                                // Llamar a la función para mostrar los eventos
                                displayEvents(eventNames);
                            } else {
                                // Manejar errores si la obtención de datos falla
                                Log.e("CalendarioEventos", "Error al obtener eventos de Firestore", task.getException());
                            }
                        }
                    });
        } else {
            // Si no se han seleccionado mes, día y hora, puedes mostrar un mensaje o hacer otra acción
            Log.d("CalendarioEventos", "No se han seleccionado Mes, Día y Hora");
        }
    }

    private void displayEvents(List<String> eventNames) {
        LinearLayout eventLinearLayout = findViewById(R.id.eventLinearLayout);
        eventLinearLayout.setOrientation(LinearLayout.VERTICAL); // Establecer orientación vertical

        // Agregar margen inferior a los botones de eventos
        int marginBetweenButtons = 16; // Puedes ajustar este valor según tus preferencias

        for (String eventName : eventNames) {
            final Button eventButton = new Button(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, marginBetweenButtons); // Margen inferior entre botones
            eventButton.setLayoutParams(layoutParams);

            eventButton.setText(eventName);
            eventButton.setBackgroundResource(R.drawable.event_button_selector); // Usar el selector
            eventButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            eventButton.setGravity(Gravity.CENTER);
            eventButton.setTextSize(16);
            eventButton.setPadding(8, 8, 8, 8);

            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Desactivar el color de fondo del último botón de evento presionado
                    if (lastPressedEventButton != null) {
                        lastPressedEventButton.setSelected(false);
                    }

                    // Activar el color de fondo del botón de evento actual
                    eventButton.setSelected(true);
                    lastPressedEventButton = eventButton;

                    // Obtener el nombre del evento seleccionado
                    String selectedEventName = eventButton.getText().toString();

                    // Crear un Intent para abrir la actividad SeleccionarEvento
                    Intent intent = new Intent(CalendarioEventos.this, SeleccionarEventos.class);

                    // Agregar datos extras al Intent (nombre del evento)
                    intent.putExtra("event_name", selectedEventName);
                    intent.putExtra("user", user);

                    // Iniciar la actividad SeleccionarEvento
                    startActivity(intent);
                }
            });

            eventLinearLayout.addView(eventButton);
        }
    }

    private void showDaysForMonth(int monthIndex) {
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int daysInSelectedMonth = daysInMonth[monthIndex];

        for (int i = 0; i < dayButtons.length; i++) {
            if (i < daysInSelectedMonth) {
                dayButtons[i].setVisibility(View.VISIBLE);
            } else {
                dayButtons[i].setVisibility(View.GONE);
            }
        }
    }

    private void showHours() {
        for (int i = 0; i < hourButtons.length; i++) {
            hourButtons[i].setVisibility(View.VISIBLE);
        }
    }

    private void selectMonth(int monthIndex) {
        for (int j = 0; j < monthButtons.length; j++) {
            monthButtons[j].setBackground(ContextCompat.getDrawable(
                    CalendarioEventos.this,
                    android.R.color.transparent));
        }
        monthButtons[monthIndex].setBackground(ContextCompat.getDrawable(
                CalendarioEventos.this,
                R.drawable.spinner_background));
        showDaysForMonth(monthIndex);
        showHours();
        selectedMonth = monthIndex;

        // Limpia la vista de eventos antes de cargar nuevos eventos
        clearEventView();

        // Intenta cargar eventos nuevamente
        loadEventsFromFirestore();

        // Desplazar el ScrollView de meses al mes seleccionado
        scrollMonthScrollViewToSelectedMonth();
    }

    private void selectDay(int day) {
        for (int j = 0; j < dayButtons.length; j++) {
            dayButtons[j].setBackgroundResource(R.drawable.button_day_default_background);
        }
        dayButtons[day - 1].setBackgroundResource(R.drawable.button_day_pressed_background);
        selectedDay = day;

        // Limpia la vista de eventos antes de cargar nuevos eventos
        clearEventView();

        // Intenta cargar eventos nuevamente
        loadEventsFromFirestore();

        // Desplazar el ScrollView de días al día seleccionado
        scrollDayScrollViewToSelectedDay();
    }

    private void selectHour(int hour) {
        for (int j = 0; j < hourButtons.length; j++) {
            hourButtons[j].setBackgroundResource(R.drawable.button_day_default_background);
        }
        hourButtons[hour - 8].setBackgroundResource(R.drawable.button_day_pressed_background);
        selectedHour = hour;

        // Limpia la vista de eventos antes de cargar nuevos eventos
        clearEventView();

        // Intenta cargar eventos nuevamente
        loadEventsFromFirestore();
    }

    private void clearEventView() {
        LinearLayout eventLinearLayout = findViewById(R.id.eventLinearLayout);
        eventLinearLayout.removeAllViews(); // Elimina todos los eventos existentes
    }

    private void setupMonthScrollView() {
        HorizontalScrollView monthScrollView = findViewById(R.id.monthScrollView);
        monthScrollView.setHorizontalScrollBarEnabled(true);

        // Configurar los listeners de clic para los botones de los meses
        for (int i = 0; i < monthButtons.length; i++) {
            final int monthIndex = i;
            monthButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectMonth(monthIndex);
                }
            });
        }
    }

    private void scrollMonthScrollViewToSelectedMonth() {
        HorizontalScrollView monthScrollView = findViewById(R.id.monthScrollView);
        Button selectedMonthButton = monthButtons[selectedMonth];
        int scrollX = selectedMonthButton.getLeft() - (monthScrollView.getWidth() - selectedMonthButton.getWidth()) / 2;
        monthScrollView.smoothScrollTo(scrollX, 0);
    }

    private void scrollDayScrollViewToSelectedDay() {
        HorizontalScrollView dayScrollView = findViewById(R.id.dayScrollView);
        Button selectedDayButton = dayButtons[selectedDay - 1];
        int scrollX = selectedDayButton.getLeft() - (dayScrollView.getWidth() - selectedDayButton.getWidth()) / 2;
        dayScrollView.smoothScrollTo(scrollX, 0);
    }
}
