package fr.unice.polytech.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import fr.unice.polytech.entities.TaskFtEventEntity;
import fr.unice.polytech.freetimedatabase.FreeTimeDbContract;
import fr.unice.polytech.freetimedatabase.FreeTimeDbContract.TaskFtEvents;

/**
 * Created by Hakim on 20/06/2014.
 */
public class TaskFtEventDataSource extends DataSource{
    public static final String[] ALL_COLUMNS = {TaskFtEvents._ID, TaskFtEvents.COLUMN_FTEVENT_ID,
                                                TaskFtEvents.COLUMN_TASK_ID};

    public static final int ID = 0, FTEVENT_ID = 1, TASK_ID = 2;

    public TaskFtEventDataSource(Context context) { super(context); }

    public TaskFtEventEntity createTaskFtEvent(long ftEventId, long taskId) {
        ContentValues values = new ContentValues();
        values.put(TaskFtEvents.COLUMN_FTEVENT_ID, ftEventId);
        values.put(TaskFtEvents.COLUMN_TASK_ID, taskId);

        open();
        long insertId = database.insert(TaskFtEvents.TABLE_NAME, null, values);

        Cursor cursor = database.query(TaskFtEvents.TABLE_NAME, ALL_COLUMNS, TaskFtEvents._ID + " = " +
                                       insertId, null, null, null, null );

        cursor.moveToFirst();
        TaskFtEventEntity newTaskFtEvent = cursoryToTaskFtEvent(cursor);
        cursor.close();
        close();
        return newTaskFtEvent;
    }

    private TaskFtEventEntity cursoryToTaskFtEvent(Cursor cursor) {
        return new TaskFtEventEntity(cursor.getLong(ID), cursor.getLong(FTEVENT_ID), cursor.getLong(TASK_ID));
    }
}
