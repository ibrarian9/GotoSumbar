package com.app.gotosumbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    RelativeLayout login;
    FirebaseAuth mAuth;
    EditText edEmail, edPass;
    String email, pass;
    TextView forget;
    ProgressBar pb;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        //  Define ID
        edEmail = findViewById(R.id.edEmailLogin);
        edPass = findViewById(R.id.edPassLogin);
        pb = findViewById(R.id.pbLogin);
        login = findViewById(R.id.rlLogin);
        forget = findViewById(R.id.forgetPass);

        forget.setOnClickListener( view -> startActivity(new Intent(this, ForgetPasswordActivity.class)));

        login.setOnClickListener(v -> {
           email = String.valueOf(edEmail.getText());
           pass = String.valueOf(edPass.getText());

           if (TextUtils.isEmpty(email)){
               NotifEmail();
           } else if (TextUtils.isEmpty(pass)) {
               NotifPassword();
           } else {
               pb.setVisibility(View.VISIBLE);
               AuthWithEmailAndPass();
           }
        });
    }

    private void AuthWithEmailAndPass() {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                    }
                    pb.setVisibility(View.GONE);
                });
    }
    private void NotifPassword() {
        Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
    }
    private void NotifEmail() {
        Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
    }
}