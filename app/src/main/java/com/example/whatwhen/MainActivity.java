package com.example.whatwhen;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private int currentIndex = 0;
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
        final Button rightIndexButton = findViewById(R.id.rightIndexButton);
        rightIndexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("rightIndexButton clicked");
                System.out.println("currentIndex: " + currentIndex);
                currentIndex = (currentIndex + 1) % assignmentList.size();
                System.out.println("currentIndex: " + currentIndex);
                viewChange();
            }
        });
        final Button leftIndexButton = findViewById(R.id.leftIndexButton);
        leftIndexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("leftIndexButton clicked");
                System.out.println("currentIndex: " + currentIndex);
                currentIndex = (currentIndex - 1) % assignmentList.size();
                System.out.println("currentIndex: " + currentIndex);
                viewChange();
            }
        });
        final Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCurrentAssignment();
            }
        });
        courseField = findViewById(R.id.courseInput);
        assignmentField = findViewById(R.id.assignmentInput);
        hrsField = findViewById(R.id.hrsInput);
        minsField = findViewById(R.id.minsInput);
    }
    EditText courseField;
    EditText assignmentField;
    EditText hrsField;
    EditText minsField;
    private class Assignment {
        boolean selected = false;
        String course;
        String assignment;
        int hrs;
        int mins;
    }
    public void addAssignment() {
        if (courseField.getText().toString().equals("")
                || assignmentField.getText().toString().equals("")
                || hrsField.getText().toString().equals("")
                || minsField.getText().toString().equals("")) {
            System.out.println("Not all fields are filled");
            return;
        }
        Assignment toAdd = new Assignment();
        toAdd.selected = false;
        toAdd.course = courseField.getText().toString();
        toAdd.assignment = assignmentField.getText().toString();
        toAdd.hrs = Integer.parseInt(hrsField.getText().toString());
        toAdd.mins = Integer.parseInt(minsField.getText().toString());
        assignmentList.add(toAdd);
        courseField.setText("");
        assignmentField.setText("");
        hrsField.setText("");
        minsField.setText("");
    }
    public void removeAssignment() {
        assignmentList.remove(currentIndex);
    }
    public void viewChange() {
        courseField.setText(assignmentList.get(currentIndex).course);
        assignmentField.setText(assignmentList.get(currentIndex).assignment);
        hrsField.setText(String.valueOf(assignmentList.get(currentIndex).hrs));
        minsField.setText(String.valueOf(assignmentList.get(currentIndex).mins));
    }
    public void updateCurrentAssignment() {
        if (courseField.getText().toString().equals("")
                || assignmentField.getText().toString().equals("")
                || hrsField.getText().toString().equals("")
                || minsField.getText().toString().equals("")) {
            return;
        }
        Assignment toAdd = new Assignment();
        toAdd.selected = false;
        toAdd.course = courseField.getText().toString();
        toAdd.assignment = assignmentField.getText().toString();
        toAdd.hrs = Integer.parseInt(hrsField.getText().toString());
        toAdd.mins = Integer.parseInt(minsField.getText().toString());
        assignmentList.set(currentIndex, toAdd);
    }
    private List<Assignment> assignmentList = new ArrayList<>();
}
