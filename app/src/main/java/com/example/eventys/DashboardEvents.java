package com.example.eventys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;

public class DashboardEvents extends AppCompatActivity {

    TextView mTitle,mDate,mDescription,mParticipants,mIcon,mTime;
    String time,icon,date,eventInfo,eventTitle,participants,eID;
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_events);
        mTitle=findViewById(R.id.title);
        mDate=findViewById(R.id.date);
        mParticipants=findViewById(R.id.nrParticipants);
        mTime=findViewById(R.id.time);
        mIcon=findViewById(R.id.type);
        done=findViewById(R.id.done);
        mDescription=findViewById(R.id.description);
        time = getIntent().getStringExtra("TIME");
        icon = getIntent().getStringExtra("ICON");
        date = getIntent().getStringExtra("DATE");
        eventInfo=getIntent().getStringExtra("DESCRIPTION");
        eventTitle=getIntent().getStringExtra("TITLE");
        participants=getIntent().getStringExtra("PARTICIPANTS");
        eID=getIntent().getStringExtra("EVENTID");
        mTitle.setText("Event Name : " + eventTitle+". ");
        mDate.setText("Event Date : " + date+".");
        mTime.setText("");
        if(time.charAt(1)==':'){
            if(time.length()==4){
                mTime.setText("The Event Takes Place At: 0"+time+".");
                try {
                    long epoch = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date+" 0"+time).getTime() / 1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(time.length()==3){
                String newTime = time.substring(0,2)+"0"+time.substring(2);
                mTime.setText("The Event Takes Place At: 0"+newTime+".");
                try {
                    long epoch = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date+" 0"+time+"0").getTime() / 1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else if(time.charAt(2)==':'){
            if(time.length()==4){
                String newTime= time.substring(0,3)+"0"+time.substring(3);
                mTime.setText("The Event Takes Place At: "+newTime+".");
                try {
                    long epoch = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date+" 0"+time+"0").getTime() / 1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(time.length()==5){
                mTime.setText("The Event Takes Place At: "+time+".");
                try {
                    long epoch = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date+" "+time).getTime() / 1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        mDescription.setText(eventInfo);
        mParticipants.setText("There will be a maximum of "+participants+" participants.");
        mIcon.setText("Event type: "+icon);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(DashboardEvents.this, UserProfile.class);
                startActivity(x);
            }
        });
    }
}