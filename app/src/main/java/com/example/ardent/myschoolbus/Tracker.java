package com.example.ardent.myschoolbus;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tracker extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback{
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private GoogleMap mMap;
    MarkerOptions mo;
    Marker marker;
    LocationManager locationManager;
    private Button buttonTrackerhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mo = new MarkerOptions().position(new LatLng(0, 0)).title("My Current Location");
//        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
//            requestPermissions(PERMISSIONS, PERMISSION_ALL);
//        } else requestLocation();
//        if (!isLocationEnabled())
//            showAlert(1);

        buttonTrackerhome = (Button)findViewById(R.id.buttonTrackerHome);
        buttonTrackerhome.setOnClickListener(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //marker = mMap.addMarker(mo);
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
//        marker.setPosition(myCoordinates);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myCoordinates,10));
//    }
//
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//
//    }

//    private void requestLocation() {
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setPowerRequirement(Criteria.POWER_HIGH);
//        String provider = locationManager.getBestProvider(criteria, true);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(provider, 0, 0, this);
//    }
//    private boolean isLocationEnabled() {
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private boolean isPermissionGranted() {
//        if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED || checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            Log.v("mylog", "Permission is granted");
//            return true;
//        } else {
//            Log.v("mylog", "Permission not granted");
//            return false;
//        }
//    }
//    private void showAlert(final int status) {
//        String message, title, btnText;
//        if (status == 1) {
//            message = "Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
//                    "use this app";
//            title = "Enable Location";
//            btnText = "Location Settings";
//        } else {
//            message = "Please allow this app to access location!";
//            title = "Permission access";
//            btnText = "Grant";
//        }
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setCancelable(false);
//        dialog.setTitle(title)
//                .setMessage(message)
//                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.M)
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        if (status == 1) {
//                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(myIntent);
//                        } else
//                            requestPermissions(PERMISSIONS, PERMISSION_ALL);
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        finish();
//                    }
//                });
//        dialog.show();
//    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("DriverLocation");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference1 = firebaseDatabase.getReference().child("Details").child(user.getUid()).child("Location");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //LatLng latLng= dataSnapshot.getValue(LatLng.class);
                Locaten locaten = dataSnapshot.getValue(Locaten.class);
                //LatLng latLng = null;
                if (locaten != null) {
                    LatLng latLng = new LatLng(locaten.lat, locaten.lont);
//                    Log.d("VERMA", "onDataChange: " );
                    Toast.makeText(Tracker.this, "hi", Toast.LENGTH_SHORT).show();
//                    marker = mMap.addMarker(mo);
                    marker.setPosition(latLng);
                        Marker melbourne = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    CameraUpdateFactory.zoomIn();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("SHREEYA", "onCancelled: ");
                Toast.makeText(Tracker.this, "db error.", Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //LatLng latLng= dataSnapshot.getValue(LatLng.class);
                Locaten locaten = dataSnapshot.getValue(Locaten.class);
                //LatLng latLng = null;
                if (locaten != null) {
                    LatLng latLng = new LatLng(locaten.lat, locaten.lont);
                    Log.d("VERMA", "onDataChange: " );
                    Toast.makeText(Tracker.this, "hi", Toast.LENGTH_SHORT).show();
//                    marker = mMap.addMarker(mo);
//                    marker.setPosition(latLng);
                    Marker melbourne = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("SHREEYA", "onCancelled: ");
                Toast.makeText(Tracker.this, "db error.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==buttonTrackerhome){
            finish();
            startActivity(new Intent(getApplicationContext(),Home.class));
        }
    }
}