package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button; // <--- Importar Button
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetalleActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGEN = "com.example.myapplication.IMAGEN";
    public static final String EXTRA_TITULO = "com.example.myapplication.TITULO";
    public static final String EXTRA_CONTENIDO = "com.example.myapplication.CONTENIDO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        Intent intent = getIntent();
        int imagenId = intent.getIntExtra(EXTRA_IMAGEN, R.drawable.logo_shadows);
        String titulo = intent.getStringExtra(EXTRA_TITULO);
        String contenido = intent.getStringExtra(EXTRA_CONTENIDO);

        ImageView imagen = findViewById(R.id.detalle_imagen);
        TextView tvTitulo = findViewById(R.id.detalle_titulo);
        TextView tvContenido = findViewById(R.id.detalle_contenido);
        Button btnVolver = findViewById(R.id.btn_detalle_volver);

        imagen.setImageResource(imagenId);
        tvTitulo.setText(titulo);
        tvContenido.setText(contenido);

        // Asignar el Listener al botón Volver
        btnVolver.setOnClickListener(v -> finish());
    }
}