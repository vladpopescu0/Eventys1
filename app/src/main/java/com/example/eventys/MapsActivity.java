package com.example.eventys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String TAG = "TAG";
    private GoogleMap mMap;
    Button addEventBtn,eventInfo,profile;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;

    String userID;

    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);

        fStore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        addEventBtn=(Button) findViewById(R.id.createEvent);
        eventInfo=(Button)findViewById(R.id.info);
        profile=(Button)findViewById(R.id.profile);

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddEvent();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapsActivity.this,UserProfile.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fStore.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Event event = document.toObject(Event.class);
                                LatLng sydney = new LatLng(event.ylat,event.xlong);
                                if(event.icon.equals("Sport")){
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(event.name1).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_sports_basketball_24)));
                                }
                                if(event.icon.equals("Movie")){
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(event.name1).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_local_movies_24)));
                                }if(event.icon.equals("Party")){
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(event.name1).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_emoji_people_24)));
                                }if(event.icon.equals("Picnic")){
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(event.name1).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_shopping_basket_24)));
                                }if(event.icon.equals("Food")){
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(event.name1).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_fastfood_24)));
                                }if(event.icon.equals("Contest")){
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(event.name1).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_create_24)));
                                }if(event.icon.equals("Fair")){
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(event.name1).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_home_work_24)));
                                }
                                if(event.icon.equals("Others")){
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(event.name1).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_dynamic_feed_24)));
                                }
                                if(event.icon.equals("Circus")){
                                    mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(event.name1).icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_baseline_sports_kabaddi_24)));
                                }
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                Log.d(TAG,  "Test213 => " + event.date);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marker.getTitle();
                marker.showInfoWindow();

                fStore.collection("events")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> boss) {
                                if (boss.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : boss.getResult()) {

                                        Event event = document.toObject(Event.class);

                                        Log.d(TAG,"TEST 0 => " + event.getName1() +" si "+ title);
                                        if(title.equals(event.getName1())){
                                            eventInfo.setVisibility(View.VISIBLE);
                                            LatLng zoomare = new LatLng(event.ylat , event.xlong);
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoomare, 15));
                                            eventInfo.setOnClickListener((View view) -> {
                                                String mTitle = event.getName1();
                                                String mDescription = event.getDescription();
                                                String mTime = event.getTime();
                                                String mDate = event.getDate();
                                                String eID = document.getId();
                                                String mParticipants = event.getNrParticipants();
                                                String mIcon = event.getIcon();
                                                Intent i = new Intent(MapsActivity.this,MarkerDetails.class);
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
                                                eventInfo.setVisibility(View.GONE);
                                                startActivity(i);
                                            });
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", boss.getException());
                                }
                            }
                        });
                //Log.d(TAG,"test_ceva_counter =>" + ceva);
                return true;
                //Log.d(TAG,  "Testclick => " + marker.getPosition() +" "+mTime);
            }
        });

//        CollectionReference collectionReference = fStore.collection("events");
//        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//            if(task.isSuccessful()){
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                } else {
//                    Log.d(TAG, "No such document");
//                }
//            } else {
//                Log.d(TAG, "get failed with ", task.getException());
//            }
//            }
//            }
//        });


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            enableUserLocation();
            zoomToUserLocation();
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
        }
    }

    private void enableUserLocation(){
        mMap.setMyLocationEnabled(true);
    }
    private void zoomToUserLocation(){
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(locationTask.isSuccessful() && locationTask.getResult()!=null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 60));
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ACCESS_LOCATION_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                enableUserLocation();
                zoomToUserLocation();
            }else{

            }
        }

    }
    public void openAddEvent(){
        Intent x = new Intent(this, AddEvent.class);
        startActivity(x);
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}