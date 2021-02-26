package com.example.eventys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;

import java.text.ParseException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrCodeGenerator extends AppCompatActivity {
    final static String TAG="TAG";
    ImageView qrImage;
    Button btn;
    //FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_generator);
        btn=findViewById(R.id.button2);
        qrImage=findViewById(R.id.imageView3);
        fAuth=FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        QRGEncoder qrgEncoder = new QRGEncoder(userID, null, QRGContents.Type.TEXT,1000);
        Bitmap qrBits = qrgEncoder.getBitmap();
        qrImage.setImageBitmap(qrBits);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UserProfile.class));
                finish();
            }
        });
    }
}