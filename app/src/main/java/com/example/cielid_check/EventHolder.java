package com.example.cielid_check;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


public class EventHolder extends RecyclerView.ViewHolder {

    ConstraintLayout cl1;
    TextView titleholder,dateholder;


    public EventHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void SetEvent(FragmentActivity activity,String url,String date,String desc,String title,String postkey){

        titleholder = itemView.findViewById(R.id.tv_title_ei);
        dateholder = itemView.findViewById(R.id.tv_date_ei);
        cl1 = itemView.findViewById(R.id.cl);

        titleholder.setText(title);
        dateholder.setText(date);

    }
}
