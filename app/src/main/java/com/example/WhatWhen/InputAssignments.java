package com.example.WhatWhen;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import static android.graphics.Color.parseColor;

public class InputAssignments extends AppCompatActivity {
    private int currentIndex = 0;
    private EditText courseField;
    private EditText assignmentField;
    private EditText hrsField;
    private EditText minsField;

    private String lastAction;

    public InputAssignments() {
    }

    private android.content.Context getContext() {
        return this;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_input_assignments:
                    //hide whatWhenLayout
                    LinearLayout whatWhenLayout = findViewById(R.id.whatWhenLayout);
                    whatWhenLayout.setVisibility(View.INVISIBLE);

                    //hide emptyListText
                    TextView emptyListText = findViewById(R.id.emptyListText);
                    emptyListText.setVisibility(View.INVISIBLE);

                    //show inputLayout
                    TableLayout inputLayout = findViewById(R.id.inputTableLayout);
                    inputLayout.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_what_when:
                    //listView refresh
                    ListAdapter listAdapter = new CustomAdapter(getContext(), assignmentList);
                    ListView listView = findViewById(R.id.listView);
                    listView.setAdapter(listAdapter);

                    //hide inputLayout
                    inputLayout = findViewById(R.id.inputTableLayout);
                    inputLayout.setVisibility(View.INVISIBLE);

                    //show whatWhenLayout
                    whatWhenLayout = findViewById(R.id.whatWhenLayout);
                    whatWhenLayout.setVisibility(View.VISIBLE);

                    emptyListText = findViewById(R.id.emptyListText);
                    if (assignmentList.size() == 0) {
                        //if assignmentList is empty, show emptyListText & hide labelLayout & hide whatWhenButton
                        emptyListText.setVisibility(View.VISIBLE);
                        LinearLayout labelLayout = findViewById(R.id.labelLayout);
                        labelLayout.setVisibility(View.INVISIBLE);
                        LinearLayout whatWhenButtonLayout = findViewById(R.id.whatWhenButtonLayout);
                        whatWhenButtonLayout.setVisibility(View.INVISIBLE);
                    } else {
                        //hide emptyListText & show labelLayout & show whatWhenButton
                        emptyListText.setVisibility(View.INVISIBLE);
                        LinearLayout labelLayout = findViewById(R.id.labelLayout);
                        labelLayout.setVisibility(View.VISIBLE);
                        LinearLayout whatWhenButtonLayout = findViewById(R.id.whatWhenButtonLayout);
                        whatWhenButtonLayout.setVisibility(View.VISIBLE);
                    }
                    return true;
                case R.id.navigation_open_calendar:
                    Date date = new Date();

                    // A date-time specified in milliseconds since the epoch.
                    long startMillis = date.getTime();
                    startMillis = Math.abs(startMillis);
                    System.out.println(startMillis);
                    Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, startMillis);
                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                    startActivity(intent);
                    return false;
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

        super.onStart();
        Calendar_IO cio = new Calendar_IO();
        cio.getCalendar(this);

        //show inputLayout
        TableLayout inputLayout = findViewById(R.id.inputTableLayout);
        inputLayout.setVisibility(View.VISIBLE);

        //buttons
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
                    lastAction = "rightIndexButton";
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
                    lastAction = "leftIndexButton";
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

        LinearLayout whatWhenLayout = findViewById(R.id.whatWhenLayout);
        whatWhenLayout.setVisibility(View.INVISIBLE);

        clearFieldErrors();

