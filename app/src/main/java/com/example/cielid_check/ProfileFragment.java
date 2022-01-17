package com.example.cielid_check;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference reference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    ImageView iv;
    TextView name,namef;
    TextView status,statusf;
    TextView editProf;
    TextView email;
    TextView logout;

    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        reference = database.getReference("All users").child(currentuid);

        name = getActivity().findViewById(R.id.tv_name_pf);
        status = getActivity().findViewById(R.id.tv_status_pf);
        iv = getActivity().findViewById(R.id.iv_pf);
        editProf = getActivity().findViewById(R.id.tv_edit_pf);
        email = getActivity().findViewById(R.id.tv_email_pf);
        namef = getActivity().findViewById(R.id.tv_fname_pf);
        statusf = getActivity().findViewById(R.id.tv_statusf_pf);
        logout = getActivity().findViewById(R.id.tv_logout_pf);

        editProf.setOnClickListener(v -> startActivity(new Intent(getActivity(), EditProfileActivity.class)));

        logout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(),LoginActivity.class));
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameholder = snapshot.child("name").getValue(String.class);
                String statusholder = snapshot.child("status").getValue(String.class);
                String emailholder = snapshot.child("email").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);

                namef.setText(nameholder);
                statusf.setText(statusholder);
                email.setText(emailholder);
                Picasso.get().load(url).into(iv);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
