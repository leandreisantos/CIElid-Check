package com.example.cielid_check;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddEventActivity extends AppCompatActivity {

    ImageButton post;
    TextView back,upload;
    ImageView iv;

    EditText title,desc;
    CardView cv;


    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    DatabaseReference db1,db3,db2;

    private static final int PICK_FILE = 1;
    private Uri selectedUri;
    UploadTask uploadTask;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        back = findViewById(R.id.tv_back_cp);

        back.setOnClickListener(v -> onBackPressed());
    }
}