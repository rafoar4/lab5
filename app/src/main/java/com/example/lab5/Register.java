package com.example.lab5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab5.model.Usuario;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {

    EditText nombre,contra,correo,numero;
    Button registarse,r_google,r_facebook;

    FirebaseAuth auth;

    Usuario usuario;

    String userID;

    FirebaseFirestore db;

    //GoogleSignInC

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombre=findViewById(R.id.r_nombre);
        contra=findViewById(R.id.r_contra);
        correo=findViewById(R.id.r_correo);
        numero=findViewById(R.id.r_numero);

        registarse=findViewById(R.id.button2);

        registarse.setOnClickListener(v -> {
            check();
        });
        db=FirebaseFirestore.getInstance();

        r_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f_reg();
            }
        });

        r_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g_reg();
            }
        });



    }

    private void g_reg() {

    }

    private void f_reg() {

    }

    private void check() {
        String s_nombre=nombre.getText().toString();
        String s_contra=contra.getText().toString();
        String s_correo=correo.getText().toString();
        String s_numero=numero.getText().toString();
        if(s_correo.isEmpty()){
            showError(correo,"Rellene el campo de correo");
        } else if (!s_correo.contains("@")) {
            showError(correo,"Correo no valido");

        } else if (s_contra.isEmpty()) {
            showError(contra,"Contraseña no puede estar vacia");

        } else if (s_nombre.isEmpty()) {
            showError(nombre,"Nombre no puede estar vacio");

        } else if (s_numero.isEmpty()) {
            showError(numero,"Nombre no puede estar vacio");

        }else {
            usuario=new Usuario();
            usuario.setNombre(s_nombre);
            usuario.setCorreo(s_correo);
            usuario.setContra(s_contra);
            usuario.setNumero(s_numero);
            auth.createUserWithEmailAndPassword(s_correo,s_contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        userID=auth.getCurrentUser().getUid();
                        db.collection("usuarios")
                                .document(userID)
                                .set(usuario)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Register.this,"Usuario registrado exitosamente",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Register.this,Login.class));

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                    }
                                });



                    }else {
                        Toast.makeText(Register.this,"Error en el registro"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                    }

                }
            });


        }
    }
    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}