package com.example.cielid_check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    LottieAnimationView lot;
    RecyclerView rv;

    RadioButton rb1,rb2;

    String namebundle,weekbundle;
    String starttime2,endtime2;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference,referenceSched,checkReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    FirebaseAuth mAuth;

    int hour,minute;

    ScheduleMember member;
    long count;

    LinearLayoutManager linearLayoutManager;


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
        lot = findViewById(R.id.loginlot);
        rv = findViewById(R.id.rv_sched_as);

        linearLayoutManager = new LinearLayoutManager(AddScheduleActivity.this);
        rv.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


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
                         starttime2 = DateFormat.format("HH:mm",calendar).toString();
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
                        endtime2 = DateFormat.format("HH:mm",calendar).toString();

                    },12,0,false
            );

            timePickerDialog.updateTime(hour,minute);
            timePickerDialog.show();
        });
    }

    private void submitData() {
        String temps = stime.getText().toString();
        String tempe = etime.getText().toString();


        checkReference = database.getReference(namebundle).child(weekbundle);

        checkReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    String stime = userSnapshot.child("startTime2").getValue(String.class);
                    String etime = userSnapshot.child("endTime2").getValue(String.class);

                    if(CheckCurrentTime(temps,stime,etime,tempe)){
                        Toast.makeText(AddScheduleActivity.this, "Bawal", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AddScheduleActivity.this, "pwde", Toast.LENGTH_SHORT).show();
                        if(temps.equals("0")||tempe.equals("0")){
                            Toast.makeText(AddScheduleActivity.this, "Please Select Time Schedule", Toast.LENGTH_SHORT).show();
                        }else if(temps.equals(tempe)){
                            Toast.makeText(AddScheduleActivity.this, "Same Starting time and End time is not available", Toast.LENGTH_SHORT).show();
                        }else{
                            if(rb1.isChecked()||rb2.isChecked()){
                                String radio_ans="";
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
                                member.setStartTime2(starttime2);
                                member.setEndTime2(endtime2);

                                referenceSched.child(id).setValue(member);

                                Toast.makeText(AddScheduleActivity.this, "Schedule Created", Toast.LENGTH_SHORT).show();
                                onBackPressed();

                            }else Toast.makeText(AddScheduleActivity.this, "Please Select purpose of your schedule", Toast.LENGTH_SHORT).show();

                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


    }

    public boolean CheckCurrentTime(String timeToCheck, String fromTimeString, String untilTime,String timetocheck2) {
        String[] checkCurrentTime = timeToCheck.split(":");
        String[] from = fromTimeString.split(":");
        String[] until = untilTime.split(":");
        String[] checkcurrenttime2 = timetocheck2.split(":");


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
            timeToCheckHour2 = Integer.parseInt(checkcurrenttime2[0]);
            timeToCheckMinute2 = Integer.parseInt(checkcurrenttime2[0]);
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

        if(((currentTime.after(fromTime) && currentTime.before(toTime)) || (currenttime2.after(fromTime) && currenttime2.before(toTime)))
                || (currentTime.before(fromTime) && currenttime2.after(toTime))){
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
            return false;
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

        referenceSched.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = snapshot.getChildrenCount();
                if(count>=1){
                    nosched.setVisibility(View.GONE);
                    lot.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);

                    FirebaseRecyclerOptions<ScheduleMember> options =
                            new FirebaseRecyclerOptions.Builder<ScheduleMember>()
                                    .setQuery(referenceSched,ScheduleMember.class)
                                    .build();

                    FirebaseRecyclerAdapter<ScheduleMember,ScheduleHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<ScheduleMember, ScheduleHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull ScheduleHolder holder, int position, @NonNull ScheduleMember model) {

                                    holder.SetSched(getApplication(),model.getRoomname(),model.getTeacher(),model.getPurpose(),model.getStartTime(),model.getEndTime(),
                                            model.getWeek(),model.getStartTime2(), model.endTime2);

                                    String idname = getItem(position).getTeacher();
                                    String purposeholder  = getItem(position).getPurpose();
                                    String sholder  = getItem(position).getStartTime();
                                    String eholder  = getItem(position).getEndTime();


                                    holder.info.setOnClickListener(view -> showIncharge(idname,purposeholder,sholder,eholder));

                                }

                                @NonNull
                                @Override
                                public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.schedule_item,parent,false);

                                    return new ScheduleHolder(view);
                                }
                            };

                    firebaseRecyclerAdapter.startListening();

                    rv.setAdapter(firebaseRecyclerAdapter);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void showIncharge(String uid_i,String pur_i,String s_i,String e_i) {

        LayoutInflater inflater = LayoutInflater.from(AddScheduleActivity.this);
        View view = inflater.inflate(R.layout.info_sched_item,null);
        TextView name = view.findViewById(R.id.tv_name_isi);
        TextView purpose = view.findViewById(R.id.tv_purpose_isi);
        TextView time = view.findViewById(R.id.tv_time_isi);
        ImageView iv = view.findViewById(R.id.iv_isi);

        AlertDialog alertDialog = new AlertDialog.Builder(AddScheduleActivity.this)
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}