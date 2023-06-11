package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class Login extends AppCompatActivity {
    Button ingresar;
    TextView registrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ingresar=findViewById(R.id.ingresar);
        registrar=findViewById(R.id.registrar);

        registrar.setOnClickListener(v -> {
            startActivity(new Intent(Login.this,Register.class));

        });






    }
}