package com.example.cielid_check;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class RoomHolder extends RecyclerView.ViewHolder {

    ImageView iv;
    TextView nameholder;
    TextView typeholder;
    TextView floorholder;
    CardView cv;

    public RoomHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void SetRoom(FragmentActivity activity,String url,String uid,String name,String type,String floor,String date,String time){

        nameholder = itemView.findViewById(R.id.tv_name_ri);
        typeholder = itemView.findViewById(R.id.tv_type_ri);
        floorholder = itemView.findViewById(R.id.tv_floor_ri);
        iv = itemView.findViewById(R.id.iv_ri);
        cv = itemView.findViewById(R.id.cv_ri);

        nameholder.setText(name);
        typeholder.setText(type);
        floorholder.setText(floor);
        Picasso.get().load(url).into(iv);

    }
}
