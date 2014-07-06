package fr.unice.polytech.appyoann;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import fr.unice.polytech.calendarmodule.EventBuilder;
import fr.unice.polytech.calendarmodule.FreeTimeCalendarService;
import fr.unice.polytech.calendarmodule.IndexDayOutOfBoundException;
import fr.unice.polytech.calendarmodule.IndexMonthOutOfBoundException;
import fr.unice.polytech.calendarmodule.RecurrenceStringBuilder;


public class MainActivity extends Activity {
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
        setContentView(R.layout.activity_main);
        idCalendar=INIT_ID_CALENDAR;
        bindService(new Intent(getApplicationContext(), FreeTimeCalendarService.class), ftcServiceConnection, Context.BIND_AUTO_CREATE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(ftcServiceConnection);

    }

    /**
     * Test 1 : Recurrence Events construct with RecurrenceStringBuilder
     */
    private static final String DELETE_TEST_1 = "DELETE_TEST_1";
    private ArrayList<Long> eventsTest1;
    private static  final int[] COLORS_TEST = {Color.CYAN,Color.GREEN,Color.BLUE,Color.MAGENTA,Color.YELLOW,Color.GRAY,Color.WHITE};
    public ArrayList<String> init_rrule_test_1(){
        GregorianCalendar g1 = new GregorianCalendar();
        //g1.set(Calendar.DAY_OF_MONTH,g1.get(Calendar.DAY_OF_MONTH)+5);g1.set(Calendar.HOUR,0);g1.set(Calendar.MINUTE,0);g1.set(Calendar.SECOND,0);
        System.out.println(g1);
        GregorianCalendar g2 = new GregorianCalendar();
        g2.set(Calendar.DAY_OF_MONTH,g2.get(Calendar.DAY_OF_MONTH)+5);g2.set(Calendar.HOUR,14);g2.set(Calendar.MINUTE,0);g2.set(Calendar.SECOND,0);

        ArrayList<String> result = new ArrayList<String>();
        RecurrenceStringBuilder rb1 = new RecurrenceStringBuilder().freqByDay().repetition(10);

        RecurrenceStringBuilder rb2 = new RecurrenceStringBuilder().freqByDay().until(g1);

        RecurrenceStringBuilder rb3 = new RecurrenceStringBuilder().freqByDay().interval(2);

        RecurrenceStringBuilder rb4 = new RecurrenceStringBuilder().freqByDay().interval(2).repetition(5);

        RecurrenceStringBuilder rb5=null;
        try {
            rb5 = new RecurrenceStringBuilder().freqByYear().until(g2).byMonth(Calendar.JANUARY)
                    .byDay(Calendar.SUNDAY).byDay(Calendar.MONDAY)
                    .byDay(Calendar.TUESDAY).byDay(Calendar.WEDNESDAY)
                    .byDay(Calendar.THURSDAY)
                    .byDay(Calendar.FRIDAY).byDay(Calendar.SATURDAY);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        } catch (IndexMonthOutOfBoundException e) {
            e.printStackTrace();
        }

        RecurrenceStringBuilder rb6 = null;
        try {
            rb6 = new RecurrenceStringBuilder().freqByDay().until(g2).byMonth(Calendar.JANUARY);
        } catch (IndexMonthOutOfBoundException e) {
            e.printStackTrace();
        }

        RecurrenceStringBuilder rb7 = new RecurrenceStringBuilder().freqByWeek().repetition(10);

        RecurrenceStringBuilder rb8 = new RecurrenceStringBuilder().freqByWeek().until(g1);

        RecurrenceStringBuilder rb9 = null;
        try {
            rb9 = new RecurrenceStringBuilder().freqByDay().interval(2).weekStart(Calendar.SUNDAY);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }

        RecurrenceStringBuilder rb10 = null;
        try {
            rb10 = new RecurrenceStringBuilder().freqByWeek().until(g1)
                    .byDay(Calendar.TUESDAY).byDay(Calendar.THURSDAY);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }

        RecurrenceStringBuilder rb11 = null;
        try {
            rb11 = new RecurrenceStringBuilder().freqByWeek().repetition(10).weekStart(Calendar.SUNDAY)
                    .byDay(Calendar.TUESDAY).byDay(Calendar.THURSDAY);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }

        RecurrenceStringBuilder rb12 = null;
        try {
            rb12 = new RecurrenceStringBuilder().freqByWeek().interval(2).until(g1)
                    .weekStart(Calendar.SUNDAY)
                    .byDay(Calendar.MONDAY).byDay(Calendar.WEDNESDAY)
                    .byDay(Calendar.FRIDAY);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }
        RecurrenceStringBuilder rb13 = new RecurrenceStringBuilder();
        rb13.freqByDay().byHour(9).byHour(10).byHour(11).byHour(12)
                .byHour(13).byHour(14).byHour(15).byHour(16)
                .byMinute(0).byMinute(20).byMinute(40);

        RecurrenceStringBuilder rb14 = new RecurrenceStringBuilder();
        rb14.freqByMinute().interval(20).byHour(9).byHour(10).byHour(11)
                .byHour(12).byHour(13).byHour(14).byHour(15).byHour(16);

        result.add(rb1.getRRule());
        result.add(rb2.getRRule());
        result.add(rb3.getRRule());
        result.add(rb4.getRRule());
        result.add(rb5.getRRule());
        result.add(rb6.getRRule());
        result.add(rb7.getRRule());
        result.add(rb8.getRRule());
        result.add(rb9.getRRule());
        result.add(rb10.getRRule());
        result.add(rb11.getRRule());
        result.add(rb12.getRRule());
        return result;

    }
    public void onClickTest_1(View view){
        //First we delete the test 1
        onClickDelTest_1(view);

        //Second Call de free time calendar if it isn't
        if(idCalendar == INIT_ID_CALENDAR) {
            System.out.println("Reading Calendar");
            idCalendar = ftcService.findFreeTimeCalendarId();
            System.out.println(idCalendar);
            eventsTest1 = new ArrayList<>();
        }

        int i=0;
        EventBuilder event=null;
        GregorianCalendar g = new GregorianCalendar();
        g.set(Calendar.HOUR,0);g.set(Calendar.MINUTE,0);g.set(Calendar.SECOND,0);

        for(String rrule : init_rrule_test_1()) {
            event = new EventBuilder(idCalendar);
            event.color(COLORS_TEST[i%COLORS_TEST.length]);
            event.availability(CalendarContract.Events.AVAILABILITY_FREE);
            event.createEvent("TEST"+(i+1));
            event.startDT(g.getTimeInMillis()).finalizeStartTime();
            event.description("test n°" + i);
            event.timeZone(TimeZone.getDefault().getID());
            event.rRule(rrule);
            event.duration("PT15M");
            eventsTest1.add(event.finalizeEvent(getContentResolver()));
            i++;
        }

    }

