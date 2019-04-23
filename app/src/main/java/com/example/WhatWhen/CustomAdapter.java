package com.example.WhatWhen;

import android.view.View;
import android.widget.ArrayAdapter;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

//https://www.youtube.com/watch?v=nOdSARCVYic

class CustomAdapter extends ArrayAdapter<InputAssignments.Assignment> {
    CustomAdapter(Context context, List<InputAssignments.Assignment> assignmentList) {
        super(context, R.layout.rowlayout, assignmentList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater1 = LayoutInflater.from(getContext());
        View customView = layoutInflater1.inflate(R.layout.rowlayout, parent, false);

        String singleCourse = getItem(position).toString();
        String singleAssignment = getItem(position).toString();
        String singleHrs = getItem(position).toString();
        String singleMins = getItem(position).toString();

        TextView courseDisplay = customView.findViewById(R.id.rowDisplayCourse);
        TextView assignmentDisplay = customView.findViewById(R.id.rowDisplayAssignment);
        TextView hrsDisplay = customView.findViewById(R.id.rowDisplayHrs);
        TextView minsDisplay = customView.findViewById(R.id.rowDisplayMins);

        courseDisplay.setText(singleCourse);
        assignmentDisplay.setText(singleAssignment);
        hrsDisplay.setText(singleHrs);
        minsDisplay.setText(singleMins);

        return customView;

    }
}
