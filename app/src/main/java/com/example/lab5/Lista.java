package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Lista extends AppCompatActivity {

    FirebaseAuth auth;
    TextView tv_profile,btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        tv_profile=findViewById(R.id.u_perfil);
        auth=FirebaseAuth.getInstance();
        String id=auth.getCurrentUser().getUid();
        Log.e("TAG", "onCreate: "+id );


    }
}