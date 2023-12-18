package com.example.practicainterna;

import com.google.android.gms.maps.CameraUpdate;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private boolean clickMenu = false;
    RelativeLayout rel_layout;

    int userId;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_categoria);

        userId = getIntent().getIntExtra("enteroKey", 0);
        actividades = new ArrayList<>();
        establecimientos = new ArrayList<>();
        sitiosHistoricos = new ArrayList<>();
        sucursales = new ArrayList<>();
        tvTituloMapa = (TextView) findViewById(R.id.tvTituloMapa);
        tvMapa = (TextView) findViewById(R.id.tvMapa);
        spCategorias = (Spinner) findViewById(R.id.spCategorias);
        rel_layout = findViewById(R.id.rel_layout);

        ArrayList<String> categorias = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        categorias.add("--Selecciona una categoría--");
        categorias.add("Actividades");
        categorias.add("Establecimientos deportivos");
        categorias.add("Restaurantes");
        categorias.add("Sitios históricos");

        ArrayAdapter<String> S = ModificarSpinner(categorias);
        //ArrayAdapter<String> S = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,categorias);
        S.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCategorias.setAdapter(S);
        spCategorias.setSelection(0);

        Almacenar("https://practicainterna.000webhostapp.com/coordenadas/obtener_actividad.php",actividades, "ACTIVIDADES");
        Almacenar("https://practicainterna.000webhostapp.com/coordenadas/obtener_establecimiento.php",establecimientos, "ESTABLECIMIENTOS");
        Almacenar("https://practicainterna.000webhostapp.com/coordenadas/obtener_sucursal.php",sucursales, "SUCURSALES");
        Almacenar("https://practicainterna.000webhostapp.com/coordenadas/obtener_sitio_historico.php",sitiosHistoricos, "SITIOS");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        spCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Boolean control = false;
                if (position == 0){
                    myMap.clear();
                }
                if (position == 1 || position == 2 || position == 3 || position == 4) {
                    myMap.clear();
                    String message = "";
                    ArrayList<Coordenadas> data = new ArrayList<>();
                    BitmapDescriptor icon = null;

                    switch (position) {
                        case 1:
                            message = "Actividades";
                            data = actividades;
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                            break;
                        case 2:
                            message = "Establecimientos deportivos";
                            data = establecimientos;
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                            break;
                        case 3:
                            message = "Restaurantes";
                            data = sucursales;
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                            break;
                        case 4:
                            message = "Sitios históricos";
                            data = sitiosHistoricos;
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

                        // Almacena el id y el tipo de lugar en el marcador utilizando setTag
                        Marker marker = myMap.addMarker(markerOptions);
                        marker.setTag(aux.getID() + "," + aux.getTipo());

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
    public void onMenuClick(View v)
    {
        clickMenu = !clickMenu;
        if (clickMenu) {
            rel_layout.setVisibility(View.VISIBLE);
        } else {
            rel_layout.setVisibility(View.GONE);
        }
    }

    public void onHomeClick(View v)
    {
        rel_layout.setVisibility(View.GONE);
        Intent i = new Intent(getApplicationContext(), MapaCategoria.class);
        i.putExtra("enteroKey", userId);
        startActivity(i);
    }

    public void onBadgeClick(View v)
    {
        rel_layout.setVisibility(View.GONE);
        Intent i = new Intent(getApplicationContext(), Insignias.class);
        i.putExtra("idUser", userId);
        startActivity(i);
    }

    public void onLogoutClick(View v)
    {
        rel_layout.setVisibility(View.GONE);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("idUser", userId);
        startActivity(i);
    }

    public void onARClick(View v)
    {
        rel_layout.setVisibility(View.GONE);
        Intent i = new Intent(getApplicationContext(), RealidadAumentada.class); //Cambiar por activity
        i.putExtra("idUser", userId);
        startActivity(i);
    }
    public ArrayAdapter<String> ModificarSpinner(ArrayList<String> items) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.textColor, typedValue, true);
        int textColor = typedValue.data;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(textColor); // Aplica el color al texto seleccionado
                return textView;
            }
        };
        return adapter;
    }
    public void Almacenar(String URL, ArrayList<Coordenadas> list, String tipo){
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
                                    aux = new Coordenadas(coordenada.getInt("ID"),coordenada.getString("Nombre"),coordenada.getDouble("Longitud"),coordenada.getDouble("Latitud"), tipo);
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

        myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });

        myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String markerInfo = (String) marker.getTag();

                String[] parts = markerInfo.split(",");
                String id = parts[0];
                String tipo = parts[1];

                Intent intent = new Intent(MapaCategoria.this, DetalleSitio.class);
                intent.putExtra("ID", id);
                intent.putExtra("Tipo", tipo);
                startActivity(intent);
            }
        });
    }
}