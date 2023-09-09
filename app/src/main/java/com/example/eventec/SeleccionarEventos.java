package com.example.eventec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SeleccionarEventos extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_eventos);

        db = FirebaseFirestore.getInstance();

        // Obtén la referencia del LinearLayout
        LinearLayout linearLayout = findViewById(R.id.linearLayoutEventos);

        // Consulta Firestore para obtener la lista de eventos
        db.collection("actividad")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Obtén el nombre del evento desde Firestore
                                String nombreEvento = document.getString("descripcion");

                                // Crea un nuevo TextView para el evento
                                TextView textView = new TextView(SeleccionarEventos.this);

                                // Establece el texto del TextView
                                textView.setText(nombreEvento);

                                // Establece el fondo del TextView como el recurso con bordes redondeados
                                textView.setBackgroundResource(R.drawable.rounded_background);

                                // Establece el tamaño de texto igual al textViewActividad
                                textView.setTextSize(24); // Tamaño en sp

                                // Agrega margen inferior entre los TextView
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                layoutParams.setMargins(0, 0, 0, 10); // Margen inferior en píxeles
                                textView.setLayoutParams(layoutParams);

                                linearLayout.addView(textView);
                            }
                        } else {
                            // Maneja errores de lectura desde Firestore
                        }
                    }
                });
    }
}
