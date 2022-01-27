package com.example.cielid_check;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity {

    TextView back;
    TextView roomName,name,submit,stime,etime;
    TextView sched,nosched,addt;
    ImageView iv;
    LottieAnimationView lot;
    RecyclerView rv;
    ConstraintLayout cv;

    RadioButton rb1,rb2;

    String namebundle,weekbundle;
    String starttime2,endtime2;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference,referenceSched,checkReference,samplereference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    FirebaseAuth mAuth;

    int hour,minute;

    ScheduleMember member;
    long count;
    Boolean issubmit = false;

    LinearLayoutManager linearLayoutManager;

    String idpick;

    Query query;

    int selechcheck;


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
        addt = findViewById(R.id.tv_add_t_as);
        cv = findViewById(R.id.cl2);

        linearLayoutManager = new LinearLayoutManager(AddScheduleActivity.this);
        rv.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        back.setOnClickListener(v -> onBackPressed());
        submit.setOnClickListener(v -> {
            submitData();
            //referenceSched.removeValue();
        });

        addt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTeacher();
            }
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

    private void showTeacher() {

        LinearLayoutManager linearLayoutManager;

        samplereference = database.getReference("All users");
        query = samplereference.orderByChild("status").equalTo("teacher");

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.show_teacher_layout,null);
        RecyclerView rv2 = view.findViewById(R.id.rv_stl);

        linearLayoutManager = new LinearLayoutManager(AddScheduleActivity.this);
        rv2.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();



        FirebaseRecyclerOptions<AlluserMember> options =
                new FirebaseRecyclerOptions.Builder<AlluserMember>()
                        .setQuery(query,AlluserMember.class)
                        .build();

        FirebaseRecyclerAdapter<AlluserMember,AllUserHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AlluserMember, AllUserHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AllUserHolder holder, int position, @NonNull AlluserMember model) {


                        holder.setUser(getApplication(),model.getUid(),model.getName(),model.getStatus(),model.getEmail(),model.getUrl());

//                        String idname = getItem(position).getTeacher();
                          String id = getItem(position).getUid();



                        holder.selectholder.setOnClickListener(view1 -> {
                            idpick = id;
                            reference = database.getReference("All users").child(idpick);
                            cv.setVisibility(View.VISIBLE);
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
                            alertDialog.dismiss();
                        });



                    }

                    @NonNull
                    @Override
                    public AllUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.show_teacher_item,parent,false);

                        return new AllUserHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        rv2.setAdapter(firebaseRecyclerAdapter);


    }

    private void submitData() {
        String temps = starttime2;
        String tempe = endtime2;

        String tempss = stime.getText().toString();
        String tempes = etime.getText().toString();

        if(tempss.equals("0")||tempes.equals("0")){
            Toast.makeText(AddScheduleActivity.this, "Please Select Time Schedule", Toast.LENGTH_SHORT).show();
        }else if(tempss.equals(tempes)) {
            Toast.makeText(AddScheduleActivity.this, "Same Starting time and End time is not available", Toast.LENGTH_SHORT).show();
        } else {

            checkReference = database.getReference(namebundle).child(weekbundle);

            checkReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Boolean samp=true;
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String stimeholder = userSnapshot.child("startTime2").getValue(String.class);
                            String etimeholder = userSnapshot.child("endTime2").getValue(String.class);
                            String idholder = userSnapshot.child("teacher").getValue(String.class);
                            String postkeyholder = userSnapshot.child("postkey").getValue(String.class);


                            if(samp == false){
                                if (CheckCurrentTime(temps, stimeholder, etimeholder)) {
                                    if(selechcheck !=1){
                                        Toast.makeText(AddScheduleActivity.this, "invalid schedule", Toast.LENGTH_SHORT).show();
                                    }
                                    samp = false;
                                } else {
                                    if (CheckCurrentTime(tempe, stimeholder, etimeholder)) {
                                        if(selechcheck !=1){
                                            Toast.makeText(AddScheduleActivity.this, "invalid schedule", Toast.LENGTH_SHORT).show();
                                        }
                                        samp = false;
                                    } else {
                                        if (CheckCurrentTime(stimeholder, temps, tempe)) {
                                            if(selechcheck !=1){
                                                Toast.makeText(AddScheduleActivity.this, "invalid schedule", Toast.LENGTH_SHORT).show();
                                            }
                                            samp = false;
                                        } else {
                                            if (CheckCurrentTime(etimeholder, temps, tempe)) {
                                                if(selechcheck !=1){
                                                    Toast.makeText(AddScheduleActivity.this, "invalid schedule", Toast.LENGTH_SHORT).show();
                                                }
                                                samp = false;
                                            } else {

                                            }
                                        }
                                    }

                                }
                            }


                        }
                        if(samp){
                            finalsubmitData();
                        }


                    } else {
                        finalsubmitData();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });

        }


    }

    private void submitcheck() {
        issubmit = true;

    }

    private void finalsubmitData() {

        String temps = stime.getText().toString();
        String tempe = etime.getText().toString();

        if(temps.equals("0")||tempe.equals("0")){
            Toast.makeText(AddScheduleActivity.this, "Please Select Time Schedule", Toast.LENGTH_SHORT).show();
        }else if(temps.equals(tempe)){
            Toast.makeText(AddScheduleActivity.this, "Same Starting time and End time is not available", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(idpick)){
            Toast.makeText(AddScheduleActivity.this, "Please selece subject teacher", Toast.LENGTH_SHORT).show();
        }
        else{
            if(rb1.isChecked()||rb2.isChecked()){
                String radio_ans="";
                if(rb1.isChecked()) radio_ans = "Class";
                if(rb2.isChecked()) radio_ans = "Event";

                String id = referenceSched.push().getKey();

                member.setRoomname(namebundle);
                member.setTeacher(idpick);
                member.setPurpose(radio_ans);
                member.setStartTime(temps);
                member.setEndTime(tempe);
                member.setWeek(weekbundle);
                member.setPostkey(temps+tempe);
                member.setStartTime2(starttime2);
                member.setEndTime2(endtime2);

                referenceSched.child(temps+tempe).setValue(member);

                Toast.makeText(AddScheduleActivity.this, "Schedule Created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddScheduleActivity.this,SelectedWeekActivity.class);
                intent.putExtra("name",namebundle);
                intent.putExtra("w",weekbundle);
                startActivity(intent);

            }else Toast.makeText(AddScheduleActivity.this, "Please Select purpose of your schedule", Toast.LENGTH_SHORT).show();

        }
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


    @Override
    protected void onStart() {
        super.onStart();


        roomName.setText(namebundle);
        sched.setText("Schedule for "+weekbundle);
        nosched.setText("No Schedule for "+weekbundle+"!");


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