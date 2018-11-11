package com.example.haifaalmeshari.coursefinder.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haifaalmeshari.coursefinder.App.AppConfig;
import com.example.haifaalmeshari.coursefinder.Models.Course;
import com.example.haifaalmeshari.coursefinder.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Organization extends AppCompatActivity implements OnMapReadyCallback {

    // Views
    @BindView(R.id.title_editText)
    EditText titleTxt;
    @BindView(R.id.desc_editText2)
    EditText descTxt;
    @BindView(R.id.date_editText3)
    EditText dateTxt;
    @BindView(R.id.time_editText4)
    EditText timeTxt;
    @BindView(R.id.price_editText5)
    EditText priceTxt;
    // vars
    private String cityKey;
    private String title, desc, date, time, price, lat, lon;
    boolean isValid = false;
    // Firebase
    private DatabaseReference mRef;
    // MapModule
//    @BindView(R.id.map)
//    MapView mapView;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final float DEFAULT_ZOOM = 15f;
    private boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Marker marker;

    // OnClicks
    @OnClick(R.id.confirm_button2)
    void goToMain() {
        CheckValidation();
        if (isValid)
            sendData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            getLocationPermission();
            init();
            cityKey = intent.getStringExtra(AppConfig.INTENT_CITY_KEY);
        } else {
            closeOnError();
        }
    }

    private void init() {
        // Firebase
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    private void sendData() {
        String key = mRef.
                child(AppConfig.COURSES)
                .child(AppConfig.CITY)
                .child(cityKey)
                .push().getKey();
        final Course course = new Course(key, title, desc, date, time, price, lat, lon);
        mRef.child(AppConfig.COURSES)
                .child(AppConfig.CITY)
                .child(cityKey)
                .child(key)
                .setValue(course)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Organization.this, getString(R.string.course_added_successfully), Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Organization.this, getString(R.string.error_adding_course), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void CheckValidation() {
        boolean titleFlag, descFlag, dateFlag, timeFlag, priceFlag;
        titleFlag = validateInput(titleTxt);
        descFlag = validateInput(titleTxt);
        dateFlag = validateInput(titleTxt);
        timeFlag = validateInput(titleTxt);
        priceFlag = validateInput(titleTxt);
        if (titleFlag && descFlag && dateFlag && timeFlag && priceFlag && !lat.isEmpty()) {
            title = getInput(titleTxt);
            desc = getInput(descTxt);
            date = getInput(dateTxt);
            time = getInput(timeTxt);
            price = getInput(priceTxt);
            isValid = true;
        } else {
            if (lat == null)
                Toast.makeText(this, getString(R.string.empty_location), Toast.LENGTH_LONG).show();
            isValid = false;
            Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateInput(EditText textInputEditText) {
        String text = getInput(textInputEditText);
        return !text.trim().isEmpty();
    }

    private String getInput(EditText editText) {
        return editText.getText().toString();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true); //to get blue marker with GPS icon
            mMap.getUiSettings().setMyLocationButtonEnabled(false); //to hide GPS icon
            mMap.getUiSettings().setZoomControlsEnabled(true);
            // Assem
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (marker != null) { //if marker exists (not null or whatever)
                        marker.setPosition(latLng);
                    } else {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("test")
                                .draggable(true));
                        lat = String.valueOf(latLng.latitude);
                        lon = String.valueOf(latLng.longitude);
                    }
                }
            });
        }
    }


    private void initMAP() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                Log.e("rrrrrr", "rrrr");
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location current_location = (Location) task.getResult();
                            try {
                                moveCamera(new LatLng(current_location.getLatitude(), current_location.getLongitude()), DEFAULT_ZOOM);
                            } catch (Exception e) {
                                onBackPressed();
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                Toast.makeText(Organization.this, "Enable location permissions", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Organization.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Log.e("rrrrrr", "ffff");
            }
        } catch (SecurityException e) {
            Log.e("error", e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMAP();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //initialize our map
                    initMAP();
                }
            }

        }
    }
}
