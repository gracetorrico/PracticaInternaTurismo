package com.example.practicainterna;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Insignias extends AppCompatActivity {

    private RecyclerView recyclerViewInsignias;
    private List<Insignia> listaInsignias;
    private InsigniasAdapter insigniasAdapter;
    private ImageView ivVolverI;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insignias);

        userId = getIntent().getIntExtra("idUser", 1);

        ivVolverI = findViewById(R.id.ivVolverI);
        recyclerViewInsignias = findViewById(R.id.recyclerViewInsignias);
        listaInsignias = new ArrayList<>();
        insigniasAdapter = new InsigniasAdapter(this, listaInsignias);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewInsignias.setLayoutManager(layoutManager);
        recyclerViewInsignias.setAdapter(insigniasAdapter);

        obtenerInsigniasDesdeServidor();
    }

    private void obtenerInsigniasDesdeServidor() {
        String URL = "https://practicainterna.000webhostapp.com/obtener_insignias.php?user_id=" + userId;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

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
                                JSONArray dataArray = response.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject insigniaData = dataArray.getJSONObject(i);
                                    int idInsignia = insigniaData.getInt("IDInsignia");
                                    String nombreInsignia = insigniaData.getString("Nombre");
                                    String descripcionInsignia = insigniaData.getString("Descripcion");
                                    boolean usuarioTieneInsignia = insigniaData.optInt("IDUsuario", 0) == userId;
                                    int cantidadInsignias = 0;

                                    if (insigniaData.has("CantidadInsignias")) {
                                        String cantidadInsigniasStr = insigniaData.optString("CantidadInsignias");
                                        try {
                                            cantidadInsignias = Integer.parseInt(cantidadInsigniasStr);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    Insignia insignia = new Insignia(idInsignia, nombreInsignia, descripcionInsignia, usuarioTieneInsignia, cantidadInsignias);
                                    listaInsignias.add(insignia);
                                }

                                insigniasAdapter.notifyDataSetChanged();
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                null
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void VolverI(View v)
    {
        if(v.getId() == R.id.ivVolverI) {
            finish();
        }
    }
}
