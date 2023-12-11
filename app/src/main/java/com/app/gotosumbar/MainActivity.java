package com.app.gotosumbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RelativeLayout signIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener( v -> startActivity(new Intent(this, LoginActivity.class)));

        signUp = findViewById(R.id.register);
        signUp.setOnClickListener( v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}