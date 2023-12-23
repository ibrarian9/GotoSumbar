package com.app.gotosumbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.gotosumbar.Model.UserDetail;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView logout, nama, hape, faq, about, report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = findViewById(R.id.tvLogout);
        nama = findViewById(R.id.nama);
        hape = findViewById(R.id.tvHape);
        faq = findViewById(R.id.faq);
        about = findViewById(R.id.about);
        report = findViewById(R.id.report);

        faq.setOnClickListener( v -> startActivity(new Intent(this, FaqActivity.class)));
        about.setOnClickListener( v -> startActivity(new Intent(this, AboutActivity.class)));
        report.setOnClickListener( v -> startActivity(new Intent(this, ReportActivity.class)));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null) {
            Intent i = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            showUserProfile(user);
        }

        Dialog dialog = new Dialog(this);
        logout.setOnClickListener(v -> {
            dialog.setContentView(R.layout.dialog_logout);
            dialog.setCancelable(false);
            if (dialog.getWindow() != null){
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            TextView no = dialog.findViewById(R.id.no);
            no.setOnClickListener( v1 -> dialog.dismiss());

            TextView yes = dialog.findViewById(R.id.yes);
            yes.setOnClickListener( v2 -> {
                mAuth.signOut();
                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(i);
                finish();
                dialog.dismiss();
            });
            dialog.show();
        });

        BottomNavigationView navbar = findViewById(R.id.botNavbar);
        navbar.setSelectedItemId(R.id.akun);
        navbar.setItemIconTintList(null);
        navbar.setOnItemSelectedListener(i -> {
            int itemId = i.getItemId();
            if (itemId == R.id.akun) {
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

    private void showUserProfile(FirebaseUser user) {
        String userId = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetail uDetail = snapshot.getValue(UserDetail.class);
                if (uDetail != null){
                    String name = uDetail.getUserName();
                    String noHape = uDetail.getUserNoHp();
                    nama.setText(name);
                    hape.setText(noHape);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}