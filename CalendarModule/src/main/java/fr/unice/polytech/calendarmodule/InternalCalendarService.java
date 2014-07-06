package fr.unice.polytech.calendarmodule;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Instances;
import android.provider.CalendarContract.Events;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import fr.unice.polytech.datasources.EmptySlotDataSource;
import fr.unice.polytech.datasources.FtEventDataSource;
import fr.unice.polytech.entities.FtEventEntity;

/**
 * Created by user on 18/06/2014.
 */
public class InternalCalendarService {

    private long calendarId;
    private ContentResolver contentResolver;
    private Context context;
    private long userWakeupTime = -1;
    private long userBedtime = -1;
    private SimpleDateFormat dateFormat;

    public InternalCalendarService(Context context, ContentResolver contentResolver,long calendarId){
        this.context = context;
        this.contentResolver = contentResolver;
        this.calendarId = calendarId;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public void importEventsFromCalendar(long calId){
        Cursor ec = getAllEventsFromCalendar(calId);

        if(ec.moveToFirst()) {
            do{
                String title = ec.getString(ec.getColumnIndex(Events.TITLE));
                long start = ec.getLong(ec.getColumnIndex(Events.DTSTART));
                long end = ec.getLong(ec.getColumnIndex(Events.DTEND));
                long duration = ec.getLong(ec.getColumnIndex(Events.DURATION));
                String timeZone = ec.getString(ec.getColumnIndex(Events.EVENT_TIMEZONE));
                String location = ec.getString(ec.getColumnIndex(Events.EVENT_LOCATION));
                int allDay = ec.getInt(ec.getColumnIndex(Events.ALL_DAY));
                String rRule = ec.getString(ec.getColumnIndex(Events.RRULE));
                int availability = ec.getInt(ec.getColumnIndex(Events.AVAILABILITY));

                long eventId = new EventBuilder(calendarId).createEvent(title).startDT(start).endDT(end)
                        .timeZone(timeZone).location(location).allDay(allDay==1).rRule(rRule)
                        .availability(availability).finalizeEvent(contentResolver);

                FtEventDataSource ftEventDataSource = new FtEventDataSource(context);
                ftEventDataSource.createFtEvent(eventId, title, start, end, FtEventEntity.FtEventType.IMPORTED);

            }while(ec.moveToNext());

            ec.close();
        }
    }

    public Cursor getAllEventsFromCalendar(long calId) {
        Cursor cursor = contentResolver.query(Events.CONTENT_URI, null, Events.CALENDAR_ID + " = ? ",
                new String[]{Long.toString(calId)}, Events.DTSTART + " ASC");
        return cursor;
    }

    private Cursor findInstancesInTimeRange(long beginRange, long endRange) {
        String searchBegin = dateFormat.format(new Date(beginRange));
        String searchEnd = dateFormat.format(new Date(endRange));
        String[] projection = new String[] {
                Instances._ID, Instances.BEGIN, Instances.END, Instances.EVENT_ID, Instances.CALENDAR_ID, Instances.TITLE
        };
        String selection = Instances.CALENDAR_ID + " = ? AND "
                         + Instances.BEGIN + " >= ? AND "
                         + Instances.BEGIN + " < ?";
        String[] selArgs = new String[]{String.valueOf(calendarId), String.valueOf(beginRange), String.valueOf(endRange)};

        Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, beginRange);
        ContentUris.appendId(builder, endRange);

        Cursor cursor = contentResolver.query(builder.build(), projection, selection, selArgs, Instances.BEGIN + " ASC");

        return cursor;
    }

    public Cursor findInstancesOfEvent(long eventId, long beginRange, long endRange) {
        String fiestartdate = dateFormat.format(new Date(beginRange));
        String fieendDate = dateFormat.format(new Date(endRange));
        String[] projection = new String[] {
                Instances._ID, Instances.BEGIN, Instances.END, Instances.DURATION, Instances.EVENT_ID, Instances.CALENDAR_ID, Instances.TITLE
        };
        String selection = Instances.CALENDAR_ID + " = ? AND " + Instances.EVENT_ID + " = ?  AND "
                                                 + Instances.BEGIN + " >= ? AND " + Instances.BEGIN + " < ?";
        String[] selArgs = new String[]{String.valueOf(calendarId), String.valueOf(eventId),
                                                       String.valueOf(beginRange), String.valueOf(endRange)};

        Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, beginRange);
        ContentUris.appendId(builder, endRange);


        Cursor cursor = contentResolver.query(builder.build(), projection, selection, selArgs, Instances.BEGIN + " ASC");

