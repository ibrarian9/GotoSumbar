package com.app.gotosumbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FaqActivity extends AppCompatActivity {

    LinearLayout ll1;
    TextView tvData1, tvData2;
    BottomNavigationView navbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        ll1 = findViewById(R.id.ll1);
        tvData1 = findViewById(R.id.tvData1);
        tvData2 = findViewById(R.id.tvData2);

        Drawable img1 = ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_arrow_up_24, null);
        Drawable img2 = ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_arrow_down_24, null);

        ll1.setOnClickListener(view -> {
            if (!tvData2.isShown()){
                tvData1.setCompoundDrawablesWithIntrinsicBounds(null, null, img1,null);
                tvData2.setVisibility(View.VISIBLE);
            } else {
                tvData1.setCompoundDrawablesWithIntrinsicBounds(null, null, img2, null);
                tvData2.setVisibility(View.GONE);
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