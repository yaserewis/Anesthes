package com.example.anesthesiarecord;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PreoperativeActivity extends AppCompatActivity {

    private EditText etPastMedicalHistory, etPresentMedicalHistory, etFamilyHistory, 
                     etAnesthesiaHistory, etCurrentComplaint, etClinicalFindings, 
                     etInvestigationFindings;
    private Button btnSave;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preoperative);

        sharedPreferences = getSharedPreferences("AnesthesiaRecord", MODE_PRIVATE);

        // Initialize views
        etPastMedicalHistory = findViewById(R.id.etPastMedicalHistory);
        etPresentMedicalHistory = findViewById(R.id.etPresentMedicalHistory);
        etFamilyHistory = findViewById(R.id.etFamilyHistory);
        etAnesthesiaHistory = findViewById(R.id.etAnesthesiaHistory);
        etCurrentComplaint = findViewById(R.id.etCurrentComplaint);
        etClinicalFindings = findViewById(R.id.etClinicalFindings);
        etInvestigationFindings = findViewById(R.id.etInvestigationFindings);
        btnSave = findViewById(R.id.btnSave);

        // Load saved data
        loadSavedData();

        btnSave.setOnClickListener(v -> {
            savePreoperativeData();
        });
    }

    private void loadSavedData() {
        etPastMedicalHistory.setText(sharedPreferences.getString("pastMedicalHistory", ""));
        etPresentMedicalHistory.setText(sharedPreferences.getString("presentMedicalHistory", ""));
        etFamilyHistory.setText(sharedPreferences.getString("familyHistory", ""));
        etAnesthesiaHistory.setText(sharedPreferences.getString("anesthesiaHistory", ""));
        etCurrentComplaint.setText(sharedPreferences.getString("currentComplaint", ""));
        etClinicalFindings.setText(sharedPreferences.getString("clinicalFindings", ""));
        etInvestigationFindings.setText(sharedPreferences.getString("investigationFindings", ""));
    }

    private void savePreoperativeData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pastMedicalHistory", etPastMedicalHistory.getText().toString());
        editor.putString("presentMedicalHistory", etPresentMedicalHistory.getText().toString());
        editor.putString("familyHistory", etFamilyHistory.getText().toString());
        editor.putString("anesthesiaHistory", etAnesthesiaHistory.getText().toString());
        editor.putString("currentComplaint", etCurrentComplaint.getText().toString());
        editor.putString("clinicalFindings", etClinicalFindings.getText().toString());
        editor.putString("investigationFindings", etInvestigationFindings.getText().toString());
        editor.apply();

        Toast.makeText(this, "Preoperative assessment saved successfully", Toast.LENGTH_SHORT).show();
    }
}

