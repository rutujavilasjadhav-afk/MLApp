package com.example.mlapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SketchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch);

        Button btnClear = findViewById(R.id.btnClear);
        Button btnDone = findViewById(R.id.btnDone);

        btnClear.setOnClickListener(v -> Toast.makeText(this, "Canvas cleared", Toast.LENGTH_SHORT).show());
        btnDone.setOnClickListener(v -> {
            Toast.makeText(this, "Sketch submitted to ML Assistant", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}