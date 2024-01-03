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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView logout, nama, hape, faq, about, report, darkMode, feedback;
    ImageView pp, camera, gallery;
    Uri imageUri;
    ActivityResultLauncher<Intent> resultLauncherCamera, resultLauncherGallery;
    FirebaseStorage storage;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //  define Firebase Storage
        storage = FirebaseStorage.getInstance();

        logout = findViewById(R.id.tvLogout);
        nama = findViewById(R.id.nama);
        hape = findViewById(R.id.tvHape);
        faq = findViewById(R.id.faq);
        about = findViewById(R.id.about);
        report = findViewById(R.id.report);
        pp = findViewById(R.id.potoProfile);
        darkMode = findViewById(R.id.darkMode);
        feedback = findViewById(R.id.feedback);

        faq.setOnClickListener( v -> startActivity(new Intent(this, FaqActivity.class)));
        about.setOnClickListener( v -> startActivity(new Intent(this, AboutActivity.class)));
        report.setOnClickListener( v -> startActivity(new Intent(this, ReportActivity.class)));
        darkMode.setOnClickListener( v -> startActivity(new Intent(this, darkModeActivity.class)));
        feedback.setOnClickListener( v -> startActivity(new Intent(this, FeedbackActivity.class)));

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
                d.dismiss();
            });
            d.show();
        });

        resultLauncherCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if (data != null) {
                        Bundle ex = data.getExtras();
                        if (ex != null) {
                            Bitmap bitmap = (Bitmap) ex.get("data");
                            assert bitmap != null;
                            uploadToFirebase(bitmap);
                        }
                    } else {
                        Toast.makeText(this, "Data tidak ada...", Toast.LENGTH_SHORT).show();
                    }
                }
        });

        resultLauncherGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            imageUri = data.getData();
                            upToFirebase(imageUri);
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
        uid = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetail uDetail = snapshot.getValue(UserDetail.class);
                if (uDetail != null){
                    String name = uDetail.getUserName();
                    String noHape = uDetail.getUserNoHp();
                    nama.setText(name);
                    hape.setText(noHape);
                    if (uDetail.getUserImage() != null){
                        Picasso.get().load(uDetail.getUserImage()).fit().into(pp);
                    } else {
                        Picasso.get().load(R.drawable.icon_akun).fit().into(pp);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void uploadToFirebase(Bitmap bitmap){
        StorageReference fileRef = storage.getReference().child("image/" + uid);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = fileRef.putBytes(data);
        uploadTask.continueWithTask(task -> {
           if (!task.isSuccessful()){
               throw Objects.requireNonNull(task.getException());
           }
            return fileRef.getDownloadUrl();
        }).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                Uri download = task1.getResult();

                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                database.child("userImage").setValue(download.toString()).addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        Toast.makeText(this, "Foto berhasil di Upload...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void upToFirebase(Uri uri){
        StorageReference fileRef = storage.getReference().child("image/" + uid);
        UploadTask uploadImg = fileRef.putFile(uri);

        uploadImg.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return fileRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Uri download = task.getResult();

                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                database.child("userImage").setValue(download.toString()).addOnCompleteListener(task1 -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Foto berhasil di Upload...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}