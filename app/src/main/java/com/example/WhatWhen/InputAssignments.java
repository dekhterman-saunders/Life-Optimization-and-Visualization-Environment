package com.example.WhatWhen;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;


public class InputAssignments extends AppCompatActivity {
    private int currentIndex = 0;
    private EditText courseField;
    private EditText assignmentField;
    private EditText hrsField;
    private EditText minsField;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_input_assignments:
                    TableLayout tableLayout1 = findViewById(R.id.tableLayout1);
                    tableLayout1.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_what_when:
                    tableLayout1 = findViewById(R.id.tableLayout1);
                    tableLayout1.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_open_calendar:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_assignments);
        BottomNavigationView navView = findViewById(R.id.nav_view);
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
                currentIndex++;
                if (currentIndex >= assignmentList.size()) {
                    currentIndex = 0;
                }
                viewChange();
            }
        });
        final Button leftIndexButton = findViewById(R.id.leftIndexButton);
        leftIndexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex--;
                if (currentIndex < 0) {
                    currentIndex = assignmentList.size() - 1;
                }
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
        final Button clearFieldsButton = findViewById(R.id.clearFieldsButton);
        clearFieldsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });
        courseField = findViewById(R.id.courseInput);
        assignmentField = findViewById(R.id.assignmentInput);
        hrsField = findViewById(R.id.hrsInput);
        minsField = findViewById(R.id.minsInput);
    }
    private class Assignment {
        boolean selected;
        String course;
        String assignment;
        int hrs;
        int mins;
    }
    private void addAssignment() {
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
        clearFields();
    }
    private void removeAssignment() {
        assignmentList.remove(currentIndex);
        clearFields();
    }
    public void viewChange() {
        courseField.setText(assignmentList.get(currentIndex).course);
        assignmentField.setText(assignmentList.get(currentIndex).assignment);
        hrsField.setText(String.valueOf(assignmentList.get(currentIndex).hrs));
        minsField.setText(String.valueOf(assignmentList.get(currentIndex).mins));
    }
    private void updateCurrentAssignment() {
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
    private void clearFields() {
        courseField.setText("");
        assignmentField.setText("");
        hrsField.setText("");
        minsField.setText("");
    }
    private List<Assignment> assignmentList = new ArrayList<>();
    ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1
    , assignmentList);
    ListView listView1 = findViewById(R.id.listView1);
    //listView1.setAdapter();
}
