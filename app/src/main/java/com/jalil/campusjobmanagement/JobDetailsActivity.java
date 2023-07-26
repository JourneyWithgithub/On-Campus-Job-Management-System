package com.jalil.campusjobmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class JobDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView mTitle;
    private TextView mDate;
    private  TextView mDescription;
    private TextView mSkills;
    private TextView mSalary;

    private  TextView mdeadline;

    Button btnApply;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        toolbar =findViewById(R.id.toolbar_job_details);

      setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Job Details");


        mTitle =findViewById(R.id.job_details_title);
        mDate =findViewById(R.id.job_details_date);
        mDescription=findViewById(R.id.job_details_description);

        mSkills=findViewById(R.id.job_detail_skills);

        mSalary=findViewById(R.id.job_detail_salary);

        mdeadline =findViewById(R.id.job_detail_deadline);

        //Recive data from all job activity using intent...

        Intent intent =getIntent();

        String title =intent.getStringExtra("title");

        String date =intent.getStringExtra("date");
        String description = intent.getStringExtra("description");

        String skills = intent.getStringExtra("skills");
        String salary = intent.getStringExtra("salary");
        String deadline =intent.getStringExtra("deadline");

        mTitle.setText(title);

        mDate.setText(date);

        mDescription.setText(description);

        mSkills.setText(skills);

        mSalary.setText(salary);

        mdeadline.setText(deadline);


        btnApply = findViewById(R.id.btn_apply);


        btnApply.setOnClickListener(v -> {

            Intent intent1 = new Intent(getApplicationContext(), employee_details_activity.class);
            startActivity(intent1);

        });





    }

}