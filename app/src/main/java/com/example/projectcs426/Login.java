package com.example.projectcs426;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.passw);
        mLoginBtn = findViewById(R.id.LoginButton);
        mCreateBtn = findViewById(R.id.TextCreateAcc);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        //writeToFile();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password must be six or more character!");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Log in successfully" + fAuth.getCurrentUser().getUid()
                                    ,Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, Maps.class));
                        }else{
                            Toast.makeText(Login.this,"Error !" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,SignIn.class));
            }
        });
    }

    private void writeToFile() {
        String file_name = String.valueOf(R.drawable.xuan_vinh);
        try {
            FileOutputStream fos = openFileOutput(file_name+".txt", 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write("Bùi Xuân Vĩnh" + '\n');
            bw.write("0989778966"+'\n');
            bw.write("Male" +'\n');
            bw.write("14/09/2000" +'\n');
            bw.write("231/83/8A Dương Bá Trạc, Phường 1, Quận 8" + '\n');
            //bw.write("220 Đường Bùi Hữu Nghĩa, Phường 2, Quận Bình Thạnh" +'\n');
            bw.write("Work early in the noon" +'\n');
            bw.write("2"+'\n');
            bw.write(String.valueOf(Integer.valueOf(R.drawable.xuan_vinh)) +'\n');
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
