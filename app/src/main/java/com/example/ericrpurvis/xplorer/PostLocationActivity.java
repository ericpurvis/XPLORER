package com.example.ericrpurvis.xplorer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

public class PostLocationActivity extends AppCompatActivity implements OnClickListener, OnItemSelectedListener, LocationListener {

    private DatabaseReference databaseReference;
    private EditText editTextName, editTextDesc;
    private Spinner editActType;
    private Button buttonPost;

    private LocationManager locationManager;
    private String provider;


    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_location);


        //db init
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Locations");

        //edit text/spinner init
        editTextName = (EditText) findViewById(R.id.name);

        editActType = (Spinner) findViewById(R.id.actType);
        editActType.setOnItemSelectedListener(this);

        editTextDesc = (EditText) findViewById(R.id.description);

        buttonPost = (Button) findViewById(R.id.buttonPost);
        buttonPost.setOnClickListener(this);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //location init
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager.requestLocationUpdates(provider, 3000, 1,this);


    }


    private void getInformation() {

        String name = editTextName.getText().toString().trim();
        String actType = editActType.getSelectedItem().toString().trim();
        String desc = editTextDesc.getText().toString().trim();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        Location loc = locationManager.getLastKnownLocation(provider);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (loc != null) {
            LocationPost locationPost = new LocationPost(name, desc, actType, loc.getLatitude(), loc.getLongitude(), user);

            String key = databaseReference.push().getKey();

            databaseReference.child(key).setValue(locationPost);

            Toast.makeText(this, "Location Saved", Toast.LENGTH_LONG).show();

            //Boots the user backg to the menu page
            startActivity(new Intent(this, ProfileActivity.class));
        } else {
            Toast.makeText(this, "Location is null, sorry", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean isConnected = netInfo != null && netInfo.isConnectedOrConnecting();
        if(isConnected) {
            if (view == buttonPost) {
                getInformation();
            }
        }
        else{
            Toast.makeText(this, "Re-Establish Network Connection.", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(PostLocationActivity.this,parent.getSelectedItem().toString()+" Selected",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}
