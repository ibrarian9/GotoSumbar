package com.app.gotosumbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class darkModeActivity extends AppCompatActivity {

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_mode);

        radioGroup = findViewById(R.id.radio);

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton radioButton = findViewById(i);
            int id = radioButton.getId();
            if (id == R.id.dark){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(this, "Dark Mode Aktif...", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.light) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(this, "Light Mode Aktif...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}