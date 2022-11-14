package com.example.redsocialuniversidadx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class PantallaDeCarga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_carga);

        final int Duracion = 2500;

        new Handler().postDelayed(() -> {
            //Accion a ejecutarse luego de la PantallaDeCarga
            //Reemplazo del MainActivity por la actividad Inicio
            Intent intent = new Intent(PantallaDeCarga.this, Inicio.class);
            startActivity(intent);
            finish();
            //MainActivity ya no aparecerá al abrir la app despues de loguearse
        },Duracion);
    }
}