package com.example.WhatWhen;

import android.view.View;
import android.widget.ArrayAdapter;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

//https://www.youtube.com/watch?v=nOdSARCVYic

class CustomAdapter extends ArrayAdapter<InputAssignments.Assignment> {
    List<InputAssignments.Assignment> assignmentListCopy;
    CustomAdapter(Context context, List<InputAssignments.Assignment> assignmentList) {
        super(context, R.layout.rowlayout, assignmentList);
        assignmentListCopy = assignmentList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater1 = LayoutInflater.from(getContext());
        View customView = layoutInflater1.inflate(R.layout.rowlayout, parent, false);

        boolean singleSelected = assignmentListCopy.get(position).selected;
        String singleCourse = assignmentListCopy.get(position).course;
        String singleAssignment = assignmentListCopy.get(position).assignment;
        String singleHrs = Integer.toString(assignmentListCopy.get(position).hrs);
        String singleMins = Integer.toString(assignmentListCopy.get(position).mins);

        CheckBox checkBoxDisplay = customView.findViewById(R.id.checkBox);
        TextView courseDisplay = customView.findViewById(R.id.rowDisplayCourse);
        TextView assignmentDisplay = customView.findViewById(R.id.rowDisplayAssignment);
        TextView hrsDisplay = customView.findViewById(R.id.rowDisplayHrs);
        TextView minsDisplay = customView.findViewById(R.id.rowDisplayMins);

        checkBoxDisplay.setSelected(singleSelected);
        courseDisplay.setText(singleCourse);
        assignmentDisplay.setText(singleAssignment);
        hrsDisplay.setText(singleHrs);
        minsDisplay.setText(singleMins);

        return customView;

    }
}