package com.jalil.campusjobmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.widget.*;

public class MainActivity extends AppCompatActivity {


    private EditText email;
    private EditText password;
    private Button btnLogin;
    private Button btnRegistration;

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        mDialog=new ProgressDialog(this);

        LoginFunction();
    }

    private void LoginFunction() {

        email =findViewById(R.id.email_login);
        password=findViewById(R.id.login_password);
        btnLogin =findViewById(R.id.btn_login);
        btnRegistration =findViewById(R.id.btn_reg);

        btnLogin.setOnClickListener(v -> {

            String mEmail=email.getText().toString().trim();
            String pass=password.getText().toString().trim();

            if(TextUtils.isEmpty(mEmail))
            {
                email.setError("Required Field...");
                return;
            }
            if(TextUtils.isEmpty(pass))
            {
                password.setError("Required Field...");
            }

            mDialog.setMessage("Processing...");
            mDialog.show();

            mAuth.signInWithEmailAndPassword(mEmail,pass).addOnCompleteListener(task -> {

                if(task.isSuccessful())
                {
                        if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                            Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                            mDialog.dismiss();
                        }

                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please Verify your Email first", Toast.LENGTH_SHORT).show();

                        }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),task.getException().toString() ,Toast.LENGTH_SHORT).show();
                }

            });



        });
        btnRegistration.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegistrationActivity.class)));

    }
}