package com.example.cielid_check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity {

    TextView back;
    TextView roomName,name,submit,stime,etime;
    TextView sched,nosched;
    ImageView iv;

    RadioButton rb1,rb2;

    String namebundle,weekbundle;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference,referenceSched;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    FirebaseAuth mAuth;

    int hour,minute;

    ScheduleMember member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        mAuth = FirebaseAuth.getInstance();
        member = new ScheduleMember();

        reference = database.getReference("All users").child(currentuid);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            namebundle = extras.getString("name");
            weekbundle = extras.getString("w");
        }

        else Toast.makeText(this, "No Week Selected", Toast.LENGTH_SHORT).show();

        referenceSched = database.getReference(namebundle).child(weekbundle);

        back = findViewById(R.id.tv_back_as);
        roomName = findViewById(R.id.tv_room_as);
        name = findViewById(R.id.tv_name_as);
        iv = findViewById(R.id.iv_as);
        rb1 = findViewById(R.id.rb_class_as);
        rb2 = findViewById(R.id.rb_event_as);
        submit = findViewById(R.id.tv_submit_as);
        sched = findViewById(R.id.tv_sched_as);
        nosched = findViewById(R.id.lbl_nosched);
        stime = findViewById(R.id.tv_st_as);
        etime = findViewById(R.id.tv_et_as);


        back.setOnClickListener(v -> onBackPressed());
        submit.setOnClickListener(v -> {
            submitData();
        });

        stime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AddScheduleActivity.this,R.style.TimePickerTheme,
                    (TimePicker view1, int hourOfDay, int minute) -> {

                        hour = hourOfDay;
                        minute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,hour,minute);

                         stime.setText(DateFormat.format("hh:mm aa",calendar));
//                        stime.setText(Integer.toString(hourOfDay)+Integer.toString(minute));

                    },12,0,false
            );

            timePickerDialog.updateTime(hour,minute);
            timePickerDialog.show();
        });
        etime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AddScheduleActivity.this,R.style.TimePickerTheme,
                    (TimePicker view1, int hourOfDay, int minute) -> {

                        hour = hourOfDay;
                        minute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,hour,minute);

                        etime.setText(DateFormat.format("hh:mm aa",calendar));

                    },12,0,false
            );

            timePickerDialog.updateTime(hour,minute);
            timePickerDialog.show();
        });
    }

    private void submitData() {
        String temps = stime.getText().toString();
        String tempe = etime.getText().toString();
        String radio_ans="";
        
        if(temps.equals("0")||tempe.equals("0")){
            Toast.makeText(AddScheduleActivity.this, "Please Select Time Schedule", Toast.LENGTH_SHORT).show();
        }else if(temps.equals(tempe)){
            Toast.makeText(AddScheduleActivity.this, "Same Starting time and End time is not available", Toast.LENGTH_SHORT).show();
        }else{
            if(rb1.isChecked()||rb2.isChecked()){
                if(rb1.isChecked()) radio_ans = "Class";
                if(rb2.isChecked()) radio_ans = "Event";

                String id = referenceSched.push().getKey();

                member.setRoomname(namebundle);
                member.setTeacher(currentuid);
                member.setPurpose(radio_ans);
                member.setStartTime(temps);
                member.setEndTime(tempe);
                member.setWeek(weekbundle);
                member.setPostkey(id);

                referenceSched.child(id).setValue(member);

                Toast.makeText(AddScheduleActivity.this, "Schedule Created", Toast.LENGTH_SHORT).show();
                onBackPressed();

            }else Toast.makeText(AddScheduleActivity.this, "Please Select purpose of your schedule", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        roomName.setText(namebundle);
        sched.setText("Schedule for "+weekbundle);
        nosched.setText("No Schedule for "+weekbundle+"!");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameholder = snapshot.child("name").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);

                name.setText(nameholder);
                Picasso.get().load(url).into(iv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}