package com.example.eventys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Dashboard extends AppCompatActivity {
    public static final String TAG = "TAG";
    LinearLayout layout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        layout=findViewById(R.id.linearLayout);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID=fAuth.getCurrentUser().getUid();

        fStore.collection("events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Event event = document.toObject(Event.class);
                        if(event.creator.equals(userID)) {
                            final Button button = new Button(Dashboard.this);
                            button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
                            button.setId((int) (event.xlong * 10000));
                            button.setText(event.name1 + " " + event.date);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String mTitle = event.getName1();
                                    String mDescription = event.getDescription();
                                    String mTime = event.getTime();
                                    String mDate = event.getDate();
                                    String eID = document.getId();
                                    String mParticipants = event.getNrParticipants();
                                    String mIcon = event.getIcon();
                                    Intent i = new Intent(Dashboard.this,DashboardEvents.class);
                                    i.putExtra("TITLE",mTitle);
                                    i.putExtra("EVENTID",eID);
                                    i.putExtra("ICON",mIcon);
                                    i.putExtra("DESCRIPTION",mDescription);
                                    i.putExtra("TIME",mTime);
                                    i.putExtra("PARTICIPANTS",mParticipants);
                                    i.putExtra("DATE",mDate);
                                    i.putExtra("XLONG",event.getXlong());
                                    i.putExtra("YLAT",event.getYlat());
                                    Log.d(TAG,"TEST 1 => "+mTime+mDate);
                                    startActivity(i);

                                }
                            });
                            layout.addView(button);
                        }
                    }
                }else{
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });



    }
}