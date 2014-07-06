package fr.unice.polytech.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.entities.FreeTimeBlockEntity;
import fr.unice.polytech.entities.FtEventEntity;
import fr.unice.polytech.entities.FtEventEntity.FtEventType;
import fr.unice.polytech.freetimedatabase.FreeTimeDbContract.FreeTimeBlocks;

/**
 * Created by Hakim on 16/06/2014.
 */
public class FreeTimeBlockDataSource extends  DataSource {
    //Table columns
    public static String[] ALL_COLUMNS = {FreeTimeBlocks._ID, FreeTimeBlocks.COLUMN_DAY,
                                          FreeTimeBlocks.COLUMN_START_TIME, FreeTimeBlocks.COLUMN_END_TIME,
                                          FreeTimeBlocks.COLUMN_EVENT_ID};

    private static final int ID = 0, DAY = 1, START = 2, END = 3, EVENT_ID = 4;

    public FreeTimeBlockDataSource(Context context) { super(context); }

    public FreeTimeBlockEntity createFreeTimeBlock(int day, long startTime, long endTime, long eventId) {
        ContentValues values = new ContentValues();
        values.put(FreeTimeBlocks.COLUMN_DAY, day);
        values.put(FreeTimeBlocks.COLUMN_START_TIME, startTime);
        values.put(FreeTimeBlocks.COLUMN_END_TIME, endTime);
        values.put(FreeTimeBlocks.COLUMN_EVENT_ID, eventId); //This can safely be removed in next versions.

        open();
        long insertId = database.insert(FreeTimeBlocks.TABLE_NAME, null, values);
        Cursor cursor = database.query(FreeTimeBlocks.TABLE_NAME, ALL_COLUMNS, FreeTimeBlocks._ID + " = "
                                      + insertId, null, null, null, null);
        cursor.moveToFirst();
        FreeTimeBlockEntity newFreeTimeBlock = cursorToFreeTimeBlock(cursor);
        cursor.close();
        close();

        return newFreeTimeBlock;
    }

    public List<FreeTimeBlockEntity> getAllFreeTimeBlocks() {
        List<FreeTimeBlockEntity> freeTimeBlocks = new ArrayList<FreeTimeBlockEntity>();

        Cursor cursor = database.query(FreeTimeBlocks.TABLE_NAME, ALL_COLUMNS,
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            freeTimeBlocks.add(cursorToFreeTimeBlock(cursor));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return freeTimeBlocks;
    }

    private FreeTimeBlockEntity cursorToFreeTimeBlock(Cursor cursor) {
        return new FreeTimeBlockEntity(cursor.getLong(ID), cursor.getString(DAY),
                                       cursor.getLong(START), cursor.getLong(END))
                                       .setEventId(cursor.getLong(EVENT_ID));
    }
}
