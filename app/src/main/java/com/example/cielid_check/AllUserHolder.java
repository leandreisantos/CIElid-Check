package com.example.cielid_check;

import android.app.Application;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class AllUserHolder extends RecyclerView.ViewHolder {

    TextView nameholder;
    ImageButton selectholder;
    ImageView ivholder;


    public AllUserHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setUser(Application application,String uid,String name,String status,String emauil,String url){

        nameholder = itemView.findViewById(R.id.tv_name_sti);
        selectholder = itemView.findViewById(R.id.buttonSignIn);
        ivholder = itemView.findViewById(R.id.iv_sti);

        nameholder.setText(name);
        Picasso.get().load(url).into(ivholder);

    }
}
