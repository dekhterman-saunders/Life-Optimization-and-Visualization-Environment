package com.example.WhatWhen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

public class WhatWhen extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_input_assignments:
                    startInputAssignments();
                    return true;
                case R.id.navigation_what_when:
                    return true;
                case R.id.navigation_open_calendar:
                    startOpenCalendar();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.what_when);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void startInputAssignments() {
        Intent inputAssignmentsIntent = new Intent(this, WhatWhen.class);
        startActivity(inputAssignmentsIntent);
    }
    public void startOpenCalendar() {
        Intent openCalendarIntent = new Intent(this, OpenCalendar.class);
        startActivity(openCalendarIntent);
    }
}
