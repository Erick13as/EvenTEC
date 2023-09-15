package com.example.eventec;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView; // Agrega la importación de TextView
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class Entrada extends AppCompatActivity {

    private ImageView qrImageView;
    private Button backToLoginButton;
    private TextView textViewInscripcionE; // Agrega la importación de TextView
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);
        user = (User) getIntent().getSerializableExtra("user");

        qrImageView = findViewById(R.id.qrImageView);
        backToLoginButton = findViewById(R.id.backToLoginButton);
        textViewInscripcionE = findViewById(R.id.textViewInscripcionE); // Inicializa el TextView

        // Recibe los datos de usuario y nombre de evento a través del intent
        String carnet = getIntent().getStringExtra("carnet");
        String nombreEvento = getIntent().getStringExtra("nombreEvento");

        // Genera el texto para el código QR
        String qrText = "Carnet: " + carnet + "\n" +
                "Evento: " + nombreEvento;

        // Actualiza el texto del TextView con el nombre del evento
        textViewInscripcionE.setText(nombreEvento);

        // Genera el código QR directamente
        Bitmap qrCodeBitmap = generateQRCode(qrText);

        if (qrCodeBitmap != null) {
            // Muestra el código QR en el ImageView
            qrImageView.setImageBitmap(qrCodeBitmap);
        }

        // Configura el OnClickListener para el botón
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la pantalla de Login
                Intent intent = new Intent(Entrada.this, LobbyEstudiantes.class);
                intent.putExtra("user", user); // Pass the user object
                startActivity(intent);
                finish(); // Cierra la actividad actual
            }
        });
    }

    private Bitmap generateQRCode(String text) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
