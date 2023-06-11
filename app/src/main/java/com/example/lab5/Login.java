package com.example.lab5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {
    Button ingresar;

    SignInButton s_google;

    FirebaseAuth auth;

    GoogleSignInClient googleSignInClient;

    TextView registrar;

    EditText e_correo,e_contra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e_correo=findViewById(R.id.e_correo);
        e_contra=findViewById(R.id.e_contra);

        ingresar=findViewById(R.id.ingresar);
        registrar=findViewById(R.id.registrar);
        s_google=findViewById(R.id.s_google);

        registrar.setOnClickListener(v -> {
            startActivity(new Intent(Login.this,Register.class));

        });

        ingresar.setOnClickListener(v -> {
            String l_correo=e_correo.getText().toString();
            String l_contra=e_contra.getText().toString();

            if (TextUtils.isEmpty(l_correo)){
                e_correo.setError("Correo vacio");
                e_correo.requestFocus();
            } else if (TextUtils.isEmpty(l_contra)) {
                e_contra.setError("Contraseña vacia");
                e_contra.requestFocus();

            }else {
                auth.signInWithEmailAndPassword(l_correo,l_contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this,"Inicio de sesion correcta",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, Profile.class));



                        }else {
                            Toast.makeText(Login.this,"Error en el inicio"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });


        s_google.setOnClickListener(v -> {
            s_reg();

        });







    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(Login.this, "Inicio de sesion correcto", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, Profile.class));


                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    int RC_SIGN_IN=40;
    private void s_reg() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account= task.getResult(ApiException.class);

                firebaseAuth(account.getIdToken());

            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
