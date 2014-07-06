package fr.unice.polytech.freetime.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import fr.unice.polytech.calendarmodule.FreeTimeCalendarService;


public class ImportCalendarActivity extends Activity {
    private FreeTimeCalendarService ftcService;
    private Cursor calCursor;
    private ListView calendarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_calendar);
        ftcService = ((FreeTimeApplication)getApplication()).getFtcService();

        calCursor = ftcService.getAllCalendars();
        String[] columns = new String[] {CalendarContract.Calendars.NAME, CalendarContract.Calendars.ACCOUNT_NAME};
        int[] to = new int[] {R.id.calendar_name, R.id.account_name};
        final CursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.calendar_info, calCursor, columns, to, 0);

        calendarList = (ListView)findViewById(R.id.calendarListView);
        calendarList.setAdapter(adapter);
        calendarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) calendarList.getItemAtPosition(position);

                String calendarName = cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Calendars.NAME));
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID));
                Intent intent = new Intent(getApplicationContext(), FreeTimeCalendarViewActivity.class);
                intent.putExtra("calId", id);
                ftcService.importEventsFromCalendar(id);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Selected calendar " + calendarName + " id: " + id, Toast.LENGTH_LONG).show();
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
}
