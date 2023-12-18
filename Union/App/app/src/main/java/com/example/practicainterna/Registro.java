package com.example.practicainterna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Patterns;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    EditText etNombre, etApellido, etCorreoR, etPassword, etNum;
    String nombre, apellido, correo, password, num;
    Button bnRegistrar;
    int userId = 8;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etNombre = (EditText) findViewById(R.id.etNombre);
        etApellido = (EditText) findViewById(R.id.etApellido);
        etCorreoR = (EditText) findViewById(R.id.etCorreoR);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etNum = (EditText) findViewById(R.id.etNum);
        bnRegistrar = (Button) findViewById(R.id.bnRegistrar);

        requestQueue = Volley.newRequestQueue(this);
    }

    public void ClickOnRegister(View v)
    {
        if(CheckInfo())
        {
            RegisterUser();
        }
    }

    private void RegisterUser() {
        String URL = "https://practicainterna.000webhostapp.com/insertar_usuario.php";

        Map<String, String> params = new HashMap<>();
        params.put("nombre", nombre);
        params.put("apellido", apellido);
        params.put("correo", correo);
        params.put("contrasena", password);
        params.put("numero", num);

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
                                Toast.makeText(getApplicationContext(), "Usuario insertado correctamente", Toast.LENGTH_SHORT).show();
                                if (response.has("user_id")) {
                                    userId = response.getInt("user_id");
                                    Log.d("UserId", String.valueOf(userId));
                                    Intent i = new Intent(Registro.this, Intereses.class);
                                    Log.d("UserId ENVIADO", String.valueOf(userId));
                                    i.putExtra("user_id", userId);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "No se pudo obtener el ID del usuario", Toast.LENGTH_LONG).show();
                                }
                            } else if (status.equals("error") && response.getString("data").contains("Ya existe un usuario con este correo")) {

                                Toast.makeText(getApplicationContext(), response.getString("data"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Registro.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error al insertar usuario", Toast.LENGTH_LONG).show();
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

    public boolean CheckInfo()
    {
        nombre = etNombre.getText().toString().trim();
        apellido = etApellido.getText().toString().trim();
        correo = etCorreoR.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        num = etNum.getText().toString().trim();

        if(nombre.isEmpty()|| apellido.isEmpty() || correo.isEmpty() || password.isEmpty()|| num.isEmpty()){
            Toast.makeText(getApplicationContext(),"Ingresa todos los datos",Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            return CheckValidMail(correo) && CheckValidPassword(password) && CheckValidNumber(num);
        }
    }

    private boolean CheckValidPassword(String password) {
        if (!(password.length() >= 8 && password.matches(".*[a-zA-Z]+.*") && password.matches(".*\\d+.*"))) {
            Toast.makeText(getApplicationContext(), "Ingresa una contraseña con más de 8 caracteres y al menos un número", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean CheckValidMail(String correo)
    {
        if(!(Patterns.EMAIL_ADDRESS.matcher(correo).matches()))
        {
            Toast.makeText(getApplicationContext(),"Ingresa un correo electrónico válido",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean CheckValidNumber(String num) {
        if (num.length() != 8 || !TextUtils.isDigitsOnly(num)) {
            Toast.makeText(getApplicationContext(), "Ingresa un número de 8 dígitos", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}