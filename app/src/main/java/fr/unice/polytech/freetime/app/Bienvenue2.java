package fr.unice.polytech.freetime.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fr.unice.polytech.calendarmodule.FreeTimeCalendarService;
import fr.unice.polytech.freetime.app.Calendar.CalendarViewDay;
import fr.unice.polytech.freetime.app.Entities.User;
import fr.unice.polytech.freetime.app.InitParam.FirstParam2;
import fr.unice.polytech.freetime.app.utils.Slide;


public class Bienvenue2 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenue2);

        //Slide slide= (Slide) findViewById(R.id.slide);
        //slide.setText("FreeTime");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bienvenue2, menu);
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
        if (id == R.id.reset) {
            ((FreeTimeApplication)getApplication()).getFtcService().resetCalendarAndPrefs();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onClick_start(View view){
        SharedPreferences sharedUser= getSharedPreferences(FreeTimeCalendarService.PREFNAME, 0);

        if(sharedUser!=null ){
            String name=sharedUser.getString(FreeTimeCalendarService.PREF_USER_NAME,"notSet");

            if(name.equals("notSet")){
                Intent intent = new Intent(this, FirstParam2.class);
                User user = new User();
                intent.putExtra("user", user);
                startActivity(intent);
            }else{
                Intent intent= new Intent(this, CalendarViewDay.class);
                startActivity(intent);
            }
        }else {
            Intent intent = new Intent(this, FirstParam2.class);
            User user = new User();
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

}

