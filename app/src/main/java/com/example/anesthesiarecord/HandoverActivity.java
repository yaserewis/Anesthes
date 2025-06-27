package com.example.anesthesiarecord;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HandoverActivity extends AppCompatActivity {

    private EditText etAdmissionEvaluation, etInterventionsPerformed, etDrugsAdministered;
    private Button btnSave;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handover);

        sharedPreferences = getSharedPreferences("AnesthesiaRecord", MODE_PRIVATE);

        // Initialize views
        etAdmissionEvaluation = findViewById(R.id.etAdmissionEvaluation);
        etInterventionsPerformed = findViewById(R.id.etInterventionsPerformed);
        etDrugsAdministered = findViewById(R.id.etDrugsAdministered);
        btnSave = findViewById(R.id.btnSave);

        // Load saved data
        loadSavedData();

        btnSave.setOnClickListener(v -> {
            saveHandoverData();
        });
    }

    private void loadSavedData() {
        etAdmissionEvaluation.setText(sharedPreferences.getString("admissionEvaluation", ""));
        etInterventionsPerformed.setText(sharedPreferences.getString("interventionsPerformed", ""));
        etDrugsAdministered.setText(sharedPreferences.getString("drugsAdministered", ""));
    }

    private void saveHandoverData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("admissionEvaluation", etAdmissionEvaluation.getText().toString());
        editor.putString("interventionsPerformed", etInterventionsPerformed.getText().toString());
        editor.putString("drugsAdministered", etDrugsAdministered.getText().toString());
        editor.apply();

        Toast.makeText(this, "Handover data saved successfully", Toast.LENGTH_SHORT).show();
    }
}

