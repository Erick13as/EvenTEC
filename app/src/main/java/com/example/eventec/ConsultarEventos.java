package com.example.eventec;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ConsultarEventos extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_eventos);

        mFirestore = FirebaseFirestore.getInstance();
    }
}
