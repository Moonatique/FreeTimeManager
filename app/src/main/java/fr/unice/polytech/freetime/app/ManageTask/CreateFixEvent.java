package fr.unice.polytech.freetime.app.ManageTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.GregorianCalendar;

import fr.unice.polytech.freetime.app.Calendar.CalendarViewDay;
import fr.unice.polytech.freetime.app.Control;
import fr.unice.polytech.freetime.app.Entities.TypeEvent;
import fr.unice.polytech.freetime.app.Entities.event;
import fr.unice.polytech.freetime.app.FreeTimeApplication;
import fr.unice.polytech.freetime.app.R;


public class CreateFixEvent extends Activity {

    private EditText title;
    private DatePicker dateEvent;
    private TimePicker startEvent;
    private TimePicker endEvent;
    private Control control;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_fix_event);
        control= new Control((FreeTimeApplication) getApplication());

        title= (EditText) findViewById(R.id.name_fixEvent);
        dateEvent= (DatePicker) findViewById(R.id.date_fixEvent);
        startEvent= (TimePicker) findViewById(R.id.timePicker_start_fixEvent);
        endEvent= (TimePicker) findViewById(R.id.timePicker_end_event);
        startEvent.setIs24HourView(true);
        endEvent.setIs24HourView(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_fix_task, menu);
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

    public void onClick_valid_fixEvent(View view){
        if(!title.getText().toString().equals("")){
            if(!(startEvent.getCurrentHour()==endEvent.getCurrentHour() && startEvent.getCurrentMinute()==endEvent.getCurrentMinute())){

                  event newFixEvent = new event(title.getText().toString(), TypeEvent.FIX);

                   // newFixEvent.setStartTimeH(startEvent.getCurrentHour());
                   // newFixEvent.setStartTimeM(startEvent.getCurrentMinute());
                    newFixEvent.setEndTimeH(endEvent.getCurrentHour());
                    newFixEvent.setEndTimeM(endEvent.getCurrentMinute());

                GregorianCalendar dateS= new GregorianCalendar(dateEvent.getYear(),dateEvent.getMonth(),dateEvent.getDayOfMonth(),startEvent.getCurrentHour(),startEvent.getCurrentMinute());
                GregorianCalendar dateE= new GregorianCalendar(dateEvent.getYear(),dateEvent.getMonth(),dateEvent.getDayOfMonth(),endEvent.getCurrentHour(),endEvent.getCurrentMinute());

                newFixEvent.setDateStart(dateS);
                newFixEvent.setDateEnd(dateE);

                control.createFixEvent(newFixEvent);
                Intent intent= new Intent(this, CalendarViewDay.class);
               startActivity(intent);

            }else{
                Toast.makeText(this,"choose a slot time",5).show();
            }
        }else{
            Toast.makeText(this,"enter a title to this event",5).show();
;        }
    }
}
