package com.example.cielid_check;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarFragment extends Fragment {

    ImageButton addEvent;
    MaterialDatePicker materialDatePicker;
    CalendarView calendar;
    TextView add_event;
    RecyclerView recyclerView;

    Date c = Calendar.getInstance().getTime();
    String year,month,day,yearSelected,monthSelected,daySelected;
    Boolean checkPickDate = false;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference,reference2,userReference;

    LinearLayoutManager linearLayoutManager;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userReference = database.getReference("All users").child(currentuid);

        addEvent = getActivity().findViewById(R.id.btn_add_ef);
        calendar = getActivity().findViewById(R.id.calendarview_cf);
        calendar = getActivity().findViewById(R.id.calendarview_cf);
        add_event = getActivity().findViewById(R.id.tv_add_cf);
        recyclerView = getActivity().findViewById(R.id.rv_cf);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);






        add_event.setOnClickListener(view -> {
            if(checkPickDate){
                Intent intent = new Intent(getActivity(),AddEventActivity.class);
                intent.putExtra("year",yearSelected);
                intent.putExtra("month",monthSelected);
                intent.putExtra("day",daySelected);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getActivity(),AddEventActivity.class);
                intent.putExtra("year",year);
                intent.putExtra("month",month);
                intent.putExtra("day",day);
                startActivity(intent);
            }
        });



        calendar.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            yearSelected = Integer.toString(i);
            monthSelected = Integer.toString(i1+1);
            daySelected = Integer.toString(i2);

            add_event.setText("Add Event for "+yearSelected+"/"+monthSelected+"/"+daySelected);
            checkPickDate = true;

            reference = database.getReference("All Event").child(yearSelected).child(monthSelected).child(daySelected);
            reference2=database.getReference("All Events");


            FirebaseRecyclerOptions<EventMember> options =
                    new FirebaseRecyclerOptions.Builder<EventMember>()
                            .setQuery(reference,EventMember.class)
                            .build();

            FirebaseRecyclerAdapter<EventMember,EventHolder> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<EventMember, EventHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull EventHolder holder, int position, @NonNull EventMember model) {
                            holder.SetEvent(getActivity(),model.getUrl(),model.getDate(),model.getDesc(),model.getTitle(),model.getPostkey());

                            String postkeyholder = getItem(position).getPostkey();
                            String urlholder = getItem(position).getUrl();
                            String dateholder = getItem(position).getDate();
                            String descholder = getItem(position).getDesc();
                            String titleholder = getItem(position).getTitle();

                            holder.delete.setOnClickListener(view -> {
                                reference.child(postkeyholder).removeValue();
                                reference2.child(postkeyholder).removeValue();
                            });

                            holder.cl.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(),ShowEventItem.class);
                                intent.putExtra("url",urlholder);
                                intent.putExtra("date",dateholder);
                                intent.putExtra("desc",descholder);
                                intent.putExtra("title",titleholder);
                                startActivity(intent);
                            });
                        }

                        @NonNull
                        @Override
                        public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.event_item_cf,parent,false);

                            return new EventHolder(view);
                        }
                    };

            firebaseRecyclerAdapter.startListening();

            recyclerView.setAdapter(firebaseRecyclerAdapter);


        });



        addEvent.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),AddEventActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String statusholder = snapshot.child("status").getValue(String.class);

                if(statusholder.equals("admin")){
                    add_event.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
        year = simpleDateFormatYear.format(c);

        SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM");
        month = simpleDateFormatMonth.format(c);

        SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("dd");
        day = simpleDateFormatDay.format(c);

        String temp = String.valueOf(month.charAt(0));
        String temp2 = String.valueOf(day.charAt(0));
        if(temp.equals("0")){
            month = String.valueOf(month.charAt(1));
        }
        if(temp2.equals("0")){
            day = String.valueOf(day.charAt(1));
        }

        add_event.setText("Add Event for "+year+"/"+month+"/"+day);

        reference = database.getReference("All Event").child(year).child(month).child(day);


        FirebaseRecyclerOptions<EventMember> options =
                new FirebaseRecyclerOptions.Builder<EventMember>()
                        .setQuery(reference,EventMember.class)
                        .build();

        FirebaseRecyclerAdapter<EventMember,EventHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<EventMember, EventHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EventHolder holder, int position, @NonNull EventMember model) {
                        holder.SetEvent(getActivity(),model.getUrl(),model.getDate(),model.getDesc(),model.getTitle(),model.getPostkey());

                        String postkeyholder = getItem(position).getPostkey();
                        String urlholder = getItem(position).getUrl();
                        String dateholder = getItem(position).getDate();
                        String descholder = getItem(position).getDesc();
                        String titleholder = getItem(position).getTitle();

                        holder.delete.setOnClickListener(view -> {
                            reference.child(postkeyholder).removeValue();
                            reference2.child(postkeyholder).removeValue();
                        });

                        holder.cl.setOnClickListener(view -> {
                            Intent intent = new Intent(getActivity(),ShowEventItem.class);
                            intent.putExtra("url",urlholder);
                            intent.putExtra("date",dateholder);
                            intent.putExtra("desc",descholder);
                            intent.putExtra("title",titleholder);
                            startActivity(intent);
                        });
                    }

                    @NonNull
                    @Override
                    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.event_item_cf,parent,false);

                        return new EventHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);




    }
}
