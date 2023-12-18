package com.example.practicainterna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetalleSitio extends AppCompatActivity {

    private TextView tvTituloDetalleLugar, tvTIDLugar, tvTIDInsignia, tvTNombreInsignia, tvTDescripcion,
            tvTCategoria, tvTIngreso, tvTTipoComida, tvTIDSucursal, tvTNombre, tvDescripcion,
            tvIDLugar, tvIDInsignia, tvNombreInsignia, tvCategoria, tvIngreso, tvTipoComida, tvIDSucursal, tvNombre;

    private ImageView ivVolverDS, ivLugar;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_sitio);

        requestQueue = Volley.newRequestQueue(this);

        ivVolverDS = (ImageView) findViewById(R.id.ivVolverDS);
        ivLugar = (ImageView) findViewById(R.id.ivLugar);
        tvIDLugar = (TextView) findViewById(R.id.tvIDLugar);
        tvIDInsignia = (TextView) findViewById(R.id.tvIDInsignia);
        tvNombreInsignia = (TextView) findViewById(R.id.tvNombreInsignia);
        tvCategoria = (TextView) findViewById(R.id.tvCategoria);
        tvIngreso = (TextView) findViewById(R.id.tvIngreso);
        tvTipoComida = (TextView) findViewById(R.id.tvTipoComida);
        tvIDSucursal = (TextView) findViewById(R.id.tvIDSucursal);
        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvTituloDetalleLugar = (TextView) findViewById(R.id.tvTituloDetalleLugar);
        tvTIDLugar = (TextView) findViewById(R.id.tvTIDLugar);
        tvTIDInsignia = (TextView) findViewById(R.id.tvTIDInsignia);
        tvTNombreInsignia = (TextView) findViewById(R.id.tvTNombreInsignia);
        tvTDescripcion = (TextView) findViewById(R.id.tvTDescripcion);
        tvTCategoria = (TextView) findViewById(R.id.tvTCategoria);
        tvTIngreso = (TextView) findViewById(R.id.tvTIngreso);
        tvTTipoComida = (TextView) findViewById(R.id.tvTTipoComida);
        tvTIDSucursal = (TextView) findViewById(R.id.tvTIDSucursal);
        tvTNombre = (TextView) findViewById(R.id.tvTNombre);
        tvDescripcion = (TextView) findViewById(R.id.tvDescripcion);

        String id = "";
        String tipo = "";

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("ID");
            tipo = intent.getStringExtra("Tipo");
        }

        AjustarView(id,tipo);
    }

    public void AjustarView(String id, String tipo){
        switch (tipo){
            case "ACTIVIDADES":
                tvTIDLugar.setText("ID Actividad:");
                LlenarActividad(id);
                break;
            case "ESTABLECIMIENTOS":
                tvTIDLugar.setText("ID Establecimiento Deportivo:");
                tvTIngreso.setVisibility(View.VISIBLE);
                tvIngreso.setVisibility(View.VISIBLE);
                LlenarEstablecimiento(id);
                break;
            case "SUCURSALES":
                tvTIDLugar.setText("ID Restaurante:");
                tvTIDSucursal.setVisibility(View.VISIBLE);
                tvIDSucursal.setVisibility(View.VISIBLE);
                tvTTipoComida.setVisibility(View.VISIBLE);
                tvTipoComida.setVisibility(View.VISIBLE);
                LlenarSucursal(id);
                break;
            case "SITIOS":
                tvTIDLugar.setText("ID Sitio Histórico:");
                tvTCategoria.setVisibility(View.VISIBLE);
                tvCategoria.setVisibility(View.VISIBLE);
                LlenarSitioHistorico(id);
                break;
        }
    }

    public void LlenarActividad(String id)
    {
        String URL = "https://practicainterna.000webhostapp.com/detalleLugar/obtener_actividad.php?id=" + id;
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
                                JSONObject auxData;

                                JSONArray dataArray = response.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    auxData = dataArray.getJSONObject(i);
                                    tvIDLugar.setText(auxData.getString("IDActividad"));
                                    tvNombre.setText(auxData.getString("Nombre"));
                                    tvDescripcion.setText(auxData.getString("Descripcion"));
                                    tvIDInsignia.setText(auxData.getString("InsigniaRelacionada"));
                                    tvNombreInsignia.setText(auxData.getString("NombreInsignia"));
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No se pudo extraer información de la base de datos",Toast.LENGTH_LONG).show();
                            }
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

    public void LlenarEstablecimiento(String id)
    {
        String URL = "https://practicainterna.000webhostapp.com/detalleLugar/obtener_establecimiento.php?id=" + id;
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
                                JSONObject auxData;
                                String aux = "xd";

                                JSONArray dataArray = response.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    auxData = dataArray.getJSONObject(i);
                                    tvIDLugar.setText(auxData.getString("IDEstablecimiento"));
                                    tvNombre.setText(auxData.getString("Nombre"));
                                    tvDescripcion.setText(auxData.getString("Descripcion"));
                                    if(auxData.getString("IngresoLibre").equals("0")){
                                        aux = "No";
                                    }
                                    else if(auxData.getString("IngresoLibre").equals("1"))
                                    {
                                        aux = "Sí";
                                    }
                                    tvIngreso.setText(aux);
                                    aux = "";
                                    tvIDInsignia.setText(auxData.getString("InsigniaRelacionada"));
                                    tvNombreInsignia.setText(auxData.getString("NombreInsignia"));
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No se pudo extraer información de la base de datos",Toast.LENGTH_LONG).show();
                            }
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

    public void LlenarSitioHistorico(String id)
    {
        String URL = "https://practicainterna.000webhostapp.com/detalleLugar/obtener_sitio_historico.php?id=" + id;
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
                                JSONObject auxData;

                                JSONArray dataArray = response.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    auxData = dataArray.getJSONObject(i);
                                    tvIDLugar.setText(auxData.getString("IDSitio"));
                                    tvNombre.setText(auxData.getString("Nombre"));
                                    tvCategoria.setText(auxData.getString("Categoria"));
                                    tvDescripcion.setText(auxData.getString("Descripcion"));
                                    tvIDInsignia.setText(auxData.getString("InsigniaRelacionada"));
                                    tvNombreInsignia.setText(auxData.getString("NombreInsignia"));
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No se pudo extraer información de la base de datos",Toast.LENGTH_LONG).show();
                            }
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

    public void LlenarSucursal(String id)
    {
        String URL = "https://practicainterna.000webhostapp.com/detalleLugar/obtener_sucursal.php?id=" + id;
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
                                JSONObject auxData;

                                JSONArray dataArray = response.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    auxData = dataArray.getJSONObject(i);
                                    tvIDLugar.setText(auxData.getString("IDRestaurante"));
                                    tvIDSucursal.setText(auxData.getString("IDSucursal"));
                                    tvNombre.setText(auxData.getString("Nombre"));
                                    tvTipoComida.setText(auxData.getString("TipoComida"));
                                    tvDescripcion.setText(auxData.getString("Descripcion"));
                                    tvIDInsignia.setText(auxData.getString("InsigniaRelacionada"));
                                    tvNombreInsignia.setText(auxData.getString("NombreInsignia"));
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No se pudo extraer información de la base de datos",Toast.LENGTH_LONG).show();
                            }
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

    public void Volver(View v)
    {
        if(v.getId() == R.id.ivVolverDS) {
            finish();
        }
    }
}