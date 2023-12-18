package com.example.practicainterna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Intereses extends AppCompatActivity {

    int userId;
    RequestQueue requestQueue;
    CheckBox cbVolley, cbFutbol, cbBasquet, cbGimnasio, cbComRapida, cbComidaTradicional, cbComidaVegana, cbMuseos, cbArte, cbMusica, cbDanza, cbTrekking, cbVoluntariado, cbCrecimiento;
    Button bnInteres;
    Map<Integer, String> intereses = new HashMap<>();
    List<Pair<Integer, Integer>> listaInteresesUsuario = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intereses);

        userId = getIntent().getIntExtra("user_id", 0);

        cbVolley = (CheckBox) findViewById(R.id.cbVolley);
        cbFutbol = (CheckBox) findViewById(R.id.cbFutbol);
        cbBasquet = (CheckBox) findViewById(R.id.cbBasquet);
        cbGimnasio = (CheckBox) findViewById(R.id.cbGimnasio);
        cbComRapida = (CheckBox) findViewById(R.id.cbComRapida);
        cbComidaTradicional = (CheckBox) findViewById(R.id.cbComidaTradicional);
        cbComidaVegana = (CheckBox) findViewById(R.id.cbComidaVegana);
        cbMuseos = (CheckBox) findViewById(R.id.cbMuseos);
        cbArte = (CheckBox) findViewById(R.id.cbArte);
        cbMusica = (CheckBox) findViewById(R.id.cbMusica);
        cbDanza = (CheckBox) findViewById(R.id.cbDanza);
        cbTrekking = (CheckBox) findViewById(R.id.cbTrekking);
        cbVoluntariado = (CheckBox) findViewById(R.id.cbVoluntariado);
        cbCrecimiento = (CheckBox) findViewById(R.id.cbCrecimiento);
        bnInteres = (Button) findViewById(R.id.bnInteres);

        requestQueue = Volley.newRequestQueue(this);

        obtenerInteresesDesdeServidor();

    }

    public void procesarCheckBoxes(View v) {
        verificarCheckBoxes();
        agregarRelacionesEnBatch();
        Toast.makeText(getApplicationContext(), "Intereses Agregados", Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), MapaCategoria.class);
        i.putExtra("enteroKey", userId);
        startActivity(i);
    }

    private void obtenerInteresesDesdeServidor() {
        String URL = "https://practicainterna.000webhostapp.com/interes.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONObject dataObject = response.getJSONObject("data");
                                intereses.clear();
                                for (Iterator<String> it = dataObject.keys(); it.hasNext(); ) {
                                    String key = it.next();
                                    int idInteres = Integer.parseInt(key);
                                    String nombreInteres = dataObject.getString(key);

                                    intereses.put(idInteres, nombreInteres);
                                }
                                Log.d("Msg","IMPRIMIENDO INTERESES");
                                for (Map.Entry<Integer, String> entry : intereses.entrySet()) {
                                    Log.d("Interes", "ID: " + entry.getKey() + ", Nombre: " + entry.getValue());
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Error al obtener intereses", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de red", Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
    private void verificarCheckBoxes() {
        listaInteresesUsuario.clear();

        verificarAgregarRelacion(cbVolley, "Vólley");
        verificarAgregarRelacion(cbFutbol, "Fútbol");
        verificarAgregarRelacion(cbBasquet, "Básquetbol");
        verificarAgregarRelacion(cbGimnasio, "Gimnasio");
        verificarAgregarRelacion(cbComRapida, "Comida Rápida");
        verificarAgregarRelacion(cbComidaTradicional, "Comida Tradicional");
        verificarAgregarRelacion(cbComidaVegana, "Comida Vegana");
        verificarAgregarRelacion(cbMuseos, "Museos");
        verificarAgregarRelacion(cbArte, "Arte");
        verificarAgregarRelacion(cbMusica, "Música");
        verificarAgregarRelacion(cbDanza, "Danza");
        verificarAgregarRelacion(cbTrekking, "Trekking");
        verificarAgregarRelacion(cbVoluntariado, "Voluntariado");
        verificarAgregarRelacion(cbCrecimiento, "Crecimiento Personal");
    }

    private int obtenerIDInteres(String nombreInteres) {
        for (Map.Entry<Integer, String> entry : intereses.entrySet()) {
            if (entry.getValue().equals(nombreInteres)) {
                return entry.getKey();
            }
        }
        return -1;
    }
    private void verificarAgregarRelacion(CheckBox checkBox, String interesNombre) {
        if (checkBox.isChecked()) {
            agregarRelacionInteresUsuario(obtenerIDInteres(interesNombre));
        }
    }
    private void agregarRelacionInteresUsuario(int idInteres) {
        if (idInteres != -1) {
            listaInteresesUsuario.add(new Pair<>(idInteres, userId));
        }
    }

    private void agregarRelacionesEnBatch() {
        if (!listaInteresesUsuario.isEmpty()) {
            String URL = "https://practicainterna.000webhostapp.com/insertar_relaciones_intereses.php";

            JSONArray jsonArray = new JSONArray();

            for (Pair<Integer, Integer> relacion : listaInteresesUsuario) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("IDInteres", relacion.first);
                    jsonObject.put("IDUsuario", relacion.second);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    URL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");
                                if (status.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Relaciones de intereses insertadas correctamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error al insertar relaciones de intereses", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error de red", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                public byte[] getBody() {
                    return jsonArray.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            requestQueue.add(jsonObjectRequest);
        }
    }

}
