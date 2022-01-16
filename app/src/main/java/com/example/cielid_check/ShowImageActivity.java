package com.example.cielid_check;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ShowImageActivity extends AppCompatActivity {

    ImageView iv;
    TextView back;
    String urlbundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);


        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            urlbundle = extras.getString("url");
        }
        else Toast.makeText(this, "No Image", Toast.LENGTH_SHORT).show();

        iv = findViewById(R.id.iv_si);
        back = findViewById(R.id.tv_back_si);


        back.setOnClickListener(view -> onBackPressed());

        Picasso.get().load(urlbundle).into(iv);
    }
}