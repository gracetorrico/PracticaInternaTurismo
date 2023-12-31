package com.example.practicainterna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    private TextView tvTitulo, tvLogin;
    private EditText etCorreo, etContrasena;
    private Button bnInicio, bnRegistro;

    private ImageView ivUsuario;

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tvTitulo = (TextView) findViewById(R.id.tvTitulo);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        etCorreo = (EditText) findViewById(R.id.etCorreo);
        etContrasena = (EditText) findViewById(R.id.etContrasena);
        bnInicio = (Button) findViewById(R.id.bnInicio);
        bnRegistro = (Button) findViewById(R.id.bnRegistro);
        ivUsuario = (ImageView) findViewById(R.id.ivUsuario);

        requestQueue = Volley.newRequestQueue(this);
    }

    public void Ejecutar(View v)
    {
        if(v.getId() == R.id.bnRegistro)
        {
            Intent i = new Intent(this, Registro.class);
            startActivity(i);
        }
        if(v.getId() == R.id.bnInicio)
        {
            String correo = etCorreo.getText().toString().trim();
            String constrasena = etContrasena.getText().toString().trim();

            if(correo.isEmpty() || constrasena.isEmpty()){
                Toast.makeText(getApplicationContext(),"Ingresa todos los datos",Toast.LENGTH_LONG).show();
            }
            else {
                VerificandoUsuario(correo,constrasena);
            }
        }
    }

    public void VerificandoUsuario(String correo, String contrasena){
        String URL = "https://practicainterna.000webhostapp.com/login.php?correo=" + correo + "&contrasena=" + contrasena;
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
                                JSONObject userData;
                                Integer idUsuario = 0;
                                String nombre = "";

                                JSONArray dataArray = response.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    userData = dataArray.getJSONObject(i);
                                    idUsuario = userData.getInt("IDUsuario");
                                    nombre = userData.getString("Nombre");
                                }
                                Toast.makeText(getApplicationContext(),"Inicio de sesión correcto \n Bienvenido/a " + nombre,Toast.LENGTH_SHORT).show();

                                etCorreo.setText("");
                                etContrasena.setText("");
                                Intent i = new Intent(getApplicationContext(), MapaCategoria.class);
                                i.putExtra("enteroKey", idUsuario);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"El usuario y contraseña ingresados no corresponden a ningún usuario",Toast.LENGTH_LONG).show();
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
}