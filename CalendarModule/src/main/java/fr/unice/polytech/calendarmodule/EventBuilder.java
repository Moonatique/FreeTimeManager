package fr.unice.polytech.calendarmodule;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.CalendarContract.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Hakim on 06/06/2014.
 */
@SuppressWarnings("ResourceType")
public class EventBuilder {
    protected ContentValues values;
    protected Calendar calStart;
    protected Calendar calEnd;

    public EventBuilder(){
        this(-1);
    }

    public EventBuilder(long calID) {
        values = new ContentValues();
        if(calID != -1)  values.put(Events.CALENDAR_ID, calID);
        calStart = new GregorianCalendar();
        calStart.setTimeZone(TimeZone.getDefault());
        calStart.set(Calendar.HOUR, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);

        calEnd = new GregorianCalendar();
        calEnd.setTimeZone(TimeZone.getDefault());
        calEnd.set(Calendar.HOUR, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, 0);
    }

    public EventBuilder createEvent(String title) {
        values.put(Events.TITLE, title);
        return this; }

    public EventBuilder startY(int startYear) { calStart.set(Calendar.YEAR, startYear); return this; }
    public EventBuilder startM(int startMonth) { calStart.set(Calendar.MONTH, startMonth); return this; }
    public EventBuilder startD(int startDay) { calStart.set(Calendar.DAY_OF_MONTH, startDay); return this; }
    public EventBuilder startH(int startHour) { calStart.set(Calendar.HOUR, startHour); return this; }
    public EventBuilder startMin(int startMinute) { calStart.set(Calendar.MINUTE, startMinute); return this; }
    public EventBuilder startS(int startSecond) { calStart.set(Calendar.SECOND, startSecond); return this; }
    public EventBuilder finalizeStartTime() {
        long start = calStart.getTimeInMillis(); values.put(Events.DTSTART, start);
        return this;
    }
    public EventBuilder startDT(long startDT) { values.put(Events.DTSTART, startDT); return this;}

    public EventBuilder endY(int endYear) { calEnd.set(Calendar.YEAR, endYear); return this; }
    public EventBuilder endM(int endMonth) { calEnd.set(Calendar.MONTH, endMonth); return this; }
    public EventBuilder endD(int endDay) { calEnd.set(Calendar.DAY_OF_MONTH, endDay); return this; }
    public EventBuilder endH(int endHour) { calEnd.set(Calendar.HOUR, endHour); return this; }
    public EventBuilder endMin(int endMinute) { calEnd.set(Calendar.MINUTE, endMinute); return this; }
    public EventBuilder endS(int endSecond) { calEnd.set(Calendar.SECOND, endSecond); return this; }
    public EventBuilder finalizeEndTime() {
        long end = calEnd.getTimeInMillis(); values.put(Events.DTEND, end);
        return this;
    }
    public EventBuilder endDT(long endDT) { values.put(Events.DTEND, endDT); return this;}

    public EventBuilder location(String location) { values.put(Events.EVENT_LOCATION, location); return this;}
    public EventBuilder calendarID(long calID) { values.put(Events.CALENDAR_ID, calID); return this;}
    public EventBuilder timeZone(String timeZone) { values.put(Events.EVENT_TIMEZONE, timeZone); return this;}
    public EventBuilder description(String description) { values.put(Events.DESCRIPTION, description); return this;}

    public EventBuilder allDay(boolean allDay) { values.put(Events.ALL_DAY, allDay? 1:0); return this;}
    public EventBuilder organizer(String organizer) { values.put(Events.ORGANIZER, organizer); return this; }
    public EventBuilder availability(int availability ) { values.put(Events.AVAILABILITY, availability); return this;}
    public EventBuilder duration(String duration){values.put(Events.DURATION,duration);return this;}
    public EventBuilder rRule(String rRule) { values.put(Events.RRULE, rRule); return this; }

    public EventBuilder color(int colors){ values.put(Events.EVENT_COLOR,colors); return this;}

    public long finalizeEvent(ContentResolver contentResolver) {
        Uri uri = contentResolver.insert(Events.CONTENT_URI, values);
        long eventId = new Long(uri.getLastPathSegment());
        System.out.println("Event created: " + eventId + " title: " + values.get(Events.TITLE));
        return eventId;
    }
}
