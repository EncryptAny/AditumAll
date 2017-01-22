package com.a1403.aditumall;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddVenueMap extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,SeekBar.OnSeekBarChangeListener {

    private GoogleMap mMap;
    private GoogleApiClient locationApi;
    private LocationRequest mLocationRequest;
    SupportMapFragment mapFragment;

    private double longitude;
    private double latitude;
    Location mLastLocation;

    SeekBar seekBar1;

    private int radius;

    CircleOptions currentCircle;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    public static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (findViewById(R.id.venue_map_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            //creating map fragment to be placed in activity layout
            mapFragment = new SupportMapFragment();

            //If the activity is started with special instructions from an Intent,
            //pass the intent's extra's to the fragment as arguments
            mapFragment.setArguments(getIntent().getExtras());

            //add the fragment to the content_my framelayout
            getSupportFragmentManager().beginTransaction().add(R.id.venue_map_container, mapFragment).commit();

            mapFragment.getMapAsync(this);

            currentCircle = new CircleOptions();

        } else {

        }

        seekBar1=(SeekBar)findViewById(R.id.seekBar1);
        seekBar1.setOnSeekBarChangeListener(this);
        seekBar1.setProgress(20);

        final Button button = (Button) findViewById(R.id.venue_done_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                EditText venueName = (EditText) findViewById(R.id.edit_venue_name);

                Intent returnedIntent = new Intent();
                returnedIntent.putExtra("venue name", venueName.getText().toString());
                returnedIntent.putExtra("geoRadius", radius);
                returnedIntent.putExtra("longitude from marker", longitude);
                returnedIntent.putExtra("latitude from marker", latitude);
                setResult(2,returnedIntent);
                finish();
            }
        });


        locationApi = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private LatLng getPastLocation() {
        SharedPreferences sp = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String returnedLongitude = sp.getString("pastLongitude", "0");
        String returnedLatitude = sp.getString("pastLatitude", "0");

        LatLng returnedLatLng = new LatLng(Double.valueOf(returnedLatitude), Double.valueOf(returnedLongitude));

        return returnedLatLng;
    }

    private void savePastLocation() {
        SharedPreferences sp = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Toast.makeText(this, "TODO: LOCATION PERMISSION STATEMENT", Toast.LENGTH_LONG).show();

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);

                }
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(locationApi, mLocationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(locationApi);
        String longitude = Double.toString(mLastLocation.getLongitude());
        String latitude = Double.toString(mLastLocation.getLatitude());

        editor.putString("pastLatitude", longitude);
        editor.putString("pastLongitude", latitude);

        editor.apply();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng pastLocation = getPastLocation();
        Log.d(TAG, "map was called ready");
        if (mMap != null) {
            if (!pastLocation.equals(new LatLng(0, 0))) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(pastLocation).zoom(17.0f).build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.moveCamera(cameraUpdate);
            }
            Log.d(TAG, "map was not null");

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {

                    Log.d(TAG, "Map was Clicked");
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(point));
                    longitude = point.longitude;
                    latitude = point.latitude;
                    mMap.addCircle(currentCircle.center(point).strokeColor(Color.BLACK).fillColor(Color.BLUE).strokeWidth(2));

                }
            });

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Toast.makeText(this, "TODO: LOCATION PERMISSION STATEMENT", Toast.LENGTH_LONG).show();

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);

                }
            }
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(locationApi);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(locationApi, mLocationRequest, this);
        } else {
            handleNewLocation(location);
            mMap.addCircle(currentCircle.center(new LatLng(location.getLatitude(),location.getLongitude())).radius(35).strokeColor(Color.BLACK).fillColor(Color.BLUE).strokeWidth(2));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            //Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        locationApi.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationApi.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(locationApi, this);
            locationApi.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void handleNewLocation(Location location) {


        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        latitude = currentLatitude;
        longitude = currentLongitude;
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        if (mMap != null) {
            mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        TextView radiusText = (TextView) findViewById(R.id.radiusText);
        radiusText.setText("Radius: " + Integer.toString(progress));
        Log.d(TAG, Double.toString(getPastLocation().latitude));
        radius = progress;
        currentCircle.radius(progress);
        //mMap.addCircle(new CircleOptions().center(getPastLocation()).radius(progress).strokeColor(Color.RED).fillColor(Color.BLUE));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
