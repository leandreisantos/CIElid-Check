package com.example.cielid_check;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


public class EventHolder extends RecyclerView.ViewHolder {

    ConstraintLayout cl;
    TextView titleholder,dateholder,delete;

    TextView ftitle,fdate,fdesc;
    ImageView fiv;


    public EventHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void SetEvent(FragmentActivity activity,String url,String date,String desc,String title,String postkey){

        titleholder = itemView.findViewById(R.id.tv_title_ei);
        dateholder = itemView.findViewById(R.id.tv_date_ei);
        cl = itemView.findViewById(R.id.cl1);
        delete = itemView.findViewById(R.id.tv_delete_ei);

        titleholder.setText(title);
        dateholder.setText(date);

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
