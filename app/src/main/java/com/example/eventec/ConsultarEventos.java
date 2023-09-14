package com.example.eventec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ConsultarEventos extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_eventos);

        mFirestore = FirebaseFirestore.getInstance();
        Button btnAgregarColaboradores = findViewById(R.id.btn_AgregarColaboradores);
        btnAgregarColaboradores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultarEventos.this, RegistrarAsociaciones.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
