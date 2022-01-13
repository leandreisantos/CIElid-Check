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

public class EventFragment extends Fragment {


    ImageButton add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_fragment, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        add = getActivity().findViewById(R.id.btn_add_ef);

        add.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),AddEventActivity.class);
            startActivity(intent);
        });
    }
}
