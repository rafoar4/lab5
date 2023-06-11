package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.lab5.databinding.ActivityListCreateBinding;
import com.example.lab5.databinding.ActivityPerfilBinding;
import com.example.lab5.entity.Doctor;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Perfil extends AppCompatActivity {

    ActivityPerfilBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Doctor doctor = (Doctor) getIntent().getSerializableExtra("doctor");

        binding.textView15.setText("Dr. "+doctor.getLastName()+" "+doctor.getFirstName());
        binding.textView16.setText(doctor.getGender());
        binding.textView17.setText("mailto:"+doctor.getEmail());
        Integer costo =  doctor.getAge()*3;
        binding.textView19.setText("S/"+costo.toString());
        binding.textView21.setText(doctor.getUbicacion());
        Integer age = doctor.getAge();
        binding.textView22.setText(age.toString());
        binding.textView23.setText(doctor.getPhone());
        binding.textView24.setText(doctor.getNationality());

        // Create a TrustManager that trusts all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((HostnameVerifier) (hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load the image using Glide
        String imageUrl = doctor.getPicture();
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(Perfil.this).load(imageUrl).apply(requestOptions).into(binding.imageView3);

        binding.imageButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Perfil.this, ListCreateActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}