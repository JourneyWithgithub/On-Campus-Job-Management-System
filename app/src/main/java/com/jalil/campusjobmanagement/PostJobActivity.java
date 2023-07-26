package com.jalil.campusjobmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jalil.campusjobmanagement.Model.Data;
import com.jalil.campusjobmanagement.EditJobFragment.EditJobListener;

public class PostJobActivity extends AppCompatActivity {



    private RecyclerView recyclerView;
    private DatabaseReference jobPostDatabase;

    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Data, MyViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        // Toolbar setup if needed
        Toolbar toolbar = findViewById(R.id.toolbar_post_job);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();

        // Firebase Database reference
        jobPostDatabase = FirebaseDatabase.getInstance().getReference().child("Job Post").child(uId);

        recyclerView = findViewById(R.id.recycler_job_post_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(jobPostDatabase.orderByChild("date").limitToLast(10), Data.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Data model) {
                holder.bindJobData(model);
                holder.editIcon.setOnClickListener(v -> {
                    editJob(model);
                });

                holder.deleteIcon.setOnClickListener(v -> {
                    deleteJobByPosition(position);
                });

                holder.navigationDrawerIcon.setOnClickListener(v -> {

                    showNavigationDrawer(model);
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_post_item, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), InsertJobPostActivity.class));
        });
    }


    private void editJob(Data jobData) {
        EditJobFragment editJobFragment = new EditJobFragment();
        editJobFragment.setJobData(jobData);
        editJobFragment.setEditJobListener(job -> {
            // Update the job data in the "Job Post" database
            jobPostDatabase.child(job.getId()).setValue(job)
                    .addOnSuccessListener(aVoid -> {
                        // If the update is successful in the "Job Post" database, then
                        // proceed to update the data in the "Public Database"
                        DatabaseReference publicDatabase = FirebaseDatabase.getInstance().getReference().child("Public Database").child(job.getId());
                        publicDatabase.setValue(job)
                                .addOnSuccessListener(aVoid1 -> {
                                    // Both updates were successful
                                    Toast.makeText(PostJobActivity.this, "Job post updated successfully in both databases", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to update the "Public Database"
                                    Toast.makeText(PostJobActivity.this, "Failed to update job post in the Public database", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Failed to update the "Job Post" database
                        Toast.makeText(PostJobActivity.this, "Failed to update job post in the Job Post database", Toast.LENGTH_SHORT).show();
                    });
        });
        editJobFragment.show(getSupportFragmentManager(), "EditJobFragment");
    }

    private void deleteJobByPosition(int position) {
        Data jobData = adapter.getItem(position);
        if (jobData != null) {
            String jobId = jobData.getId();

            // Remove data from the "Job Post" database
            jobPostDatabase.child(jobId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        // If the data is successfully deleted from the "Job Post" database, then
                        // proceed to remove it from the "Public database"
                        DatabaseReference publicDatabase = FirebaseDatabase.getInstance().getReference().child("Public Database").child(jobId);
                        publicDatabase.removeValue()
                                .addOnSuccessListener(aVoid1 -> {
                                    // Both deletions were successful
                                    Toast.makeText(PostJobActivity.this, "Job post deleted successfully from both databases", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to delete from the "Public database"
                                    Toast.makeText(PostJobActivity.this, "Failed to delete job post from the Public database", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Failed to delete from the "Job Post" database
                        Toast.makeText(PostJobActivity.this, "Failed to delete job post from the Job Post database", Toast.LENGTH_SHORT).show();
                    });
        }
    }


    private void showNavigationDrawer(Data jobModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View navigationDrawerView = LayoutInflater.from(this).inflate(R.layout.navigate_drawer, null);
        builder.setView(navigationDrawerView);

        AlertDialog navigationDrawer = builder.create();
        navigationDrawer.show();


        Button buttonCandidateList = navigationDrawerView.findViewById(R.id.buttonCandidateList);

        // Set a click listener for the "Candidate List" button
        buttonCandidateList.setOnClickListener(v -> {

            Intent intent = new Intent(PostJobActivity.this, CandidateListActivity.class);

            intent.putExtra("jobId", jobModel.getId());
            startActivity(intent);


            navigationDrawer.dismiss();
        });
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView jobTitle;
        private final TextView jobDate;
        private final TextView jobDescription;
        private final TextView jobSkills;
        private final TextView jobSalary;
        private final TextView jobDeadline;
        private final ImageView editIcon;
        private final ImageView deleteIcon;

        private final ImageView navigationDrawerIcon;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.job_title);
            jobDate = itemView.findViewById(R.id.job_date);
            jobDescription = itemView.findViewById(R.id.job_description);
            jobSkills = itemView.findViewById(R.id.job_skills);
            jobSalary = itemView.findViewById(R.id.job_salary);
            jobDeadline = itemView.findViewById(R.id.job_deadline);
            editIcon = itemView.findViewById(R.id.edit_icon);
            deleteIcon = itemView.findViewById(R.id.delete_icon);

            navigationDrawerIcon = itemView.findViewById(R.id.navigation_drawer_icon);
            deleteIcon.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ((PostJobActivity) itemView.getContext()).deleteJobByPosition(position);
                }
            });

        }


        public void bindJobData(Data jobData) {
            jobTitle.setText(jobData.getTitle());
            jobDate.setText(jobData.getDate());
            jobDescription.setText(jobData.getDescription());
            jobSkills.setText(jobData.getSkills());
            jobSalary.setText(jobData.getSalary());
            jobDeadline.setText(jobData.getDeadline());
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




    }
