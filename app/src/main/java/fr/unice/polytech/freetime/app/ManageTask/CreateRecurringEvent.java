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

import fr.unice.polytech.freetime.app.Calendar.CalendarViewDay;
import fr.unice.polytech.freetime.app.Control;
import fr.unice.polytech.freetime.app.Entities.TypeEvent;
import fr.unice.polytech.freetime.app.Entities.event;
import fr.unice.polytech.freetime.app.FreeTimeApplication;
import fr.unice.polytech.freetime.app.InitParam.recurrentEvent.RecurrentEventList;
import fr.unice.polytech.freetime.app.R;
import fr.unice.polytech.freetime.app.utils.MultipleChoiseSpinner;

public class CreateRecurringEvent extends Activity {

    private ArrayList<String> tabDay= new ArrayList<String>();
    private TimePicker start;
    private TimePicker end;
    private MultipleChoiseSpinner spinner;
    private Control control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recurring_event);

        control= new Control((FreeTimeApplication) getApplication());
        String[] array = { "MONDAY", "TUESDAY", "WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY" };
        spinner = (MultipleChoiseSpinner) findViewById(R.id.mySpinner1);
        spinner.setItems(array);

        start= (TimePicker) findViewById(R.id.timePicker_start);
        end= (TimePicker) findViewById(R.id.timePicker_end);
        start.setIs24HourView(true);
        end.setIs24HourView(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_recurring_event, menu);
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

    public void onClick_okValid(View view){

        EditText title= (EditText) findViewById(R.id.title_recurrent_event);
        DatePicker startDate = (DatePicker) findViewById(R.id.datePicker_start_period);
        startDate.setSaveFromParentEnabled(false);
        startDate.setSaveEnabled(true);
        DatePicker endDate = (DatePicker) findViewById(R.id.datePicker_end_period);
        endDate.setSaveFromParentEnabled(false);
        endDate.setSaveEnabled(true);

        // ilfautsleectionner au moins un jour
        if(!spinner.getSelectedStrings().isEmpty()) {

            // il faut entrer un titre
            if(!title.getText().toString().equals("")) {

                if(start.getCurrentHour()!=end.getCurrentHour() || start.getCurrentMinute()!=end.getCurrentMinute()) {

                    int sy= startDate.getYear();
                    int ey= endDate.getYear();
                    int sm=startDate.getMonth();
                    int em=endDate.getMonth();
                    int sd=startDate.getDayOfMonth();
                    int ed= endDate.getDayOfMonth();
                    String[] days=new String[spinner.getSelectedStrings().size()];

                    int i=0;
                    for(String s: spinner.getSelectedStrings()){
                        days[i]=s;i++;
                    }

                    GregorianCalendar dateDeb= new GregorianCalendar(sy,sm,sd);
                    GregorianCalendar dateFin= new GregorianCalendar(ey,em,ed);

                    event newEvent= new event(title.getText().toString(), TypeEvent.RECURRENT);
                    newEvent.setStartTimeH(start.getCurrentHour());
                    newEvent.setStartTimeM(start.getCurrentMinute());
                    newEvent.setEndTimeH(end.getCurrentHour());
                    newEvent.setEndTimeM(end.getCurrentMinute());
                    newEvent.setDateStart(dateDeb);
                    newEvent.setDateEnd(dateFin);

                    newEvent.setDay(days);

                    control.createRecurrentEvent(newEvent);
                    Intent intent = new Intent(this, CalendarViewDay.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(this, "Select a time slot", 5).show();
                }
            }else{
                Toast.makeText(this,"Enter a name for this recurrent activity",5).show();
            }
        }else{
            Toast.makeText(this,"Select at least one day",5).show();
        }

    }
}
