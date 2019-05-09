package com.example.WhatWhen;
import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Calendars;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentResolver;
import android.content.UriPermission;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import 	android.content.ContentValues;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            Calendars.OWNER_ACCOUNT,                  // 3
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

    private static List<long[]> freeTime = new ArrayList<>(100);
    private static ArrayList<String> calendarIDsList = new ArrayList<>();
    private static ArrayList<Event> eventList = new ArrayList<>();
    private static long[] eventInfo = new long[eventInfoSize];

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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setCalendarEvents(Activity context, int index) {
        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        List<long[]> freeTimeArray = null;
        try {
            freeTimeArray = getFreeTime(context);
        } catch (ParseException e) {

        }
        long minBlock = freeTimeArray.get(0)[2];
        String eventTittle = InputAssignments.assignmentList.get(index).course
                + " " + InputAssignments.assignmentList.get(index).assignment;

        int durationHrs = InputAssignments.assignmentList.get(index).hrs;
        int durationMins = InputAssignments.assignmentList.get(index).mins;
        long durationMilli = (long) (durationHrs * 3.6e+6 + durationMins * 60000);

        for (int i = 0; i < freeTimeArray.size(); i++) {
            if (freeTimeArray.get(i)[2] < minBlock && freeTimeArray.get(i)[2] > durationMilli) {
                minBlock = freeTimeArray.get(i)[2];
                startMillis = freeTimeArray.get(i)[0];
                endMillis = freeTimeArray.get(i)[0] + durationMilli;
            }
        }
        if (startMillis == 0 && endMillis == 0) {
            throw new IllegalArgumentException();
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

    static class Event {
        String title;
        String startTime;
        String endTime;
        String duration;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static List getFreeTime(Activity context) throws ParseException {
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = Calendars.CONTENT_URI;

        //get's start of day to compare date of events against when sorting
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, day, 0, 0);
        long startMillis = beginTime.getTimeInMillis();

        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        if(eventList == null) {
            while (cur.moveToNext()) {
                calendarIDsList.add(cur.getString(PROJECTION_ID_INDEX));
            }

            for (String id : calendarIDsList) {
                Uri uriTest = Uri.parse("content://com.android.calendar/events");

                Cursor eventCursor = context.getContentResolver().query(uriTest,
                        new String[]{Events.TITLE, Events.CALENDAR_ID, Events.DTSTART, Events.DTEND}, null,
                        null, null);
                while (eventCursor.moveToNext()) {
                    String title = eventCursor.getString(0);
                    String startTime = eventCursor.getString(2);
                    String endTime = eventCursor.getString(3);
                    Long startTimeLong = Long.parseLong(startTime);
                    Long endTimeLong = Long.parseLong(endTime);
                    Event toAdd = new Event();
                    toAdd.title = title;
                    toAdd.startTime = startTime;
                    toAdd.endTime = endTime;
                    toAdd.duration = String.valueOf(endTimeLong - startTimeLong);
                    eventList.add(toAdd);
                    Log.d("hello", "event: " + title + " startTime: " + startTime + " endTime: " + endTime);
                }
                System.out.println(eventList.size());
            }
        }

        for (int i = 0; i < eventList.size() - 1; i++) {
            if (Long.parseLong(eventList.get(i).startTime) > startMillis) {
                eventInfo[0] = Long.parseLong(eventList.get(i).startTime) + 300000;
                eventInfo[1] = Long.parseLong(eventList.get(i).endTime) + 300000;
                eventInfo[2] = Long.parseLong(eventList.get(i).duration);
                freeTime.add(eventInfo);
            }
        }
        return freeTime;
    }
}
