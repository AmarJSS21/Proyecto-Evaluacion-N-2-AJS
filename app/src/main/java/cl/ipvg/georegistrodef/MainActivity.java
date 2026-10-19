package cl.ipvg.georegistrodef;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnDejarRegistro;
    private Button btnVerMapa;
    private Button btnVerHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDejarRegistro = findViewById(R.id.btnDejarRegistro);
        btnVerMapa = findViewById(R.id.btnVerMapa);
        btnVerHistorial = findViewById(R.id.btnVerHistorial);

        // NAVEGACIÓN 1: Ir a la pantalla de crear/dejar un nuevo registro
        btnDejarRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        // NAVEGACIÓN 2: Ir a la pantalla del Mapa
        btnVerMapa.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        });

        // navegacion 3: Ir a la pantalla de Historial
        btnVerHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistorialActivity.class);
            startActivity(intent);
        });
            // Aquí enlazamos la Activity que mostrará la lista de datos desde Firestore
            // Intent intent = new Intent(MainActivity.this, HistorialActivity.class);
            // startActivity(intent);

    }
}










