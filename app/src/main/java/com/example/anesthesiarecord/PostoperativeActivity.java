package com.example.anesthesiarecord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;

public class PostoperativeActivity extends AppCompatActivity {

    private EditText etOperativeDataSummary, etDischargeVitals, etDrugsToContinue, etRecommendations;
    private Button btnSave, btnGeneratePDF, btnShareReport;
    private SharedPreferences sharedPreferences;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postoperative);

        sharedPreferences = getSharedPreferences("AnesthesiaRecord", MODE_PRIVATE);

        // Initialize views
        etOperativeDataSummary = findViewById(R.id.etOperativeDataSummary);
        etDischargeVitals = findViewById(R.id.etDischargeVitals);
        etDrugsToContinue = findViewById(R.id.etDrugsToContinue);
        etRecommendations = findViewById(R.id.etRecommendations);
        btnSave = findViewById(R.id.btnSave);
        btnGeneratePDF = findViewById(R.id.btnGeneratePDF);
        btnShareReport = findViewById(R.id.btnShareReport);

        // Load saved data
        loadSavedData();

        btnSave.setOnClickListener(v -> {
            savePostoperativeData();
        });

        btnGeneratePDF.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                generatePDF();
            } else {
                requestStoragePermission();
            }
        });

        btnShareReport.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                shareReport();
            } else {
                requestStoragePermission();
            }
        });
    }

    private void loadSavedData() {
        etOperativeDataSummary.setText(sharedPreferences.getString("operativeDataSummary", ""));
        etDischargeVitals.setText(sharedPreferences.getString("dischargeVitals", ""));
        etDrugsToContinue.setText(sharedPreferences.getString("drugsToContinue", ""));
        etRecommendations.setText(sharedPreferences.getString("recommendations", ""));
    }

    private void savePostoperativeData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("operativeDataSummary", etOperativeDataSummary.getText().toString());
        editor.putString("dischargeVitals", etDischargeVitals.getText().toString());
        editor.putString("drugsToContinue", etDrugsToContinue.getText().toString());
        editor.putString("recommendations", etRecommendations.getText().toString());
        editor.apply();

        Toast.makeText(this, "Postoperative recommendations saved successfully", Toast.LENGTH_SHORT).show();
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void generatePDF() {
        // Save current data first
        savePostoperativeData();
        
        PDFGenerator pdfGenerator = new PDFGenerator(this);
        File pdfFile = pdfGenerator.generatePDFReport();
        
        if (pdfFile != null && pdfFile.exists()) {
            Toast.makeText(this, "PDF generated successfully: " + pdfFile.getName(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareReport() {
        // Save current data first
        savePostoperativeData();
        
        PDFGenerator pdfGenerator = new PDFGenerator(this);
        File pdfFile = pdfGenerator.generatePDFReport();
        
        if (pdfFile != null && pdfFile.exists()) {
            Uri pdfUri = FileProvider.getUriForFile(this, "com.example.anesthesiarecord.fileprovider", pdfFile);
            
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Patient Anesthetic Record");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Please find attached the patient anesthetic record.");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            
            startActivity(Intent.createChooser(shareIntent, "Share Anesthetic Record"));
        } else {
            Toast.makeText(this, "Failed to generate PDF for sharing", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage permission denied. Cannot generate PDF.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

