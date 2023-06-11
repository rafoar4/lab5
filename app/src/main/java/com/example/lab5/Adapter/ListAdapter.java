package com.example.lab5.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.lab5.ListCreateActivity;
import com.example.lab5.R;
import com.example.lab5.entity.Doctor;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.DoctorViewHolder>{
    private List<Doctor> doctorList;
    private Context context;

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_doc,parent,false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor d = doctorList.get(position);
        holder.doctor = d;

        TextView nombre = holder.itemView.findViewById(R.id.nombre);
        nombre.setText(d.getLastName());

        TextView ubicacion = holder.itemView.findViewById(R.id.descripcion);
        ubicacion.setText(d.getUbicacion());

        ImageView imageView = holder.itemView.findViewById(R.id.imageView);
       //Glide.with(context).load(d.getPicture()).into(foto);
        //Picasso.get().load("https://randomuser.me/api/portraits/women/0.jpg").into(foto);

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
        String imageUrl = d.getPicture();
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder{

        Doctor doctor;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
