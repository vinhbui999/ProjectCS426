package com.example.projectcs426;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HelperProfile extends AppCompatActivity {

    RatingBar ratingBar;//only for showing
    HelperInfor helperInfor = new HelperInfor();
    int index = 0;
    Intent intentIn=null;
    Intent intentToHired = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_profile);

        intentIn = getIntent();
        Bundle arg = intentIn.getBundleExtra("bundle_arg");
        helperInfor = (HelperInfor) arg.getParcelable("helperInf");

        Button booking = findViewById(R.id.booking);
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(helperInfor.available){
                    Toast.makeText(HelperProfile.this, "You can book this helper, because they are available",
                            Toast.LENGTH_SHORT).show();

                    intentToHired = new Intent(HelperProfile.this, CustomerHire.class);
                    Bundle arg2 = new Bundle();
                    arg2.putParcelable("modifyHelper", helperInfor);
                    intentToHired.putExtra("bundle_modify", arg2);

                    startActivity(intentToHired);
                    finish();
                }
                else{
                    Toast.makeText(HelperProfile.this, "You can not book this helper, because they are not available", Toast.LENGTH_SHORT).show();
                    //return to current page because they are not available
                }
            }
        });

        initHelperProfile();
    }

    private void initHelperProfile() {
        ratingBar = findViewById(R.id.ratingCus);

        TextView name = findViewById(R.id.fullNameCus);
        TextView gender = findViewById(R.id.sexCus);
        TextView dob = findViewById(R.id.dob);
        TextView address = findViewById(R.id.addsCus);
        TextView note = findViewById(R.id.note);
        TextView phone = findViewById(R.id.phoneCus);
        ImageView avatar = findViewById(R.id.avatarCus);

        name.setText(helperInfor.getHName());
        gender.setText(helperInfor.getGender());
        dob.setText(helperInfor.getDOB());
        address.setText(helperInfor.getAddress());
        avatar.setImageDrawable(getResources().getDrawable(helperInfor.avatar));
        note.setText(helperInfor.getNotes());
        phone.setText(helperInfor.getPhone());
        ratingBar.setRating(helperInfor.getRating());

    }
}