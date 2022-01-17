package com.example.cielid_check;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    EditText name,email,number;
    TextView save,cancel,back;
    ImageView iv;
    ProgressBar pb;

    Uri imageUridp;

    String urlBundle;

    private static final int PICK_IMAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            urlBundle = extras.getString("w");
        }
        else Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();

        reference = database.getReference("All users").child(currentuid);

        name = findViewById(R.id.et_name_ep);
        email = findViewById(R.id.et_email_ep);
        number = findViewById(R.id.et_number_ep);
        save = findViewById(R.id.tv_save_ep);
        cancel = findViewById(R.id.tv_cancel_ep);
        iv = findViewById(R.id.iv_ep);
        back = findViewById(R.id.tv_back_ep);
        pb = findViewById(R.id.pv_login);

        back.setOnClickListener(view -> onBackPressed());

        iv.setOnClickListener(view -> chooseImage());

        cancel.setOnClickListener(view -> onBackPressed());

        save.setOnClickListener(view -> updateprofile());

    }

    private void updateprofile() {
        pb.setVisibility(View.VISIBLE);

        String nameholder = name.getText().toString();
        String emailholder = email.getText().toString();
        String numberholder = number.getText().toString();

        if(TextUtils.isEmpty(nameholder) || TextUtils.isEmpty(emailholder) || TextUtils.isEmpty(numberholder)){
            Toast.makeText(EditProfileActivity.this, "Empty data invalid please fill up all requirements", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
        }else{
            reference.child("name").setValue(nameholder);
            reference.child("email").setValue(emailholder);
//            reference.child("floor").setValue(floorholder);
            Toast.makeText(EditProfileActivity.this, "Successfully change data", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

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
                iv.setImageDrawable(null);
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


    @Override
    public void onStart() {
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameholder = snapshot.child("name").getValue(String.class);
                String emailholder = snapshot.child("email").getValue(String.class);
                String numberholder = snapshot.child("number").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);

                name.setText(nameholder);
                email.setText(emailholder);
                number.setText(numberholder);
                Picasso.get().load(url).into(iv);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}