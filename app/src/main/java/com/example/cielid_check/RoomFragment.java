package com.example.cielid_check;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RoomFragment extends Fragment {

    ImageButton add;
    RecyclerView rv;
    CardView cv;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference databaseReference,userReference,referencesched;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.room_fragment, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        databaseReference = database.getReference("All Room");
        userReference = database.getReference("All users").child(currentuid);

        add = getActivity().findViewById(R.id.btn_add_rf);
        cv = getActivity().findViewById(R.id.cv_rf);
        rv = getActivity().findViewById(R.id.rv_dash);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        add.setOnClickListener(v -> startActivity(new Intent(getActivity(),AddRoomActivity.class)));
    }

    @Override
    public void onStart() {
        super.onStart();

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String statusholder = snapshot.child("status").getValue(String.class);

                if(statusholder.equals("admin")){
                    cv.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        FirebaseRecyclerOptions<AllRoomMember> options2 =
                new FirebaseRecyclerOptions.Builder<AllRoomMember>()
                        .setQuery(databaseReference,AllRoomMember.class)
                        .build();

        FirebaseRecyclerAdapter<AllRoomMember,RoomHolder> firebaseRecyclerAdapter2 =
                new FirebaseRecyclerAdapter<AllRoomMember, RoomHolder>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull RoomHolder holder, int position, @NonNull AllRoomMember model) {

                        holder.SetRoom(getActivity(),model.getUrl(),model.getUid(),model.getName(),model.getType(),model.getFloor(),model.getDate(),
                                model.getTime());

                        String name = getItem(position).getName();
                        String postkey = getItem(position).getUid();

                        holder.cv.setOnClickListener(v -> {
                            Intent intent = new Intent(getActivity(),RoomWeekActivity.class);
                            intent.putExtra("name",name);
                            startActivity(intent);
                        });
                        holder.more.setOnClickListener(view -> showMore(name,postkey));

                    }

                    @NonNull
                    @Override
                    public RoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.room_item,parent,false);

                        return new RoomHolder(view);
                    }
                };


        firebaseRecyclerAdapter2.startListening();
//
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(firebaseRecyclerAdapter2);

    }

    private void showMore(String nameholder,String postkeyholder) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.more_item,null);
        TextView edit = view.findViewById(R.id.tv_edit_mi);
        TextView delete = view.findViewById(R.id.tv_delete_ei);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        alertDialog.show();

        edit.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(),EditRoomActivity.class);
            intent.putExtra("post",postkeyholder);
            intent.putExtra("name",nameholder);
            startActivity(intent);
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(nameholder,postkeyholder);
            }
        });

    }

    public void logout(String sholder,String eholder ) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.logout_layout2,null);
        TextView lbl = view.findViewById(R.id.desc);
        TextView logout_tv = view.findViewById(R.id.logout_tv_ll);
        TextView cancel_tv = view.findViewById(R.id.cancel_tv_ll);

        referencesched=database.getReference(sholder);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        alertDialog.show();

        lbl.setText("Are you sure want to delete? All save schedule will remove");

        logout_tv.setText("Delete");

        logout_tv.setOnClickListener(v -> {
//            Toast.makeText(getActivity(), sholder, Toast.LENGTH_SHORT).show();
            referencesched=database.getReference(sholder);
            referencesched.child("Monday").removeValue();
            referencesched.child("Tuesday").removeValue();
            databaseReference.child(eholder).removeValue();
        });
        cancel_tv.setOnClickListener(v -> alertDialog.dismiss());
    }
}
