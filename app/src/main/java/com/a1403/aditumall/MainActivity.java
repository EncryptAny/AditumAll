package com.a1403.aditumall;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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

