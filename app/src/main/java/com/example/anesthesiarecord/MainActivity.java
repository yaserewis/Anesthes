
package com.example.anesthesiarecord;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPreoperative = findViewById(R.id.btnPreoperative);
        Button btnHandover = findViewById(R.id.btnHandover);
        Button btnOperative = findViewById(R.id.btnOperative);
        Button btnPostoperative = findViewById(R.id.btnPostoperative);

        btnPreoperative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PreoperativeActivity.class);
                startActivity(intent);
            }
        });

        btnHandover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HandoverActivity.class);
                startActivity(intent);
            }
        });

        btnOperative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OperativeActivity.class);
                startActivity(intent);
            }
        });

        btnPostoperative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostoperativeActivity.class);
                startActivity(intent);
            }
        });
    }
}

