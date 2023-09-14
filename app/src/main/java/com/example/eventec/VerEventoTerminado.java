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


        mFirestore = FirebaseFirestore.getInstance();
        Button btnAgregarColaboradores = findViewById(R.id.buttonCalificar);
        btnAgregarColaboradores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerEventoTerminado.this, RegistrarAsociaciones.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