        return cursor;
    }

    public void findUnoccupiedTimeSlots(long beginRange, long endRange, EmptySlotDataSource ds) {

        EmptySlotDataSource emptySlotDS;

        if(ds == null) { emptySlotDS = new EmptySlotDataSource(context.getApplicationContext()); }
        else { emptySlotDS = ds; }

        Cursor instancesInTimeRange = findInstancesInTimeRange(beginRange, endRange);

        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
        Log.d("findUnoccupiedTimeSlots", "calling with beginRange = "
                + df.format(new Date(beginRange)) + " endRange = " + df.format(new Date(endRange)));
        Log.d("findUnoccupiedTimeSlots", "nb Events found = " + instancesInTimeRange.getCount());

        findUnoccupiedTimeSlots(instancesInTimeRange, beginRange, endRange, emptySlotDS);
    }

    public void findUnoccupiedTimeSlots(Cursor instancesInTimeRange, long beginRange, long endRange,
                                        EmptySlotDataSource emptySlotDS) {

        String beginrange = dateFormat.format(new Date(beginRange));
        String endrange = dateFormat.format(new Date(endRange));

        final int BEGIN = instancesInTimeRange.getColumnIndex(Instances.BEGIN);
        final int END = instancesInTimeRange.getColumnIndex(Instances.END);
        final int TITLE = instancesInTimeRange.getColumnIndex(Instances.TITLE);

        //TODO: ignore instances of AllDay Events, and perhaps Events with availability = available.
        if(instancesInTimeRange.getCount() == 0) {
            //If there are no event instances in a given time range, that whole time range is an empty slot
            emptySlotDS.createEmptySlot(beginRange, endRange);
            instancesInTimeRange.close();
        }

        else {
            instancesInTimeRange.moveToFirst();
            String instancestart = dateFormat.format(new Date(instancesInTimeRange.getLong(BEGIN)));
            if(instancesInTimeRange.getLong(BEGIN) != beginRange) {
                //the starttime of the first empty slot in a given time range is equal to the start time of that time range (beginRange)
                //if there is no event instance that starts at time=beginRange. The endTime of the EmptySlot is the BEGIN time of the
                //found instance.
                emptySlotDS.createEmptySlot(beginRange, instancesInTimeRange.getLong(BEGIN));
            }

            long newBeginRange = instancesInTimeRange.getLong(END);
            String newbeginrange = dateFormat.format(new Date(instancesInTimeRange.getLong(END)));
            instancesInTimeRange.close();
            findUnoccupiedTimeSlots(newBeginRange, endRange, emptySlotDS);
        }
    }


    public void detectEmptySlotsDayByDay(long start, long end, SharedPreferences prefs){

        // get the user preferences for his effective hours.

        int userWakeHour = prefs.getInt(FreeTimeCalendarService.PREF_WAKEUP_HOUR, FreeTimeCalendarService.PREF_DEFAULT_WAKEUP_HOUR);
        int userWakeMinutes = prefs.getInt(FreeTimeCalendarService.PREF_WAKEUP_MINUTE, FreeTimeCalendarService.PREF_DEFAULT_WAKEUP_MINUTE);

        int userBedtimeHour = prefs.getInt(FreeTimeCalendarService.PREF_BEDTIME_HOUR, FreeTimeCalendarService.PREF_DEFAULT_BEDTIME_HOUR);
        int userBedtimeMinutes = prefs.getInt(FreeTimeCalendarService.PREF_BEDTIME_MINUTE, FreeTimeCalendarService.PREF_DEFAULT_BEDTIME_MINUTE);


        GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(new Date(start));
        GregorianCalendar firstDayEndTime = new GregorianCalendar();
        firstDayEndTime.set(Calendar.YEAR, gCal.get(Calendar.YEAR));
        firstDayEndTime.set(Calendar.MONTH, gCal.get(Calendar.MONTH));
        firstDayEndTime.set(Calendar.DAY_OF_MONTH, gCal.get(Calendar.DAY_OF_MONTH));
        firstDayEndTime.set(Calendar.HOUR_OF_DAY, userBedtimeHour);
        firstDayEndTime.set(Calendar.MINUTE, userBedtimeMinutes);
        firstDayEndTime.set(Calendar.SECOND, 0);

        findUnoccupiedTimeSlots(start, firstDayEndTime.getTimeInMillis(),null);


        long nextDayStart = calculateNextDayStart(firstDayEndTime.getTimeInMillis(), userWakeHour, userWakeMinutes);
        long nextDayStop = calculateNextDayStop(nextDayStart, userBedtimeHour, userBedtimeMinutes);

        while(nextDayStop < end) {
            String nextstart = new Date(nextDayStart).toString();
            String nextstop = new Date(nextDayStop).toString();
            findUnoccupiedTimeSlots(nextDayStart, nextDayStop,null);
            nextDayStart = calculateNextDayStart(nextDayStop, userWakeHour, userWakeMinutes);
            nextDayStop = calculateNextDayStop(nextDayStart, userBedtimeHour, userBedtimeMinutes);
        }

        long lastDayStart = nextDayStart;
        findUnoccupiedTimeSlots(lastDayStart, end,null);
    }

    private long calculateNextDayStart(long firstDayStop, int wakeupHour, int wakeupMinute) {
        final long ONE_SECOND = 1000;

        GregorianCalendar nextDayStartTime = new GregorianCalendar();
        nextDayStartTime.setTimeInMillis(firstDayStop);
        nextDayStartTime.set(Calendar.HOUR_OF_DAY, 23);
        nextDayStartTime.set(Calendar.MINUTE, 59);
        nextDayStartTime.set(Calendar.SECOND, 59);
        nextDayStartTime.setTimeInMillis(nextDayStartTime.getTimeInMillis() + ONE_SECOND);
        nextDayStartTime.set(Calendar.HOUR_OF_DAY, wakeupHour);
        nextDayStartTime.set(Calendar.MINUTE, wakeupMinute);
        nextDayStartTime.set(Calendar.SECOND, 0);
        nextDayStartTime.set(Calendar.MILLISECOND, 0);

        return nextDayStartTime.getTimeInMillis();
    }

    private long calculateNextDayStop(long nextDayStart, int bedtimeHour, int bedtimeMinute) {
        GregorianCalendar nextDayStopTime = new GregorianCalendar();
        nextDayStopTime.setTimeInMillis(nextDayStart);
        nextDayStopTime.set(Calendar.HOUR_OF_DAY, bedtimeHour);
        nextDayStopTime.set(Calendar.MINUTE, bedtimeMinute);
        nextDayStopTime.set(Calendar.SECOND, 0);
        nextDayStopTime.set(Calendar.MILLISECOND, 0);

        return nextDayStopTime.getTimeInMillis();
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
