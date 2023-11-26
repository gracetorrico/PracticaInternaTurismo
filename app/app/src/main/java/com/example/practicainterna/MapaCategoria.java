package com.example.practicainterna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapaCategoria extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tvTituloMapa;
    private Spinner spCategorias;
    private GoogleMap myMap;

    private ArrayList<LatLng> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_categoria);

        tvTituloMapa = (TextView) findViewById(R.id.tvTituloMapa);
        spCategorias = (Spinner) findViewById(R.id.spCategorias);

        ArrayList<String> categorias = new ArrayList<>();
        categorias.add("--Selecciona una categoría--");
        categorias.add("Actividades");
        categorias.add("Establecimientos deportivos");
        categorias.add("Restaurantes");
        categorias.add("Sitios históricos");

        ArrayAdapter<String> S = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,categorias);
        spCategorias.setAdapter(S);
        spCategorias.setSelection(0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        spCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 1)
                {
                    Toast.makeText(getApplicationContext(), "Actividades", Toast.LENGTH_SHORT).show();
                }
                if (position == 2)
                {
                    Toast.makeText(getApplicationContext(), "Establecimientos deportivos", Toast.LENGTH_SHORT).show();
                }
                if (position == 3)
                {
                    Toast.makeText(getApplicationContext(), "Restaurantes", Toast.LENGTH_SHORT).show();
                }
                if (position == 4)
                {
                    Toast.makeText(getApplicationContext(), "Sitios históricos", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        LatLng sydney = new LatLng(-34,151);
        myMap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}