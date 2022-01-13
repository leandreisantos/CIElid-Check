package com.example.cielid_check;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new EventFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNav = item -> {
        Fragment selected = null;
        switch(item.getItemId()){
            case R.id.event_bottom:
                selected = new EventFragment();
                break;
            case R.id.room_bottom:
                selected = new RoomFragment();
                break;
            case R.id.calendar_bottom:
                selected = new CalendarFragment();
                break;
            case R.id.profile_bottom:
                selected = new ProfileFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selected).commit();
        return true;
    };
}