package com.a1403.aditumall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Cory on 1/22/2017.
 */

public class StartupHandler extends BroadcastReceiver {
    public static final String TAG = "StartupHandler";

    private GoogleApiClient googleApiClient = null;

    //TODO: Get nearby venues from server

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, GeofenceNotify.class);
        context.startService(startServiceIntent);
        googleApiClient = new GoogleApiClient.Builder(context)
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
}