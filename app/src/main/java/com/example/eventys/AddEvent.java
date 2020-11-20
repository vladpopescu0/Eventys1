package com.example.eventys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddEvent extends AppCompatActivity {

    GoogleMap gMap;
    Button btnNext,back;
    FirebaseFirestore fStore;
    Double xlong,ylat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        btnNext=(Button)findViewById(R.id.buttonNext);
        back=(Button)findViewById(R.id.back);
        fStore = FirebaseFirestore.getInstance();

        SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this::onMapReady);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }
        });

    }

    public void onMapReady(GoogleMap googleMap){
        gMap = googleMap;

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Your event's location");
                gMap.clear();
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                gMap.addMarker(markerOptions);
                xlong=latLng.longitude;
                ylat=latLng.latitude;
                btnNext.setVisibility(View.VISIBLE);
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AddEvent.this, AddEventInfo.class);
                        intent.putExtra("LONGITUDE",xlong);
                        intent.putExtra("LATITUDE",ylat);
                        startActivity(intent);
                    }
                });
            }
        });
    }

}