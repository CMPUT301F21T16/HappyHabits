package com.example.happyhabitapp;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private LatLng latlng;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button setLocationButton;
    private Boolean edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.map_view);
        Bundle bundle = getIntent().getExtras();
        latlng = bundle.getParcelable("latlng");
        edit = bundle.getBoolean("edit");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void onMapReady(GoogleMap googleMap) {
        if (!edit) { // not editing so get current location from map
            getLastLocation(googleMap);
        }
        else if(edit){ // if editing place marker at previously chosen location
            Bundle bundle = getIntent().getExtras();
            latlng = bundle.getParcelable("latlng");
            addMarker(googleMap);
        }
    }
    public void addMarker(GoogleMap googleMap){
        googleMap.addMarker(new MarkerOptions()
                .draggable(true)
                .position(latlng)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                latlng = marker.getPosition();
                Log.d("testdrag", String.valueOf(latlng.latitude));
                Log.d("testdrag", String.valueOf(latlng.longitude));
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });
        setLocationButton = findViewById(R.id.set_location_btn);
        setLocationButton.setOnClickListener(new View.OnClickListener() { // return latlng from marker to the HabitEventFragment
            @Override
            public void onClick(View v) {
                Log.d("testlatlng", String.valueOf(latlng.latitude));
                Log.d("testlatlng", String.valueOf(latlng.longitude));
                Intent returnIntent = new Intent();
                returnIntent.putExtra("latlng",latlng);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation(GoogleMap googleMap) {
        Log.d("Test", "called");
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                latlng = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("Test","got location");
                addMarker(googleMap);
            }
            });
    }

}
