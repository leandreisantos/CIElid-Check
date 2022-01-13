package com.example.cielid_check;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {

    ImageView back;
    TextView student,teacher,admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        back = findViewById(R.id.btn_back_rg);

        student = findViewById(R.id.tv_student_op);
        teacher = findViewById(R.id.tv_teacher_op);
        admin = findViewById(R.id.tv_admin_op);

        back.setOnClickListener(v -> onBackPressed());

        student.setOnClickListener(v -> {
            Intent intent = new Intent(OptionsActivity.this,RegisterActivity.class);
            intent.putExtra("uid","student");
            startActivity(intent);
        });
        teacher.setOnClickListener(v -> showDialog("t"));
        admin.setOnClickListener(v -> showDialog("a"));
    }

    public void showDialog(String statusKey){

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.option_dialog,null);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        alertDialog.show();

        EditText user = view.findViewById(R.id.et_user_dia);
        EditText password = view.findViewById(R.id.et_pass_dia);
        ImageButton submit = view.findViewById(R.id.buttonSignIn);

        submit.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(user.getText().toString())&&!TextUtils.isEmpty(password.getText().toString())){
                if(user.getText().toString().equals("admin")){
                    if(password.getText().toString().equals("123")){
                        if(statusKey.equals("t")){
                            Intent intent = new Intent(OptionsActivity.this,RegisterActivity.class);
                            intent.putExtra("uid","teacher");
                            startActivity(intent);
                        }else{
                             Intent intent = new Intent(OptionsActivity.this,RegisterActivity.class);
                             intent.putExtra("uid","admin");
                             startActivity(intent);
                        }
                    }else Toast.makeText(OptionsActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(OptionsActivity.this, "Incorrect Username", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(OptionsActivity.this, "Input Email and Password", Toast.LENGTH_SHORT).show();

        });


    }
}