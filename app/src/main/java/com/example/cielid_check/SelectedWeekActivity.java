package com.example.cielid_check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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

public class SelectedWeekActivity extends AppCompatActivity {

    String weekBundle,namebundle;
    TextView title,back;
    ImageButton add;
    RecyclerView recyclerView;
    LottieAnimationView lot;
    TextView nosched;
    CardView cv;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference,referenceSched,userReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    LinearLayoutManager linearLayoutManager;
    long count;

    String temptime = "03:30";

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

        referenceSched = database.getReference(namebundle).child(weekBundle);
        userReference = database.getReference("All users").child(currentuid);

        title = findViewById(R.id.tv_title_sw);
        back = findViewById(R.id.tv_back_sw);
        add = findViewById(R.id.btn_add_ef);
        recyclerView = findViewById(R.id.rv_sw);
        lot = findViewById(R.id.loginlot);
        cv = findViewById(R.id.cv_la);
        nosched = findViewById(R.id.tv_nosched_asw);

        linearLayoutManager = new LinearLayoutManager(SelectedWeekActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);



        back.setOnClickListener(v -> {
            Intent intent = new Intent(SelectedWeekActivity.this,RoomWeekActivity.class);
            intent.putExtra("name",namebundle);
            startActivity(intent);
        });

        add.setOnClickListener(v -> {
            Intent intent = new Intent(SelectedWeekActivity.this,AddScheduleActivity.class);
            intent.putExtra("name",namebundle);
            intent.putExtra("w",weekBundle);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelectedWeekActivity.this,RoomWeekActivity.class);
        intent.putExtra("name",namebundle);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();



        title.setText("Schedule For "+weekBundle);
        nosched.setText("No Schedule for " + weekBundle+".....");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String statusholder = snapshot.child("status").getValue(String.class);

                if(statusholder.equals("admin")||statusholder.equals("teacher")){
                    cv.setVisibility(View.VISIBLE);
                }

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
                    recyclerView.setVisibility(View.VISIBLE);

                    FirebaseRecyclerOptions<ScheduleMember> options =
                            new FirebaseRecyclerOptions.Builder<ScheduleMember>()
                                    .setQuery(referenceSched,ScheduleMember.class)
                                    .build();

                    FirebaseRecyclerAdapter<ScheduleMember,ScheduleHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<ScheduleMember, ScheduleHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull ScheduleHolder holder, int position, @NonNull ScheduleMember model) {

                                    holder.SetSched(getApplication(),model.getRoomname(),model.getTeacher(),model.getPurpose(),model.getStartTime(),
                                            model.getEndTime(),model.getWeek(),model.getStartTime2(),model.getEndTime2());

                                    String idname = getItem(position).getTeacher();
                                    String purposeholder  = getItem(position).getPurpose();
                                    String sholder  = getItem(position).getStartTime();
                                    String eholder  = getItem(position).getEndTime();
                                    String stime = getItem(position).getStartTime2();
                                    String etime = getItem(position).getEndTime2();


                                    holder.info.setOnClickListener(view -> {
                                        showIncharge(idname, purposeholder, sholder, eholder);

                                        //Toast.makeText(SelectedWeekActivity.this,CheckCurrentTime(temptime,stime,etime)+"", Toast.LENGTH_SHORT).show();
                                    });

                                    holder.delete.setOnClickListener(view -> {
                                        logout(sholder,eholder);
                                    });




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

                    recyclerView.setAdapter(firebaseRecyclerAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void logout(String sholder,String eholder ) {
        LayoutInflater inflater = LayoutInflater.from(SelectedWeekActivity.this);
        View view = inflater.inflate(R.layout.logout_layout2,null);
        TextView lbl = view.findViewById(R.id.desc);
        TextView logout_tv = view.findViewById(R.id.logout_tv_ll);
        TextView cancel_tv = view.findViewById(R.id.cancel_tv_ll);

        AlertDialog alertDialog = new AlertDialog.Builder(SelectedWeekActivity.this)
                .setView(view)
                .create();
        alertDialog.show();

        lbl.setText("Are you sure want to delete?");

        logout_tv.setText("Delete");

        logout_tv.setOnClickListener(v -> {
            referenceSched.child(sholder + eholder).removeValue();
            alertDialog.dismiss();
        });
        cancel_tv.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void showIncharge(String uid_i,String pur_i,String s_i,String e_i) {

        LayoutInflater inflater = LayoutInflater.from(SelectedWeekActivity.this);
        View view = inflater.inflate(R.layout.info_sched_item,null);
        TextView name = view.findViewById(R.id.tv_name_isi);
        TextView purpose = view.findViewById(R.id.tv_purpose_isi);
        TextView time = view.findViewById(R.id.tv_time_isi);
        ImageView iv = view.findViewById(R.id.iv_isi);

        AlertDialog alertDialog = new AlertDialog.Builder(SelectedWeekActivity.this)
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

    public boolean CheckCurrentTime(String timeToCheck, String fromTimeString, String untilTime) {
        String[] checkCurrentTime = timeToCheck.split(":");
        String[] from = fromTimeString.split(":");
        String[] until = untilTime.split(":");

        int timeToCheckHour = 0;
        int timeToCheckMinute = 0;
        int fromHour = 0;
        int untilHour = 0;
        int fromMinute = 0;
        int untilMinute = 0;

        try {
            timeToCheckHour = Integer.parseInt(checkCurrentTime[0]);
            timeToCheckMinute = Integer.parseInt(checkCurrentTime[0]);
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

        //Toast.makeText(this, currentTime.get(Calendar.HOUR_OF_DAY) + ":" + currentTime.get(Calendar.MINUTE),Toast.LENGTH_SHORT).show();

        if(currentTime.after(fromTime) && currentTime.before(toTime)){
            return true;
            //Toast.makeText(this, fromHour + ":" + fromMinute + " - " + untilHour + ":" + untilMinute + "   true", Toast.LENGTH_SHORT).show();
        } else {
            return false;
            //Toast.makeText(this, fromHour + ":" + fromMinute + " - " + untilHour + ":" + untilMinute + "   false", Toast.LENGTH_SHORT).show();
        }
    }


}