package com.example.practicainterna;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvTitulo, tvLogin;
    EditText etCorreo, etContrasena;
    Button bnInicio, bnRegistro;
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
    }
}