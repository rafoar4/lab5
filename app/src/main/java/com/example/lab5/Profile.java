package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.lab5.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    FirebaseAuth auth;
    TextView tprofile,logout;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        tprofile=findViewById(R.id.profile);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        String id=auth.getCurrentUser().getUid();

        db.collection("usuarios")
                        .document(id)
                                .get().addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot documentSnapshot= task.getResult();
                                        if(documentSnapshot.exists()){
                                            tprofile.setText(documentSnapshot.get("nombre").toString());
                                        }
                                    }

                });
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(Profile.this,Login.class));
            finish();

        });





    }
}