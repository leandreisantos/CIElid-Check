package com.example.cielid_check;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ScheduleHolder extends RecyclerView.ViewHolder {

    ImageView ivholder;
    TextView timeholder,purholder,info,delete;
    TextView starholder,availholder;
    ConstraintLayout cl;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference,userReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    Boolean checkavail = false;



    public ScheduleHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void SetSched(Application application,String rooname,String teacher,String purpose,String startingTime,String endTime,String week,String starting2,String endtime2){
        userReference = database.getReference("All users").child(currentuid);

        ivholder = itemView.findViewById(R.id.iv_si);
        timeholder = itemView.findViewById(R.id.tv_time_si);
        purholder = itemView.findViewById(R.id.tv_purpose_si);
        info = itemView.findViewById(R.id.tv_info_si);
        delete = itemView.findViewById(R.id.tv_delete_si);
        starholder = itemView.findViewById(R.id.tv_star_si);
        availholder = itemView.findViewById(R.id.tv_reserve_si);
        cl = itemView.findViewById(R.id.cl4);

        reference = database.getReference("All users").child(teacher);

        if(currentuid.equals(teacher)){
            starholder.setVisibility(View.VISIBLE);
            availholder.setVisibility(View.VISIBLE);
            cl.setVisibility(View.VISIBLE);

        }
        availholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkavail){
                    availholder.setText("Not Available today give schedule to othersx");
                    checkavail = false;
                }else{
                    availholder.setText("Cancel");
                    checkavail = true;
                }
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url = snapshot.child("url").getValue(String.class);

                Picasso.get().load(url).into(ivholder);
                timeholder.setText(startingTime + " - "+endTime);
                purholder.setText(purpose);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String statusholder = snapshot.child("status").getValue(String.class);

                if(statusholder.equals("admin")){
                    delete.setVisibility(View.VISIBLE);
                    cl.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
