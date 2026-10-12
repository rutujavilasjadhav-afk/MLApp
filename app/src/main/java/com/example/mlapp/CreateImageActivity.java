package com.example.mlapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateImageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_image);

        EditText etPrompt = findViewById(R.id.etPrompt);
        Button btnGenerate = findViewById(R.id.btnGenerate);

        btnGenerate.setOnClickListener(v -> {
            String prompt = etPrompt.getText().toString().trim();
            if (prompt.isEmpty()) {
                Toast.makeText(this, "Please enter a prompt", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Generating ML diagram for: " + prompt, Toast.LENGTH_LONG).show();
            finish();
        });
    }
}