package com.a1403.aditumall;


import android.os.Bundle;
import android.widget.TextView;

import android.support.v7.app.AppCompatActivity;
import com.a1403.aditumall.model.AccessibilityPoint;

public class MainActivity extends AppCompatActivity implements VenueDetailListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @Override
    public void onSelectedAP(AccessibilityPoint ap) {
        // be helpful here
    }
}