    public void onClickDelTest_1(View view){
        if(idCalendar == INIT_ID_CALENDAR){
            return;
        }
        for(Long eventID : eventsTest1){
            Uri deleteUri = null;
            deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
            int rows = getContentResolver().delete(deleteUri, null, null);
            Log.i(DELETE_TEST_1, "Rows deleted: " + rows);
        }
        eventsTest1.removeAll(eventsTest1);
    }

    public void onClickTest_2(View view) {
        //First we delete the test 1
        onClickDelTest_1(view);

        //Second Call de free time calendar if it isn't
        if (idCalendar == INIT_ID_CALENDAR) {
            System.out.println("Reading Calendar");
            idCalendar = ftcService.findFreeTimeCalendarId();
            System.out.println(idCalendar);
            eventsTest1 = new ArrayList<>();
        }
        //Until the next month
        GregorianCalendar dEnd = new GregorianCalendar();
        dEnd.set(Calendar.MONTH,dEnd.get(Calendar.MONTH)+2);
        //Hour and minutes start and end
        GregorianCalendar ds =new GregorianCalendar(2013,5,1,8,0,0);
        GregorianCalendar de =new GregorianCalendar(2013,5,1,8,15,0);
        try {
            ftcService.createRecurringTask("TEST2", new int[]{Calendar.SATURDAY, Calendar.MONDAY},
                    ds.getTimeInMillis(), de.getTimeInMillis(), dEnd.getTimeInMillis());
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    public GregorianCalendar stringToCalendar(String s){
        String[] d = s.split("/");
        return new GregorianCalendar(Integer.parseInt(d[2]),Integer.parseInt(d[1])-1,Integer.parseInt(d[0]));
    }
}
