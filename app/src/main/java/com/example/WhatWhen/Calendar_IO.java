package com.example.WhatWhen;

public class Calendar_IO {
    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
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

    // Run query
    Cursor cur = null;
    ContentResolver cr = getContentResolver();
    Uri uri = Calendars.CONTENT_URI;
    String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
            + Calendars.ACCOUNT_TYPE + " = ?) AND ("
            + Calendars.OWNER_ACCOUNT + " = ?))";
    String[] selectionArgs = new String[] {"hera@example.com", "com.example",
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

        // Do something with the values...

        ...
    }
    private static final String DEBUG_TAG = "MyActivity";
    ...
    long calID = 2;
    ContentValues values = new ContentValues();
    // The new display name for the calendar
    values.put(Calendars.CALENDAR_DISPLAY_NAME, "Trevor's Calendar");
    Uri updateUri = ContentUris.withAppendedId(Calendars.CONTENT_URI, calID);
    int rows = getContentResolver().update(updateUri, values, null, null);
    Log.i(DEBUG_TAG, "Rows updated: " + rows);
}
