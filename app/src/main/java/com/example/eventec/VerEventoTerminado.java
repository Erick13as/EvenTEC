package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class VerEventoTerminado extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private TextView editViewCantidadEve;
    private TextView editViewFechaEve;
    private TextView editViewHoraIEve;
    private TextView editViewHoraFEve;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_evento_terminado);

        editViewCantidadEve = findViewById(R.id.textViewSedeEvento);

        String nombreActividad="";
        mFirestore = FirebaseFirestore.getInstance();
        if (getIntent().hasExtra("eventoDatos")) {
            EventoCalificar eventoDatos = (EventoCalificar) getIntent().getSerializableExtra("eventoDatos");

            // Now you can use the data as needed
            nombreActividad = eventoDatos.getNombre();
            String fechaEvento = eventoDatos.getFechaInicio();
            String horaInicio = eventoDatos.getHoraInicio();
            String horaFin = eventoDatos.getHoraFin();
            String cantidadEventos = eventoDatos.getCapacidad();

            // Do something with the data, e.g., display it in TextViews or perform other actions
        }
        Button btnAgregarColaboradores = findViewById(R.id.buttonCalificar);
        String finalNombreActividad = nombreActividad;
        btnAgregarColaboradores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(VerEventoTerminado.this, Encuesta.class);
                intent.putExtra("eventoEncuestar", finalNombreActividad); // Pasa el objeto de usuario
                startActivity(intent);
                finish();
            }
        });

    }
}
