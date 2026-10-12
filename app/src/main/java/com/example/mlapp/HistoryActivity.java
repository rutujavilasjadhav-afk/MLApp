package com.example.mlapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    public static final String EXTRA_SESSION_ID = "extra_session_id";

    private List<HistoryManager.ChatSession> sessions;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView rvHistory = findViewById(R.id.rvHistory);
        Button btnClearAllHistory = findViewById(R.id.btnClearAllHistory);

        sessions = HistoryManager.getSessions(this);
        adapter = new HistoryAdapter();
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(adapter);

        btnClearAllHistory.setOnClickListener(v -> {
            if (sessions.isEmpty()) {
                Toast.makeText(this, "No history to clear", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle("Clear All History?")
                    .setMessage("Are you sure you want to delete all saved chat history?")
                    .setPositiveButton("Clear", (dialog, which) -> {
                        HistoryManager.clearAllSessions(this);
                        sessions.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HistoryManager.ChatSession session = sessions.get(position);
            holder.text1.setText(session.getTitle());
            holder.text1.setTextColor(getColor(R.color.text_dark_brown));
            holder.text2.setText(session.getTimestamp() + " • " + session.getMessages().size() + " messages");
            holder.text2.setTextColor(getColor(R.color.text_muted_brown));

            holder.itemView.setOnClickListener(v -> {
                Intent data = new Intent();
                data.putExtra(EXTRA_SESSION_ID, session.getId());
                setResult(RESULT_OK, data);
                finish();
            });
        }

        @Override
        public int getItemCount() {
            return sessions.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1, text2;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                text2 = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}