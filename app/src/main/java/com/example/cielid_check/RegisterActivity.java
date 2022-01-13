package com.example.cielid_check;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    String status;
    TextView title;
    ImageView back,iv;
    EditText username,pass,conf_pass,name;
    LottieAnimationView lot;
    ImageButton signup;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    ProgressBar pb;
    StorageReference storageReference;
    Uri imageUridp;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference databaseReference;
    DocumentReference documentReference;
    AlluserMember member;
    String currentUserId;
    UploadTask uploadTask;

    private static final int PICK_IMAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            status = bundle.getString("uid");

        }else{
            Toast.makeText(this, "status missing", Toast.LENGTH_SHORT).show();
        }

        databaseReference = database.getReference("All users");
        member= new AlluserMember();

        title = findViewById(R.id.tv_ca_rg);
        back = findViewById(R.id.btn_back_rg);
        username = findViewById(R.id.et_username_rg);
        name = findViewById(R.id.et_name_rg);
        pass = findViewById(R.id.et_password_rg);
        conf_pass = findViewById(R.id.et_password2_rg);
        signup = findViewById(R.id.sign_up_rg);
        pb = findViewById(R.id.pv_login);
        lot = findViewById(R.id.loginlot);
        iv = findViewById(R.id.iv_register);

        back.setOnClickListener(v -> onBackPressed());
        signup.setOnClickListener(v -> register());
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");

        iv.setOnClickListener(v -> {
            chooseImage();
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData()!=null){
                imageUridp = data.getData();
                Picasso.get().load(imageUridp).into(iv);
            }

        }catch (Exception e){
            Toast.makeText(this, "Error"+e, Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void register() {
        String email = username.getText().toString();
        String password = pass.getText().toString();
        String confirm_password = conf_pass.getText().toString();
        String tempname = name.getText().toString();


        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)&& !TextUtils.isEmpty(confirm_password)&& !TextUtils.isEmpty(tempname)&& imageUridp != null){
            if(password.equals(confirm_password)){
                pb.setVisibility(View.VISIBLE);
                enabledElement(false);
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Successfully account created", Toast.LENGTH_LONG).show();
                        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task2 -> {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            currentUserId = user.getUid();
                            documentReference = db.collection("user").document(currentUserId);


                            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUridp));
                            uploadTask = reference.putFile(imageUridp);

                            Task<Uri> urlTask = uploadTask.continueWithTask(task4 -> {
                                if(!task4.isSuccessful()){
                                    throw task4.getException();
                                }
                                return reference.getDownloadUrl();
                            }).addOnCompleteListener(task3 -> {

                                if(task3.isSuccessful()){
                                    Uri downloadUri = task3.getResult();

                                    Map<String,String> profile = new HashMap<>();
                                    profile.put("name",tempname);
                                    profile.put("status",status);
                                    profile.put("email",email);
                                    profile.put("uid",currentUserId);

                                    member.setName(tempname);
                                    member.setStatus(status);
                                    member.setUid(currentUserId);
                                    member.setEmail(email);
                                    member.setUrl(downloadUri.toString());

                                    databaseReference.child(currentUserId).setValue(member);

                                    documentReference.set(profile)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(RegisterActivity.this, "Profile Created", Toast.LENGTH_SHORT).show();

                                                Handler handler = new Handler();
                                                handler.postDelayed(() -> {
                                                    mAuth.signOut();
                                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                },2000);
                                            });
                                }
                            });

                        });
//                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                        finish();
                    }else{
                        pb.setVisibility(View.GONE);
                        enabledElement(true);
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(RegisterActivity.this, "Error:"+error, Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                pb.setVisibility(View.GONE);
                enabledElement(true);
                Toast.makeText(RegisterActivity.this, "password and confirm password is not matching", Toast.LENGTH_SHORT).show();
            }
        }else{
            pb.setVisibility(View.GONE);
            enabledElement(true);
            Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();

        }
    }

    private void enabledElement(Boolean x){
        username.setEnabled(x);
        pass.setEnabled(x);
        back.setEnabled(x);
        conf_pass.setEnabled(x);
        signup.setEnabled(x);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(status.equals("student")){
            title.setText("Create Student Account");
            lot.setAnimation(R.raw.studentlot);
        }else if(status.equals("teacher")){
            title.setText("Create Teacher Account");
            lot.setAnimation(R.raw.teacherlot);
        }else if(status.equals("admin")){
            title.setText("Create Admin Account");
            lot.setAnimation(R.raw.adminlot);
        }
    }
}