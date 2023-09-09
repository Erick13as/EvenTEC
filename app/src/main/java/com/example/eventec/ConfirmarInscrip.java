package com.example.eventec;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class ConfirmarInscrip extends AppCompatActivity {

    private ImageView qrImageView;
    private Button backToLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_inscrip);

        qrImageView = findViewById(R.id.qrImageView);
        backToLoginButton = findViewById(R.id.backToLoginButton);

        // Recibe los datos de Inscrip a través del intent
        Inscrip inscripcion = (Inscrip) getIntent().getSerializableExtra("inscripcion");

        // Genera el texto a partir de los datos de Inscrip
        String inscripcionText = "Carnet: " + inscripcion.getCarnet() + "\n" +
                "Evento: " + inscripcion.getIdEvento();

        // Genera el código QR directamente
        Bitmap qrCodeBitmap = generateQRCode(inscripcionText);

        if (qrCodeBitmap != null) {
            // Muestra el código QR en el ImageView
            qrImageView.setImageBitmap(qrCodeBitmap);
        }

        // Configura el OnClickListener para el botón
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la pantalla de Login
                Intent intent = new Intent(ConfirmarInscrip.this, Login.class);
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

