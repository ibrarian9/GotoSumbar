package com.app.gotosumbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gotosumbar.Adapter.WisataAdapter;
import com.app.gotosumbar.Model.TempatWisata;
import com.app.gotosumbar.Model.UserDetail;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    WisataAdapter wisataAdapter;
    RecyclerView rv;
    FirebaseUser user;
    FirebaseAuth mAuth;
    TextView userName;
    LinearLayout wisata, kuliner, edukasi ,religi;
    ArrayList<TempatWisata> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Define Id
        wisata = findViewById(R.id.llWisata);
        kuliner = findViewById(R.id.llKuliner);
        edukasi = findViewById(R.id.llEdukasi);
        religi = findViewById(R.id.llReligi);
        userName = findViewById(R.id.userName);
        BottomNavigationView navbar = findViewById(R.id.botNavbar);

        if (user == null) {
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            showDataProfile(user);
        }

        wisataAdapter = new WisataAdapter(this, list);
        rv = findViewById(R.id.rv);
        rv.setAdapter(wisataAdapter);
        rv.setLayoutManager(new GridLayoutManager(this, 2));

        ValueEventListener eventListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wisataAdapter.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TempatWisata wisata = dataSnapshot.getValue(TempatWisata.class);
                    list.add(wisata);
                }
                wisataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        //  Query All Data
        Query listData = FirebaseDatabase.getInstance().getReference("Wisata");
        listData.addValueEventListener(eventListener);

        // List by Kategori
        wisata.setOnClickListener(v -> {
            //  By Tempat Wisata
            Query listWisata = FirebaseDatabase.getInstance().getReference("Wisata")
                    .orderByChild("kategori")
                    .equalTo("Tempat Wisata");
            listWisata.addValueEventListener(eventListener);
            Toast.makeText(this, "Query data by Tempat Wisata", Toast.LENGTH_SHORT).show();
        });
        religi.setOnClickListener(v -> {
            //  By Kuliner
            Query listReligi = FirebaseDatabase.getInstance().getReference("Wisata")
                    .orderByChild("kategori")
                    .equalTo("Religi");
            listReligi.addValueEventListener(eventListener);
            Toast.makeText(this, "Query data by Religi", Toast.LENGTH_SHORT).show();
        });
        edukasi.setOnClickListener(v -> {
            //  By Edukasi
            Query listEdukasi = FirebaseDatabase.getInstance().getReference("Wisata")
                    .orderByChild("kategori")
                    .equalTo("Edukasi");
            listEdukasi.addValueEventListener(eventListener);
            Toast.makeText(this, "Query data by Edukasi", Toast.LENGTH_SHORT).show();

        });
        kuliner.setOnClickListener(v -> {
            //  By kuliner
            Query listKuliner = FirebaseDatabase.getInstance().getReference("Wisata")
                    .orderByChild("kategori")
                    .equalTo("Kuliner");
            listKuliner.addValueEventListener(eventListener);
            Toast.makeText(this, "Query data by Kuliner", Toast.LENGTH_SHORT).show();
        });

        //  Bottom Navbar
        navbar.setSelectedItemId(R.id.home);
        navbar.setItemIconTintList(null);
        navbar.setOnItemSelectedListener(i -> {
            int itemId = i.getItemId();
            if (itemId == R.id.home) {
                return true;
            } else if (itemId == R.id.akun) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.notif) {
                startActivity(new Intent(this, NotifActivity.class));
                return true;
            } else if (itemId == R.id.search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            }
            return false;
        });
    }

    private void showDataProfile(FirebaseUser user) {
        String userId = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetail uDetail = snapshot.getValue(UserDetail.class);
                if (uDetail != null){
                    String name = uDetail.getUserName();
                    userName.setText(name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}