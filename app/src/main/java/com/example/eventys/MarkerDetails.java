package com.example.eventys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class MarkerDetails extends AppCompatActivity {
    public static final String TAG = "TAG";
    String time,date,eventTitle,eventInfo,participants,icon;
    TextView mTitle,mTime,mDescription,mDate,mParticipants,extra;
    Button eBack, eJoin;
    FirebaseFirestore fStore;
    FirebaseAuth  fAuth;
    double ylat,xlong;
    String userID,eID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_details);

        mTime=(TextView)findViewById(R.id.time);
        mDate=(TextView)findViewById(R.id.date);
        mDescription=(TextView)findViewById(R.id.info);
        mTitle=(TextView)findViewById(R.id.title_marker);
        mParticipants=(TextView)findViewById(R.id.participants);
        eBack=(Button)findViewById(R.id.back);
        eJoin=(Button)findViewById(R.id.join);
        extra=(TextView)findViewById(R.id.extraText);


        time = getIntent().getStringExtra("TIME");
        icon = getIntent().getStringExtra("ICON");
        date = getIntent().getStringExtra("DATE");
        eventInfo=getIntent().getStringExtra("DESCRIPTION");
        eventTitle=getIntent().getStringExtra("TITLE");
        participants=getIntent().getStringExtra("PARTICIPANTS");
        eID=getIntent().getStringExtra("EVENTID");
        ylat=getIntent().getDoubleExtra("YLAT",0);
        xlong=getIntent().getDoubleExtra("XLONG",0);

        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();

        userID=fAuth.getCurrentUser().getUid();



        Log.d(TAG,"timpul este : " + time+".");
        //mTime.setText("The event takes place at: "+time);
        mTitle.setText("Event Name : " + eventTitle+"."+"Type: "+icon);
        mDate.setText("Event Date : " + date+".");
        mDescription.setText(eventInfo);
        mParticipants.setText("There will be a maximum of "+participants+" participants.");
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
        eBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MarkerDetails.this,MapsActivity.class);
                startActivity(i);
            }
        });

        DocumentReference documentReference = fStore.collection("events").document(eID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Log.d(TAG,"getresult este => "+task.getResult());
                String events = document.getString("allParticipants");
                String creator = document.getString("creator");
                if(creator.equals(userID)){
                    eJoin.setVisibility(View.INVISIBLE);
                    extra.setText("You are the creator of the event");
                }
                Log.d(TAG,"acesta este event.getstring =>"+events);
                int isExists = events.indexOf(userID);
                if(isExists!=-1){
                    eJoin.setVisibility(View.INVISIBLE);
                    extra.setText("You have already joined the event");
                }
            }
        });
        /*fStore.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userID=fAuth.getCurrentUser().getUid();
                                String creator = document.getString("creator");
                                Log.d(TAG,"creatorul este =>"+creator+" "+userID);
                                if(userID.equals(creator) && document.getId().equals(eID)){
                                    eJoin.setVisibility(View.GONE);
                                    extra.setText("You are the creator of the event!");
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
*/



        eJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference documentReference = fStore.collection("events").document(eID);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        Log.d(TAG,"getresult este => "+task.getResult());
                        String allParticipants = document.getString("allParticipants") + "," + userID;
                        String creator = document.getString("creator");
                        Event event = new Event(
                                eventTitle,
                                eventInfo,
                                participants,
                                time,
                                date,
                                xlong,
                                ylat,
                                creator,
                                icon,
                                allParticipants
                        );
                        fStore.collection("events").document(eID)
                                .set(event)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

                    }
                });
                Toast.makeText(MarkerDetails.this,"You joined the event!",Toast.LENGTH_LONG).show();
                Intent x = new Intent(MarkerDetails.this,MapsActivity.class);
                startActivity(x);
                //eJoin.setVisibility(View.VISIBLE);
            }
        });
    }
}