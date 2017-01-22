package com.a1403.aditumall;

import android.app.PendingIntent;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.support.v7.app.AppCompatActivity;
import com.a1403.aditumall.model.AccessibilityPoint;
import com.a1403.aditumall.model.DummyData;
import com.a1403.aditumall.model.Reliability;
import com.a1403.aditumall.model.Venue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements VenueDetailListFragment.OnListFragmentInteractionListener {
    public static FragmentManager fragmentManager;
    private final int ADD_INFO_CODE = 1;
    private final int ADD_VENUE_CODE = 2;
    private Venue active_Venue = DummyData.getDummyVenue_Libwest();
    GoogleApiClient googleApiClient = null;
    private double lat = 0;
    private double longt = 0;
    private String geoFenceId;
    MapsActivity mapFragment;
    Venue tempVenue;
    public static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geoFenceId = getIntent().getStringExtra("geoFenceId");
        Log.d(TAG, "geofence ID is " + geoFenceId);
        if(findViewById(R.id.contentContainer) != null){
            if(savedInstanceState != null){
                return;
            }
            //creating map fragment to be placed in activity layout
            mapFragment = new MapsActivity();

            //If the activity is started with special instructions from an Intent,
            //pass the intent's extra's to the fragment as arguments
            mapFragment.setArguments(getIntent().getExtras());

            tempVenue = DummyData.getDummyVenue_Libwest();
            //add the fragment to the content_my framelayout
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer,mapFragment).commit();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Log.d(TAG, "Connected to GoogleApiClient");

                            startLocationMonitoring();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            Log.d(TAG, "Suspended connection to GoogleApiClient");
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.d(TAG, "Failed to connect to GoogleApiClient - " + result.getErrorMessage());
                        }
                    }).build();
        }else{

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        geoFenceId = getIntent().getStringExtra("geoFenceId");
        Log.d(TAG, "geofence ID is " + geoFenceId);
        tempVenue = DummyData.getDummyVenue_Libwest();
        int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (response != ConnectionResult.SUCCESS) {
            Log.d(TAG, "Google Play Services not available - show dialog to ask user to download it");
            GoogleApiAvailability.getInstance().getErrorDialog(this, response, 1).show();
        } else {
        }
    }

    // Controlling running location services in background
    @Override
    protected void onStart() {
        Log.d(TAG, "onStart called");
        super.onStart();
        geoFenceId = getIntent().getStringExtra("geoFenceId");
        Log.d(TAG, "geofence ID is " + geoFenceId);
        googleApiClient.reconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.fullMapButton:
                // search action
                SwitchToMap();
                return true;
            case R.id.addInfoButton:
                switchToAddInfo();
                return true;
            case R.id.addVenueButton:
                switchToAddVenue();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Venue getActive_Venue() {
        return active_Venue;
    }

    public void setActive_Venue(Venue active_Venue) {
        this.active_Venue = active_Venue;
    }

    /**
     * Launching new activity
     * */
    private void SwitchToMap() {
        Intent i = new Intent(MainActivity.this, FullMap.class);
        startActivity(i);
    }
    private void switchToAddInfo(){
        Intent i = new Intent(MainActivity.this, AddAccessibilityInfoActivity.class);
        if(tempVenue.getEpiPen() == null){
            i.putExtra(tempVenue.HAS_EPI_PENS,false);
        }else{
            i.putExtra(tempVenue.HAS_EPI_PENS,true);
        }
        if(tempVenue.getAed() == null){
            i.putExtra(tempVenue.HAS_AED,false);
        }else{
            i.putExtra(tempVenue.HAS_AED,true);
        }
        if(tempVenue.getBathrooms() == null){
            i.putExtra(tempVenue.HAS_BATHROOMS,false);
        }else{
            i.putExtra(tempVenue.HAS_BATHROOMS,true);
        }
        startActivityForResult(i,ADD_INFO_CODE);
    }
    private void switchToAddVenue() {
        Intent i = new Intent(MainActivity.this, AddVenueMap.class);
        startActivityForResult(i,ADD_VENUE_CODE);
    }
    @Override
    public void onSelectedAP(AccessibilityPoint ap) {
        // be helpful here
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Returned from activity");
        // Check which request we're responding to
        if (requestCode == ADD_INFO_CODE) {
            Log.d(TAG, "correct code");
            // Make sure the request was successful
            if (data != null) {
                Log.d(TAG, "Returned ok from activity");
                if (data.getBooleanExtra(tempVenue.HAS_EPI_PENS, false)) {
                    tempVenue.setEpiPen(new Reliability(0, 0));
                    Log.d(TAG, "EPI PEN ADDED.");
                }
                if (data.getBooleanExtra(tempVenue.HAS_AED, false)) {
                    tempVenue.setAed(new Reliability(0, 0));
                }
                if (data.getBooleanExtra(tempVenue.HAS_BATHROOMS, false)) {
                    tempVenue.setBathrooms(new Reliability(0, 0));
                }
                if (data.getBooleanExtra("parking", false)) {
                    //insert network call
                    Log.d(TAG, "parking working");
                }
                if (data.getBooleanExtra("ramp", false)) {
                    //insert networkcall
                    Log.d(TAG, "ramps working");
                }
                if (data.getBooleanExtra("doors", false)) {
                    //network call
                    Log.d(TAG, "doors working");
                }
            }
        } else if (requestCode == ADD_VENUE_CODE) {
            Log.d(TAG, "venue code");
            if (data != null) {
                Log.d(TAG, "intent was not null and name was " + data.getStringExtra("venue name"));
                Log.d(TAG, "longitude " + data.getDoubleExtra("longitude from marker",0));
                Log.d(TAG, "latitude " + data.getDoubleExtra("latitude from marker",0));
                Log.d(TAG, "radius " + data.getIntExtra("geoRadius",0));
                data.getStringExtra("venue name");
                addGeofence(data.getStringExtra("venue name"),data.getDoubleExtra("latitude from marker",0),data.getDoubleExtra("longitude from marker",0),data.getIntExtra("geoRadius",0),googleApiClient);
            }
        }
    }
    private void startLocationMonitoring() {
        // Defines properties around getting location updates (location req params)
        Log.d(TAG, "startLocation called");
        try {

            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(1000) // rate of updates
                    .setFastestInterval(100) // maximum rate of updates triggered by other apps
                    // .setNumUpdates(5) // can specify the number of updates to get (not needed)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Suggests accuracy (RIP battery)
            // Ask for location updates
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                    locationRequest, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.d(TAG, "Location updated lat/long " +
                                    location.getLatitude() + " " + location.getLongitude());
                        }
                    });
        } catch (SecurityException e) {
            Log.d(TAG, "SecurityException - " + e.getMessage());
        }
    }
    public void addGeofence(
            String geofenceId, double lat, double lon, float rad, GoogleApiClient googleApiClient) {
        Log.d(TAG, "startMonitoring called");
        try {
            // googleApiClient.connect();
            Log.d(TAG, "try to start");
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(geofenceId)
                    .setCircularRegion(lat,lon,rad) // lat, long, radius
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setNotificationResponsiveness(350) // time in ms to respond to event
                    // Events that raise actions
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();

            // Request object
            // Video mentions a variation that allows multiple geos grouped into one request
            GeofencingRequest geofenceRequest = new GeofencingRequest.Builder()
                    // If the device is already in geofence, this will cause entry transition to fire
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofence(geofence).build();

            // Need to instantiate intent and set it up as pending intent for future use.
            Intent intent = new Intent(this, GeofenceNotify.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (!googleApiClient.isConnected()) {
                Log.d(TAG, "GoogleApiClient is not connected");
            } else {
                LocationServices.GeofencingApi.addGeofences(googleApiClient, geofenceRequest, pendingIntent)
                        .setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    Log.d(TAG, "Successfully added geofence");
                                } else {
                                    Log.d(TAG, "Failed to add geofence + " + status.getStatus());
                                }
                            }
                        });
            }
        } catch (SecurityException e) {
            Log.d(TAG, "SecurityException - " + e.getMessage());
        }
    }

}

