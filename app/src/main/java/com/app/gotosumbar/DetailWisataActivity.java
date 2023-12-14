package com.app.gotosumbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gotosumbar.Fragment.AboutFragment;
import com.app.gotosumbar.Fragment.ReviewFragment;
import com.app.gotosumbar.Model.Komen;
import com.app.gotosumbar.Model.UserDetail;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class DetailWisataActivity extends AppCompatActivity {

    ImageView poto, like, send;
    TextView nama, lokasi, rating;
    RatingBar rateBar;
    EditText edKomen;
    BottomNavigationView navbar;
    FloatingActionButton btnBack;
    FirebaseUser user;
    FirebaseAuth mAuth;
    String name;

    @Override
    protected void onStart() {
        super.onStart();

        navbar = findViewById(R.id.navbar);
        navbar.setSelectedItemId(R.id.about);
        navbar.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.about) {
                Fragment f = new AboutFragment();
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.frame, f).commit();
                return true;
            } else if (id == R.id.review) {
                Fragment f = new ReviewFragment();
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.frame, f).commit();
                return true;
            }
            return false;
        });
        navbar.performClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);

        //  define id
        btnBack = findViewById(R.id.BtnBack);
        nama = findViewById(R.id.tvNamaDetail);
        lokasi = findViewById(R.id.tvLocDetail);
        poto = findViewById(R.id.imageView);
        rating = findViewById(R.id.tvRating);
        navbar = findViewById(R.id.navbar);
        rateBar = findViewById(R.id.rate);
        like = findViewById(R.id.like);
        edKomen = findViewById(R.id.edKomen);
        send = findViewById(R.id.send);

        //  Get Data from Intent
        int dataId = getIntent().getIntExtra("id", 1);
        String urlPoto = getIntent().getStringExtra("poto");
        String dataNama = getIntent().getStringExtra("nama");
        String dataLok = getIntent().getStringExtra("loc");
        Float dataRate = getIntent().getFloatExtra("rate", 3);
        getIntent().getStringExtra("ket");

        //  Set Data
        nama.setText(dataNama);
        rating.setText(String.valueOf(dataRate));
        rateBar.setRating(dataRate);

        //  Set Data Poto
        if (urlPoto == null || urlPoto.isEmpty()) {
            Picasso.get().load(R.drawable.no_image).fit().into(poto);
        } else {
            Picasso.get().load(urlPoto).fit().into(poto);
        }
        //  Set Data Lokasi
        assert dataLok != null;
        if (dataLok.isEmpty()) {
            lokasi.setText("Maaf, Data Belum di Input...");
        } else {
            lokasi.setText(dataLok);
        }

        //  Send Komen to database
        send.setOnClickListener(v -> {
            //  Komen
            String dataKomen = edKomen.getText().toString();

            if (dataKomen.isEmpty()){
                Toast.makeText(this, "Komentar Kosong...", Toast.LENGTH_SHORT).show();
            } else {
                //   Get UserName
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                assert user != null;
                String userId = user.getUid();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserDetail uDetail = snapshot.getValue(UserDetail.class);
                        if (uDetail != null){
                            name = uDetail.getUserName();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wisata")
                                                    .child(String.valueOf(dataId));
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild("komen")){

                                        Query query = ref.child("komen").limitToLast(1);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot child: snapshot.getChildren()){
                                                    String id = child.getKey();
                                                    assert id != null;
                                                    int data = Integer.parseInt(id) + 1;

                                                    Komen komen = new Komen(dataKomen, name);

                                                    ref.child("komen").child(String.valueOf(data)).setValue(komen).addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(DetailWisataActivity.this, "Komen Berhasil", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(DetailWisataActivity.this, "Komen Tidak Berhasil", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        Komen komen = new Komen(dataKomen, name);
                                        ref.child("komen").child("1").setValue(komen).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()){
                                                Toast.makeText(DetailWisataActivity.this, "Komen Berhasil", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(DetailWisataActivity.this, "Komen Tidak Berhasil", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                edKomen.getText().clear();
            }
        });

        like.setOnClickListener(v -> {
            //  Register Notification Channel
            NotificationChannel channel = new NotificationChannel("Test", "App gotoSumbar", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Anda berhasil memberikan Like...");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            //  Set Content Notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Test")
                    .setContentTitle("App gotoSumbar")
                    .setContentText("Anda berhasil memberikan Like " + dataNama)
                    .setSmallIcon(R.drawable.baseline_notif_24)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);


            NotificationManagerCompat compat = NotificationManagerCompat.from(DetailWisataActivity.this);
            //  Check Permissions
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.POST_NOTIFICATIONS
                }, 1);
            }
            compat.notify(1, builder.build());
        });

        //  Back Button
        btnBack.setOnClickListener(v -> super.onBackPressed());

        //  Navbar For Fragment
        navbar.setSelectedItemId(R.id.about);
        navbar.setOnItemSelectedListener(i -> {
            int id = i.getItemId();
            if (id == R.id.about) {
                showFragmentAbout();
                return true;
            } else if (id == R.id.review) {
                showFragmentReview();
                return true;
            }
            return false;
        });
    }

    private void showFragmentReview() {
        Fragment f = new ReviewFragment();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame, f).commit();
    }

    private void showFragmentAbout() {
        Fragment f = new AboutFragment();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame, f).commit();
    }
}