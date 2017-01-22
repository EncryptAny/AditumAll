package com.a1403.aditumall;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.security.Security;
import java.util.ArrayList;

public class GeofencingEx extends AppCompatActivity implements GeofenceService.GeofenceListener {

    private double lat;
    private double longt;

    public static final String TAG = "GeofencingEx";
    public static final String GEOFENCE_ID = "MyGeofenceId";

    GoogleApiClient googleApiClient = null;

    private Button startGeofenceMonitoring;
    private Button stopGeofenceMonitoring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofencing_ex);

        startGeofenceMonitoring = (Button) findViewById(R.id.startGeofenceMonitoring);
        startGeofenceMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGeofenceMonitoring();
            }
        });

        stopGeofenceMonitoring = (Button) findViewById(R.id.stopGeofenceMonitoring);
        stopGeofenceMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGeofenceMonitoring();
            }
        });
        googleApiClient = new GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Log.d(TAG, "Connected to GoogleApiClient");
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

        // Thank you Dakota
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Toast.makeText(this,"TODO: LOCATION PERMISSION STATEMENT",Toast.LENGTH_LONG).show();

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);

                }
            }
        }

    }
    // 9:30, just making sure Play Services installed
    @Override
    protected void onResume() {
        Log.d(TAG, "onResume called");
        super.onResume();

        int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (response != ConnectionResult.SUCCESS) {
            Log.d(TAG, "Google Play Services not available - show dialog to ask user to download it");
            GoogleApiAvailability.getInstance().getErrorDialog(this, response, 1).show();
        } else {
            Log.d(TAG, "Google Play Services is available - no action required");
        }
    }
    // Controlling running location services in background
    @Override
    protected void onStart() {
        Log.d(TAG, "onStart called");
        super.onStart();
        googleApiClient.reconnect();
        GeofenceService.registerGeofenceListener(this);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop called");
        //GeofenceService.unregisterGeofenceListener();
        super.onStop();
        //googleApiClient.disconnect();
    }

    private void startGeofenceMonitoring() {
        Log.d(TAG, "startMonitoring called");
        try {
            // googleApiClient.connect();

            Geofence geofence = new Geofence.Builder()
                    .setRequestId(GEOFENCE_ID)
                    .setCircularRegion(lat,longt,35) // lat, long, radius
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
            Intent intent = new Intent(this, GeofenceService.class);
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

    private void stopGeofenceMonitoring() {
        Log.d(TAG, "stopMonitoring called");
        ArrayList<String> geofenceIds = new ArrayList<String>();
        geofenceIds.add(GEOFENCE_ID);
        LocationServices.GeofencingApi.removeGeofences(googleApiClient, geofenceIds);
    }

    @Override
    public void onEnterSpace() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.map_pin)
                        .setContentTitle("Entered Geofence!")
                        .setContentText("Entered Geofence!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(13, mBuilder.build());
    }

    @Override
    public void onExitSpace() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.map_pin)
                        .setContentTitle("Exited Geofence!")
                        .setContentText("Exited Geofence!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(14, mBuilder.build());
    }
}
