package com.example.WhatWhen;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

//Thurs Apr 25 8:47 pm ListView doesn't properly reflect updates or removes from my list. I feel like I should regenerate the ListView each time the user navigates to that page.

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
                    LinearLayout whatWhenLayout = findViewById(R.id.whatWhenLayout);
                    whatWhenLayout.setVisibility(View.INVISIBLE);
                    TextView emptyListText = findViewById(R.id.emptyListText);
                    emptyListText.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_what_when:
                    tableLayout1 = findViewById(R.id.tableLayout1);
                    tableLayout1.setVisibility(View.INVISIBLE);
                    whatWhenLayout = findViewById(R.id.whatWhenLayout);
                    whatWhenLayout.setVisibility(View.VISIBLE);
                    emptyListText = findViewById(R.id.emptyListText);
                    if (assignmentList.size() == 0) {
                        emptyListText.setVisibility(View.VISIBLE);
                        LinearLayout labelLayout = findViewById(R.id.labelLayout);
                        labelLayout.setVisibility(View.INVISIBLE);
                    } else {
                        emptyListText.setVisibility(View.INVISIBLE);
                        LinearLayout labelLayout = findViewById(R.id.labelLayout);
                        labelLayout.setVisibility(View.VISIBLE);
                    }
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
        TableLayout tableLayout1 = findViewById(R.id.tableLayout1);
        tableLayout1.setVisibility(View.VISIBLE);
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
                if (assignmentList.size() != 0) {
                    currentIndex++;
                    if (currentIndex >= assignmentList.size()) {
                        currentIndex = 0;
                    }
                    viewChange();
                }
            }
        });
        final Button leftIndexButton = findViewById(R.id.leftIndexButton);
        leftIndexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assignmentList.size() != 0) {
                    currentIndex--;
                    if (currentIndex < 0) {
                        currentIndex = assignmentList.size() - 1;
                    }
                    viewChange();
                }
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

        ListAdapter listAdapter1 = new CustomAdapter(this, assignmentList);
        ListView listView1 = findViewById(R.id.listView1);
        listView1.setAdapter(listAdapter1);

        LinearLayout whatWhenLayout = findViewById(R.id.whatWhenLayout);
        whatWhenLayout.setVisibility(View.INVISIBLE);

        clearFieldErrors();
    }
    public class Assignment {
        boolean selected;
        String course;
        String assignment;
        int hrs;
        int mins;
    }
    private void addAssignment() {
        clearFieldErrors();
        if (courseField.getText().toString().equals("")
                || assignmentField.getText().toString().equals("")
                || hrsField.getText().toString().equals("")
                || minsField.getText().toString().equals("")) {
            TextView emptyFieldTxt = findViewById(R.id.emptyFieldTxt);
            emptyFieldTxt.setVisibility(View.VISIBLE);
            return;
        }
        try {
            Assignment toAdd = new Assignment();
            toAdd.selected = false;
            toAdd.course = courseField.getText().toString();
            toAdd.assignment = assignmentField.getText().toString();
            toAdd.hrs = Integer.parseInt(hrsField.getText().toString());
            if (toAdd.hrs < 0) {
                throw new IllegalArgumentException();
            }
            toAdd.mins = Integer.parseInt(minsField.getText().toString());
            if (toAdd.mins < 0) {
                throw new IllegalArgumentException();
            }
            assignmentList.add(toAdd);
            clearFields();
        } catch (IllegalArgumentException e) {
            TextView invalidIntText = findViewById(R.id.invalidIntTxt);
            invalidIntText.setVisibility(View.VISIBLE);
        }
    }
    private void removeAssignment() {
        clearFieldErrors();
        assignmentList.remove(currentIndex);
        clearFields();
    }
    public void viewChange() {
        clearFieldErrors();
        courseField.setText(assignmentList.get(currentIndex).course);
        assignmentField.setText(assignmentList.get(currentIndex).assignment);
        hrsField.setText(String.valueOf(assignmentList.get(currentIndex).hrs));
        minsField.setText(String.valueOf(assignmentList.get(currentIndex).mins));
    }
    private void updateCurrentAssignment() {
        clearFieldErrors();
        if (courseField.getText().toString().equals("")
                || assignmentField.getText().toString().equals("")
                || hrsField.getText().toString().equals("")
                || minsField.getText().toString().equals("")) {
            TextView emptyFieldText = findViewById(R.id.emptyFieldTxt);
            emptyFieldText.setVisibility(View.VISIBLE);
            return;
        }
        try {
            Assignment toAdd = new Assignment();
            toAdd.selected = false;
            toAdd.course = courseField.getText().toString();
            toAdd.assignment = assignmentField.getText().toString();
            toAdd.hrs = Integer.parseInt(hrsField.getText().toString());
            if (toAdd.hrs < 0) {
                throw new IllegalArgumentException();
            }
            toAdd.mins = Integer.parseInt(minsField.getText().toString());
            if (toAdd.mins < 0) {
                throw new IllegalArgumentException();
            }
            assignmentList.set(currentIndex, toAdd);
        } catch (IllegalArgumentException e) {
            TextView invalidIntText = findViewById(R.id.invalidIntTxt);
            invalidIntText.setVisibility(View.VISIBLE);
        }
    }
    private void clearFields() {
        clearFieldErrors();
        courseField.setText("");
        assignmentField.setText("");
        hrsField.setText("");
        minsField.setText("");
    }
    private void clearFieldErrors() {
        TextView invalidIntText = findViewById(R.id.invalidIntTxt);
        invalidIntText.setVisibility(View.INVISIBLE);
        TextView emptyFieldText = findViewById(R.id.emptyFieldTxt);
        emptyFieldText.setVisibility(View.INVISIBLE);
    }
    private List<Assignment> assignmentList = new ArrayList<>();
}
