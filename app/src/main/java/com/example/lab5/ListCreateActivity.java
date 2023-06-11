package com.example.lab5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.lab5.Adapter.ListAdapter;
import com.example.lab5.Retrofit.Repository;
import com.example.lab5.databinding.ActivityListCreateBinding;
import com.example.lab5.entity.Doctor;
import com.example.lab5.entity.RandomUserResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ListCreateActivity extends AppCompatActivity {
    ActivityListCreateBinding binding;
    List<Doctor> doctorList;
    ListAdapter adapter;
    List<Doctor> filteredList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseFirestore.getInstance().collection("Doctor").addSnapshotListener((snapshot, error) -> {
            if(error != null){
                Log.w("msg-test", "Listen failed.", error);
                return;
            }
            doctorList = new ArrayList<>();
            for(QueryDocumentSnapshot doc: snapshot){
                Doctor doctor = doc.toObject(Doctor.class);
                doctorList.add(doctor);
                //Log.d("msg-test", "Nombre: "+doctor.getFirstName());
            }
            //System.out.println(doctorList.size());
            //System.out.println(doctorList.get(0).getEmail());
            adapter = new ListAdapter();
            adapter.setContext(ListCreateActivity.this);
            filteredList = new ArrayList<>(doctorList);
            adapter.setDoctorList(filteredList);

            binding.recycler.setAdapter(adapter);
            binding.recycler.setLayoutManager(new LinearLayoutManager(ListCreateActivity.this));

            EditText searchEditText = findViewById(R.id.editTextTextPersonName);

            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    filterData(s.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        });


        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Load the SSL certificate from the raw resource
                    InputStream inputStream = getResources().openRawResource(R.raw.server);
                    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                    X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);

                    // Create a KeyStore and add the certificate
                    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    keyStore.load(null, null);
                    keyStore.setCertificateEntry("server", certificate);

                    // Create a TrustManagerFactory with the KeyStore
                    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    trustManagerFactory.init(keyStore);

                    // Create an SSLContext with the TrustManagerFactory
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

                    // Create a custom OkHttpClient with the SSLContext
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagerFactory.getTrustManagers()[0])
                            .build();

                    Repository repository = new Retrofit.Builder()
                            .baseUrl("https://randomuser.me")
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(Repository.class);

                    repository.getDoctor().enqueue(new Callback<RandomUserResponse>() {
                        @Override
                        public void onResponse(Call<RandomUserResponse> call, Response<RandomUserResponse> response) {
                            if (response.isSuccessful()) {
                                List<RandomUserResponse.RandomUser> doctor = response.body().getResults();
                                Doctor doctor1 = new Doctor();
                                doctor1.setFirstName(doctor.get(0).getName().getFirst());
                                doctor1.setLastName(doctor.get(0).getName().getLast());
                                if(doctor.get(0).getGender().equalsIgnoreCase("male")){
                                    doctor1.setGender("Hombre");
                                }else {
                                    doctor1.setGender("Mujer");
                                }
                                doctor1.setEmail(doctor.get(0).getEmail());
                                doctor1.setUbicacion(doctor.get(0).getLocation().getCity()+" - "+doctor.get(0).getLocation().getState()+" - "+doctor.get(0).getLocation().getCountry());
                                doctor1.setAge(doctor.get(0).getDob().getAge());
                                doctor1.setPhone(doctor.get(0).getPhone());
                                doctor1.setNationality(doctor.get(0).getNat());
                                doctor1.setPicture(doctor.get(0).getPicture().getLarge());

                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("firstName",doctor1.getFirstName());
                                hashMap.put("lastName",doctor1.getLastName());
                                hashMap.put("gender",doctor1.getGender());
                                hashMap.put("email",doctor1.getEmail());
                                hashMap.put("ubicacion",doctor1.getUbicacion());
                                hashMap.put("age",doctor1.getAge());
                                hashMap.put("phone",doctor1.getPhone());
                                hashMap.put("nationality",doctor1.getNationality());
                                hashMap.put("picture",doctor1.getPicture());

                                FirebaseFirestore.getInstance().collection("Doctor")
                                        .add(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(ListCreateActivity.this,"Doctor creado", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ListCreateActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }
                        @Override
                        public void onFailure(Call<RandomUserResponse> call, Throwable t) {
                            Log.e("API Error", "Request failed: " + t.getMessage());
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
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