package com.app.gotosumbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout signIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener( v -> startActivity(new Intent(this, LoginActivity.class)));

        signUp = findViewById(R.id.register);
        signUp.setOnClickListener( v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}