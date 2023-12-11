package com.app.gotosumbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.app.gotosumbar.Adapter.NotifAdapter;
import com.app.gotosumbar.Model.TempatWisata;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotifActivity extends AppCompatActivity {

    ArrayList<TempatWisata> list = new ArrayList<>();
    RecyclerView rv;
    NotifAdapter notifAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        notifAdapter = new NotifAdapter(this, list);
        rv = findViewById(R.id.rvNotif);
        rv.setAdapter(notifAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ValueEventListener valueListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifAdapter.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    TempatWisata data = snapshot1.getValue(TempatWisata.class);
                    list.add(data);
                }
                notifAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Query listNew = FirebaseDatabase.getInstance().getReference("Wisata")
                .orderByChild("update")
                .equalTo("New");

        listNew.addValueEventListener(valueListener);

        BottomNavigationView navbar = findViewById(R.id.botNavbar);
        navbar.setSelectedItemId(R.id.notif);
        navbar.setItemIconTintList(null);
        navbar.setOnItemSelectedListener(i -> {
            int itemId = i.getItemId();
            if (itemId == R.id.notif) {
                return true;
            } else if (itemId == R.id.akun) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            }
            return false;
        });
    }
}