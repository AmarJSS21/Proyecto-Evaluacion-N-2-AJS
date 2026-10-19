package cl.ipvg.georegistrodef;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText etTitulo, etDescripcion;
    private TextView tvCoordenadas;
    private Button btnObtenerUbicacion, btnGuardarFirebase, btnVolver;

    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db;

    private double latitud = 0.0;
    private double longitud = 0.0;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        tvCoordenadas = findViewById(R.id.tvCoordenadas);
        btnObtenerUbicacion = findViewById(R.id.btnObtenerUbicacion);
        btnGuardarFirebase = findViewById(R.id.btnGuardarFirebase);
        btnVolver = findViewById(R.id.btnVolver);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        db = FirebaseFirestore.getInstance();

        // Botón para obtener la ubicación actual por GPS real
        btnObtenerUbicacion.setOnClickListener(v -> obtenerUbicacionGps());

        // Botón para guardar los datos en Firestore (el botón verde)
        btnGuardarFirebase.setOnClickListener(v -> guardarEnFirestore());

        // Botón para volver atrás
        btnVolver.setOnClickListener(v -> finish());
    }

    private void obtenerUbicacionGps() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        Toast.makeText(this, "Buscando ubicación GPS real...", Toast.LENGTH_SHORT).show();

        // Forzar lectura actual con alta precisión para el dispositivo físico
        fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                new CancellationTokenSource().getToken()
        ).addOnSuccessListener(this, location -> {
            if (location != null) {
                latitud = location.getLatitude();
                longitud = location.getLongitude();
                tvCoordenadas.setText("Lat: " + latitud + ", Lng: " + longitud);
                Toast.makeText(this, "¡Ubicación GPS real obtenida!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación. Active el GPS.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarEnFirestore() {
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (titulo.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (latitud == 0.0 && longitud == 0.0) {
            Toast.makeText(this, "Primero debe obtener la ubicación GPS", Toast.LENGTH_SHORT).show();
            return;
        }

        // Estructura de datos para Firestore
        Map<String, Object> registro = new HashMap<>();
        registro.put("titulo", titulo);
        registro.put("descripcion", descripcion);
        registro.put("latitud", latitud);
        registro.put("longitud", longitud);
        registro.put("fecha", System.currentTimeMillis());

        db.collection("registros")
                .add(registro)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "¡Registro guardado en Firestore con éxito!", Toast.LENGTH_LONG).show();
                    finish(); // Cierra la pantalla y vuelve al menú
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionGps();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}