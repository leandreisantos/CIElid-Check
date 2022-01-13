package com.example.cielid_check;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddRoomActivity extends AppCompatActivity {

    TextView back;
    ImageView bg;
    EditText name,type,floor;
    ImageButton submit;
    ProgressBar pb;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference databaseReference;
    StorageReference storageReference;

    AllRoomMember member;
    private static final int PICK_IMAGE=1;
    Uri imageUridp;
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        databaseReference = database.getReference("All Room");
        storageReference = FirebaseStorage.getInstance().getReference("Room Background");
        member= new AllRoomMember();

        back = findViewById(R.id.tv_back_ar);
        bg = findViewById(R.id.iv_bg_ar);
        name = findViewById(R.id.et_name_cp);
        type = findViewById(R.id.et_type_cp);
        floor = findViewById(R.id.et_floor_cp);
        submit = findViewById(R.id.btn_sub_ar);
        pb = findViewById(R.id.pv_login);

        back.setOnClickListener(v -> onBackPressed());

        bg.setOnClickListener(v -> chooseImage());
        submit.setOnClickListener(v -> submitRoom());


    }

    private void submitRoom() {
        pb.setVisibility(View.VISIBLE);
        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyy");
        final String savedate = currentdate.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime =new SimpleDateFormat("HH-mm-ss");
        final String savetime = currenttime.format(ctime.getTime());

        String nameholder = name.getText().toString();
        String typeholder = type.getText().toString();
        String floorholder = floor.getText().toString();

        if(!(TextUtils.isEmpty(nameholder)&&TextUtils.isEmpty(typeholder)&&TextUtils.isEmpty(floorholder))&&imageUridp!=null){

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

                        member.setUid(id1);
                        member.setDate(savedate);
                        member.setTime(savetime);
                        member.setUrl(downloadUri.toString());
                        member.setName(nameholder);
                        member.setType(typeholder);
                        member.setFloor(floorholder);

                        databaseReference.child(id1).setValue(member);

                        Toast.makeText(AddRoomActivity.this, "Room Created", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                        pb.setVisibility(View.GONE);
                }else{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            pb.setVisibility(View.GONE);
            Toast.makeText(this, "Please fill up all the mandatory fields", Toast.LENGTH_SHORT).show();
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
                imageUridp = data.getData();
                Picasso.get().load(imageUridp).into(bg);
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
}