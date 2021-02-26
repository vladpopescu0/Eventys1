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

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import javax.xml.transform.Result;

public class Scanner extends AppCompatActivity {
    final static String TAG="TAG";
    CodeScanner mCodeScanner;
    FirebaseFirestore fStore;
    TextView txt;
    String eID;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        fStore=FirebaseFirestore.getInstance();
        eID=getIntent().getStringExtra("EVENTID");
        back = findViewById(R.id.button4);
        txt=findViewById(R.id.textView4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(Scanner.this,UserProfile.class);
                startActivity(x);
            }
        });
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull com.google.zxing.Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(Scanner.this, result.getText(), Toast.LENGTH_SHORT).show();
                        DocumentReference docRef = fStore.collection("events").document(eID);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        Log.d(TAG,"#1#2#3##DocumentSnapshot data: "+document.getData());
                                        String allParticipants = (String)document.get("allParticipants");
                                        int isParticipant = allParticipants.indexOf(result.getText());
                                        if(isParticipant!=-1){
                                            txt.setText("User Accepted");
                                            Log.d(TAG,"ACCEPTED!!!############");
                                            //txt.setBackgroundResource(R.drawable.emerald_background);
                                        }else{
                                            txt.setText("User Not Registered");
                                            //txt.setBackgroundResource(R.drawable.red_background);
                                        }
                                    }else{
                                        Log.d(TAG,"#####No such document");
                                        txt.setText("Invalid Code");
                                    }
                                }else{
                                    Log.d(TAG,"####get failed with ",task.getException());
                                }
                            }
                        });
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}