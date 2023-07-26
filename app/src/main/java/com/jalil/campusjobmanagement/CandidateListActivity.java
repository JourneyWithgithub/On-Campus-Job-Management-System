package com.jalil.campusjobmanagement;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jalil.campusjobmanagement.Model.EmployeeInfo;

import java.util.ArrayList;
import java.util.List;


public class CandidateListActivity extends AppCompatActivity {


    private RecyclerView recyclerViewCandidateList;
    private DatabaseReference candidateDatabase;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<EmployeeInfo, CandidateViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_list);

        Toolbar toolbar = findViewById(R.id.toolbar_candidate_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewCandidateList = findViewById(R.id.recyclerViewCandidateList);
        recyclerViewCandidateList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String jobId = mUser.getUid();
        candidateDatabase = FirebaseDatabase.getInstance().getReference().child("Job Application").child(jobId);

        FirebaseRecyclerOptions<EmployeeInfo> options =
                new FirebaseRecyclerOptions.Builder<EmployeeInfo>()
                        .setQuery(candidateDatabase, EmployeeInfo.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<EmployeeInfo, CandidateViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidateViewHolder holder, int position, @NonNull EmployeeInfo model) {
                holder.bindCandidateData(model);
                holder.btnEmail.setOnClickListener(v -> {
                    EmployeeInfo candidate = getItem(position);
                    sendEmail(candidate.getEmail());
                });
            }

            @NonNull
            @Override
            public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.item_candidate, parent, false);
                return new CandidateViewHolder(view);
            }
        };

        recyclerViewCandidateList.setAdapter(adapter);
    }

    public static class CandidateViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;
        private final TextView textViewAge;
        private final TextView textViewDepartment;
        private final TextView textViewYearSemester;
        private final TextView textViewContactEmail;
        private final ImageButton btnEmail;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAge = itemView.findViewById(R.id.textViewAge);
            textViewDepartment = itemView.findViewById(R.id.textViewDepartment);
            textViewYearSemester = itemView.findViewById(R.id.textViewYearSemester);
            textViewContactEmail = itemView.findViewById(R.id.textViewContactEmail);
            btnEmail = itemView.findViewById(R.id.btnEmail);
        }

        // Method to bind candidate data to the ViewHolder's views
        public void bindCandidateData(EmployeeInfo candidateData) {
            textViewName.setText(candidateData.getName());
            textViewAge.setText(candidateData.getAge());
            textViewDepartment.setText(candidateData.getDept());
            textViewYearSemester.setText(candidateData.getYear() + ", " + candidateData.getSemester());
            textViewContactEmail.setText(candidateData.getEmail());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // Method to send an email
    private void sendEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject of your email");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body of your email");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email"));
        } catch (ActivityNotFoundException ex) {
            // Handle the case where no email application is installed on the device
            Toast.makeText(this, "No email application found.", Toast.LENGTH_SHORT).show();
        }
    }
    /*
    private RecyclerView recyclerViewCandidateList;
    private DatabaseReference candidateDatabase;

    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<EmployeeInfo, CandidateViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_list);

        Toolbar toolbar = findViewById(R.id.toolbar_candidate_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewCandidateList = findViewById(R.id.recyclerViewCandidateList);
        recyclerViewCandidateList.setLayoutManager(new LinearLayoutManager(this));


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String jobId = mUser.getUid();
        candidateDatabase = FirebaseDatabase.getInstance().getReference().child("Job Application").child(jobId);

        // Set up FirebaseRecyclerAdapter for candidates
        FirebaseRecyclerOptions<EmployeeInfo> options =
                new FirebaseRecyclerOptions.Builder<EmployeeInfo>()
                        .setQuery(candidateDatabase, EmployeeInfo.class)
                        .build();


        adapter = new FirebaseRecyclerAdapter<EmployeeInfo, CandidateViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CandidateViewHolder holder, int position, @NonNull EmployeeInfo model) {
                holder.bindCandidateData(model);
            }

            @NonNull
            @Override
            public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.item_candidate, parent, false);
                return new CandidateViewHolder(view);
            }
        };

        recyclerViewCandidateList.setAdapter(adapter);
    }

    // View holder class for each candidate item
    public static class CandidateViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;
        private final TextView textViewAge;
        private final TextView textViewDepartment;
        private final TextView textViewYearSemester;
        private final TextView textViewContactEmail;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAge = itemView.findViewById(R.id.textViewAge);
            textViewDepartment = itemView.findViewById(R.id.textViewDepartment);
            textViewYearSemester = itemView.findViewById(R.id.textViewYearSemester);
            textViewContactEmail = itemView.findViewById(R.id.textViewContactEmail);
        }

        // Method to bind candidate data to the ViewHolder's views
        public void bindCandidateData(EmployeeInfo candidateData) {
            textViewName.setText(candidateData.getName());
            textViewAge.setText(candidateData.getAge());
            textViewDepartment.setText(candidateData.getDept());
            textViewYearSemester.setText(candidateData.getYear() + ", " + candidateData.getSemester());
            textViewContactEmail.setText(candidateData.getEmail());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


     */
}
