package com.example.cielid_check;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class RoomHolder extends RecyclerView.ViewHolder {

    ImageView iv;
    TextView nameholder;
    TextView typeholder;
    TextView floorholder,more;
    CardView cv;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference userReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    public RoomHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void SetRoom(FragmentActivity activity,String url,String uid,String name,String type,String floor,String date,String time){

        userReference = database.getReference("All users").child(currentuid);

        nameholder = itemView.findViewById(R.id.tv_name_ri);
        typeholder = itemView.findViewById(R.id.tv_type_ri);
        floorholder = itemView.findViewById(R.id.tv_floor_ri);
        iv = itemView.findViewById(R.id.iv_ri);
        cv = itemView.findViewById(R.id.cv_ri);
        more = itemView.findViewById(R.id.tv_more_ri);


        nameholder.setText(name);
        typeholder.setText(type);
        floorholder.setText(floor);
        Picasso.get().load(url).into(iv);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String statusholder = snapshot.child("status").getValue(String.class);

                if(statusholder.equals("admin")){
                    more.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
