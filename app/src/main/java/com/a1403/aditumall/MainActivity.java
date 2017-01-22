package com.a1403.aditumall;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import android.support.v7.app.AppCompatActivity;
import com.a1403.aditumall.model.AccessibilityPoint;

public class MainActivity extends AppCompatActivity implements VenueDetailListFragment.OnListFragmentInteractionListener {
    public static FragmentManager fragmentManager;
    MapsActivity mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.RECEIVE_BOOT_COMPLETED)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.RECEIVE_BOOT_COMPLETED},1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if(findViewById(R.id.contentContainer) != null){
            if(savedInstanceState != null){
                return;
            }
            //creating map fragment to be placed in activity layout
            mapFragment = new MapsActivity();

            //If the activity is started with special instructions from an Intent,
            //pass the intent's extra's to the fragment as arguments
            mapFragment.setArguments(getIntent().getExtras());

            //add the fragment to the content_my framelayout
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer,mapFragment).commit();


        }else{

        }
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
            case R.id.geofenceButton:
                SwitchToGeo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Launching new activity
     * */
    private void SwitchToMap() {
        Intent i = new Intent(MainActivity.this, FullMap.class);
        startActivity(i);
    }
    private void SwitchToGeo() {
        Intent i = new Intent(MainActivity.this, GeofencingEx.class);
        startActivity(i);
    }
    @Override
    public void onSelectedAP(AccessibilityPoint ap) {
        // be helpful here
    }
}

