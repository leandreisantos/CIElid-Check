package com.example.cielid_check;

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


public class EventHolder extends RecyclerView.ViewHolder {

    ConstraintLayout cl;
    TextView titleholder,dateholder,delete;

    TextView ftitle,fdate,fdesc;
    ImageView fiv;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference userReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();


    public EventHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void SetEvent(FragmentActivity activity,String url,String date,String desc,String title,String postkey){
        userReference = database.getReference("All users").child(currentuid);

        titleholder = itemView.findViewById(R.id.tv_title_ei);
        dateholder = itemView.findViewById(R.id.tv_date_ei);
        cl = itemView.findViewById(R.id.cl1);
        delete = itemView.findViewById(R.id.tv_delete_ei);

        titleholder.setText(title);
        dateholder.setText(date);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String statusholder = snapshot.child("status").getValue(String.class);

                if(statusholder.equals("admin")){
                    delete.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void FragSetEvent(FragmentActivity activity,String url,String date,String desc,String title,String postkey){

        ftitle = itemView.findViewById(R.id.tv_title_ei);
        fdate = itemView.findViewById(R.id.tv_date_ei);
        fdesc = itemView.findViewById(R.id.tv_desc_ei);
        fiv = itemView.findViewById(R.id.iv_ei);

        ftitle.setText(title);
        fdate.setText(date);
        fdesc.setText(desc);

        Picasso.get().load(url).into(fiv);

    }
}
