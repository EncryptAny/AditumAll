package com.a1403.aditumall;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by Cory on 1/22/2017.
 */

public class GeofenceNotify extends IntentService{
    public static final String TAG = "GeofenceService";

    public GeofenceNotify() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Can extract event and handle
        GeofencingEvent event  = GeofencingEvent.fromIntent(intent);
        if(event.hasError()) {
            // TODO: Handle error
        } else {
            int transition = event.getGeofenceTransition();
            List<Geofence> geofences = event.getTriggeringGeofences();
            Geofence geofence = geofences.get(0);
            String requestId = geofence.getRequestId();

            // event handling
            if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.d(TAG, "Entering geofence - " + requestId);
            } else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.d(TAG, "Exiting geofence - " + requestId);
            }
        }
    }
}
