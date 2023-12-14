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

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.MyViewHolder> {

    Context context;
    private final ArrayList<TempatWisata> list;

    public NotifAdapter(Context context, ArrayList<TempatWisata> list) {
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
    public NotifAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notif, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NotifAdapter.MyViewHolder h, int pos) {
        TempatWisata data = list.get(pos);

        //  Data Poto
        if (data.getFoto().isEmpty()){
            Picasso.get()
                    .load(R.drawable.no_image).fit()
                    .into(h.poto);
        } else {
            Picasso.get().load(data.getFoto()).fit()
                    .into(h.poto);
        }

        h.ket.setText(data.getKet().substring(0, 8) + "...");

        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, DetailWisataActivity.class);
            i.putExtra("id", data.getId());
            i.putExtra("poto", data.getFoto());
            i.putExtra("nama", data.getNama());
            i.putExtra("ket", data.getKet());
            i.putExtra("rate", data.getRate());
            i.putExtra("loc", data.getLokasi());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView poto;
        TextView ket;
        public MyViewHolder(@NonNull View i) {
            super(i);

            poto = i.findViewById(R.id.newImage);
            ket = i.findViewById(R.id.tvNotif);
        }
    }
}
