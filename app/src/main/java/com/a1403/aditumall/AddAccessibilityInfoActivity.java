package com.a1403.aditumall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.a1403.aditumall.adapters.AccessibilityPointRecyclerViewAdapter;
import com.a1403.aditumall.adapters.AddInfoOptionsViewAdapter;
import com.a1403.aditumall.model.AccessibilityPoint;

import java.util.ArrayList;
import java.util.List;

public class AddAccessibilityInfoActivity extends AppCompatActivity {
        private static final String TAG = "RecyclerViewExample";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_accessibility_info);

            Intent intent = getIntent();
            boolean value =false;

            View i1 = findViewById(R.id.list_item_1);
            TextView accessName1 = (TextView) i1.findViewById(R.id.accessName);
            if(!intent.getBooleanExtra("has epi pens",false)){
                accessName1.setEnabled(false);
            }
            i1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clicked element 1");
                    Intent returnedIntent = new Intent();
                    returnedIntent.putExtra("has epi pens", true);
                    setResult(1,returnedIntent);
                    finish();
                }
            });

            accessName1.setText("Has Epi Pens");

            View i2 = findViewById(R.id.list_item_2);
            TextView accessName2 = (TextView) i2.findViewById(R.id.accessName);
            if(!intent.getBooleanExtra("has aed",false)){
                accessName2.setEnabled(false);
            }
            i2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnedIntent = new Intent();
                    returnedIntent.putExtra("has aed", true);
                    setResult(1,returnedIntent);
                    finish();
                }
            });
            accessName2.setText("Has AED");

            View i3 = findViewById(R.id.list_item_3);
            TextView accessName3 = (TextView) i3.findViewById(R.id.accessName);
            if(!intent.getBooleanExtra("has bathrooms",false)){
                accessName3.setEnabled(false);
            }
            i3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnedIntent = new Intent();
                    returnedIntent.putExtra("has bathrooms", true);
                    setResult(1,returnedIntent);
                    finish();
                }
            });
            accessName3.setText("Has Bathrooms");

            View i4 = findViewById(R.id.list_item_4);
            TextView accessName4 = (TextView) i4.findViewById(R.id.accessName);
            i4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent forwardIntent = new Intent();
                    forwardIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(forwardIntent);
                    finish();
                }
            });
            accessName4.setText("Has Disabled Parking");

        }


}