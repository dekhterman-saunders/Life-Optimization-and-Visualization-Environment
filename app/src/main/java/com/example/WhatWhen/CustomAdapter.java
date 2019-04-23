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
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CheckBox;

class CustomAdapter extends ArrayAdapter<Assignment> {
    CustomAdapter(Context context, List<Assignment> assignmentList) {
        super(context, R.layout.rowlayout, assignmentList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater1 = LayoutInflater.from(getContext());
        View customView = layoutInflater1.inflate(R.layout.rowlayout, parent, false);

        String singleCourse = getItem(position);
        String singleAssignment = getItem(position);
        String singleHrs = getItem(position);
        String singleMins = getItem(position);

        TextView courseDisplay = customView.findViewById(R.id.rowDisplayCourse);
        TextView assignmentDisplay = customView.findViewById(R.id.rowDisplayAssignment);
        TextView hrsDisplay = customView.findViewById(R.id.rowDisplayHrs);
        TextView minsDisplay = customView.findViewById(R.id.rowDisplayMins);

        courseDisplay.setText(singleCourse);
        assignmentDisplay.setText(singleAssignment);
        courseDisplay.setText(singleHrs);
        courseDisplay.setText(singleMins);

    }
}
