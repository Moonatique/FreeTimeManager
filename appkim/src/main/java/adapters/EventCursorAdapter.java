package adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.util.Date;

import fr.unice.polytech.freetime.app.R;

/**
 * Created by Hakim on 09/06/2014.
 */
public class EventCursorAdapter extends ResourceCursorAdapter {
    public EventCursorAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
        super(context, layout, c, autoRequery);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView)view.findViewById(R.id.eventId)).setText(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events._ID)));

        ((TextView)view.findViewById(R.id.eventinfo_eventTitle)).setText(cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)));

        TextView startTime = ((TextView)view.findViewById(R.id.startDateTime));
        Date startDateTime = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)));
        startTime.setText(startDateTime.toString());

        TextView endTime = ((TextView)view.findViewById(R.id.endDateTime));
        Date endDateTime = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(CalendarContract.Events.DTEND)));
        endTime.setText(endDateTime.toString());

    }
}
