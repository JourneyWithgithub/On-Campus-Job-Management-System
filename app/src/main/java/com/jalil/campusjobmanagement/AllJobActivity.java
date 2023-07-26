
package com.jalil.campusjobmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jalil.campusjobmanagement.Model.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AllJobActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private DatabaseReference mAllJobPost;
    private FirebaseRecyclerAdapter<Data, AllJobPostViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_job);

        // Set up the custom Toolbar
        Toolbar customToolbar = findViewById(R.id.all_job_post);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setTitle("All Job Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the database reference
        mAllJobPost = FirebaseDatabase.getInstance().getReference().child("Public Database");
        mAllJobPost.keepSynced(true);

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_all_job);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up FirebaseRecyclerAdapter for all jobs
        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mAllJobPost.orderByChild("date").limitToLast(10), Data.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Data, AllJobPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllJobPostViewHolder holder, int position, @NonNull Data model) {
                holder.setJobTitle(model.getTitle());
                holder.setJobDate(model.getDate());
                holder.setJobDescription(model.getDescription());
                holder.setJobSkill(model.getSkills());
                holder.setJobSalary(model.getSalary());
                holder.setJobDeadline(model.getDeadline());

                // Handle item click to open job details
                holder.myview.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), JobDetailsActivity.class);
                    intent.putExtra("title", model.getTitle());
                    intent.putExtra("date", model.getDate());
                    intent.putExtra("description", model.getDescription());
                    intent.putExtra("skills", model.getSkills());
                    intent.putExtra("salary", model.getSalary());
                    intent.putExtra("deadline", model.getDeadline());
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public AllJobPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alljobpost, parent, false);
                return new AllJobPostViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_all_job, menu);

        // Get the SearchView from the menu
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Jobs...");

        // Set a query text listener to handle search operations
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Since FirebaseRecyclerAdapter automatically listens for data changes,
                // there's no need to manually call searchJobs() when submitting the query.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Call searchJobs() with the new text to perform the search.
                searchJobs(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchJobs(String searchText) {
        try {
            FirebaseRecyclerOptions<Data> options =
                    new FirebaseRecyclerOptions.Builder<Data>()
                            .setQuery(mAllJobPost.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff"), Data.class)
                            .build();

            adapter.updateOptions(options);
        } catch (Exception e) {
            e.printStackTrace();
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

    public static class AllJobPostViewHolder extends RecyclerView.ViewHolder {
        View myview;


        public AllJobPostViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = itemView;
        }

        public void setJobTitle(String title) {
            TextView mTitle = myview.findViewById(R.id.all_job_post_title);
            mTitle.setText(title);
        }

        public void setJobDate(String date) {
            TextView mDate = myview.findViewById(R.id.all_job_post_date);
            mDate.setText(date);
        }

        public void setJobDescription(String description) {
            TextView mDescription = myview.findViewById(R.id.all_job_post_description);
            mDescription.setText(description);
        }

        public void setJobSkill(String skill) {
            TextView mSkill = myview.findViewById(R.id.all_job_post_skills);
            mSkill.setText(skill);
        }

        public void setJobSalary(String salary) {
            TextView mSalary = myview.findViewById(R.id.all_job_post_salary);
            mSalary.setText(salary);
        }

        public void setJobDeadline(String deadline) {
            TextView mDeadline = myview.findViewById(R.id.all_job_post_deadline);
            mDeadline.setText(deadline);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date currentDate = new Date();
            try {
                Date deadlineDate = sdf.parse(deadline);
                if (deadlineDate != null && deadlineDate.before(currentDate)) {
                    mDeadline.setTextColor(ContextCompat.getColor(myview.getContext(), R.color.red));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
/*
package com.jalil.campusjobmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jalil.campusjobmanagement.Model.Data;

public class AllJobActivity extends AppCompatActivity {



    private RecyclerView recyclerView;
    private DatabaseReference mAllJobPost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_job);

        // Set up the toolbar
        setSupportActionBar(findViewById(R.id.all_job_post));
        getSupportActionBar().setTitle("All Job Post");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the database reference
        mAllJobPost = FirebaseDatabase.getInstance().getReference().child("Public Database");
        mAllJobPost.keepSynced(true);

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_all_job);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mAllJobPost.orderByChild("date").limitToLast(10), Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data, AllJobPostViewHolder> adapter =
                new FirebaseRecyclerAdapter<Data, AllJobPostViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AllJobPostViewHolder holder, int position, @NonNull Data model) {
                        holder.setJobTitle(model.getTitle());
                        holder.setJobDate(model.getDate());
                        holder.setJobDescription(model.getDescription());
                        holder.setJobSkill(model.getSkills());
                        holder.setJobSalary(model.getSalary());
                        holder.setJobDeadline(model.getDeadline());


                        // Handle item click to open job details
                        holder.myview.setOnClickListener(v -> {
                            Intent intent = new Intent(getApplicationContext(), JobDetailsActivity.class);
                            intent.putExtra("title", model.getTitle());
                            intent.putExtra("date", model.getDate());
                            intent.putExtra("description", model.getDescription());
                            intent.putExtra("skills", model.getSkills());
                            intent.putExtra("salary", model.getSalary());
                            intent.putExtra("deadline", model.getDeadline());
                            startActivity(intent);
                        });
                    }

                    @NonNull
                    @Override
                    public AllJobPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alljobpost, parent, false);
                        return new AllJobPostViewHolder(view);
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AllJobPostViewHolder extends RecyclerView.ViewHolder {
        View myview;

        public AllJobPostViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = itemView;

        }

        public void setJobTitle(String title) {
            TextView mTitle = myview.findViewById(R.id.all_job_post_title);
            mTitle.setText(title);
        }

        public void setJobDate(String date) {
            TextView mDate = myview.findViewById(R.id.all_job_post_date);
            mDate.setText(date);
        }

        public void setJobDescription(String description) {
            TextView mDescription = myview.findViewById(R.id.all_job_post_description);
            mDescription.setText(description);
        }

        public void setJobSkill(String skill) {
            TextView mSkill = myview.findViewById(R.id.all_job_post_skills);
            mSkill.setText(skill);
        }

        public void setJobSalary(String salary) {
            TextView mSalary = myview.findViewById(R.id.all_job_post_salary);
            mSalary.setText(salary);
        }

        public void setJobDeadline (String deadline)
        {
            TextView mDeadline =myview.findViewById(R.id.all_job_post_deadline);

            mDeadline.setText(deadline);
        }
    }



}


*/


/*

package com.jalil.campusjobmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jalil.campusjobmanagement.Model.Data;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.firestore.CollectionReference;

public class AllJobActivity extends AppCompatActivity {


    /*
    private RecyclerView recyclerView;
    private DatabaseReference mAllJobPost;
    private FirebaseRecyclerAdapter<Data, AllJobPostViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_job);

        // Set up the custom Toolbar
        Toolbar customToolbar = findViewById(R.id.all_job_post);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setTitle("All Job Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the database reference
        mAllJobPost = FirebaseDatabase.getInstance().getReference().child("Public Database");
        mAllJobPost.keepSynced(true);

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_all_job);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up FirebaseRecyclerAdapter for all jobs
        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mAllJobPost.orderByChild("date").limitToLast(10), Data.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Data, AllJobPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllJobPostViewHolder holder, int position, @NonNull Data model) {
                holder.setJobTitle(model.getTitle());
                holder.setJobDate(model.getDate());
                holder.setJobDescription(model.getDescription());
                holder.setJobSkill(model.getSkills());
                holder.setJobSalary(model.getSalary());
                holder.setJobDeadline(model.getDeadline());
            }

            @NonNull
            @Override
            public AllJobPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alljobpost, parent, false);
                return new AllJobPostViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_all_job, menu);

        // Get the SearchView from the menu
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Jobs...");

        // Set a query text listener to handle search operations
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Since FirebaseRecyclerAdapter automatically listens for data changes,
                // there's no need to manually call searchJobs() when submitting the query.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Call searchJobs() with the new text to perform the search.
                searchJobs(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchJobs(String searchText) {
        try {
            FirebaseRecyclerOptions<Data> options =
                    new FirebaseRecyclerOptions.Builder<Data>()
                            .setQuery(mAllJobPost.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff"), Data.class)
                            .build();

            adapter.updateOptions(options);
        } catch (Exception e) {
            e.printStackTrace();
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

    public static class AllJobPostViewHolder extends RecyclerView.ViewHolder {
        View myview;

        public AllJobPostViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = itemView;
        }

        public void setJobTitle(String title) {
            TextView mTitle = myview.findViewById(R.id.all_job_post_title);
            mTitle.setText(title);
        }

        public void setJobDate(String date) {
            TextView mDate = myview.findViewById(R.id.all_job_post_date);
            mDate.setText(date);
        }

        public void setJobDescription(String description) {
            TextView mDescription = myview.findViewById(R.id.all_job_post_description);
            mDescription.setText(description);
        }

        public void setJobSkill(String skill) {
            TextView mSkill = myview.findViewById(R.id.all_job_post_skills);
            mSkill.setText(skill);
        }

        public void setJobSalary(String salary) {
            TextView mSalary = myview.findViewById(R.id.all_job_post_salary);
            mSalary.setText(salary);
        }

        public void setJobDeadline(String deadline) {
            TextView mDeadline = myview.findViewById(R.id.all_job_post_deadline);
            mDeadline.setText(deadline);
        }
    }




*/