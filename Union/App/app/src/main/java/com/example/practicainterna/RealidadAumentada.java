package com.example.practicainterna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RealidadAumentada extends AppCompatActivity {

    private Button btEscanear;
    private TextView tvContenido, tvTituloRA, tvNombreRA, tvDescripcionRA;
    private ImageView ivVolverRA, ivInsignia;
    private int userId;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realidad_aumentada);
        userId = getIntent().getIntExtra("idUser", 1);
        tvDescripcionRA = (TextView) findViewById(R.id.tvDescripcionRA);
        tvContenido = (TextView) findViewById(R.id.tvIDInsigniaRA);
        tvTituloRA = (TextView) findViewById(R.id.tvTituloRA);
        tvNombreRA = (TextView) findViewById(R.id.tvNombreRA);
        btEscanear = (Button) findViewById(R.id.btEscanear);
        ivVolverRA = (ImageView) findViewById(R.id.ivVolverRA);
        ivInsignia = (ImageView) findViewById(R.id.ivInsignia);

        requestQueue = Volley.newRequestQueue(this);
    }

    public void Escanear(View view) {
        if(view.getId() == R.id.btEscanear){
            new IntentIntegrator(this).initiateScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String contenido = resultado.getContents();
        if(contenido != null){
            btEscanear.setVisibility(View.GONE);
            tvTituloRA.setText("Felicidades, atrapaste una insignia!");
            LlenarInsignia(contenido);
            CambiarImagen(contenido);
            putInsignia(contenido);
        }
        else {
            Toast.makeText(getApplicationContext(),"No se escaneo ninguna insignia",Toast.LENGTH_SHORT).show();
        }
    }

    public void VolverRA(View v)
    {
        if(v.getId() == R.id.ivVolverRA) {
            finish();
        }
    }

    public void LlenarInsignia(String id)
    {
        String URL = "https://practicainterna.000webhostapp.com/obtener_datos_insignia.php?id=" + id;
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
                                    tvContenido.setVisibility(View.VISIBLE);
                                    tvContenido.setText(auxData.getString("IDInsignia"));
                                    tvNombreRA.setVisibility(View.VISIBLE);
                                    tvNombreRA.setText(auxData.getString("Nombre"));
                                    tvDescripcionRA.setVisibility(View.VISIBLE);
                                    tvDescripcionRA.setText(auxData.getString("Descripcion"));
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"No se pudo extraer información de la insignia",Toast.LENGTH_LONG).show();
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

    public void CambiarImagen(String id){
        ivInsignia.setVisibility(View.VISIBLE);
        switch (id) {
            case "1":
                ivInsignia.setImageResource(R.drawable.activo);
                break;
            case "2":
                ivInsignia.setImageResource(R.drawable.bandido);
                break;
            case "3":
                ivInsignia.setImageResource(R.drawable.comida);
                break;
            case "4":
                ivInsignia.setImageResource(R.drawable.zen);
                break;
            case "5":
                ivInsignia.setImageResource(R.drawable.epico);
                break;
            case "6":
                ivInsignia.setImageResource(R.drawable.coffelover);
                break;
            default:
                break;
        }
    }

    public void putInsignia(String idInsignia) {
        String URL = "https://practicainterna.000webhostapp.com/put_insignia.php";

        Map<String, String> params = new HashMap<>();
        params.put("userID", String.valueOf(userId));
        params.put("idInsignia", idInsignia);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), "Insignia actualizada correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error al actualizar la insignia", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}