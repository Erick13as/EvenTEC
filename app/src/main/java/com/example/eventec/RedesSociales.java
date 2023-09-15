package com.example.eventec;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Random;


import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.FirebaseFirestore;

public class RedesSociales extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    private Button btnOpenInstagram;
    private Button salir;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redes_sociales);

        mFirestore = FirebaseFirestore.getInstance();

        btnOpenInstagram = findViewById(R.id.miBoton);
        salir = findViewById(R.id.button);
        spinner = findViewById(R.id.spinner);

        int random = generateRandomNumber(1, 1000);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, random);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);



        btnOpenInstagram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openInstagramProfile("teccostarica");


            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                abrirLobbyAsociaciones();


            }
        });






    }

    private void abrirLobbyAsociaciones() {
        Intent intent = new Intent(this, LobbyAsociaciones.class);
        startActivity(intent);
    }

    private void openInstagramProfile(String username) {
        String instagramUrl = "https://www.instagram.com/" + username;
        Uri uri = Uri.parse(instagramUrl);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        // Comprueba si la aplicación de Instagram está instalada en el dispositivo
        PackageManager packageManager = getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        } else {
            // Si Instagram no está instalado, puedes abrir Instagram en un navegador web.
            String webUrl = "https://www.instagram.com/" + username;
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
            startActivity(webIntent);
        }
    }

    public static int generateRandomNumber(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("El valor mínimo debe ser menor que el valor máximo");
        }

        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }




}
