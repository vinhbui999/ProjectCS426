package com.example.projectcs426;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CurrentHelperHired extends AppCompatActivity implements View.OnClickListener {

    Intent intentIn;
    Button vote;
    ImageView avt;
    TextView name, phone;
    HelperInfor mhelper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_helper_hired);


        getCurrentHelper();
        initComponents();

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CurrentHelperHired.this);
                LinearLayout linearLayout = new LinearLayout(CurrentHelperHired.this);
                RatingBar ratingBar = new RatingBar(CurrentHelperHired.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                ratingBar.setLayoutParams(lp);
                ratingBar.setNumStars(5);
                ratingBar.setStepSize((float)0.5);

                linearLayout.addView(ratingBar);

                builder.setTitle("Your vote to this helper: ");
                builder.setView(linearLayout);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        float score = ratingBar.getRating();
                        float result = (score + mhelper.Rating) /2;
                        mhelper.setRating(result);
                        mhelper.setAvailable(true);
                        clearToUsersFile();
                        writeToHelperFile();
                    }
                });

                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(CurrentHelperHired.this, Maps.class));
                            }

                        })

                        // Button Cancel
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                builder.create();
                builder.show();
            }
        });
    }

    private void getCurrentHelper() {
        FileInputStream fis = null;
        String file_name = FirebaseAuth.getInstance().getCurrentUser().getUid();

        try {
            fis = openFileInput(file_name + ".txt");
            InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String text = null;
            StringBuilder sb = new StringBuilder();
            int k = 7;
            if((text = bufferedReader.readLine()) != null){
                mhelper = new HelperInfor();
                mhelper.setHName(text);
            }
            while ((text = bufferedReader.readLine()) != null) {
                sb.append(text).append('\n');if (k == 7) {
                    mhelper.setPhone(text);
                    k--;
                } else if (k == 6) {
                    mhelper.setGender(text);
                    k--;
                } else if (k == 5) {
                    mhelper.setDOB(text);
                    k--;
                } else if (k == 4) {
                    mhelper.setAddress(text);
                    k--;
                } else if (k == 3) {
                    mhelper.setNotes(text);
                    k--;
                } else if (k == 2) {
                    mhelper.setRating(Float.parseFloat(text));
                    k--;
                } else if (k == 1) {
                    mhelper.setAvatar(Integer.parseInt(text));
                    k--;
                } else if (k == 0) {
                    mhelper.setAvailable(Boolean.parseBoolean(text));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initComponents() {
        vote=findViewById(R.id.vote);
        avt=findViewById(R.id.Cavt);
        name = findViewById(R.id.CName);
        phone = findViewById(R.id.Cphone);

        if(mhelper != null){
            avt.setImageDrawable(getResources().getDrawable(mhelper.avatar));
            name.setText(mhelper.HName);
            phone.setText(mhelper.phone);

        }
    }

    private void clearToUsersFile() {
            String file_name = FirebaseAuth.getInstance().getCurrentUser().getUid();
            try {
                FileOutputStream fos = openFileOutput(file_name+".txt", 0);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write("");

                bw.close();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void writeToHelperFile() {
        String file_name = String.valueOf(mhelper.avatar);
        try {
            FileOutputStream fos = openFileOutput(file_name+".txt", 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(mhelper.getHName() + '\n');
            bw.write(mhelper.getPhone()+'\n');
            bw.write(mhelper.getGender() +'\n');
            bw.write(mhelper.getDOB() +'\n');
            bw.write(mhelper.getAddress() + '\n');
            bw.write(mhelper.getNotes() +'\n');
            bw.write(mhelper.getRating().toString()+'\n');
            bw.write(String.valueOf(Integer.valueOf(mhelper.avatar)) +'\n');
            bw.write(String.valueOf(mhelper.available) +'\n');

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