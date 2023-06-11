package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.lab5.databinding.ActivityFelicitacioneBinding;
import com.example.lab5.databinding.ActivityPerfilBinding;
import com.example.lab5.entity.Doctor;

public class Felicitaciones extends AppCompatActivity {
    ActivityFelicitacioneBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFelicitacioneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String doctor = (String) getIntent().getSerializableExtra("name");

        binding.textView12.setText("Se agendo su cita con el Dr. "+doctor);

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseFirestore db = FirebaseFirestore.getInstance();
                //CollectionReference collectionRef = db.collection("Doctor");
                //DocumentReference documentRef = collectionRef.document("your_document_id");
                Intent intent = new Intent(Felicitaciones.this, ListCreateActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}