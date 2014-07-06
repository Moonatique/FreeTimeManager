package fr.unice.polytech.freetime.app.ManageTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.GregorianCalendar;

import fr.unice.polytech.freetime.app.Calendar.CalendarViewDay;
import fr.unice.polytech.freetime.app.Control;
import fr.unice.polytech.freetime.app.Entities.TypeEvent;
import fr.unice.polytech.freetime.app.Entities.event;
import fr.unice.polytech.freetime.app.FreeTimeApplication;
import fr.unice.polytech.freetime.app.R;


public class CreateAllDayEvent extends Activity {

    private EditText title;
    private DatePicker dateEvent;
    private Control control;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_all_day_event);
        control= new Control((FreeTimeApplication)getApplication());

        title= (EditText) findViewById(R.id.name_fixEventAllDAy);
        dateEvent= (DatePicker) findViewById(R.id.date_fixEventAllDay);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_all_day_event, menu);
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

    public void onClick_valid_allDayEvent(View view){
        if(!title.getText().toString().equals("")){
            event newAllDayEvent= new event(title.getText().toString(), TypeEvent.ALL_DAY);
            GregorianCalendar date= new GregorianCalendar(dateEvent.getYear(),dateEvent.getMonth(),dateEvent.getDayOfMonth());
            newAllDayEvent.setDateStart(date);
            control.createAllDayEvent(newAllDayEvent);
            Intent intent = new Intent(this, CalendarViewDay.class);
            startActivity(intent);
        }else{
            Toast.makeText(this,"enter a title to this event",5).show();
        }
    }
}
