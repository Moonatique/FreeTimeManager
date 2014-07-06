package freetime.com.myappforactivities.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity2 extends Activity {

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT,                 // 3
            CalendarContract.Calendars.SYNC_EVENTS,
            CalendarContract.Calendars.ALLOWED_REMINDERS,
            CalendarContract.Calendars.ALLOWED_AVAILABILITY


    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    private String title;
    private ArrayList<Long> calendarsId = new ArrayList<Long>();
    private ArrayList<String> calendarsDisplayName = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

        getAllCalendars();
        //fetch the listView
        ListView list = (ListView) findViewById(R.id.listCal);


        //create an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        for (String calDisplayName : calendarsDisplayName){
            adapter.add(calDisplayName);
        }

        //add the adapter to the list
        list.setAdapter(adapter);

        //get an action on the click of one calendar
        /*list.setOnClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/


/*
        readCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Reading a google Calendar");
                //createCalendar();

                ArrayList<String> listEventTitle = getCalendars();

                Intent myIntent = new Intent(MainActivity2.this, MainActivityViewcalendar.class);
                myIntent.putStringArrayListExtra("listEventTitle",listEventTitle); //Optional parameters
                myIntent.putExtra("title",title);
                MainActivity2.this.startActivity(myIntent);
            }
        }); */


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void getAllCalendars() {

        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};

        Cursor calCursor = getContentResolver().query(CalendarContract.Calendars.CONTENT_URI,
                projection, null, null,
                CalendarContract.Calendars._ID + " ASC");



        if (calCursor.moveToFirst()) {
            do {
                // Get the field values
                long calID = calCursor.getLong(PROJECTION_ID_INDEX);
                String displayName = calCursor.getString(PROJECTION_DISPLAY_NAME_INDEX);
                String accountName = calCursor.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                String ownerName = calCursor.getString(PROJECTION_OWNER_ACCOUNT_INDEX);


                System.out.println(calID);
                System.out.println(displayName);
                System.out.println(accountName);
                System.out.println(ownerName);

                calendarsId.add(calID);
                calendarsDisplayName.add(displayName);

            } while (calCursor.moveToNext());
        }
    }

    public void getAllEvents(int calID){

        String[] proj = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.RRULE,
                CalendarContract.Events.TITLE};
        Cursor eventCursor =
                getContentResolver().
                        query(
                                CalendarContract.Events.CONTENT_URI,
                                proj,
                                CalendarContract.Events.CALENDAR_ID + " = ? ",
                                new String[]{Long.toString(calID)},
                                null);

        ArrayList<String> eventsTitle = new ArrayList<String>();
        if (eventCursor.moveToFirst()) {
            do {
                String eventTitle = eventCursor.getString(4);
                eventsTitle.add(eventTitle);
                //System.out.println("event: " + eventTitle);
            } while (eventCursor.moveToNext());
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public ArrayList<String> getCalendars() {

        // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[] {"cristin.clement@gmail.com", "com.google",
                "cristin.clement@gmail.com"};
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
            TextView t = (TextView) findViewById(R.id.textGoogle);
            t.setText(ownerName);

            System.out.println(ownerName);
            title = accountName;




            String[] proj = new String[]{
                    CalendarContract.Events._ID,
                    CalendarContract.Events.DTSTART,
                    CalendarContract.Events.DTEND,
                    CalendarContract.Events.RRULE,
                    CalendarContract.Events.TITLE};
            Cursor eventCursor =
                    getContentResolver().
                            query(
                                    CalendarContract.Events.CONTENT_URI,
                                    proj,
                                    CalendarContract.Events.CALENDAR_ID + " = ? ",
                                    new String[]{Long.toString(calID)},
                                    null);

            ArrayList<String> eventsTitle = new ArrayList<String>();
            if (eventCursor.moveToFirst()) {
                do {
                    String eventTitle = eventCursor.getString(4);
                    eventsTitle.add(eventTitle);
                    //System.out.println("event: " + eventTitle);
                } while (eventCursor.moveToNext());
            }

            return eventsTitle;
        }

        return null;

/*

        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        Cursor calCursor =
                getContentResolver().
                        query(CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                CalendarContract.Calendars.VISIBLE + " = 1",
                                null,
                                CalendarContract.Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                // Get the field values
                long calID = calCursor.getLong(PROJECTION_ID_INDEX);
                String displayName = calCursor.getString(PROJECTION_DISPLAY_NAME_INDEX);
                String accountName = calCursor.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                String ownerName = calCursor.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
                System.out.println(ownerName);

                /*
                System.out.println(calID);
                System.out.println(displayName);
                System.out.println(accountName);
                System.out.println(ownerName);
                *
                String[] proj = new String[]{
                        CalendarContract.Events._ID,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND,
                        CalendarContract.Events.RRULE,
                        CalendarContract.Events.TITLE};
                Cursor eventCursor =
                        getContentResolver().
                                query(
                                        CalendarContract.Events.CONTENT_URI,
                                        proj,
                                        CalendarContract.Events.CALENDAR_ID + " = ? ",
                                        new String[]{Long.toString(calID)},
                                        null);
                if (eventCursor.moveToFirst()) {
                    do{
                        String eventTitle = eventCursor.getString(4);
                        //System.out.println("event: " + eventTitle);
                    } while(eventCursor.moveToNext());
                }
            } while (calCursor.moveToNext());
        }
        */
    }


}
