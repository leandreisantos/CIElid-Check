package com.example.cielid_check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RoomWeekActivity extends AppCompatActivity {

    TextView monday,tuesday,wednesday,thursday,friday,saturday,sunday,currenttv;
    String namebundle;
    TextView back;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference;

    String dayOfTheWeek;
    Boolean checkcurrent = false;

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
        back = findViewById(R.id.tv_back_arw);
        currenttv = findViewById(R.id.tv_curren_arw);

        back.setOnClickListener(v -> {
            Intent intent = new Intent(RoomWeekActivity.this,MainActivity.class);
            startActivity(intent);
        });




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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RoomWeekActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
        String sample = sd.format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);


        reference = database.getReference(namebundle).child(dayOfTheWeek);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    String stime = userSnapshot.child("startTime2").getValue(String.class);
                    String etime = userSnapshot.child("endTime2").getValue(String.class);
                    String id = userSnapshot.child("postkey").getValue(String.class);
                    String idteacher = userSnapshot.child("teacher").getValue(String.class);
                    String purpose = userSnapshot.child("purpose").getValue(String.class);
                    String times = userSnapshot.child("startTime").getValue(String.class);
                    String ends = userSnapshot.child("endTime").getValue(String.class);

                    if(CheckCurrentTime(sample,stime,etime)){

                        showIncharge(idteacher,purpose,times,ends);


                    }

                    currenttv.setOnClickListener((View view) -> {
                        if(checkcurrent) showIncharge(idteacher, purpose, times, ends);
                        else Toast.makeText(RoomWeekActivity.this, "No Current Event/Class happening", Toast.LENGTH_SHORT).show();
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    private void showIncharge(String uid_i,String pur_i,String s_i,String e_i) {

        checkcurrent = true;

        LayoutInflater inflater = LayoutInflater.from(RoomWeekActivity.this);
        View view = inflater.inflate(R.layout.show_incharge_item,null);
        TextView rooName = view.findViewById(R.id.tv_room_sii);
        TextView week = view.findViewById(R.id.tv_week_sii);
        TextView name = view.findViewById(R.id.tv_name_sii);
        TextView purpose = view.findViewById(R.id.tv_purpose_sii);
        TextView time = view.findViewById(R.id.tv_time_sii);
        ImageView iv = view.findViewById(R.id.iv_sii);

        AlertDialog alertDialog = new AlertDialog.Builder(RoomWeekActivity.this)
                .setView(view)
                .create();
        alertDialog.show();

        reference = database.getReference("All users").child(uid_i);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url = snapshot.child("url").getValue(String.class);
                String nameholder = snapshot.child("name").getValue(String.class);

                Picasso.get().load(url).into(iv);
                name.setText(nameholder);
                time.setText(s_i + " - "+e_i);
                purpose.setText(pur_i);
                rooName.setText(namebundle);
                week.setText(dayOfTheWeek);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public boolean CheckCurrentTime(String timeToCheck, String fromTimeString, String untilTime) {
        String[] checkCurrentTime = timeToCheck.split(":");
        String[] from = fromTimeString.split(":");
        String[] until = untilTime.split(":");
        //String[] checkcurrenttime2 = timetocheck2.split(":");


        int timeToCheckHour = 0;
        int timeToCheckMinute = 0;
        int timeToCheckHour2 = 0;
        int timeToCheckMinute2 = 0;
        int fromHour = 0;
        int untilHour = 0;
        int fromMinute = 0;
        int untilMinute = 0;

        try {
            timeToCheckHour = Integer.parseInt(checkCurrentTime[0]);
            timeToCheckMinute = Integer.parseInt(checkCurrentTime[0]);
            //timeToCheckHour2 = Integer.parseInt(checkcurrenttime2[0]);
            //timeToCheckMinute2 = Integer.parseInt(checkcurrenttime2[0]);
            fromHour = Integer.parseInt(from[0]);
            fromMinute = Integer.parseInt(from[1]);
            untilHour = Integer.parseInt(until[0]);
            untilMinute = Integer.parseInt(until[1]);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        Calendar fromTime;
        fromTime = Calendar.getInstance();
        fromTime.set(Calendar.HOUR_OF_DAY, fromHour);
        fromTime.set(Calendar.MINUTE, fromMinute);

        Calendar toTime;
        toTime = Calendar.getInstance();
        toTime.set(Calendar.HOUR_OF_DAY, untilHour);
        toTime.set(Calendar.MINUTE, untilMinute);

        // get time to check
        Calendar currentTime;
        currentTime = Calendar.getInstance();
        currentTime.set(Calendar.HOUR_OF_DAY, timeToCheckHour);
        currentTime.set(Calendar.MINUTE, timeToCheckMinute);

        Calendar currenttime2;
        currenttime2 = Calendar.getInstance();
        currenttime2.set(Calendar.HOUR_OF_DAY, timeToCheckHour2);
        currenttime2.set(Calendar.MINUTE, timeToCheckMinute2);

        //Toast.makeText(this, currentTime.get(Calendar.HOUR_OF_DAY) + ":" + currentTime.get(Calendar.MINUTE),Toast.LENGTH_SHORT).show();

        if((currentTime.after(fromTime) && currentTime.before(toTime))||currentTime.equals(toTime)||currentTime.equals(fromTime)){
            return true;
            //Toast.makeText(this, fromHour + ":" + fromMinute + " - " + untilHour + ":" + untilMinute + "   true", Toast.LENGTH_SHORT).show();
        } else {
            return false;
            //Toast.makeText(this, fromHour + ":" + fromMinute + " - " + untilHour + ":" + untilMinute + "   false", Toast.LENGTH_SHORT).show();
        }


    }
}