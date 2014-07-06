package fr.unice.polytech.freetime.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DatabaseTestActivity extends Activity {
    private FreeTimeApplication app;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);
        app = (FreeTimeApplication)getApplication();

        startDatePicker = (DatePicker)findViewById(R.id.db_startDatePicker);
        endDatePicker = (DatePicker)findViewById(R.id.db_endDatePicker);
        startTimePicker = (TimePicker)findViewById(R.id.db_startTimePicker);
        endTimePicker = (TimePicker)findViewById(R.id.db_endTimePicker);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.database_test, menu);
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

    public void onTestButtonClick(View view) {
        int startDay = startDatePicker.getDayOfMonth();
        int startMonth = startDatePicker.getMonth();
        int startYear = startDatePicker.getYear();
        int startHour = startTimePicker.getCurrentHour();
        int startMinute = startTimePicker.getCurrentMinute();

        int endDay = endDatePicker.getDayOfMonth();
        int endMonth = endDatePicker.getMonth();
        int endYear = endDatePicker.getYear();
        int endHour = endTimePicker.getCurrentHour();
        int endMinute = endTimePicker.getCurrentMinute();

        Calendar calStart = new GregorianCalendar(startYear, startMonth, startDay, startHour, startMinute);
        Calendar calEnd = new GregorianCalendar(endYear, endMonth, endDay, endHour, endMinute);

        app.getFtcService().findUnoccupiedTimeSlots(calStart.getTimeInMillis(), calEnd.getTimeInMillis(), null);

    }
}