        //finds user's calender
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        Calendar_IO cio = new Calendar_IO();
        cio.getCalendar(this);
    }*/

    class Assignment {
        boolean selected;
        String course;
        String assignment;
        int hrs;
        int mins;
    }
    private void addAssignment() {
        clearFieldErrors();
        hideCurrentIndexAndMsgDisplayTxt();
        if (emptyFields()) {
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
            toAdd.mins = Integer.parseInt(minsField.getText().toString());

            //negative hrs or mins
            if (toAdd.hrs < 0 || toAdd.mins < 0) {
                throw new IllegalArgumentException();
            }

            //invalid duration hrs0mins0
            if (toAdd.hrs == 0 && toAdd.mins == 0) {
                throw new NullPointerException();
            }
            assignmentList.add(toAdd);
            clearFields();
            hideCurrentIndexAndMsgDisplayTxt();
            currentIndex = -1;
            lastAction = "add";

        } catch (IllegalArgumentException e) {
            TextView invalidIntText = findViewById(R.id.hrsMinsErrTxt);
            invalidIntText.setText(R.string.invalidNumber);
            invalidIntText.setVisibility(View.VISIBLE);

            //invalid duration
        } catch (NullPointerException e) {
            TextView invalidIntText = findViewById(R.id.hrsMinsErrTxt);
            invalidIntText.setText(R.string.invalidDuration);
            invalidIntText.setVisibility(View.VISIBLE);
        }
    }
    private void removeAssignment() {
        clearFieldErrors();
        try {
            if (assignmentList.size() == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (currentIndex < 0) {
                throw new ArithmeticException();
            }
            assignmentList.remove(currentIndex);
            hideCurrentIndexAndMsgDisplayTxt();
            currentIndex = -1;
            lastAction = "remove";
            clearFields();
        } catch (ArrayIndexOutOfBoundsException e) {
            clearFields();
            TextView currentIndexAndMsgDisplayTxt = findViewById(R.id.currentIndexAndMsgDisplayTxt);
            currentIndexAndMsgDisplayTxt.setTextColor(parseColor("#F55B5B"));
            currentIndexAndMsgDisplayTxt.setText("List is empty, Nothing to remove");
            currentIndexAndMsgDisplayTxt.setVisibility(View.VISIBLE);
        } catch (ArithmeticException e) {
            clearFields();
            TextView currentIndexAndMsgDisplayTxt = findViewById(R.id.currentIndexAndMsgDisplayTxt);
            currentIndexAndMsgDisplayTxt.setTextColor(parseColor("#F55B5B"));
            currentIndexAndMsgDisplayTxt.setText("Please view a new assignment");
            currentIndexAndMsgDisplayTxt.setVisibility(View.VISIBLE);
        }


    }
    public void viewChange() {
        clearFieldErrors();
        courseField.setText(assignmentList.get(currentIndex).course);
        assignmentField.setText(assignmentList.get(currentIndex).assignment);
        hrsField.setText(String.valueOf(assignmentList.get(currentIndex).hrs));
        minsField.setText(String.valueOf(assignmentList.get(currentIndex).mins));
        showCurrentIndex();
    }
    private void updateCurrentAssignment() {
        clearFieldErrors();
        hideCurrentIndexAndMsgDisplayTxt();
        try {
            //empty list
            if (assignmentList.size() == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            //last action was add, update, or clear fields
            if (currentIndex < 0) {
                throw new ArithmeticException();
            }
            Assignment toAdd = new Assignment();
            toAdd.selected = false;
            toAdd.course = courseField.getText().toString();
            toAdd.assignment = assignmentField.getText().toString();
            toAdd.hrs = Integer.parseInt(hrsField.getText().toString());
            toAdd.mins = Integer.parseInt(minsField.getText().toString());
            if (emptyFields()) {
                TextView emptyFieldText = findViewById(R.id.emptyFieldTxt);
                emptyFieldText.setVisibility(View.VISIBLE);
                return;
            }
            //negative hrs or mins
            if (toAdd.hrs < 0 || toAdd.mins < 0) {
                throw new IllegalArgumentException();
            }

            //invalid duration hrs0mins0
            if (toAdd.hrs == 0 && toAdd.mins == 0) {
                throw new NullPointerException();
            }
            assignmentList.set(currentIndex, toAdd);
            lastAction = "update";
            showCurrentIndex();
        } catch (NullPointerException e) {
            TextView invalidIntText = findViewById(R.id.hrsMinsErrTxt);
            invalidIntText.setText(R.string.invalidDuration);
            invalidIntText.setVisibility(View.VISIBLE);
        } catch (ArithmeticException e) {
            TextView currentIndexAndMsgDisplayTxt = findViewById(R.id.currentIndexAndMsgDisplayTxt);
            currentIndexAndMsgDisplayTxt.setTextColor(parseColor("#F55B5B"));
            currentIndexAndMsgDisplayTxt.setText("Please view a new assignment");
            currentIndexAndMsgDisplayTxt.setVisibility(View.VISIBLE);
        } catch (IllegalArgumentException e) {
            TextView invalidIntText = findViewById(R.id.hrsMinsErrTxt);
            invalidIntText.setText(R.string.invalidNumber);
            invalidIntText.setVisibility(View.VISIBLE);
        } catch (ArrayIndexOutOfBoundsException e) {
            TextView currentIndexAndMsgDisplayTxt = findViewById(R.id.currentIndexAndMsgDisplayTxt);
            currentIndexAndMsgDisplayTxt.setTextColor(parseColor("#F55B5B"));
            currentIndexAndMsgDisplayTxt.setText("List is empty, Add Assignments");
            currentIndexAndMsgDisplayTxt.setVisibility(View.VISIBLE);
        }
    }
    private void clearFields() {
        clearFieldErrors();
        hideCurrentIndexAndMsgDisplayTxt();
        courseField.setText("");
        assignmentField.setText("");
        hrsField.setText("");
        minsField.setText("");
        currentIndex = -1;
        lastAction = "clearFields";
    }
    private void clearFieldErrors() {
        TextView invalidIntText = findViewById(R.id.hrsMinsErrTxt);
        invalidIntText.setVisibility(View.INVISIBLE);
        TextView emptyFieldText = findViewById(R.id.emptyFieldTxt);
        emptyFieldText.setVisibility(View.INVISIBLE);
    }
    private List<Assignment> assignmentList = new ArrayList<>();

    private boolean emptyFields() {
        if (courseField.getText().toString().equals("")
                || assignmentField.getText().toString().equals("")
                || hrsField.getText().toString().equals("")
                || minsField.getText().toString().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private void hideCurrentIndexAndMsgDisplayTxt() {
        TextView currentIndexAndMsgDisplayTxt = findViewById(R.id.currentIndexAndMsgDisplayTxt);
        currentIndexAndMsgDisplayTxt.setVisibility(View.INVISIBLE);
    }

    private void showCurrentIndex() {
        TextView currentIndexAndMsgDisplayTxt = findViewById(R.id.currentIndexAndMsgDisplayTxt);
        currentIndexAndMsgDisplayTxt.setText(currentIndex + 1 + "/" + assignmentList.size());
        currentIndexAndMsgDisplayTxt.setTextColor(parseColor("#000000"));
        currentIndexAndMsgDisplayTxt.setVisibility(View.VISIBLE);
    }
}
