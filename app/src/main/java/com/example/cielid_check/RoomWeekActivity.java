package com.example.cielid_check;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RoomWeekActivity extends AppCompatActivity {

    TextView monday,tuesday,wednesday,thursday,friday,saturday,sunday;
    String namebundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_week);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            namebundle = extras.getString("name");
        }
        else Toast.makeText(this, "No Week Selected", Toast.LENGTH_SHORT).show();


        monday = findViewById(R.id.tv_monday_arw);
        tuesday = findViewById(R.id.tv_tues_arw);
        wednesday = findViewById(R.id.tv_wed_arw);
        thursday = findViewById(R.id.tv_thurs_arw);
        friday = findViewById(R.id.tv_fri_arw);
        sunday = findViewById(R.id.tv_sun_arw);
        saturday = findViewById(R.id.tv_sat_arw);


        monday.setOnClickListener(v -> {
            Intent intent = new Intent(RoomWeekActivity.this,SelectedWeekActivity.class);
            intent.putExtra("w","Monday");
            intent.putExtra("name",namebundle);
            startActivity(intent);
        });
        tuesday.setOnClickListener(v -> {
            Intent intent = new Intent(RoomWeekActivity.this,SelectedWeekActivity.class);
            intent.putExtra("w","Tuesday");
            intent.putExtra("name",namebundle);
            startActivity(intent);
        });
        wednesday.setOnClickListener(v -> {
            Intent intent = new Intent(RoomWeekActivity.this,SelectedWeekActivity.class);
            intent.putExtra("w","Wednesday");
            intent.putExtra("name",namebundle);
            startActivity(intent);
        });
        thursday.setOnClickListener(v -> {
            Intent intent = new Intent(RoomWeekActivity.this,SelectedWeekActivity.class);
            intent.putExtra("w","Thursday");
            intent.putExtra("name",namebundle);
            startActivity(intent);
        });
        friday.setOnClickListener(v -> {
            Intent intent = new Intent(RoomWeekActivity.this,SelectedWeekActivity.class);
            intent.putExtra("w","Friday");
            intent.putExtra("name",namebundle);
            startActivity(intent);
        });
        saturday.setOnClickListener(v -> {
            Intent intent = new Intent(RoomWeekActivity.this,SelectedWeekActivity.class);
            intent.putExtra("w","Saturday");
            intent.putExtra("name",namebundle);
            startActivity(intent);
        });
        sunday.setOnClickListener(v -> {
            Intent intent = new Intent(RoomWeekActivity.this,SelectedWeekActivity.class);
            intent.putExtra("w","Sunday");
            intent.putExtra("name",namebundle);
            startActivity(intent);
        });

    }
}