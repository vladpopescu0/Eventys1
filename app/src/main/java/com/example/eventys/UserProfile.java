package com.example.eventys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {
    public static final String TAG="TAG";
    TextView fullName,userEmail,logout;
    FirebaseFirestore fStore;
FirebaseAuth fAuth;
String userID;

@Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_profile);

    fullName = (TextView) findViewById(R.id.fullName);
    userEmail = (TextView) findViewById(R.id.userEmail);
    fStore = FirebaseFirestore.getInstance();
    fAuth = FirebaseAuth.getInstance();
    logout = (TextView) findViewById(R.id.logout);

    userID = fAuth.getCurrentUser().getUid();
    DocumentReference docRef = fStore.collection("users").document(userID);
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            Log.d(TAG, "If working show =>" + task.getResult());
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                String name = document.getString("fName");
                String email = document.getString("email");
                fullName.setText(name);
                userEmail.setText(email);
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

}
}