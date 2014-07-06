package fr.unice.polytech.calendarmodule;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.prefs.Preferences;

import fr.unice.polytech.datasources.EmptySlotDataSource;
import fr.unice.polytech.datasources.FreeTimeBlockDataSource;
import fr.unice.polytech.datasources.FtEventDataSource;
import fr.unice.polytech.datasources.TaskDataSource;
import fr.unice.polytech.datasources.TaskFtEventDataSource;
import fr.unice.polytech.entities.EmptySlotEntity;
import fr.unice.polytech.entities.FreeTimeBlockEntity;
import fr.unice.polytech.entities.FtEventEntity;
import fr.unice.polytech.entities.FtEventEntity.FtEventType;
import fr.unice.polytech.entities.TaskEntity;
import fr.unice.polytech.freetimedatabase.FreeTimeDbContract;
import fr.unice.polytech.freetimedatabase.FreeTimeDbHelper;

import static java.lang.Math.max;

/**
 * Created by Hakim on 5/6/2014.
 */
public class FreeTimeCalendarService extends Service {

    public class FreeTimeBinder extends Binder {
        public FreeTimeCalendarService getService() {
            return FreeTimeCalendarService.this;
        }
    }

    private final IBinder binder = new FreeTimeBinder();
    private InternalCalendarService internalCalendarService;
    private long freeTimeCalendarId;
    private TaskDataSource taskDataSource;
    private FtEventDataSource ftEventDataSource;
    private FreeTimeBlockDataSource freeTimeBlockDataSource;
    private EmptySlotDataSource emptySlotDataSource;
    private TaskFtEventDataSource taskFtEventDataSource;
    private SimpleDateFormat dateFormat;

    @Override
    public IBinder onBind(Intent intent) {
        freeTimeCalendarId = findFreeTimeCalendarId();
        internalCalendarService = new InternalCalendarService(getApplicationContext(),getContentResolver(),freeTimeCalendarId);
        emptySlotDataSource = new EmptySlotDataSource(getApplicationContext());
        freeTimeBlockDataSource = new FreeTimeBlockDataSource(getApplicationContext());
        ftEventDataSource = new FtEventDataSource(getApplicationContext());
        taskDataSource = new TaskDataSource(getApplicationContext());
        taskFtEventDataSource = new TaskFtEventDataSource(getApplicationContext());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        return binder;
    }

    /*
        Constants for acessing sharedPreferences
     */
    public static final String PREFNAME = "freetime_prefs"; //The name of the sharedPreferences file.
    public static final String PREF_USER_NAME = "user_name"; //The name of the user.
    public static final String PREF_WAKEUP_TIME = "wakeup_time";
    //The start time of the user's effective hours.
    public static final String PREF_WAKEUP_HOUR = "wakeup_hour";
    public static final String PREF_WAKEUP_MINUTE = "wakeup_minute";
    public static final String PREF_BEDTIME = "bedtime";
    //The end time of the user's effective hours.
    public static final String PREF_BEDTIME_HOUR = "bedtime_hour";
    public static final String PREF_BEDTIME_MINUTE = "bedtime_minute";

    /*
        Defaults for sharedPreferences
     */
    public static final int PREF_DEFAULT_WAKEUP_HOUR = 6; //default wakeup time = 6 am.
    public static final int PREF_DEFAULT_WAKEUP_MINUTE = 0;
    public static final int PREF_DEFAULT_BEDTIME_HOUR = 23; //default bedtime = 11 pm.
    public static final int PREF_DEFAULT_BEDTIME_MINUTE = 0;


    /*
        Methods that are called by the Application Activities.
     */

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public Cursor getAllCalendars() {

        String[] projection = new String[]{ CalendarContract.Calendars._ID,
                                            CalendarContract.Calendars.NAME,
                                            CalendarContract.Calendars.ACCOUNT_NAME };

        Cursor calCursor = getContentResolver().query(CalendarContract.Calendars.CONTENT_URI,
                projection, null, null, Calendars.NAME + " ASC");

        return calCursor;
    }

    public void importEventsFromCalendar(long calId){
        internalCalendarService.importEventsFromCalendar(calId);
    }

