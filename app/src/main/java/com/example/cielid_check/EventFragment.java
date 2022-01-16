package com.example.cielid_check;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventFragment extends Fragment {


    ImageButton add;
    RecyclerView recyclerView;


    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference;

    LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_fragment, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        add = getActivity().findViewById(R.id.btn_add_ef);
        recyclerView = getActivity().findViewById(R.id.rv_ef);

        reference = database.getReference("All Events");

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        add.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),AddEventActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<EventMember> options =
                new FirebaseRecyclerOptions.Builder<EventMember>()
                        .setQuery(reference,EventMember.class)
                        .build();

        FirebaseRecyclerAdapter<EventMember,EventHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<EventMember, EventHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull EventHolder holder, int position, @NonNull EventMember model) {
                        holder.FragSetEvent(getActivity(),model.getUrl(),model.getDate(),model.getDesc(),model.getTitle(),model.getPostkey());

                        String urlholder = getItem(position).getUrl();

                        holder.fiv.setOnClickListener(view -> {
                            Intent intent = new Intent(getActivity(),ShowImageActivity.class);
                            intent.putExtra("url",urlholder);
                            startActivity(intent);
                        });
                    }

                    @NonNull
                    @Override
                    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.event_item,parent,false);

                        return new EventHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }
}
