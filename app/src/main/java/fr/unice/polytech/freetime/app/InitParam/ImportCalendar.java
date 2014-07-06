package fr.unice.polytech.freetime.app.InitParam;

import android.content.Intent;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import fr.unice.polytech.freetime.app.Control;
import fr.unice.polytech.freetime.app.Entities.User;
import fr.unice.polytech.freetime.app.FreeTimeApplication;
import fr.unice.polytech.freetime.app.InitParam.FirstParam2;
import fr.unice.polytech.freetime.app.R;


public class ImportCalendar extends ActionBarActivity {

    private User user;
    private Cursor calCursor;
    private ListView calendarList;
    private Control control;
    private long idCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_calendar);
        user= (User) getIntent().getSerializableExtra("userParam");
        // récupéré les calendrier et afficher

        if(control==null){
            control= new Control((FreeTimeApplication)getApplication());
        }

        calCursor= control.getListCalendar();
        String[] columns = new String[] {CalendarContract.Calendars.NAME, CalendarContract.Calendars.ACCOUNT_NAME};
        int[] to = new int[] {R.id.calendar_name, R.id.account_name};
        final CursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.calendar_info, calCursor, columns, to, 0);

        calendarList = (ListView)findViewById(R.id.listview_calendars);
        calendarList.setAdapter(adapter);

        if(to.length==0){
            TextView info = (TextView) findViewById(R.id.info_no_cal);
            info.setVisibility(View.VISIBLE);
        }
        calendarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) calendarList.getItemAtPosition(position);

                String calendarName = cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Calendars.NAME));
                 idCalendar = cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID));

                onClick_import(view);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.import_calendar, menu);
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

    public void onClick_import(View view){
        if(idCalendar!=0){
            user.setCal_ID(idCalendar);
        }
        user.setImportCal(true);
        int step=user.getStep();
        user.setStep(++step);
        Intent intent= new Intent(this, FirstParam2.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

}
