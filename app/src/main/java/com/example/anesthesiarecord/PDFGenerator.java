package com.example.anesthesiarecord;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PDFGenerator {
    
    private Context context;
    private SharedPreferences sharedPreferences;
    
    public PDFGenerator(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("AnesthesiaRecord", Context.MODE_PRIVATE);
    }
    
    public File generatePDFReport() {
        PdfDocument pdfDocument = new PdfDocument();
        
        // Create a page
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        
        // Title
        paint.setTextSize(24);
        paint.setFakeBoldText(true);
        canvas.drawText("Patient Anesthetic Record", 50, 50, paint);
        
        // Date
        paint.setTextSize(12);
        paint.setFakeBoldText(false);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        canvas.drawText("Generated on: " + currentDate, 50, 80, paint);
        
        int yPosition = 120;
        
        // Preoperative Assessment
        yPosition = addSection(canvas, paint, "PREOPERATIVE ASSESSMENT", yPosition);
        yPosition = addField(canvas, paint, "Past Medical History:", 
            sharedPreferences.getString("pastMedicalHistory", ""), yPosition);
        yPosition = addField(canvas, paint, "Present Medical History:", 
            sharedPreferences.getString("presentMedicalHistory", ""), yPosition);
        yPosition = addField(canvas, paint, "Family History:", 
            sharedPreferences.getString("familyHistory", ""), yPosition);
        yPosition = addField(canvas, paint, "Anesthesia History:", 
            sharedPreferences.getString("anesthesiaHistory", ""), yPosition);
        yPosition = addField(canvas, paint, "Current Complaint:", 
            sharedPreferences.getString("currentComplaint", ""), yPosition);
        yPosition = addField(canvas, paint, "Clinical Findings:", 
            sharedPreferences.getString("clinicalFindings", ""), yPosition);
        yPosition = addField(canvas, paint, "Investigation Findings:", 
            sharedPreferences.getString("investigationFindings", ""), yPosition);
        
        // ICU/Ward Handover
        yPosition = addSection(canvas, paint, "ICU/WARD TO OR HANDOVER", yPosition + 20);
        yPosition = addField(canvas, paint, "Admission Medical Evaluation:", 
            sharedPreferences.getString("admissionEvaluation", ""), yPosition);
        yPosition = addField(canvas, paint, "Interventions Performed:", 
            sharedPreferences.getString("interventionsPerformed", ""), yPosition);
        yPosition = addField(canvas, paint, "Drugs Administered:", 
            sharedPreferences.getString("drugsAdministered", ""), yPosition);
        
        // Check if we need a new page
        if (yPosition > 700) {
            pdfDocument.finishPage(page);
            page = pdfDocument.startPage(pageInfo);
            canvas = page.getCanvas();
            yPosition = 50;
        }
        
        // Operative Notes
        yPosition = addSection(canvas, paint, "OPERATIVE NOTES", yPosition + 20);
        yPosition = addField(canvas, paint, "Time Out Data:", 
            sharedPreferences.getString("timeOutData", ""), yPosition);
        yPosition = addField(canvas, paint, "Medical Evaluation & Basic Vitals:", 
            sharedPreferences.getString("medicalEvaluation", ""), yPosition);
        yPosition = addField(canvas, paint, "Anesthetic Plan:", 
            sharedPreferences.getString("anestheticPlan", ""), yPosition);
        yPosition = addField(canvas, paint, "Induction Drugs & Time:", 
            sharedPreferences.getString("inductionDrugs", ""), yPosition);
        yPosition = addField(canvas, paint, "Drugs & Fluids:", 
            sharedPreferences.getString("drugsFluids", ""), yPosition);
        yPosition = addField(canvas, paint, "Vital Signs Charts:", 
            sharedPreferences.getString("vitalSigns", ""), yPosition);
        
        // Check if we need a new page
        if (yPosition > 700) {
            pdfDocument.finishPage(page);
            page = pdfDocument.startPage(pageInfo);
            canvas = page.getCanvas();
            yPosition = 50;
        }
        
        // Postoperative Recommendations
        yPosition = addSection(canvas, paint, "POSTOPERATIVE RECOMMENDATIONS", yPosition + 20);
        yPosition = addField(canvas, paint, "Operative Data Summary:", 
            sharedPreferences.getString("operativeDataSummary", ""), yPosition);
        yPosition = addField(canvas, paint, "Discharge Vitals:", 
            sharedPreferences.getString("dischargeVitals", ""), yPosition);
        yPosition = addField(canvas, paint, "Drugs to Continue:", 
            sharedPreferences.getString("drugsToContinue", ""), yPosition);
        yPosition = addField(canvas, paint, "Recommendations:", 
            sharedPreferences.getString("recommendations", ""), yPosition);
        
        pdfDocument.finishPage(page);
        
        // Save the PDF
        String fileName = "AnesthesiaRecord_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".pdf";
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
        
        try {
            FileOutputStream fos = new FileOutputStream(file);
            pdfDocument.writeTo(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        pdfDocument.close();
        return file;
    }
    
    private int addSection(Canvas canvas, Paint paint, String title, int yPosition) {
        paint.setTextSize(16);
        paint.setFakeBoldText(true);
        canvas.drawText(title, 50, yPosition, paint);
        return yPosition + 25;
    }
    
    private int addField(Canvas canvas, Paint paint, String label, String value, int yPosition) {
        paint.setTextSize(12);
        paint.setFakeBoldText(true);
        canvas.drawText(label, 50, yPosition, paint);
        
        paint.setFakeBoldText(false);
        if (value != null && !value.isEmpty()) {
            String[] lines = value.split("\n");
            int lineHeight = 15;
            for (String line : lines) {
                yPosition += lineHeight;
                if (line.length() > 80) {
                    // Split long lines
                    int start = 0;
                    while (start < line.length()) {
                        int end = Math.min(start + 80, line.length());
                        canvas.drawText(line.substring(start, end), 70, yPosition, paint);
                        start = end;
                        if (start < line.length()) {
                            yPosition += lineHeight;
                        }
                    }
                } else {
                    canvas.drawText(line, 70, yPosition, paint);
                }
            }
        } else {
            yPosition += 15;
            canvas.drawText("Not specified", 70, yPosition, paint);
        }
        
        return yPosition + 20;
    }
}

