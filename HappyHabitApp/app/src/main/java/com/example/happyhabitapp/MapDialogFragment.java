package com.example.happyhabitapp;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback {
    private View view; // The fragments overall layout
    private GoogleMap gMap;
    private MapView mView;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private int permissionGranted;
    private LatLng latLng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_view, container, false);
        permissionGranted = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setTitle("Location");
                        alertDialog.setMessage("Location is needed if you would like to add it to your habit event, If denied, location will be disabled");

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"allow",new DialogInterface.OnClickListener(){

                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("test", String.valueOf(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)));
                                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"deny",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //disable location add button for habit event
                                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        });
                        alertDialog.show();
                    }});
        if (permissionGranted != PERMISSION_GRANTED){
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);


    }
}
