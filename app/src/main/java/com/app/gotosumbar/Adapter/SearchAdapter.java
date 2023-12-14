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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    Context context;
    private ArrayList<TempatWisata> list;

    public SearchAdapter(Context context, ArrayList<TempatWisata> list) {
        this.context = context;
        this.list = list;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<TempatWisata> filterList) {
        list = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int pos) {
        TempatWisata wisata = list.get(pos);

        holder.nama.setText(wisata.getNama());
        if (wisata.getLokasi().isEmpty()){
            holder.lokasi.setText("Maaf, Data Belum Di Input...");
        } else {
            holder.lokasi.setText(wisata.getLokasi());
        }

        if (wisata.getFoto().isEmpty()){
            Picasso.get()
                    .load(R.drawable.no_image).fit()
                    .into(holder.poto);
        } else {
            Picasso.get().load(wisata.getFoto()).fit()
                    .into(holder.poto);
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

        public MyViewHolder(@NonNull View i) {
            super(i);

            poto = i.findViewById(R.id.ivFoto);
            nama = i.findViewById(R.id.tvNama);
            lokasi = i.findViewById(R.id.tvLokasi);
        }
    }
}
