package com.app.gotosumbar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    RelativeLayout btnNext;
    EditText edEmail;
    FirebaseAuth auth;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btnNext = findViewById(R.id.btnNext);
        edEmail = findViewById(R.id.edEmail);
        pb = findViewById(R.id.pb);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.setCancelable(false);
        if (dialog.getWindow() != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        
        btnNext.setOnClickListener( v -> {
            String email = edEmail.getText().toString();

            if (TextUtils.isEmpty(email)){
                Toast.makeText(this, "Email Kosong...", Toast.LENGTH_SHORT).show();
            } else {
                pb.setVisibility(View.VISIBLE);
                auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                           if (task.isSuccessful()){
                               pb.setVisibility(View.GONE);
                               TextView ok = dialog.findViewById(R.id.tvOk);
                               ok.setOnClickListener(v1 -> dialog.dismiss());
                               dialog.show();
                           } else {
                               pb.setVisibility(View.GONE);
                               Toast.makeText(this, "Ada Kesalahan...", Toast.LENGTH_SHORT).show();
                           }
                            pb.setVisibility(View.GONE);
                        });
            }
        });

    }
}