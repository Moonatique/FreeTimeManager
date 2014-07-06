package fr.unice.polytech.freetime.app.TopView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fr.unice.polytech.freetime.app.ManageTask.CreateAllDayEvent;
import fr.unice.polytech.freetime.app.ManageTask.CreateFixEvent;
import fr.unice.polytech.freetime.app.ManageTask.CreateFreeTimeSlot;
import fr.unice.polytech.freetime.app.ManageTask.CreateRecurringEvent;
import fr.unice.polytech.freetime.app.ManageTask.CreateTaskProject;
import fr.unice.polytech.freetime.app.R;

public class AddElements extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_elements);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_elements, menu);
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


    public void onClick_new_long_term_task(View view){
        Intent intent = new Intent(this, CreateTaskProject.class);
        startActivity(intent);
    }

    public void onClick_new_recurring_task(View view){
        Intent intent = new Intent(this, CreateRecurringEvent.class);
        startActivity(intent);
    }

    public void onClick_new_freeTime_slot(View view){
        Intent intent = new Intent(this, CreateFreeTimeSlot.class);
        startActivity(intent);
    }
    public void onClick_new_all_day_event(View view){
        Intent intent = new Intent(this, CreateAllDayEvent.class);
        startActivity(intent);
    }

    public void onClick_new_one_time_task(View view){

        Intent intent= new Intent(this, CreateFixEvent.class);
        startActivity(intent);
    }
}
