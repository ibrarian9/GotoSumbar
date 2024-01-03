package com.app.gotosumbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedbackActivity extends AppCompatActivity {

    RatingBar ratingBar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    BottomNavigationView navbar;
    EditText edReport;
    long maxId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        String userId = user.getUid();

        edReport = findViewById(R.id.edReport);
        ratingBar = findViewById(R.id.rating);
        RelativeLayout send = findViewById(R.id.send);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feedback").child(userId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxId = (snapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send.setOnClickListener(v -> {
            String rate = String.valueOf(ratingBar.getRating());
            String report = edReport.getText().toString();

            if (TextUtils.isEmpty(report)){
                Toast.makeText(this, "Report belum diisi...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(rate)) {
                Toast.makeText(this, "Rating belum diisi...", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Feedback").child(userId);
                db.child(String.valueOf(maxId + 1)).child("rating").setValue(rate);
                db.child(String.valueOf(maxId + 1)).child("feedback").setValue(report).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(this, "Terima Kasih telah memberikan kami masukan...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, HomeActivity.class));
                    } else {
                        Toast.makeText(this, "Report Gagal Terkirim...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, HomeActivity.class));
                    }
                });
            }
        });

        navbar = findViewById(R.id.botNavbar);
        navbar.setSelectedItemId(R.id.akun);
        navbar.setItemIconTintList(null);
        navbar.setOnItemSelectedListener(i -> {
            int itemId = i.getItemId();
            if (itemId == R.id.akun) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.home) {
                startActivity(new Intent(this, HomeActivity.class));
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
}