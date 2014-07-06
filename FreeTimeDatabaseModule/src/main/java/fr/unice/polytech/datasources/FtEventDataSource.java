package fr.unice.polytech.datasources;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract.Events;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.entities.FtEventEntity;
import fr.unice.polytech.entities.FtEventEntity.FtEventType;
import fr.unice.polytech.freetimedatabase.FreeTimeDbContract.FtEvents;

/**
 * Created by Hakim on 17/06/2014.
 */
public class FtEventDataSource extends DataSource {
    public static int UNCOMPLETED = 0;
    public static int COMPLETED = 1;

    public static final String[] ALL_COLUMNS = {FtEvents._ID, FtEvents.COLUMN_EVENT_ID,
                                                FtEvents.COLUMN_START_TIME, FtEvents.COLUMN_END_TIME,
                                                FtEvents.COLUMN_COMPLETED,
                                                FtEvents.COLUMN_TITLE, FtEvents.COLUMN_TYPE};

    private static final int ID = 0, EVENT_ID = 1, START_TIME = 2, END_TIME = 3, IS_COMPLETED = 4, TITLE = 5, TYPE = 6;

    private static final String[] eventsProj = {Events._ID, Events.DTSTART, Events.DTEND, Events.DURATION };
    /*
        column numbers corresponding to the projection of the Events table (Do not confuse with  the FtEvents table!)
     */
    private static final int EVENTS_TABLE_ID = 0, EVENTS_TABLE_DTSTART = 1, EVENTS_TABLE_DTEND = 2, EVENTS_TABLE_DURATION = 3;

    public FtEventDataSource(Context context) { super(context); }

    public FtEventEntity createFtEvent(long eventId, String title, long startTime, long endTime, FtEventEntity.FtEventType ftEventType) {
        ContentValues values = new ContentValues();
        values.put(FtEvents.COLUMN_EVENT_ID, eventId);
        values.put(FtEvents.COLUMN_COMPLETED, UNCOMPLETED);
        values.put(FtEvents.COLUMN_START_TIME, startTime);
        values.put(FtEvents.COLUMN_END_TIME, endTime);
        values.put(FtEvents.COLUMN_TITLE, title);
        values.put(FtEvents.COLUMN_TYPE, ftEventType.name());

        open();
        long insertId = database.insert(FtEvents.TABLE_NAME, null, values);

        Cursor cursor = database.query(FtEvents.TABLE_NAME, ALL_COLUMNS, FtEvents._ID + " = " +
                                       insertId, null, null, null, null);
        cursor.moveToFirst();
        FtEventEntity newFtEvent = cursorToFtEvent(cursor);
        cursor.close();
        close();

        return newFtEvent;
    }

    public void deleteFtEvent(FtEventEntity ftEvent) {
        long id = ftEvent.getId();
        System.out.println("FtEvent deleted with id: " + id);
        open();
        database.delete(FtEvents.TABLE_NAME, FtEvents._ID + " = " + id, null);

        close();
    }

    public ArrayList<FtEventEntity> getAllFtEvents() {
        ArrayList<FtEventEntity> ftEvents = new ArrayList<FtEventEntity>();

        open();
        Cursor cursor = database.query(FtEvents.TABLE_NAME, ALL_COLUMNS,
                                       null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ftEvents.add(cursorToFtEvent(cursor));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return ftEvents;
    }

    public ArrayList<FtEventEntity> findFtEventInTimeRange(long startRange, long endRange) {
        ArrayList<FtEventEntity> ftEventEntities = new ArrayList<FtEventEntity>();
        String selection = FtEvents.COLUMN_START_TIME + " >= ? AND " + FtEvents.COLUMN_START_TIME + " < ?";
        String[] selArgs = new String[] {String.valueOf(startRange), String.valueOf(endRange)};
        open();
        Cursor cursor = database.query(FtEvents.TABLE_NAME, ALL_COLUMNS, selection, selArgs, null, null, null, null);

        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                ftEventEntities.add(cursorToFtEvent(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        close();

        return ftEventEntities;
    }

    public ArrayList<FtEventEntity> findFtEventByTitle(String title) {
        ArrayList<FtEventEntity> ftEventEntities = new ArrayList<FtEventEntity>();
        String selection = FtEvents.COLUMN_TITLE + " = ? ";
        String[] selArgs = new String[] {title};
        open();
        Cursor cursor = database.query(FtEvents.TABLE_NAME, ALL_COLUMNS, selection, selArgs, null, null, null, null);

        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                ftEventEntities.add(cursorToFtEvent(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        close();

        return ftEventEntities;
    }

    public FtEventEntity cursorToFtEvent(Cursor cursor) {

        String title = cursor.getString(TITLE);
        String typeString = cursor.getString(TYPE);
        FtEventType type = FtEventType.valueOf(typeString);
        return new FtEventEntity(cursor.getLong(ID), cursor.getLong(EVENT_ID), type)
                   .setCompleted(cursor.getInt(IS_COMPLETED)==1).setTitle(title)
                   .setStartTime(cursor.getLong(START_TIME)).setEndTime(cursor.getLong(END_TIME));
    }

    public long getStartTime(long eventId) {
        String selection = Events._ID + " = ? ";
        String[] selArgs = new String[] {String.valueOf(eventId)};

        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(Events.CONTENT_URI, null, selection,
                selArgs, null);

        int nbEvents = cursor.getCount();
        if(cursor.moveToFirst()) {
            long start = cursor.getLong(cursor.getColumnIndex(Events.DTSTART));
            cursor.close();
            return start;
        }

        cursor.close();

        return -1;
    }
}
