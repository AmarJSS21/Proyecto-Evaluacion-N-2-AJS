package cl.ipvg.georegistrodef;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        db = FirebaseFirestore.getInstance();

        // Obtener el fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Cargar los marcadores dinámicamente desde Firestore
        cargarMarcadoresDesdeFirestore();
    }

    private void cargarMarcadoresDesdeFirestore() {
        db.collection("registros")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "No hay registros guardados en el mapa", Toast.LENGTH_SHORT).show();
                        // Ubicación por defecto si no hay registros (Concepción)
                        LatLng concepcion = new LatLng(-36.8261, -73.0498);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(concepcion, 12));
                        return;
                    }

                    LatLng ultimaUbicacion = null;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String titulo = document.getString("titulo");
                        String descripcion = document.getString("descripcion");
                        Double lat = document.getDouble("latitud");
                        Double lng = document.getDouble("longitud");

                        if (lat != null && lng != null) {
                            LatLng coordenada = new LatLng(lat, lng);

                            // Añadir el marcador al mapa con su título y descripción
                            mMap.addMarker(new MarkerOptions()
                                    .position(coordenada)
                                    .title(titulo != null ? titulo : "Incidencia")
                                    .snippet(descripcion != null ? descripcion : "Sin descripción"));

                            ultimaUbicacion = coordenada;
                        }
                    }

                    // Centrar la cámara en el último registro agregado si existe
                    if (ultimaUbicacion != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ultimaUbicacion, 15));
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar el mapa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}