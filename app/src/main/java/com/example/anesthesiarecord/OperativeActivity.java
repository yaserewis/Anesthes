package com.example.anesthesiarecord;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OperativeActivity extends AppCompatActivity {

    private EditText etTimeOutData, etMedicalEvaluation, etAnestheticPlan, 
                     etInductionDrugs, etDrugsFluids, etVitalSigns;
    private Button btnSave;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operative);

        sharedPreferences = getSharedPreferences("AnesthesiaRecord", MODE_PRIVATE);

        // Initialize views
        etTimeOutData = findViewById(R.id.etTimeOutData);
        etMedicalEvaluation = findViewById(R.id.etMedicalEvaluation);
        etAnestheticPlan = findViewById(R.id.etAnestheticPlan);
        etInductionDrugs = findViewById(R.id.etInductionDrugs);
        etDrugsFluids = findViewById(R.id.etDrugsFluids);
        etVitalSigns = findViewById(R.id.etVitalSigns);
        btnSave = findViewById(R.id.btnSave);

        // Load saved data
        loadSavedData();

        btnSave.setOnClickListener(v -> {
            saveOperativeData();
        });
    }

    private void loadSavedData() {
        etTimeOutData.setText(sharedPreferences.getString("timeOutData", ""));
        etMedicalEvaluation.setText(sharedPreferences.getString("medicalEvaluation", ""));
        etAnestheticPlan.setText(sharedPreferences.getString("anestheticPlan", ""));
        etInductionDrugs.setText(sharedPreferences.getString("inductionDrugs", ""));
        etDrugsFluids.setText(sharedPreferences.getString("drugsFluids", ""));
        etVitalSigns.setText(sharedPreferences.getString("vitalSigns", ""));
    }

    private void saveOperativeData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("timeOutData", etTimeOutData.getText().toString());
        editor.putString("medicalEvaluation", etMedicalEvaluation.getText().toString());
        editor.putString("anestheticPlan", etAnestheticPlan.getText().toString());
        editor.putString("inductionDrugs", etInductionDrugs.getText().toString());
        editor.putString("drugsFluids", etDrugsFluids.getText().toString());
        editor.putString("vitalSigns", etVitalSigns.getText().toString());
        editor.apply();

        Toast.makeText(this, "Operative notes saved successfully", Toast.LENGTH_SHORT).show();
    }
}

