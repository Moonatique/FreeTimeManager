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

import adapters.EventCursorAdapter;


public class FreeTimeCalendarViewActivity extends Activity {
    private FreeTimeApplication app;
    private Cursor eventCursor;
    private ListView eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_time_calendar_view);
        app = (FreeTimeApplication)getApplication();


        eventCursor = app.getFtcService().getAllEventsFromCalendar(getIntent().getLongExtra("calId", -1));
        /*
        String[] columns = new String[] {CalendarContract.Events._ID, CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND};
        int[] to = new int[] {R.id.eventId, R.id.eventTitle, R.id.startDateTime, R.id.endDateTime};
        final CursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.event_info, eventCursor, columns, to, 0);
        */

        EventCursorAdapter adapter = new EventCursorAdapter(getApplicationContext(), R.layout.event_info, eventCursor, true);

        eventList = (ListView)findViewById(R.id.eventListView);
        eventList.setAdapter(adapter);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) eventList.getItemAtPosition(position);

                String calendarName = cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Calendars.NAME));
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID));
                Intent intent = new Intent(getApplicationContext(), FreeTimeCalendarViewActivity.class);
                intent.putExtra("calId", id);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Selected calendar " + calendarName + " id: " + id, Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.free_time_calendar_view, menu);
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
