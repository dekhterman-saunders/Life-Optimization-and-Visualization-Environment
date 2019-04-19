package com.example.whatwhen;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private int currentIndex;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.InputYourAssignments);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAssignment();
            }
        });
        final Button removeButton = findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAssignment();
            }
        });
    }
    public void addAssignment() {
        EditText course = findViewById(R.id.courseInput);
        EditText assignment = findViewById(R.id.assignmentInput);
        EditText hrs = findViewById(R.id.hrsInput);
        EditText mins = findViewById(R.id.minsInput);
        String setCourse = course.getText().toString();
        String setName = assignment.getText().toString();
        int setHrs = Integer.parseInt(hrs.getText().toString());
        int setMins = Integer.parseInt(mins.getText().toString());
        Assignment toAdd = new Assignment();
        toAdd.selected = false;
        toAdd.course = setCourse;
        toAdd.name = setName;
        toAdd.hrs = setHrs;
        toAdd.mins = setMins;
        assignmentList.add(toAdd);
    }
    public void removeAssignment() {
        assignmentList.remove(currentIndex);
    }

    private class Assignment {
        boolean selected = false;
        String course;
        String name;
        int hrs;
        int mins;
    }

    private List<Assignment> assignmentList = new ArrayList();



}
