import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.test.RenamingDelegatingContext;
import android.test.ServiceTestCase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import fr.unice.polytech.calendarmodule.FreeTimeCalendarService;
import fr.unice.polytech.calendarmodule.FreeTimeCalendarService.FreeTimeBinder;
import fr.unice.polytech.datasources.DataSource;
import fr.unice.polytech.datasources.EmptySlotDataSource;
import fr.unice.polytech.datasources.FtEventDataSource;
import fr.unice.polytech.datasources.TaskDataSource;
import fr.unice.polytech.entities.EmptySlotEntity;
import fr.unice.polytech.entities.FtEventEntity;
import fr.unice.polytech.freetimedatabase.FreeTimeDbContract;

import static java.lang.Math.max;


/**
 * Created by Hakim on 11/06/2014.
 */

public class FreeTimeCalendarServiceTest extends ServiceTestCase<FreeTimeCalendarService>{
    private FreeTimeCalendarService ftcService;

    public FreeTimeCalendarServiceTest() {
        super(FreeTimeCalendarService.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        FreeTimeBinder binder = (FreeTimeBinder)bindService(new Intent(getContext(), FreeTimeCalendarService.class));
        ftcService = binder.getService();
    }

    public void testFindEmptySlots() throws Exception {

        Calendar calStart = new GregorianCalendar(2014, 5, 11, 0, 0, 0);
        Calendar calEnd = new GregorianCalendar(2014, 5, 11, 23, 59, 0);
        //ftcService.createEvent("Test Event", calStart, calEnd);

        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        EmptySlotDataSource ds = new EmptySlotDataSource(context);
        //ds.clearEmptySlotTable();
        ftcService.findUnoccupiedTimeSlots(calStart.getTimeInMillis(), calEnd.getTimeInMillis(), ds);

        Cursor cursor = ftcService.findEventByTitle("Recurring");
        cursor.moveToFirst();

        String rrule = cursor.getString(2);

        System.out.println(rrule);
    }


    public void testFindEvents() throws Exception {
        long ftCalId = ftcService.findFreeTimeCalendarId();

        Calendar calStart = new GregorianCalendar(2014, 5, 11, 0, 0, 0);
        Calendar calEnd = new GregorianCalendar(2014, 5, 11, 23, 59, 0);

        String selection = CalendarContract.Instances.CALENDAR_ID + " = ? ";
        String[] selArgs = new String[]{String.valueOf(ftCalId)};

        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, calStart.getTimeInMillis());
        ContentUris.appendId(builder, calEnd.getTimeInMillis());
        Cursor cursor = ftcService.getContentResolver().query(builder.build(), null, selection, selArgs, null);


        if(cursor.moveToFirst()) {
            do{
                String title = cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.TITLE));
                long startTime = cursor.getLong(cursor.getColumnIndex(CalendarContract.Instances.BEGIN));
                long endTime = cursor.getLong(cursor.getColumnIndex(CalendarContract.Instances.END));
                long calID = cursor.getLong(cursor.getColumnIndex(CalendarContract.Instances.CALENDAR_ID));

                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");

                String dateInfo = "title: " + title + " calID = " + calID
                        +  " start : "  + dateFormatter.format(new Date(startTime))
                        +  " end : " + dateFormatter.format(new Date(endTime));


                Log.i("FreeTime", "event info : " + dateInfo);

            } while(cursor.moveToNext());
        }

        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }

    public void testFindDayByDay() throws Exception{

        Calendar calStart = new GregorianCalendar(2014, 5, 11, 0, 0, 0);
        Calendar calEnd = new GregorianCalendar(2014, 5, 13, 23, 59, 0);
        //ftcService.createEvent("Test Event", calStart, calEnd);

        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        EmptySlotDataSource emptySlotDataSource = new EmptySlotDataSource(context);
        ftcService.setInternalCalendarServiceContext(context);
        emptySlotDataSource.clearEmptySlotTable();

        //ds.clearEmptySlotTable();
        ftcService.detectEmptySlotsDayByDay(calStart.getTimeInMillis(), calEnd.getTimeInMillis());

        ArrayList<EmptySlotEntity> emptySlots = emptySlotDataSource.getAllEmptySlots();
        String startDate1 = new Date(emptySlots.get(0).getStartTime()).toString();
        String endDate1 = new Date(emptySlots.get(0).getEndTime()).toString();
        String startDate2 = new Date(emptySlots.get(1).getStartTime()).toString();
        String endDate2 = new Date(emptySlots.get(1).getEndTime()).toString();
        String startDate3 = new Date(emptySlots.get(2).getStartTime()).toString();
        String endDate3 = new Date(emptySlots.get(2).getEndTime()).toString();
        System.out.println();
       /* DataSource ds = new DataSource(context);
        ContentValues values = new ContentValues();
        values.put(FreeTimeDbContract.EmptySlots.COLUMN_START_TIME, calStart.getTimeInMillis());
        values.put(FreeTimeDbContract.EmptySlots.COLUMN_END_TIME, calEnd.getTimeInMillis());




        cursor.moveToFirst();

        String rrule = cursor.getString(2);

        System.out.println(rrule);*/
    }

    public void testCreateLongTermTask() throws Exception {
        String title = "Major Epic Project We don't have enough time for";
        String description = "there's no more time";
        long startTime = new GregorianCalendar(2014, 5, 20, 0, 0, 0).getTimeInMillis();
        long endTime = new GregorianCalendar(2014, 5, 29, 23, 59, 0).getTimeInMillis();
        int hourEstimation = 30;
        int priority = 1;

        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");

        EmptySlotDataSource emptySlotDataSource = new EmptySlotDataSource(context);
        ftcService.setInternalCalendarServiceContext(context);
        emptySlotDataSource.clearEmptySlotTable();

        ftcService.createLongTermTask(title, description, startTime, endTime, hourEstimation, priority);

        ftcService.detectEmptySlotsDayByDay(max(new GregorianCalendar().getTimeInMillis(), startTime), endTime);

        ArrayList<EmptySlotEntity> emptySlots = emptySlotDataSource.getAllEmptySlots();

        for(EmptySlotEntity es : emptySlots) {
            Log.i("task creation", String.valueOf(es.getStartTime()));
            Log.i("task creation", String.valueOf(es.getEndTime()));
        }

        System.out.println();

    }

    public void testCreateOneTimeTask() throws  Exception{
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        ftcService.setInternalCalendarServiceContext(context);

        long startTime = new GregorianCalendar(2014, 5, 20, 15, 0, 0).getTimeInMillis();
        long endTime = new GregorianCalendar(2014, 5, 20, 16, 30, 0).getTimeInMillis();

        ftcService.createOneTimeTask("OneTimeTask",startTime, endTime );
    }

    public void testFindEventsInTimeRange(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        ftcService.setInternalCalendarServiceContext(context);

        long startTime = new GregorianCalendar(2014, 5, 20, 12, 0, 0).getTimeInMillis();
        long endTime = new GregorianCalendar(2014, 5, 20, 17, 0, 0).getTimeInMillis();

        long startTimeTask = new GregorianCalendar(2014, 5, 20, 15, 0, 0).getTimeInMillis();
        long endTimeTask = new GregorianCalendar(2014, 5, 20, 16, 30, 0).getTimeInMillis();

        ftcService.createOneTimeTask("I love tasks",startTimeTask,endTimeTask);

        FtEventDataSource ftEventDataSource = new FtEventDataSource(context);

        ArrayList<FtEventEntity> allFtEvents = ftEventDataSource.getAllFtEvents();

        ArrayList<FtEventEntity> ftEventEntityArrayList =
                ftcService.findFtEventsInTimeRange(startTime,endTime);

        System.out.print("");
    }
}