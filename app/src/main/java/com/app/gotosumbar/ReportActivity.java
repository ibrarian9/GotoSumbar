package com.app.gotosumbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
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

public class ReportActivity extends AppCompatActivity {

    EditText edReport;
    RelativeLayout rlSend;
    FirebaseAuth mAuth;
    FirebaseUser user;
    BottomNavigationView navbar;
    long maxId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        edReport = findViewById(R.id.edReport);
        rlSend = findViewById(R.id.send);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        String userId = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Report").child(userId);
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

        rlSend.setOnClickListener(v -> {
            String report = edReport.getText().toString();

            if (TextUtils.isEmpty(report)){
                Toast.makeText(this, "Report belum diisi...", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Report").child(userId);
                db.child(String.valueOf(maxId + 1)).child("data").setValue(report).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(this, "Terima Kasih telah memberikan kami masukan...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Report Gagal Terkirim...", Toast.LENGTH_SHORT).show();
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