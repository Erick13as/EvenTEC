package com.example.eventec;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class CalendarioEventos extends AppCompatActivity {

    private Button[] monthButtons;
    private Button[] dayButtons;
    private int currentMonth = 0; // Indica el mes seleccionado actualmente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_eventos);

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
        dayButtons = new Button[31]; // Asumiendo un máximo de 31 días
        for (int i = 0; i < 31; i++) {
            Button dayButton = new Button(this);
            dayButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            dayButton.setText(String.valueOf(i + 1));

            // Configurar el fondo predeterminado de los botones de los días
            dayButton.setBackgroundResource(R.drawable.button_day_default_background);

            // Configurar el color del texto a negro
            dayButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));

            dayButton.setVisibility(View.GONE); // Ocultar los botones inicialmente
            dayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Deseleccionar todos los botones de los días (cambiar su fondo a predeterminado)
                    for (int j = 0; j < dayButtons.length; j++) {
                        dayButtons[j].setBackgroundResource(R.drawable.button_day_default_background);
                    }

                    // Establecer el fondo del botón presionado
                    dayButton.setBackgroundResource(R.drawable.button_day_pressed_background);

                    // Lógica para manejar la selección del día aquí
                }
            });
            dayLinearLayout.addView(dayButton);
            dayButtons[i] = dayButton;
        }

        // Configurar el ScrollView para los días para que pueda desplazarse horizontalmente
        HorizontalScrollView dayScrollView = findViewById(R.id.dayScrollView);
        dayScrollView.setHorizontalScrollBarEnabled(true);

        // Configurar los listeners de clic para los botones de los meses
        for (int i = 0; i < monthButtons.length; i++) {
            final int monthIndex = i;
            monthButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Deselect all months first (clear background)
                    for (int j = 0; j < monthButtons.length; j++) {
                        monthButtons[j].setBackground(ContextCompat.getDrawable(
                                CalendarioEventos.this,
                                android.R.color.transparent));
                    }

                    // Select the clicked month
                    monthButtons[monthIndex].setBackground(ContextCompat.getDrawable(
                            CalendarioEventos.this,
                            R.drawable.spinner_background));

                    // Show the days for the selected month
                    showDaysForMonth(monthIndex);

                    currentMonth = monthIndex; // Update the current month
                }
            });
        }
    }

    private void showDaysForMonth(int monthIndex) {
        // Muestra los botones de los días para el mes seleccionado
        // Asegúrate de ocultar los botones de los días de otros meses.
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int daysInSelectedMonth = daysInMonth[monthIndex]; // Obtener el número de días del mes seleccionado


        for (int i = 0; i < dayButtons.length; i++) {
            // Compara el día actual con el número de días en el mes seleccionado
            if (i < daysInSelectedMonth) {
                dayButtons[i].setVisibility(View.VISIBLE);
            } else {
                dayButtons[i].setVisibility(View.GONE);
            }
        }
    }


    // Función para obtener el número de días en un mes dado
    private int getDaysInMonth(int month) {
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return daysInMonth[month];
    }

    private void hideDaysForMonth(int monthIndex) {
        // Oculta los botones de los días para el mes no seleccionado
        // Puedes implementar lógica específica para ocultar los días aquí
        // Asegúrate de ocultar los botones de los días de otros meses.
        for (int i = 0; i < dayButtons.length; i++) {
            dayButtons[i].setVisibility(View.GONE);
        }
    }
}
