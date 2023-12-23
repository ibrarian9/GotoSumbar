package com.app.gotosumbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gotosumbar.Model.UserDetail;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView logout, nama, hape, faq, about, report;
    ImageView pp, camera, gallery;
    Uri filePath;
    ActivityResultLauncher<Intent> resultLauncherCamera, resultLauncherGallery;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //  define Firebase Storage
        storage = FirebaseStorage.getInstance();
        StorageReference sRef = storage.getReference();

        logout = findViewById(R.id.tvLogout);
        nama = findViewById(R.id.nama);
        hape = findViewById(R.id.tvHape);
        faq = findViewById(R.id.faq);
        about = findViewById(R.id.about);
        report = findViewById(R.id.report);
        pp = findViewById(R.id.potoProfile);

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

        //  Choose Poto Profile
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.dialog_poto);
        d.setCancelable(false);
        dialogFull(d);
        camera = d.findViewById(R.id.camera);
        gallery = d.findViewById(R.id.gallery);

        pp.setOnClickListener(v -> {
            camera.setOnClickListener(v1 -> {
                Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                resultLauncherCamera.launch(takePic);
                d.dismiss();
            });

            gallery.setOnClickListener(v2 -> {
                Intent openGallery = new Intent(Intent.ACTION_PICK);
                openGallery.setType("image/*");
                resultLauncherGallery.launch(openGallery);
            });
            d.show();
        });

        resultLauncherCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if (data != null){
                        Bitmap poto = (Bitmap) data.getExtras().get("data");
                        System.out.println(poto);
                        Picasso.get().load(poto.toString()).fit().into(pp);
                    } else {
                        Toast.makeText(this, "Data tidak ada...", Toast.LENGTH_SHORT).show();
                    }
                }
        });

        resultLauncherGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null){
                            filePath = data.getData();
                            Picasso.get().load(filePath).fit().into(pp);
                        } else {
                            Toast.makeText(this, "Data tidak ada...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //  Confirm Logout
        Dialog dialog = new Dialog(this);
        logout.setOnClickListener(v -> {
            dialog.setContentView(R.layout.dialog_logout);
            dialog.setCancelable(false);
            dialogFull(dialog);

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

    private void dialogFull(Dialog dialog) {
        if (dialog.getWindow() != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
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