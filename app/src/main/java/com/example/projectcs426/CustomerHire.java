package com.example.projectcs426;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CustomerHire extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText address, description, day,time, purchase;
    ActionBar actionBar;
    Intent intentHelper = null;
    HelperInfor helperInfor = new HelperInfor();
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_hire);

        actionBar = getSupportActionBar();
        intentHelper=getIntent();
        Bundle arg2 = intentHelper.getBundleExtra("bundle_modify");
        helperInfor = arg2.getParcelable("modifyHelper");

        send = findViewById(R.id.btn_sendRe);
        initEditTexts();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(address.getText().toString() != null| description.getText().toString() != null | day.getText().toString() != null
                        | time.getText().toString() != null | purchase.getText().toString() != null ){

                    helperInfor.available = false;
                    changeInFile(helperInfor);

                    Snackbar.make(view, "Send request to helper with current user is " + FirebaseAuth.getInstance().getCurrentUser().getUid(), Snackbar.LENGTH_LONG)
                            .setAction("DISMISS", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();

                    //send to current helper was hired. but have to fix for not showing then
                    writeToUsersFile();
                    Intent intentToCurrent = new Intent(CustomerHire.this, CurrentHelperHired.class);
                    startActivity(intentToCurrent);

                }
                else {
                    Snackbar.make(view, "Please fill all the information", Snackbar.LENGTH_LONG)
                            .setAction("DISMISS", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                    }
                }
        });
        modifyActionBar();
    }

    private void writeToUsersFile() {
        if(helperInfor != null){
        String file_name = FirebaseAuth.getInstance().getCurrentUser().getUid();
        try {
            FileOutputStream fos = openFileOutput(file_name+".txt", 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write("Bùi Xuân Vĩnh" + '\n');
            bw.write("0989778966"+'\n');
            bw.write("Male" +'\n');
            bw.write("14/09/2000" +'\n');
            bw.write("231/83/8A Dương Bá Trạc, Phường 1, Quận 8" + '\n');
            bw.write("Work early in the noon" +'\n');
            bw.write("2"+'\n');
            bw.write(String.valueOf(Integer.valueOf(helperInfor.avatar)) +'\n');
            bw.write("true" +'\n');

            bw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    private void modifyActionBar() {
        actionBar.setTitle("Request to " + helperInfor.getHName());
    }

    private void initEditTexts() {
        address= findViewById(R.id.addHire);
        description = findViewById(R.id.descripWork);
        day=findViewById(R.id.requDay);
        time = findViewById(R.id.requTime);
        purchase = findViewById(R.id.purchase);

        address.setText(null);
        description.setText(null);
        day.setText(null);
        time.setText(null);
        purchase.setText(null);
    }

    @Override
    public void onBackPressed() {
        Intent intentBack = new Intent(this, HelperProfile.class);
        Bundle arg = new Bundle();

        arg.putParcelable("helperInf", (Parcelable) helperInfor);

        intentBack.putExtra("bundle_arg", arg);
        startActivity(intentBack);

    }


    private void changeInFile(HelperInfor helperInfor) {
        String file_name = String.valueOf(helperInfor.avatar);
        try {
            FileOutputStream fos = openFileOutput(file_name +".txt", 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(helperInfor.getHName() +"\n");
            bw.write(helperInfor.getPhone()+'\n');
            bw.write(helperInfor.getGender()+'\n');
            bw.write(helperInfor.getDOB() +'\n');
            bw.write(helperInfor.getAddress() + '\n');
            bw.write(helperInfor.getNotes() +'\n');
            bw.write(helperInfor.getRating().toString()+'\n');
            bw.write(String.valueOf(Integer.valueOf(helperInfor.avatar)) +'\n');
            bw.write(String.valueOf(helperInfor.available) +'\n');

            bw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }
}