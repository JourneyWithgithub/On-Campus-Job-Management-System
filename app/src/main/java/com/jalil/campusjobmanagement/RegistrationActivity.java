package com.jalil.campusjobmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailReg;
    private EditText passReg;
    private Button btnReg;
    private Button btnLogin;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();

        mDialog=new ProgressDialog(this);

        Registration();
    }

    private void Registration()
    {

      emailReg=findViewById((R.id.email_registration));

        passReg=findViewById(R.id.registration_password);

        btnReg=findViewById(R.id.btn_registration);

        btnLogin=findViewById((R.id.btn_login));

        btnReg.setOnClickListener(v -> {

            String email=emailReg.getText().toString().trim();
            String pass=passReg.getText().toString().trim();
            if(TextUtils.isEmpty(email))
            {
                emailReg.setError("Required Field... ...");
                return;
            }

            if(TextUtils.isEmpty(pass))
            {
                passReg.setError("Required Field");
                return;
            }

            mDialog.setMessage("Processing...");
            mDialog.show();

            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {

                if(task.isSuccessful())
                {


                   sendVerificationEmail();
                  //  Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();

                    //  startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                    //mDialog.dismiss();
                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Registration Failed...",Toast.LENGTH_SHORT).show();

                    mDialog.dismiss();
                }

            });

        });

        btnLogin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),MainActivity.class)));




    }

    private void sendVerificationEmail() {

        FirebaseAuth.getInstance().getCurrentUser()
                .sendEmailVerification()
                .addOnSuccessListener(unused -> {

                    mDialog.dismiss();
            Toast.makeText(RegistrationActivity.this,"Email Verification link send to your email. please verify and then signin", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {

                    mDialog.dismiss();

                    Toast.makeText(RegistrationActivity.this, "Email not sent",Toast.LENGTH_SHORT).show();

                });
    }
}