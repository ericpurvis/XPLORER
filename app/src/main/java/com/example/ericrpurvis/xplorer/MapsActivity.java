package com.example.ericrpurvis.xplorer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, LocationListener, OnInfoWindowClickListener {

    private static final String TAG = "MapsActivity";
    private DatabaseReference databaseReference;
    private GoogleMap map;

    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() was called");
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //location init
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager.requestLocationUpdates(provider, 3000, 1,this);
    }

    @Override
    public void onStart(){
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Locations");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot locations : dataSnapshot.getChildren()) {
                    com.example.ericrpurvis.xplorer.LatLng tempLatLng = locations.child("coord").getValue(com.example.ericrpurvis.xplorer.LatLng.class);
                    LatLng mapsLatLng =
                            new LatLng(tempLatLng.getLatitude(),
                                    tempLatLng.getLongitude());
                    map.addMarker(new MarkerOptions()
                            .position(mapsLatLng)
                            .title(locations.child("name").getValue().toString())
                            .snippet("Type: " + locations.child("actType").getValue().toString() +
                                    "\n" + locations.child("desc").getValue().toString()))
                            .setTag(locations.getKey());


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        map = googleMap;
        if(map != null){
            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
                @Override
                public View getInfoWindow(Marker arg0){
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker){

                    View v = getLayoutInflater().inflate(R.layout.info_window, null);
                    TextView tvTitle = (TextView) v.findViewById(R.id.title);
                    TextView tvDescription = (TextView) v.findViewById(R.id.description);

                    tvTitle.setText(marker.getTitle());
                    tvDescription.setText(marker.getSnippet());

                    return v;
                }
            });
            map.setOnInfoWindowClickListener(this);
        }
        Location loc = locationManager.getLastKnownLocation(provider);
        if (loc != null) {
            LatLng currentLocation = new LatLng(loc.getLatitude(), loc.getLongitude());

            googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        } else {
            Toast.makeText(this, "Make sure that your location services are on.", Toast.LENGTH_LONG).show();
        }
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

    @Override
    public void onInfoWindowClick(Marker marker) {
//        Toast.makeText(this, marker.getTitle().toString() + " info window clicked",
//                Toast.LENGTH_LONG).show();

        if(marker.getTitle().equals("Current Location")){
            startActivity(new Intent(this, PostLocationActivity.class));
        }
        else{
            Intent intent = new Intent(this, ViewLocationActivity.class);
            intent.putExtra("ID", marker.getTag().toString());
            startActivity(intent);
        }

    }

}

