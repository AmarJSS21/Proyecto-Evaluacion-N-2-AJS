package cl.ipvg.georegistrodef;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HistorialActivity extends AppCompatActivity {

    private ListView lvHistorial;
    private Button btnVolverHistorial;
    private FirebaseFirestore db;
    private ArrayList<String> listaRegistros;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        lvHistorial = findViewById(R.id.lvHistorial);
        btnVolverHistorial = findViewById(R.id.btnVolverHistorial);

        db = FirebaseFirestore.getInstance();
        listaRegistros = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaRegistros);
        lvHistorial.setAdapter(adapter);

        cargarDatosDesdeFirestore();

        btnVolverHistorial.setOnClickListener(v -> finish());
    }

    private void cargarDatosDesdeFirestore() {
        db.collection("registros")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaRegistros.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String titulo = document.getString("titulo");
                        String descripcion = document.getString("descripcion");
                        Double lat = document.getDouble("latitud");
                        Double lng = document.getDouble("longitud");

                        String item = "📌 " + titulo + "\n" + descripcion + "\n📍 Lat: " + lat + ", Lng: " + lng;
                        listaRegistros.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    if (listaRegistros.isEmpty()) {
                        Toast.makeText(this, "No hay registros guardados aún", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar historial: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}