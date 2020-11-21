package com.example.eventys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {
    public static final String TAG="TAG";
    TextView fullName,logout;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    Button dashboardBtn,goToMap;
    String userID;

@Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_profile);

    fullName = (TextView) findViewById(R.id.fullName);
    fStore = FirebaseFirestore.getInstance();
    fAuth = FirebaseAuth.getInstance();
    goToMap=findViewById(R.id.button);
    logout = (TextView) findViewById(R.id.logout);
    dashboardBtn=findViewById(R.id.dashboard);

    userID = fAuth.getCurrentUser().getUid();

    DocumentReference docRef = fStore.collection("users").document(userID);
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            Log.d(TAG, "If working show =>" + task.getResult());
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                String name = document.getString("fName");
                fullName.setText("Welcome, "+name+ " !");
            } else {
                Log.d(TAG, "get failed with", task.getException());
            }
        }
    });
    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
    });
    goToMap.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            finish();
        }
    });
    dashboardBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
        }
    });


}
}