package com.example.cielid_check;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ShowEventItem extends AppCompatActivity {

    TextView back,lbl,title,desc;
    ImageView iv;

    String titlebundle,ivbundle,descbundle,datebundle,urlbundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event_item);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            titlebundle = extras.getString("title");
            urlbundle = extras.getString("url");
            datebundle = extras.getString("date");
            descbundle = extras.getString("desc");
        }
        else Toast.makeText(this, "No Image", Toast.LENGTH_SHORT).show();


        back = findViewById(R.id.tv_back_se);
        lbl = findViewById(R.id.lbl_se);
        title = findViewById(R.id.tv_title_se);
        iv = findViewById(R.id.iv_se);
        desc = findViewById(R.id.tv_desc_se);

        back.setOnClickListener(view -> onBackPressed());

    }

    @Override
    protected void onStart() {
        super.onStart();

        lbl.setText("This event will happen on "+datebundle);
        title.setText("This event will happen on "+titlebundle);
        desc.setText("This event will happen on "+descbundle);
        Picasso.get().load(urlbundle).into(iv);

    }
}