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
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CalendarFragment extends Fragment {

    ImageButton addEvent;
    MaterialDatePicker materialDatePicker;
    CalendarView calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addEvent = getActivity().findViewById(R.id.btn_add_ef);
        calendar = getActivity().findViewById(R.id.calendarview_cf);



        addEvent.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),AddEventActivity.class);
            startActivity(intent);
        });

    }

}
