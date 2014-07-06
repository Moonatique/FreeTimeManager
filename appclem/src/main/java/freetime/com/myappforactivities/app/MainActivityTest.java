package freetime.com.myappforactivities.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.unice.polytech.calendarmodule.FreeTimeCalendarService;
import fr.unice.polytech.calendarmodule.IndexDayOutOfBoundException;
import fr.unice.polytech.calendarmodule.RecurrenceStringBuilder;
import fr.unice.polytech.datasources.FtEventDataSource;
import fr.unice.polytech.entities.FtEventEntity;


public class MainActivityTest extends Activity {
    private FreeTimeCalendarService ftcService;
    private long idCalendar;
    private static final int INIT_ID_CALENDAR = 0;


    private ServiceConnection ftcServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            ftcService = ((FreeTimeCalendarService.FreeTimeBinder)service).getService();

            Toast.makeText(getApplicationContext(), "@string/service_connected", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            ftcService = null;

            Toast.makeText(getApplicationContext(), "@string/service_disconnected", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_test);
        idCalendar=INIT_ID_CALENDAR;
        bindService(new Intent(getApplicationContext(), FreeTimeCalendarService.class), ftcServiceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_test, menu);
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

    public void testFreeBlockCreation(View v){
        //Second Call de free time calendar if it isn't
        if(idCalendar == INIT_ID_CALENDAR) {
            System.out.println("Reading Calendar");
            idCalendar = ftcService.findFreeTimeCalendarId();
            System.out.println(idCalendar);
        }

        Calendar calStart = new GregorianCalendar(2014, 5, 11, 13, 0, 0);
        Calendar calEnd = new GregorianCalendar(2014, 5, 11, 14, 0, 0);
        try {
            ftcService.createFreeTimeBlock(Calendar.WEDNESDAY, 13,30,14,30);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    public void testFtEventPersistence(View v){
        long startTime = new GregorianCalendar(2014, 5, 20, 12, 0, 0).getTimeInMillis();
        long endTime = new GregorianCalendar(2014, 5, 20, 17, 0, 0).getTimeInMillis();

        long startTimeTask = new GregorianCalendar(2014, 5, 20, 15, 0, 0).getTimeInMillis();
        long endTimeTask = new GregorianCalendar(2014, 5, 20, 16, 30, 0).getTimeInMillis();

        ftcService.createOneTimeTask("I love tasks",startTimeTask,endTimeTask);

        FtEventDataSource ftEventDataSource = new FtEventDataSource(getApplicationContext());

        ArrayList<FtEventEntity> allFtEvents = ftEventDataSource.getAllFtEvents();

        ArrayList<FtEventEntity> ftEventEntityArrayList = ftcService.findFtEventsInTimeRange(startTime,endTime);

        System.out.print("");
    }

    public void testLongTaskCreation(View v){

        long startTime = new GregorianCalendar(2014, 5, 27, 12, 0, 0).getTimeInMillis();
        long endTime = new GregorianCalendar(2014, 6, 30, 17, 0, 0).getTimeInMillis();


        long startTimeTask = new GregorianCalendar(2014, 5, 22, 15, 0, 0).getTimeInMillis();
        long endTimeTask = new GregorianCalendar(2014, 5, 22,16, 30, 0).getTimeInMillis();


       // ftcService.createOneTimeTask("taskTEST",startTimeTask,endTimeTask);


        ftcService.createLongTermTask("this is a long task","project for 10months",startTime,endTime,30,1);
    }

    public void testImportAsFtEvent(View v) {

    }

    public void testRecurringTaskCreation(View vi) {
        long startTime = new GregorianCalendar(2014, 6, 10, 12, 0, 0).getTimeInMillis();
        long endTime = new GregorianCalendar(2014, 6, 10, 14, 0, 0).getTimeInMillis();
        int[] days = new int[] {2, 3};
        long recurrenceEndTime = new GregorianCalendar(2014, 7, 20, 12, 0, 0).getTimeInMillis();

        try {
            ftcService.createRecurringTask("recurring thingy", days, startTime, endTime, recurrenceEndTime);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }
    }
}
