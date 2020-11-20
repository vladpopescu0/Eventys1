package com.example.eventys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddEventInfo extends AppCompatActivity {

    public static final String TAG = "TAG";
    double xlong,ylat;
    FirebaseFirestore fStore;
    String userID;
    String color;
    String timestring,datestring;
    String eventGlobalName;
    EditText mEventName, mNrParticipants, mDescription;
    TextView mydate;
    FirebaseAuth fAuth;
    Button timeBtn,sendEvent;
    Spinner eventIcon;
    String icon;
    int tHour,tMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_info);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        xlong=getIntent().getDoubleExtra("LONGITUDE",0);
        ylat=getIntent().getDoubleExtra("LATITUDE",0);
        timeBtn=(Button)findViewById(R.id.time);
        mEventName=(EditText) findViewById(R.id.eventName);
        mNrParticipants=(EditText)findViewById(R.id.numberOfPart);
        mDescription=(EditText)findViewById(R.id.description);
        eventIcon=(Spinner) findViewById(R.id.eventIcon);
        sendEvent=findViewById(R.id.done);

        userID=fAuth.getCurrentUser().getUid();

        CalendarView calenderView = (CalendarView) findViewById(R.id.calendar);
        mydate=(TextView)findViewById(R.id.textView9);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,Calendar.getInstance().getActualMinimum(Calendar.HOUR_OF_DAY));
        long currentdate = calendar.getTime().getTime();
        Log.d(TAG,"current date is =>"+currentdate);
        calenderView.setMinDate(currentdate);

        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = i2 + "/" + (i1+1) + "/" +i;
                mydate.setText(date);
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date1 = null;
                try {
                    date1 = (Date)formatter.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Selected date is => "+date1.getTime());

                long datelong = date1.getTime()/1000;
                datestring=Long.toString(datelong);
            }
        });
        eventIcon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==0){
                    icon="Sport";
                }
                else if(position==1){
                    icon="Party";
                }
                else if(position==2){
                    icon="Movie";
                }
                else if(position==3){
                    icon="Picnic";
                }
                else if(position==4){
                    icon="Food";
                }
                else if(position==5){
                    icon="Contest";
                }
                else if(position==6){
                    icon="Circus";
                }
                else if(position==7){
                    icon="Fair";
                }
                else if(position==8){
                    icon="Others";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    icon="Sport";
            }
        });
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventInfo.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        tHour = i;
                        tMinute = i1;

                        String time = tHour + ":" + tMinute;
                        timestring=time;
                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");

                        try{
                            Date date = f24Hours.parse(time);
                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                            timeBtn.setText(f12Hours.format(date));
                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                    }
                },12,0,false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                timePickerDialog.updateTime(tHour,tMinute);
                timePickerDialog.show();
            }
        });
        sendEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = mEventName.getText().toString();
                String description = mDescription.getText().toString();
                String xlongstring = String.valueOf(xlong);
                String ylatstring = String.valueOf(ylat);
                String nrParticipants = mNrParticipants.getText().toString().trim();

                if(TextUtils.isEmpty(eventName)){
                    mEventName.setError("Name is Required!");
                    return;
                }
                if(TextUtils.isEmpty(description)){
                    mDescription.setError("Description is Required");
                    return;
                }
                if(TextUtils.isEmpty(nrParticipants)){
                    mNrParticipants.setError("Number of Participants is Required");
                    return;
                }
                if(description.length() > 60){
                    mDescription.setError("Description is too long");
                    return;
                }
                CollectionReference dbEvents = fStore.collection("events");
                Event event = new Event(
                        eventName,
                        description,
                        nrParticipants,
                        timestring,
                        datestring,
                        xlong,
                        ylat,
                        userID,
                        icon
                );
                dbEvents.add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(AddEventInfo.this,"Event Added",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),MapsActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddEventInfo.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}