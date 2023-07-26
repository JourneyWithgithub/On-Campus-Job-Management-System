package com.jalil.campusjobmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jalil.campusjobmanagement.Model.Data;
import androidx.annotation.Nullable;


public class EditJobFragment extends BottomSheetDialogFragment {



    private EditJobListener listener;
    private Data jobData;

    // Declare EditText fields for job details
    private EditText etJobTitle;
    private EditText etJobDescription;
    private EditText etJobSkills;
    private EditText etJobSalary;
    private EditText etJobDeadline;

    public void setEditJobListener(EditJobListener listener) {
        this.listener = listener;
    }
    public interface EditJobListener {
        void onJobEdit(Data job);
    }

    public void setJobData(Data jobData) {
        this.jobData = jobData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_job_fragment, container, false);

        // Initialize EditText fields
        etJobTitle = view.findViewById(R.id.et_job_title);
        etJobDescription = view.findViewById(R.id.et_job_description);
        etJobSkills = view.findViewById(R.id.et_job_skills);
        etJobSalary = view.findViewById(R.id.et_job_salary);
        etJobDeadline = view.findViewById(R.id.et_job_deadline);

        // Set the current job details to the EditText fields
        etJobTitle.setText(jobData.getTitle());
        etJobDescription.setText(jobData.getDescription());
        etJobSkills.setText(jobData.getSkills());
        etJobSalary.setText(jobData.getSalary());
        etJobDeadline.setText(jobData.getDeadline());

        // ... Other existing code ...

        // Set click listener for "Save" button
        Button btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            // Get the edited job details from the EditText fields
            String editedTitle = etJobTitle.getText().toString();
            String editedDescription = etJobDescription.getText().toString();
            String editedSkills = etJobSkills.getText().toString();
            String editedSalary = etJobSalary.getText().toString();
            String editedDeadline = etJobDeadline.getText().toString();

            // Update the jobData object with the edited details
            jobData.setTitle(editedTitle);
            jobData.setDescription(editedDescription);
            jobData.setSkills(editedSkills);
            jobData.setSalary(editedSalary);
            jobData.setDeadline(editedDeadline);

            // Notify the listener that job editing is done
            if (listener != null) {
                listener.onJobEdit(jobData);
            }

            dismiss(); // Close the bottom sheet after saving
        });

        return view;


    }



}
