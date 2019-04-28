package com.example.WhatWhen;
import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Calendars;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentResolver;
import android.content.UriPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import 	android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.TimeZone;

import javax.xml.datatype.Duration;


public class Calendar_IO {
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    public static String localTime = TimeZone.getDefault().getDisplayName();
    public static long eventID;

    //date used to call/ inserts events
    public static int day = Calendar.DAY_OF_MONTH;
    public static int month = Calendar.MONTH;
    public static int year = Calendar.YEAR;

    //size of array storing info on each event from calendar
    public static int eventInfoSize = 3;

    public void getCalendar(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CALENDAR}, 711);
        }
        // Run query
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = Calendars.CONTENT_URI;
        String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
                + Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"hera@example.com", "com.example",
                "hera@example.com"};
        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        // Use the cursor to step through the returned records
        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

        }
    }
    public void setCalenderEvents(Activity context, int index) {
        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        List<long[]> freeTimeArray = getFreeTime(context);
        long minBlock = freeTimeArray.get(0)[2];
        String eventTittle = InputAssignments.assignmentList.get(index).course
                + " " + InputAssignments.assignmentList.get(index).assignment;

        int durationHrs = InputAssignments.assignmentList.get(index).hrs;
        int durationMins = InputAssignments.assignmentList.get(index).mins;
        long durationMilli = (long) (durationHrs * 3.6e+6 + durationMins * 60000);

        for (int i = 0; i < freeTimeArray.size(); i++) {
            if (freeTimeArray.get(i)[2] < minBlock && freeTimeArray.get(i)[2] > durationMilli) {
                startMillis = freeTimeArray.get(i)[0];
                endMillis = freeTimeArray.get(i)[0] + durationMilli;
            }
        }

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, eventTittle);
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_TIMEZONE, localTime);
        Uri uri = cr.insert(Events.CONTENT_URI, values);

        // get the event ID that is the last element in the Uri
        eventID = Long.parseLong(uri.getLastPathSegment());
    }

    private List getFreeTime(Activity context){
         final String[] INSTANCE_PROJECTION = new String[]{
                 CalendarContract.Instances.BEGIN,         // 0
                 CalendarContract.Instances.END,           // 1
        };

        // The indices for the projection array above.
        final int PROJECTION_BEGIN_INDEX = 0;
        final int PROJECTION_END_INDEX = 1;

        //A list of everything event schdeuled for that day. I user goes over GLHF.
        List<long[]> scheduledTime = new ArrayList<>(100);

        //A list of everything event free for that day.
        List<long[]> freeTime = new ArrayList<>(100);

        long[] eventInfo = new long[eventInfoSize];
        // Specify the date range you want to search for recurring
        // event instances
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, day, 0, 0);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, day, 23, 59, 59);
        long endMillis = endTime.getTimeInMillis();

        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();

        // The ID of the recurring event whose instances you are searching
        // for in the Instances table
        String selection = CalendarContract.Instances.EVENT_ID + " = ?";
        String[] selectionArgs = new String[]{"207"};

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

       // Submit the query
        cur = cr.query(builder.build(),
                INSTANCE_PROJECTION,
                selection,
                selectionArgs,
                null);

        while (cur.moveToNext()) {
            long eventID = 0;
            long start = 0;
            long end = 0;

            // Get the field values
            start = cur.getLong(PROJECTION_BEGIN_INDEX);
            end = cur.getInt(PROJECTION_END_INDEX);

            // Do something with the values.
            eventInfo[0] = start;
            eventInfo[1] = end;
            scheduledTime.add(eventInfo);
        }
        for (int i = 0; i < scheduledTime.size() - 1; i++) {
            eventInfo[0] = scheduledTime.get(i)[1] + 300000;
            eventInfo[1] = scheduledTime.get(i)[0];
            eventInfo[2] = eventInfo[1] - eventInfo[0];
            freeTime.add(eventInfo);
        }
        return freeTime;
    }
}