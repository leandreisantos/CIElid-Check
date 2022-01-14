package com.example.cielid_check;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedWeekActivity extends AppCompatActivity {

    String weekBundle,namebundle;
    TextView title,back;
    ImageButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_week);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            weekBundle = extras.getString("w");
            namebundle = extras.getString("name");
        }
        else Toast.makeText(this, "No Week Selected", Toast.LENGTH_SHORT).show();

        title = findViewById(R.id.tv_title_sw);
        back = findViewById(R.id.tv_back_sw);
        add = findViewById(R.id.btn_add_ef);

        back.setOnClickListener(v -> onBackPressed());

        add.setOnClickListener(v -> {
            Intent intent = new Intent(SelectedWeekActivity.this,AddScheduleActivity.class);
            intent.putExtra("name",namebundle);
            intent.putExtra("w",weekBundle);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        title.setText("Schedule For "+weekBundle);

    }
}