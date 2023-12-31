package com.app.gotosumbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.gotosumbar.Model.UserDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText ednama, edemail, edhape, edpass;
    RelativeLayout signUp;
    FirebaseAuth mAuth;
    ProgressBar pb;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        //  Check if user Signed
        FirebaseUser current = mAuth.getCurrentUser();
        if (current != null){
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ednama = findViewById(R.id.edName);
        edemail = findViewById(R.id.edEmail);
        edhape = findViewById(R.id.edNoHape);
        edpass = findViewById(R.id.edPass);
        signUp = findViewById(R.id.rlSignUp);
        pb = findViewById(R.id.pb);

        signUp.setOnClickListener(v -> {
            String nama, email, pass, hape;

            nama = String.valueOf(ednama.getText());
            email = String.valueOf(edemail.getText());
            pass = String.valueOf(edpass.getText());
            hape = String.valueOf(edhape.getText());

            if (TextUtils.isEmpty(nama)){
                Toast.makeText(RegisterActivity.this, "Enter Nama", Toast.LENGTH_SHORT).show();
                ednama.requestFocus();
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                edemail.requestFocus();
            } else if (TextUtils.isEmpty(pass)) {
                Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                edpass.requestFocus();
            } else if (TextUtils.isEmpty(hape)) {
                Toast.makeText(RegisterActivity.this, "Enter No Handphone", Toast.LENGTH_SHORT).show();
                edhape.requestFocus();
            } else {
                pb.setVisibility(View.VISIBLE);
                registerUser(nama, email, pass, hape);
            }
        });
    }

    private void registerUser(String nama, String email, String pass, String hape) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegisterActivity.this, task -> {
           if (task.isSuccessful()) {

               // Get Uid from Auth Database
               FirebaseUser fUser = mAuth.getCurrentUser();
               assert fUser != null;
               String uid = fUser.getUid();

               UserDetail user = new UserDetail(uid, nama, email, hape, "");

               DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
               db.child(fUser.getUid()).setValue(user).addOnCompleteListener(t -> {
                   if (t.isSuccessful()) {
                       Toast.makeText(RegisterActivity.this, "Account create", Toast.LENGTH_SHORT).show();
                       Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                       i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(i);
                       finish();
                   } else {
                       Toast.makeText(RegisterActivity.this, "Account Registered Failed", Toast.LENGTH_SHORT).show();
                       pb.setVisibility(View.GONE);
                   }
               });
           } else {
               try {
                   throw Objects.requireNonNull(task.getException());
               } catch (FirebaseAuthUserCollisionException e) {
                   edemail.setError("Email is Already registered");
               } catch (Exception e) {
                   // If sign in fails, display a message to the user.
                   Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
               }
               pb.setVisibility(View.GONE);
           }
        });
    }
}