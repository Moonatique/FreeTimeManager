package fr.unice.polytech.freetime.app.InitParam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import fr.unice.polytech.freetime.app.Entities.User;
import fr.unice.polytech.freetime.app.InitParam.freeTime.FreeTimeSlotList;
import fr.unice.polytech.freetime.app.R;


public class DefineEffectiveHours extends Activity {

    private User user;
    private TimePicker start;
    private TimePicker end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_effective_hours);
        user= (User) getIntent().getSerializableExtra("userParam");

        start= (TimePicker) findViewById(R.id.timePicker_start_day);
        end= (TimePicker) findViewById(R.id.timePicker_end_day);
        start.setIs24HourView(true);
        end.setIs24HourView(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.define_effective_hours, menu);
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

    public void onClick_okValid_effectiveHours(View view){

        if((start.getCurrentHour()!=end.getCurrentHour()) || (start.getCurrentMinute()!=end.getCurrentMinute())) {
            user.setStartDayHour(start.getCurrentHour());
            user.setStartDayMinute(start.getCurrentMinute());
            user.setEndDayHour(end.getCurrentHour());
            user.setEndDayMinute(end.getCurrentMinute());

            Intent intent= new Intent(this,FreeTimeSlotList.class);
            intent.putExtra("userParam", user);
            startActivity(intent);
        }else{
            Toast.makeText(this,"set your effective Hours",5).show();
        }


    }
}
