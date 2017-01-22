package com.a1403.aditumall;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.support.v7.app.AppCompatActivity;
import com.a1403.aditumall.model.AccessibilityPoint;
import com.a1403.aditumall.model.Reliability;
import com.a1403.aditumall.model.Venue;

public class MainActivity extends AppCompatActivity implements VenueDetailListFragment.OnListFragmentInteractionListener {
    public static FragmentManager fragmentManager;
    private final int ADD_INFO_CODE = 1;
    MapsActivity mapFragment;
    Venue tempVenue;
    public static final String TAG = MapsActivity.class.getSimpleName();

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

            tempVenue = new Venue("45abnfe", "test",null,new Reliability(3,3),new Reliability(4,5), new Reliability(5,5));
            //add the fragment to the content_my framelayout
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer,mapFragment).commit();


        }else{

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        tempVenue = new Venue("45abnfe", "test",null,new Reliability(3,3),new Reliability(4,5), new Reliability(5,5));

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
                switchToAdd();
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
        startActivityForResult(i,ADD_INFO_CODE);
    }
    private void switchToAdd(){
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
            if(data != null) {
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
                if(data.getBooleanExtra("parking",false)){
                    //insert network call
                    Log.d(TAG, "parking working");
                }
                if(data.getBooleanExtra("ramp",false)){
                    //insert networkcall
                    Log.d(TAG, "ramps working");
                }
                if(data.getBooleanExtra("doors",false)){
                    //network call
                    Log.d(TAG, "doors working");
                }
            }

        }
    }
}

