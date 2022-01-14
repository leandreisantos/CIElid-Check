package com.example.cielid_check;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class SelectedWeekActivity extends AppCompatActivity {

    String weekBundle,namebundle;
    TextView title,back;
    ImageButton add;
    RecyclerView recyclerView;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference,referenceSched;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    LinearLayoutManager linearLayoutManager;

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

        title = findViewById(R.id.tv_title_sw);
        back = findViewById(R.id.tv_back_sw);
        add = findViewById(R.id.btn_add_ef);
        recyclerView = findViewById(R.id.rv_sw);

        linearLayoutManager = new LinearLayoutManager(SelectedWeekActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);



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

        FirebaseRecyclerOptions<ScheduleMember> options =
                new FirebaseRecyclerOptions.Builder<ScheduleMember>()
                        .setQuery(referenceSched,ScheduleMember.class)
                        .build();

        FirebaseRecyclerAdapter<ScheduleMember,ScheduleHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ScheduleMember, ScheduleHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ScheduleHolder holder, int position, @NonNull ScheduleMember model) {

                        holder.SetSched(getApplication(),model.getRoomname(),model.getTeacher(),model.getPurpose(),model.getStartTime(),model.getEndTime(),model.getWeek());

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

        recyclerView.setAdapter(firebaseRecyclerAdapter);

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
}