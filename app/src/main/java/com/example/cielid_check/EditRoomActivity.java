package com.example.cielid_check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditRoomActivity extends AppCompatActivity {

    EditText name,type,floor;
    TextView title,back;
    ImageView iv;
    ImageButton save;
    ProgressBar pb;

    String namebundle,postkeybundle;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            postkeybundle = extras.getString("post");
            namebundle = extras.getString("name");
        }
        else Toast.makeText(this, "No Week Selected", Toast.LENGTH_SHORT).show();

        reference = database.getReference("All Room").child(postkeybundle);

        name = findViewById(R.id.et_name_er);
        type = findViewById(R.id.et_type_er);
        floor = findViewById(R.id.et_floor_er);
        title = findViewById(R.id.tv_title_er);
        iv = findViewById(R.id.iv_er);
        save = findViewById(R.id.buttonSignIn);
        pb = findViewById(R.id.pv_login);
        back = findViewById(R.id.tv_back_er);


        back.setOnClickListener(view -> onBackPressed());


        save.setOnClickListener(view -> submit());


    }

    private void submit() {

        pb.setVisibility(View.VISIBLE);

        String typeholder = type.getText().toString();
        String floorholder = floor.getText().toString();

        if(TextUtils.isEmpty(typeholder) || TextUtils.isEmpty(floorholder)){
            Toast.makeText(EditRoomActivity.this, "Empty data invalid please fill up all requirements", Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.GONE);
        }else{
            reference.child("type").setValue(typeholder);
            reference.child("floor").setValue(floorholder);
            Toast.makeText(EditRoomActivity.this, "Successfully change data", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        title.setText("Edit Room "+namebundle);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameholder = snapshot.child("name").getValue(String.class);
                String typeholder = snapshot.child("type").getValue(String.class);
                String floorholder = snapshot.child("floor").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);

                name.setText(nameholder);
                type.setText(typeholder);
                floor.setText(floorholder);
                Picasso.get().load(url).into(iv);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}