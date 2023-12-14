package com.app.gotosumbar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.gotosumbar.Model.Komen;
import com.app.gotosumbar.R;

import java.util.ArrayList;

public class KomenAdapter extends RecyclerView.Adapter<KomenAdapter.MyViewHolder> {

    Context context;
    private final ArrayList<Komen> list;

    public KomenAdapter(Context context, ArrayList<Komen> list) {
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
    public KomenAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_komen, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KomenAdapter.MyViewHolder holder, int pos) {
        Komen data = list.get(pos);

        holder.nama.setText(data.getNama());
        holder.komen.setText(data.getKomen());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nama, komen;

        public MyViewHolder(@NonNull View v) {
            super(v);

            nama = v.findViewById(R.id.tvNamaKomen);
            komen = v.findViewById(R.id.tvKomen);
        }
    }
}
