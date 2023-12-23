package com.app.gotosumbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutActivity extends AppCompatActivity {

    BottomNavigationView navbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

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