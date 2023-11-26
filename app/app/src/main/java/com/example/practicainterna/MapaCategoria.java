package com.example.practicainterna;

import com.google.android.gms.maps.CameraUpdate;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapaCategoria extends AppCompatActivity implements OnMapReadyCallback {

    ArrayList<Coordenadas> actividades;
    ArrayList<Coordenadas> establecimientos;
    ArrayList<Coordenadas> sitiosHistoricos;
    ArrayList<Coordenadas> sucursales;
    private TextView tvTituloMapa, tvMapa;
    private Spinner spCategorias;
    private GoogleMap myMap;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_categoria);

        actividades = new ArrayList<>();
        establecimientos = new ArrayList<>();
        sitiosHistoricos = new ArrayList<>();
        sucursales = new ArrayList<>();
        tvTituloMapa = (TextView) findViewById(R.id.tvTituloMapa);
        tvMapa = (TextView) findViewById(R.id.tvMapa);
        spCategorias = (Spinner) findViewById(R.id.spCategorias);

        ArrayList<String> categorias = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        categorias.add("--Selecciona una categoría--");
        categorias.add("Actividades");
        categorias.add("Establecimientos deportivos");
        categorias.add("Restaurantes");
        categorias.add("Sitios históricos");

        ArrayAdapter<String> S = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,categorias);
        spCategorias.setAdapter(S);
        spCategorias.setSelection(0);

        Almacenar("https://practicainterna.000webhostapp.com/coordenadas/obtener_actividad.php",actividades);
        Almacenar("https://practicainterna.000webhostapp.com/coordenadas/obtener_establecimiento.php",establecimientos);
        Almacenar("https://practicainterna.000webhostapp.com/coordenadas/obtener_sucursal.php",sucursales);
        Almacenar("https://practicainterna.000webhostapp.com/coordenadas/obtener_sitio_historico.php",sitiosHistoricos);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        spCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Boolean control = false;
                if (position == 1 || position == 2 || position == 3 || position == 4) {
                    myMap.clear();
                    String message = "";
                    ArrayList<Coordenadas> data = new ArrayList<>();
                    BitmapDescriptor icon = null;

                    switch (position) {
                        case 1:
                            message = "Actividades";
                            data = actividades;
                            // Establece el ícono para actividades (por ejemplo, rojo)
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                            break;
                        case 2:
                            message = "Establecimientos deportivos";
                            data = establecimientos;
                            // Establece el ícono para establecimientos deportivos (por ejemplo, verde)
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                            break;
                        case 3:
                            message = "Restaurantes";
                            data = sucursales;
                            // Establece el ícono para restaurantes (por ejemplo, azul)
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                            break;
                        case 4:
                            message = "Sitios históricos";
                            data = sitiosHistoricos;
                            // Establece el ícono para sitios históricos (por ejemplo, amarillo)
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                            break;
                    }

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (int i = 0; i < data.size(); i++) {
                        Coordenadas aux = data.get(i);
                        LatLng c = new LatLng(aux.getLatitud(), aux.getLongitud());
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(c)
                                .title(aux.getNombre())
                                .icon(icon); // Establece el ícono personalizado para el marcador
                        myMap.addMarker(markerOptions);
                        builder.include(c); // Incluir las coordenadas en el cálculo del límite
                        control = true;
                    }

                    if (control) {
                        LatLngBounds bounds = builder.build();
                        int padding = 100; // Espacio en píxeles para el borde del mapa
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                        myMap.animateCamera(cu);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void Almacenar(String URL, ArrayList<Coordenadas> list){
        //String URL = "https://practicainterna.000webhostapp.com/coordenadas/obtener_actividad.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if(status.equals("success"))
                            {
                                JSONObject coordenada;
                                Coordenadas aux;

                                JSONArray dataArray = response.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    coordenada = dataArray.getJSONObject(i);
                                    aux = new Coordenadas(coordenada.getString("Nombre"),coordenada.getDouble("Longitud"),coordenada.getDouble("Latitud"));
                                    list.add(aux);
                                }
                            };
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
    }
}