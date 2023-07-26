package com.jalil.campusjobmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Button btnAllJob;
    private Button btnPostJob;

    private Toolbar toolbar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("On Campus Job Management");

        mAuth =FirebaseAuth.getInstance();

      btnAllJob=findViewById(R.id.btn_allJob);
      btnPostJob=findViewById(R.id.btn_PostJob);


        btnAllJob.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),AllJobActivity.class))




        );
        btnPostJob.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),PostJobActivity.class))






        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.logout)
        {
                mAuth.signOut();

                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            return true;




        }
        return super.onOptionsItemSelected(item);
    }
}