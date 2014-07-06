package fr.unice.polytech.freetime.apple.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Calendars;
import android.widget.Toast;

import java.util.HashMap;
import java.util.TimeZone;

import fr.unice.polytech.calendarmodule.EventBuilder;
import fr.unice.polytech.datasources.FtEventDataSource;
import fr.unice.polytech.entities.FtEventEntity;
import fr.unice.polytech.freetime.apple.FreeTimeApplication;
import fr.unice.polytech.freetime.apple.firstuse.ImportCalendarFragment;
import fr.unice.polytech.freetimedatabase.FreeTimeDbHelper;

/**
 * Created by Hakim on 21/06/2014.
 */
public class ImportCalendarService extends IntentService {
    private static final String FREETIME_CALENDAR_NAME = "FreeTime Calendar";

    public static final String PARAM_METHOD = "method";
    public static final String METHOD_GET_ALL_CALENDARS = "getAllCalendars";
    public static final String METHOD_RESET = "reset";
    public static final String METHOD_IMPORT = "import";
    public static final String PARAM_IN_CAL_ID = "calId";
    public static final String PARAM_OUT_CALENDARS = "calendars";


    public ImportCalendarService() { super("ImportCalendarService"); }

    @Override
    protected void onHandleIntent(Intent intent) {
        long calId = intent.getLongExtra(PARAM_IN_CAL_ID, -1);
        String method = intent.getStringExtra(PARAM_METHOD);

        if(method.equals(METHOD_IMPORT)) {
            importEventsFromCalendar(calId);
        }

        if(method.equals(METHOD_GET_ALL_CALENDARS)) {
            HashMap<String, Long> calendarIds = getAllCalendars();
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(ImportCalendarFragment.CalendarReceiver.ACTION_RESP);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(PARAM_OUT_CALENDARS, calendarIds);
            sendBroadcast(broadcastIntent);
        }

        if(method.equals(METHOD_RESET)) {
            resetCalendarAndPrefs();
        }

    }

    public void importEventsFromCalendar(long calId){
        Cursor ec = getAllEventsFromCalendar(calId);
        long freeTimeCalendarId = findFreeTimeCalendarId();

        if(ec.moveToFirst()) {
            do{
                String title = ec.getString(ec.getColumnIndex(CalendarContract.Events.TITLE));
                long start = ec.getLong(ec.getColumnIndex(CalendarContract.Events.DTSTART));
                long end = ec.getLong(ec.getColumnIndex(CalendarContract.Events.DTEND));
                String timeZone = ec.getString(ec.getColumnIndex(CalendarContract.Events.EVENT_TIMEZONE));
                String location = ec.getString(ec.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                int allDay = ec.getInt(ec.getColumnIndex(CalendarContract.Events.ALL_DAY));
                String rRule = ec.getString(ec.getColumnIndex(CalendarContract.Events.RRULE));
                int availability = ec.getInt(ec.getColumnIndex(CalendarContract.Events.AVAILABILITY));

                long eventId = new EventBuilder(freeTimeCalendarId).createEvent(title).startDT(start).endDT(end)
                        .timeZone(timeZone).location(location).allDay(allDay==1).rRule(rRule)
                        .availability(availability).finalizeEvent(getContentResolver());

                FtEventDataSource ftEventDataSource = new FtEventDataSource(getApplicationContext());
                ftEventDataSource.createFtEvent(eventId, title, start, end, FtEventEntity.FtEventType.IMPORTED);

            }while(ec.moveToNext());

            ec.close();
        }
    }

    public Cursor getAllEventsFromCalendar(long calId) {
        Cursor cursor = getContentResolver().query(Events.CONTENT_URI, null,
                                                   Events.CALENDAR_ID + " = ? ",
                                                   new String[]{Long.toString(calId)},
                                                   Events.DTSTART + " ASC");
        return cursor;
    }

    public HashMap<String, Long> getAllCalendars() {
        HashMap<String, Long> calendarIds = new HashMap<>();

        String[] projection = new String[]{ Calendars._ID, Calendars.NAME, Calendars.ACCOUNT_NAME };

        Cursor calCursor = getContentResolver().query(Calendars.CONTENT_URI, projection,
                                                      Calendars.NAME + " != ?",
                                                      new String[]{FREETIME_CALENDAR_NAME},
                                                      Calendars.NAME + " ASC");
        if(calCursor.moveToFirst()) {
            int columnName = calCursor.getColumnIndex(Calendars.NAME);
            int columnId = calCursor.getColumnIndex(Calendars._ID);
            while(!calCursor.isAfterLast()) {
                calendarIds.put(calCursor.getString(columnName), calCursor.getLong(columnId));
                calCursor.moveToNext();
            }
        }

        calCursor.close();

        return calendarIds;
    }

    public long findFreeTimeCalendarId() {
        String[] projection = new String[]{Calendars._ID};
        String selection = Calendars.ACCOUNT_NAME + " = ? AND " + Calendars.ACCOUNT_TYPE +  " = ? ";
        String[] selArgs = new String[]{"com.freetime", CalendarContract.ACCOUNT_TYPE_LOCAL};
        Cursor cursor = getContentResolver().query(Calendars.CONTENT_URI, projection, selection, selArgs, null);
        if (!cursor.moveToFirst()) {
            Uri uri = createCalendar();
            cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
        }

        long calId = cursor.getLong(0);
        cursor.close();
        SharedPreferences prefs = getSharedPreferences(FreeTimeApplication.PREFNAME, 0);
        prefs.edit().putLong(FreeTimeApplication.PREF_FREETIME_CAL_ID, calId);
        return calId;
    }

    private Uri createCalendar(){
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, "com.freetime");
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Calendars.NAME, FREETIME_CALENDAR_NAME);
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "FreeTime Calendar");
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xffff0000);
        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, "freetime@gmail.com");
        values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().getID());
        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "com.freetime");
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,"true");
        Uri uri = getContentResolver().insert(builder.build(), values);

        System.out.println(uri);
        Toast.makeText(getApplicationContext(), "Created FreeTime Calendar", Toast.LENGTH_LONG).show();
        return uri;
    }

    private long resetCalendarAndPrefs() {
        FreeTimeDbHelper freeTimeDbHelper = new FreeTimeDbHelper(getApplicationContext());
        freeTimeDbHelper.reset(freeTimeDbHelper.getWritableDatabase());
        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "com.freetime");
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,"true");
        getContentResolver().delete(builder.build(), null, null);

        SharedPreferences prefs = getSharedPreferences(FreeTimeApplication.PREFNAME, 0);
        prefs.edit().clear().commit();

        return findFreeTimeCalendarId();
    }
}
