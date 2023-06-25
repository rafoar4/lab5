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
import com.example.lab5.R;

import com.example.lab5.model.Actividad;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder> {

    Context context;
    ArrayList<Actividad> arrayList;

    StorageReference storageReference;

    public ActividadAdapter(Context context,ArrayList<Actividad> arrayList){
        this.context=context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public ActividadAdapter.ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_actividad,parent,false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadAdapter.ActividadViewHolder holder, int position) {

        storageReference= FirebaseStorage.getInstance().getReference("actividades");
        Actividad actividad = arrayList.get(position);
        holder.titulo.setText(actividad.getTitulo());
        holder.descripcion.setText(actividad.getDescripcion());
        holder.fechai.setText(actividad.getFechai());
        holder.fechaf.setText(actividad.getFechaf());
        holder.inicio.setText(actividad.getInicio());
        holder.fin.setText(actividad.getFin());
        Glide.with(context).load(actividad.getImagenUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ActividadViewHolder extends RecyclerView.ViewHolder{
        TextView titulo;
        TextView descripcion;
        TextView fechai,fechaf;
        TextView inicio;
        TextView fin;

        ImageView imageView;


        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo=itemView.findViewById(R.id.titulo);
            descripcion=itemView.findViewById(R.id.descripcion);
            fechai=itemView.findViewById(R.id.fechai);
            fechaf=itemView.findViewById(R.id.fechaf);
            inicio=itemView.findViewById(R.id.inicio);
            fin=itemView.findViewById(R.id.fin);
            imageView=itemView.findViewById(R.id.imageView);

        }
    }
}
