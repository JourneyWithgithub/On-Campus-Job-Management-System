package com.jalil.campusjobmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jalil.campusjobmanagement.Model.EmployeeInfo;

public class employee_details_activity extends AppCompatActivity {


    private EditText editTextStudentName;
    private EditText editTextAge;
    private EditText editTextDepartment;
    private EditText editTextYear;
    private EditText editTextSemester;
    private EditText editTextContactNumber;
    private EditText editTextEmailAddress;

    private Button btnSubmit;

    private FirebaseAuth mAuth;
    private DatabaseReference mJobApplicationDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();


       mJobApplicationDatabase = FirebaseDatabase.getInstance().getReference().child("Job Application").child(uId);

       // mJobApplicationDatabase = FirebaseDatabase.getInstance().getReference().child("Job Application").child("Candidate").child(uId);


        initViews();
        btnSubmit.setOnClickListener(v -> {
            submitJobApplication();
        });
    }

    private void initViews() {
        editTextStudentName = findViewById(R.id.editTextStudentName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextDepartment = findViewById(R.id.editTextDepartment);
        editTextYear = findViewById(R.id.editTextYear);
        editTextSemester = findViewById(R.id.editTextSemester);
        editTextContactNumber = findViewById(R.id.editTextContactNumber);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void submitJobApplication() {
        String name = editTextStudentName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String dept = editTextDepartment.getText().toString().trim();
        String year = editTextYear.getText().toString().trim();
        String semester = editTextSemester.getText().toString().trim();
        String contactNum = editTextContactNumber.getText().toString().trim();
        String emailAddress = editTextEmailAddress.getText().toString().trim();

        // Validate form data
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age) || TextUtils.isEmpty(dept) ||
                TextUtils.isEmpty(year) || TextUtils.isEmpty(semester) || TextUtils.isEmpty(contactNum) ||
                TextUtils.isEmpty(emailAddress)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Create a unique key for the new job application
           String id = mJobApplicationDatabase.push().getKey();



            // Create a new EmployeeInfo object with the data
            EmployeeInfo employeeInfo = new EmployeeInfo(name, age, dept, year, semester, contactNum, emailAddress, id);

            // Save the employeeInfo object to the database
            mJobApplicationDatabase.child(id).setValue(employeeInfo);

            // Show a success message
            Toast.makeText(this, "Form submitted successfully", Toast.LENGTH_SHORT).show();

           // startActivity(new Intent(getApplicationContext(),CandidateListActivity.class));




            clearFormFields();
        }
    }

    private void clearFormFields() {
        editTextStudentName.setText("");
        editTextAge.setText("");
        editTextDepartment.setText("");
        editTextYear.setText("");
        editTextSemester.setText("");
        editTextContactNumber.setText("");
        editTextEmailAddress.setText("");
    }


}