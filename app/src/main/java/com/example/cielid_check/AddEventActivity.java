package com.example.cielid_check;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddEventActivity extends AppCompatActivity {

    ImageButton post;
    TextView back,upload,title;
    ImageView iv;
    ProgressBar pb;

    EditText ettitle,etdesc;
    CardView cv;


    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    DatabaseReference databaseReference;

    Uri imageUridp;
    UploadTask uploadTask;
    StorageReference storageReference;
    private static final int PICK_IMAGE=1;

    String year,month,day;

    EventMember member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        member = new EventMember();

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            year = extras.getString("year");
            month = extras.getString("month");
            day = extras.getString("day");
        }
        else Toast.makeText(this, "No Day Selected", Toast.LENGTH_SHORT).show();

        databaseReference = database.getReference("All Event").child(year).child(month).child(day);
        storageReference = FirebaseStorage.getInstance().getReference("Event Picture");

        back = findViewById(R.id.tv_back_cp);
        title = findViewById(R.id.tv_title_ae);
        post = findViewById(R.id.btn_submit);
        ettitle = findViewById(R.id.et_title_cp);
        etdesc = findViewById(R.id.et_desc_cp);
        iv = findViewById(R.id.iv_ae);
        pb = findViewById(R.id.pv_login);

        back.setOnClickListener(v -> onBackPressed());

        post.setOnClickListener(view -> doPost());

        iv.setOnClickListener(view -> chooseImage());
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

    private void doPost() {
        pb.setVisibility(View.VISIBLE);
        String titleholder = ettitle.getText().toString();
        String descholder = etdesc.getText().toString();

        if(!(TextUtils.isEmpty(titleholder)&&TextUtils.isEmpty(descholder))&&imageUridp!=null){

            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUridp));
            uploadTask = reference.putFile(imageUridp);

            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if(!task.isSuccessful()){
                    throw task.getException();

                }
                return reference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();

                    String id1 = databaseReference.push().getKey();

                    member.setDate(year+"/"+month+"/"+day);
                    member.setUrl(downloadUri.toString());
                    member.setDesc(descholder);
                    member.setTitle(titleholder);
                    member.setPostkey(id1);

                    databaseReference.child(id1).setValue(member);

                    Toast.makeText(AddEventActivity.this, "Event Created", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                    pb.setVisibility(View.GONE);
                }else{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                }
            });

        }else{
            pb.setVisibility(View.GONE);
            Toast.makeText(this, "Please fill up all the mandatory fields", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        title.setText("Create Event ("+month+"/"+day+"/"+year+")");




    }
}