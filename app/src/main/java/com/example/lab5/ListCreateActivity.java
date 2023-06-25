package com.example.lab5;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.lab5.Adapter.ActividadAdapter;
import com.example.lab5.databinding.ActivityListCreateBinding;
import com.example.lab5.entity.Doctor;
import com.example.lab5.model.Actividad;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListCreateActivity extends AppCompatActivity {
    ActivityListCreateBinding binding;
    List<Doctor> doctorList;
    ActividadAdapter adapter;
    List<Doctor> filteredList;

    ArrayList<Actividad> actividadList;

    FirebaseAuth auth;
    FirebaseFirestore db;

    TextView textView;

    ImageButton button;
    Button lgout;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        String id=auth.getCurrentUser().getUid();

        textView= findViewById(R.id.nombre_perfil);


        db.collection("usuarios")
                .document(id)
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot= task.getResult();
                        if(documentSnapshot.exists()){
                            String nombreCompleto = documentSnapshot.get("nombre").toString();
                            String primerNombre = nombreCompleto.split(" ")[0];
                            textView.setText(primerNombre);
                        }
                    }

                });

        button=findViewById(R.id.imageView2);

        lgout=findViewById(R.id.btn_logout);

        lgout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(ListCreateActivity.this,Login.class));
            finish();
        });





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListCreateActivity.this, Profile.class));
            }
        });

        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        actividadList=new ArrayList<Actividad>();
        adapter=new ActividadAdapter(ListCreateActivity.this,actividadList);

        recyclerView.setAdapter(adapter);
        EventChangeListener();




        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ListCreateActivity.this, InsertarActividad.class));

            }
        });
    }

    private void EventChangeListener() {

        db.collection("actividades")
                .whereEqualTo("user", auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            return;
                        }
                        for (DocumentChange dc:value.getDocumentChanges()){
                            if (dc.getType()==DocumentChange.Type.ADDED){
                                actividadList.add(dc.getDocument().toObject(Actividad.class));
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }
                });
    }

    private void filterData(String query) {
        filteredList.clear();

        // Perform the filtering
        for (Doctor item : doctorList) {
            if (item.getLastName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }
}