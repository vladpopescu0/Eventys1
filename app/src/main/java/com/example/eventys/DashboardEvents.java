package com.example.eventys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DashboardEvents extends AppCompatActivity {

    TextView eventTitle;
    String eventName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_events);
        eventTitle=findViewById(R.id.eventName);
        eventName=getIntent().getStringExtra("TITLE");

        eventTitle.setText(eventName);
    }
}