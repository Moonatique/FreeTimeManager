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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import fr.unice.polytech.freetime.app.Calendar.CalendarViewDay;
import fr.unice.polytech.freetime.app.Control;
import fr.unice.polytech.freetime.app.Entities.TypeEvent;
import fr.unice.polytech.freetime.app.Entities.User;
import fr.unice.polytech.freetime.app.Entities.event;
import fr.unice.polytech.freetime.app.FreeTimeApplication;
import fr.unice.polytech.freetime.app.InitParam.freeTime.FreeTimeSlotList;
import fr.unice.polytech.freetime.app.InitParam.recurrentEvent.RecurrentEventList;
import fr.unice.polytech.freetime.app.R;
import fr.unice.polytech.freetime.app.utils.MultipleChoiseSpinner;

public class CreateFreeTimeSlot extends Activity {

    private ArrayList<String> tabDay= new ArrayList<String>();
    private TimePicker start;
    private TimePicker end;
    private MultipleChoiseSpinner spinner;
    private Control control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_free_time_slot);
        control= new Control((FreeTimeApplication)getApplication());

        String[] array = { "MONDAY", "TUESDAY", "WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY" };
        spinner = (MultipleChoiseSpinner) findViewById(R.id.mySpinner2);
        spinner.setItems(array);

        start= (TimePicker) findViewById(R.id.timePicker_start_freeTime);
        end= (TimePicker) findViewById(R.id.timePicker_end_freeTime);
        start.setIs24HourView(true);
        end.setIs24HourView(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_free_time_slot, menu);
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

    public void onClick_okValid_FreeTimeSlot(View view){
        event newEvent;
        EditText editText= (EditText) findViewById(R.id.title_freeTimeSlot);
        String title= editText.getText().toString();


        List<String> tabDay = spinner.getSelectedStrings();

        int startSlotH= start.getCurrentHour();
        int endSlotH= end.getCurrentHour();

        int startSlotM= start.getCurrentMinute();
        int endSlotM= start.getCurrentMinute();


        if(!title.equals("")) {

            if(!tabDay.isEmpty()) {
                for (String s : tabDay) {
                    newEvent = new event(title, TypeEvent.FREE);
                    newEvent.setStartTimeH(startSlotH);
                    newEvent.setEndTimeH(endSlotH);
                    newEvent.setStartTimeM(startSlotM);
                    newEvent.setEndTimeM(endSlotM);
                    newEvent.setOneDay(s);
                    control.createFreeTimeSlot(newEvent);
                }

                Intent intent = new Intent(this, CalendarViewDay.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"you have to select at least one day",5).show();
            }


        }else{
            Toast.makeText(this,"you have enter a title",5).show();
        }
    }
}