    //The startTime is also the recurrenceStartTime
    public void createRecurringTask(String title, int[] days, long startTime, long endTime, long recurrenceEndTime) throws IndexDayOutOfBoundException {
        //Recover startTime Hour and Minute
        GregorianCalendar d1 = new GregorianCalendar();
        d1.setTimeInMillis(startTime);
        //Build RecurrenceStringBuilder to define the until attribut
        RecurrenceStringBuilder rb = new RecurrenceStringBuilder();
        //definbe de frequence
        rb.freqByWeek();
        GregorianCalendar dEnd;
        if (recurrenceEndTime > 0) {
            dEnd = new GregorianCalendar();
            dEnd.setTimeInMillis(recurrenceEndTime);
            rb.until(dEnd);
        }
        //Add all day to the recurrence
        for(int d : days){
            try {
                rb.byDay(d);
            } catch (IndexDayOutOfBoundException e) {
                e.printStackTrace();
            }
        }
        //Use the current date to find the first event of the recurrence
        GregorianCalendar current = new GregorianCalendar();
        try {
            current.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)+getCloserDay(days[0]));
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }
        current.set(Calendar.HOUR_OF_DAY,d1.get(Calendar.HOUR_OF_DAY));
        current.set(Calendar.MINUTE,d1.get(Calendar.MINUTE));
        current.set(Calendar.SECOND,0);

        //Creation of the recurrent event
        EventBuilder result = new EventBuilder(freeTimeCalendarId);
        result.createEvent(title);
        result.duration(RecurrenceStringBuilder.timeToDuration(endTime-startTime));
        result.startDT(current.getTimeInMillis());
        result.rRule(rb.getRRule());
        result.timeZone(TimeZone.getDefault().getID());
        //Finalize event
        long eventId = result.finalizeEvent(getContentResolver());

        //Create an FtEvent for each instance of the newly created Event.
        Cursor instances = internalCalendarService.findInstancesOfEvent(eventId, startTime, recurrenceEndTime);
        ArrayList<FtEventEntity> ftEventInstances = new ArrayList<>();
        long duration = endTime - startTime;

        if(instances.moveToFirst()) {
            while(!instances.isAfterLast()){
                long start = instances.getLong(1); //c.f. the projection[] variable in findInstancesOfEvent(); Instances.BEGIN = 1
                long end = start + duration;   //c.f. the projection[] variable in findInstancesOfEvent(); Instances.DURATION = 3
                String recstartdate = dateFormat.format(new Date(start));
                String recendDate = dateFormat.format(new Date(end));
                FtEventEntity ftEvent = ftEventDataSource.createFtEvent(eventId, title, start, end, FtEventType.RECURRING);
                ftEventInstances.add(ftEvent);
                instances.moveToNext();
            }
        }
        instances.close();

        TaskDataSource tds = new TaskDataSource(getApplicationContext());
        tds.createRecurringTask(title,current, endTime, ftEventInstances);
        //TODO: create the Task....
    }

    //Get the closer day wich correspond to the parameter
    public static int getCloserDay(int day) throws IndexDayOutOfBoundException {
        if(day<Calendar.SUNDAY || day>Calendar.SATURDAY) throw new IndexDayOutOfBoundException(day);
        GregorianCalendar result = new GregorianCalendar();
        if(result.get(Calendar.DAY_OF_WEEK) == day){
            return 0;
        }
        for(int i = 1;i<8;i++){
            result.set(Calendar.DAY_OF_WEEK,result.get(Calendar.DAY_OF_WEEK)+1);
            if(result.get(Calendar.DAY_OF_WEEK)==day){
                return i;
            }
        }
        return -1;
    }

    public TaskEntity createOneTimeTask(String title, long startTime, long endTime ) {
        long newEventId = new EventBuilder(freeTimeCalendarId)
                              .createEvent(title).startDT(startTime).endDT(endTime)
                              .timeZone(TimeZone.getDefault().getID())
                              .finalizeEvent(getContentResolver());
        return taskDataSource.createOneTimeTask(title, startTime, endTime, newEventId);

    }

    public ArrayList<FtEventEntity> createLongTermTask(String title, String description, long startDate, long endDate, int hourEstimation,
                                                       int priority) {
        TaskDataSource taskDataSource = new TaskDataSource(getApplicationContext());
        TaskEntity newTask =  taskDataSource.createLongTermTask(title, description, startDate,
                endDate, hourEstimation, priority);
        emptySlotDataSource.clearEmptySlotTable();

        return createLongTermTask(title, description, startDate, endDate, hourEstimation, priority,newTask);

    }

    public ArrayList<FtEventEntity> createLongTermTask(String title, String description, long startDate, long endDate, int hourEstimation,
                                                       int priority, TaskEntity newTask) {


        //we can test if the estimation of the task is higher than the empty slots time

        long newTaskId = newTask.getId();

        long maxStartTime = max(new GregorianCalendar().getTimeInMillis(), startDate);
        Log.i("THE MAX IS ", String.valueOf(maxStartTime));
        Log.i("startDAte ", String.valueOf(startDate));
        Log.i("NOW ", String.valueOf(new GregorianCalendar().getTimeInMillis()));
        detectEmptySlotsDayByDay(maxStartTime, endDate);

        ArrayList<EmptySlotEntity> emptySlots = emptySlotDataSource.getAllEmptySlots();
        int countedHours = 0;
        int loopCounter = 0;

        for(EmptySlotEntity ese : emptySlots){
            long startTime = ese.getStartTime();
            long endTime = ese.getEndTime();
            //get the duration of an emptySlot in hour
            long emptySlotDuration = (endTime-startTime)/3600000;

            //if the duration is more than 1 hour, we can add ftevent in it
            if(emptySlotDuration>=1 && hourEstimation>countedHours){
                if(emptySlots.size() == 1 || loopCounter%2==0) {
                    EventBuilder eventBuilder = new EventBuilder(freeTimeCalendarId);
                    long eventId = eventBuilder.createEvent(title)
                            .timeZone(TimeZone.getDefault().getID())
                            .startDT(startTime)
                            .endDT(startTime+3600000)
                            .color(Color.MAGENTA)
                            .finalizeEvent(getContentResolver());

                    FtEventEntity ftEvent = ftEventDataSource.createFtEvent(eventId, title, startTime, startTime+3600000, FtEventType.LONGTERM);
                    taskFtEventDataSource.createTaskFtEvent(ftEvent.getId(), newTaskId);

                    countedHours++;
                }
            }
            loopCounter++;
        }

        if(!emptySlots.isEmpty()){
            if(countedHours<hourEstimation){
                Toast.makeText(getApplicationContext(),"we reached the end of empty slots. and we have to add: " +
                                                       (hourEstimation-countedHours), Toast.LENGTH_SHORT).show();
                createLongTermTask(title,description,(startDate),endDate,(hourEstimation-countedHours),priority);
            }

            //TODO step to add a longterm task in the freeTimeCalendar
            //Recover all the eventFT between the start and the end of the newly added  task
            //Recover all freeTimeBlock between the start and the end of the newly added task
            //Verify the new duration can be add to the new interval
            //Calculate the weight of each event in the interval (priority, deadLine)
            //Sort all events
            //detect all overlapping tasks
            //find the latest end time among the overlapping tasks
            //detect empty slots between max(now, startDate) and latest end time.
            //calculate weight for each task in the overlapping tasks list
        }
        return null;
    }

    public ArrayList<FtEventEntity> findFtEventsInTimeRange(long startRange, long endRange) {
        String fftestartdate = dateFormat.format(new Date(startRange));
        String ffteendDate = dateFormat.format(new Date(endRange));
        ArrayList<FtEventEntity> ftEventsInRange = ftEventDataSource.findFtEventInTimeRange(startRange, endRange);
        return  ftEventsInRange;
    }


    /*
        Methods that are called by the TaskOptimisationModule
     */

    public long createEvent(String title, int startYear, int startMonth, int startDay,
                            int endYear, int endMonth, int endDay) {
        //long calId = findFreeTimeCalendarId();

        EventBuilder eb = new EventBuilder(freeTimeCalendarId);
        return eb.createEvent(title)
                .startY(startYear).startM(startMonth).startD(startDay)
                .endY(endYear).endM(endMonth).endD(endDay)
                .timeZone(TimeZone.getDefault().getID())
                .finalizeStartTime().finalizeEndTime()
                .allDay(true).description("description goes here")
                .availability(Events.AVAILABILITY_BUSY)
                .organizer("demo@freetime.com")
                .finalizeEvent(getContentResolver());
    }

    public long createEvent(String title, String description, boolean available,
                            int startYear, int startMonth, int startDay,
                            int startHour, int startMinute,
                            int endYear, int endMonth, int endDay,
                            int endHour, int endMinute, boolean allDay) {

        long calId = findFreeTimeCalendarId();

        EventBuilder eb = new EventBuilder(calId);
        return eb.createEvent(title)
                .startY(startYear).startM(startMonth).startD(startDay)
                .startH(startHour).startMin(startMinute).startS(0)
                .endY(endYear).endM(endMonth).endD(endDay)
                .endH(endHour).endMin(endMinute).endS(0)
                .timeZone(TimeZone.getDefault().getID())
                .finalizeStartTime().finalizeEndTime()
                .allDay(allDay).description(description == null ? "" : description)
                .availability(available ? Events.AVAILABILITY_FREE : Events.AVAILABILITY_BUSY)
                .finalizeEvent(getContentResolver());
    }

    public Cursor findEventByTitle(String title){
        String[] projection = {Events._ID, Events.TITLE, Events.RRULE, Events.DESCRIPTION,
                                       Events.DTSTART, Events.DTEND, Events.AVAILABILITY, Events.DURATION,};
        String selection = Events.TITLE + " = ? ";
        String[]selArgs = new String[] {title};
        Cursor cursor = getContentResolver().query(Events.CONTENT_URI, projection, selection, selArgs, null);

        return cursor;
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
        return calId;
    }


    public FreeTimeBlockEntity createFreeTimeBlock(int day, int startHourTime, int startMinuteTime,
                                                   int endHourTime, int endMinuteTime) throws IndexDayOutOfBoundException {

        int dayDifference = getCloserDay(day);

        Calendar calStart = new GregorianCalendar();
        calStart.set(Calendar.HOUR_OF_DAY, startHourTime);
        calStart.set(Calendar.MINUTE, startMinuteTime);
        calStart.set(Calendar.DAY_OF_WEEK, calStart.get(Calendar.DAY_OF_WEEK) + dayDifference);

        Calendar calEnd = new GregorianCalendar();
        calEnd.set(Calendar.HOUR_OF_DAY, endHourTime);
        calEnd.set(Calendar.MINUTE, endMinuteTime);
        calEnd.set(Calendar.DAY_OF_WEEK, calEnd.get(Calendar.DAY_OF_WEEK) + dayDifference);

        long duration = calEnd.getTimeInMillis() - calStart.getTimeInMillis();

        //fetch the number of the day

        EventBuilder eventBuilder = new EventBuilder(freeTimeCalendarId);
        eventBuilder.createEvent("Free Time")
                    .startDT(calStart.getTimeInMillis()).endDT(calEnd.getTimeInMillis())
                    .allDay(false) .timeZone(TimeZone.getDefault().getID());

        RecurrenceStringBuilder recurrenceStringBuilder = new RecurrenceStringBuilder();
        String rRule = recurrenceStringBuilder.freqByWeek().getRRule();
        eventBuilder.rRule(rRule);
        Long eventId = eventBuilder.finalizeEvent(getContentResolver());

        GregorianCalendar until = new GregorianCalendar();
        until.setTimeInMillis(calStart.getTimeInMillis());
        until.set(Calendar.YEAR, until.get(Calendar.YEAR)+1);
        Cursor instances = internalCalendarService.findInstancesOfEvent(eventId, calStart.getTimeInMillis(), until.getTimeInMillis());
        ArrayList<FtEventEntity> ftEventInstances = new ArrayList<>();

        if(instances.moveToFirst()) {
            while(!instances.isAfterLast()){
                long start = instances.getLong(1); //c.f. the projection[] variable in findInstancesOfEvent(); Instances.BEGIN = 1
                long end = start + duration;   //c.f. the projection[] variable in findInstancesOfEvent(); Instances.DURATION = 3
                String ftbstartdate = dateFormat.format(new Date(start));
                String ftbendDate = dateFormat.format(new Date(end));
                FtEventEntity ftEvent = ftEventDataSource.createFtEvent(eventId, "Free Time", start, end, FtEventType.FREETIME);
                ftEventInstances.add(ftEvent);
                instances.moveToNext();
            }
        }
        instances.close();


        return freeTimeBlockDataSource.createFreeTimeBlock(day,calStart.getTimeInMillis(),calEnd.getTimeInMillis(),eventId);
    }

    public ArrayList<TaskEntity> findAllTasksInRange(long startRange, long endRange){
        return taskDataSource.getTasksInRange(startRange, endRange);
    }

    public ArrayList<TaskEntity> findAllTasks() {
        return taskDataSource.getAllTasks();
    }

    public long resetCalendarAndPrefs() {
        FreeTimeDbHelper freeTimeDbHelper = new FreeTimeDbHelper(getApplicationContext());
        freeTimeDbHelper.reset(freeTimeDbHelper.getWritableDatabase());
        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "com.freetime");
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,"true");
        getContentResolver().delete(builder.build(), null, null);

        builder = Events.CONTENT_URI.buildUpon();
        getContentResolver().delete(builder.build(), Events.CALENDAR_ID + " = ? ", new String[]{String.valueOf(freeTimeCalendarId)});

        SharedPreferences prefs = getSharedPreferences(FreeTimeCalendarService.PREFNAME, 0);
        prefs.edit().clear().commit();

        return findFreeTimeCalendarId();
    }

    /*
        Private methods
     */

    private Uri createCalendar(){
        ContentValues values = new ContentValues();
        values.put(Calendars.ACCOUNT_NAME, "com.freetime");
        values.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(Calendars.NAME, "FreeTime Calendar");
        values.put(Calendars.CALENDAR_DISPLAY_NAME, "FreeTime Calendar");
        values.put(Calendars.CALENDAR_COLOR, 0xffff0000);
        values.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
        values.put(Calendars.OWNER_ACCOUNT, "freetime@gmail.com");
        values.put(Calendars.CALENDAR_TIME_ZONE, "Europe/Paris");
        Uri.Builder builder = Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(Calendars.ACCOUNT_NAME, "com.freetime");
        builder.appendQueryParameter(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,"true");
        Uri uri = getContentResolver().insert(builder.build(), values);

        System.out.println(uri);
        Toast.makeText(getApplicationContext(), "Created FreeTime Calendar" , Toast.LENGTH_SHORT).show();
        return uri;
    }

    //TODO change back to private later.
    public Cursor getAllEventsFromCalendar(long calId) {
        Cursor cursor = getContentResolver().query(Events.CONTENT_URI, null, Events.CALENDAR_ID + " = ? ",
                new String[]{Long.toString(calId)}, Events.DTSTART + " ASC");
        return cursor;
    }

    public void findUnoccupiedTimeSlots(long beginRange, long endRange, EmptySlotDataSource ds){
        internalCalendarService.findUnoccupiedTimeSlots(beginRange, endRange, ds);
    }

    public void detectEmptySlotsDayByDay(long start, long end){
        SharedPreferences prefs = getSharedPreferences(PREFNAME, 0);

        internalCalendarService.detectEmptySlotsDayByDay(start, end, prefs);
    }

    public void setFreeTimeCalendarId(long freeTimeCalendarId) {
        this.freeTimeCalendarId = freeTimeCalendarId;
    }

    public long getFreeTimeCalendarId() {
        return freeTimeCalendarId;
    }

    public void setInternalCalendarServiceContext(Context context ){
        internalCalendarService.setContext(context);
        taskDataSource.setContext(context);
        emptySlotDataSource.setContext(context);
        ftEventDataSource.setContext(context);
        freeTimeBlockDataSource.setContext(context);
    }



}
