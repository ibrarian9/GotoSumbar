package com.app.gotosumbar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.gotosumbar.DetailWisataActivity;
import com.app.gotosumbar.Model.TempatWisata;
import com.app.gotosumbar.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.MyViewHolder> {

    Context context;

    private final ArrayList<TempatWisata> list;

    public WisataAdapter(Context context, ArrayList<TempatWisata> list) {
        this.context = context;
        this.list = list;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WisataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WisataAdapter.MyViewHolder holder, int position) {
        TempatWisata wisata = list.get(position);

        //  Data Nama
        if (wisata.getNama().length() <= 10){
            holder.nama.setText(wisata.getNama().substring(0, 10) + "...");
        } else if (wisata.getNama().length() <= 13) {
            holder.nama.setText(wisata.getNama().substring(0, 13) + "...");
        } else if (wisata.getNama().length() <= 14){
            holder.nama.setText(wisata.getNama().substring(0, 14) + "...");
        } else {
            holder.nama.setText(wisata.getNama().substring(0, 15) + "...");
        }

        //  Data Lokasi
        if (wisata.getLokasi().isEmpty()){
            holder.lokasi.setText("Maaf, Data Belum di Input...".substring(0, 15) + "...");
        } else {
            if (wisata.getLokasi().length() <= 10){
                holder.lokasi.setText(wisata.getLokasi().substring(0, 10) + "...");
            } else if (wisata.getLokasi().length() <= 13) {
                holder.lokasi.setText(wisata.getLokasi().substring(0, 13) + "...");
            } else {
                holder.lokasi.setText(wisata.getLokasi().substring(0, 15) + "...");
            }
        }

        //  Data Poto
        if (wisata.getFoto().isEmpty()) {
            Picasso.get()
                    .load(R.drawable.no_image)
                    .fit().into(holder.poto);
        } else {
            Picasso.get()
                    .load(wisata.getFoto())
                    .fit().into(holder.poto);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, DetailWisataActivity.class);
            i.putExtra("id", wisata.getId());
            i.putExtra("poto", wisata.getFoto());
            i.putExtra("nama", wisata.getNama());
            i.putExtra("ket", wisata.getKet());
            i.putExtra("rate", wisata.getRate());
            i.putExtra("loc", wisata.getLokasi());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView poto;
        TextView nama, lokasi;

        public MyViewHolder(@NonNull View v) {
            super(v);

            nama = v.findViewById(R.id.tvNama);
            poto = v.findViewById(R.id.ivFoto);
            lokasi = v.findViewById(R.id.tvLokasi);
        }
    }
}
