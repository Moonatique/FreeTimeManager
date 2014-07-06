package fr.unice.polytech.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.unice.polytech.entities.FtEventEntity;
import fr.unice.polytech.entities.FtEventEntity.FtEventType;
import fr.unice.polytech.entities.TaskEntity;
import fr.unice.polytech.freetimedatabase.FreeTimeDbContract.*;

/**
 * Created by Hakim on 15/06/2014.
 */
public class TaskDataSource extends DataSource {
    public static final double DEFAULT_WEIGHT = 0;

    //Table columns
    public static String[] ALL_COLUMNS = {Tasks._ID, Tasks.COLUMN_TITLE,
                                   Tasks.COLUMN_START_DATE, Tasks.COLUMN_END_DATE,
                                   Tasks.COLUMN_ESTIMATION, Tasks.COLUMN_DESCRIPTION,
                                   Tasks.COLUMN_USER_PRIORITY, Tasks.COLUMN_WEIGHT,
                                   Tasks.COLUMN_ONE_TIME, Tasks.COLUMN_RECURRING, Tasks.COLUMN_LONGTERM};
    private static final int ID = 0, TITLE = 1, START = 2, END = 3, ESTIMATION = 4,
                             DESCRIPTION = 5, PRIORITY = 6, WEIGHT = 7, ONE_TIME = 8,
                             RECURRING = 9, LONGTERM = 10;

    public TaskDataSource(Context context) {
        super(context);
    }

    public TaskEntity createLongTermTask(String title, String description, long startDate, long endDate,
                           int hourEstimation, int priority) {
        ContentValues values = new ContentValues();
        values.put(Tasks.COLUMN_TITLE, title);
        values.put(Tasks.COLUMN_START_DATE, startDate);
        values.put(Tasks.COLUMN_END_DATE, endDate);
        values.put(Tasks.COLUMN_USER_PRIORITY, priority);
        values.put(Tasks.COLUMN_WEIGHT, DEFAULT_WEIGHT);
        values.put(Tasks.COLUMN_ESTIMATION, hourEstimation);
        values.put(Tasks.COLUMN_ONE_TIME, false);
        values.put(Tasks.COLUMN_RECURRING, false);
        values.put(Tasks.COLUMN_LONGTERM, true);

        if(description != null) {
            values.put(Tasks.COLUMN_DESCRIPTION, description);
        }
        open();
        long newTaskId = database.insert(Tasks.TABLE_NAME, null, values);
        Cursor cursor = database.query(Tasks.TABLE_NAME,
                ALL_COLUMNS, Tasks._ID + " = " + newTaskId, null,
                null, null, null);
        cursor.moveToFirst();
        TaskEntity newTask = cursorToTask(cursor);
        cursor.close();
        close();

        return newTask;
    }

    public List<FtEventEntity> findFtEventsInTimeRange(long startRange, long endRange) {
        List<FtEventEntity> ftEvents = new ArrayList<FtEventEntity>();

        Cursor cursor = database.query(Tasks.TABLE_NAME,
                ALL_COLUMNS, null, null, null, null, null);

        FtEventDataSource ftEventDataSource = new FtEventDataSource(context);



        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ftEvents.add(ftEventDataSource.cursorToFtEvent(cursor));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return ftEvents;
    }

    public TaskEntity createOneTimeTask(String title, long startDate, long endDate, long eventId) {
        ContentValues values = new ContentValues();
        values.put(Tasks.COLUMN_TITLE, title);
        values.put(Tasks.COLUMN_START_DATE, startDate);
        values.put(Tasks.COLUMN_END_DATE, endDate);
        values.put(Tasks.COLUMN_ONE_TIME, true);
        values.put(Tasks.COLUMN_RECURRING, false);
        values.put(Tasks.COLUMN_LONGTERM, false);

        open();
        long newTaskId = database.insert(Tasks.TABLE_NAME, null, values);
        close();

        FtEventDataSource ftEventDataSource = new FtEventDataSource(context);
        FtEventEntity ftEvent  = ftEventDataSource.createFtEvent(eventId, title, startDate, endDate, FtEventType.ONETIME);

        TaskFtEventDataSource taskFtEventDataSource = new TaskFtEventDataSource(context);
        taskFtEventDataSource.createTaskFtEvent(ftEvent.getId(), newTaskId);

        open();
        Cursor cursor = database.query(Tasks.TABLE_NAME, ALL_COLUMNS, Tasks._ID + " = "
                                      + newTaskId, null, null, null, null);

        cursor.moveToFirst();
        TaskEntity newTask = cursorToTask(cursor);
        cursor.close();
        close();
        return newTask;
    }

    public TaskEntity createRecurringTask(String title, Calendar startDate, long endDate, ArrayList<FtEventEntity> ftEventInstances  ) {

        ContentValues values = new ContentValues();
        values.put(Tasks.COLUMN_TITLE, title);
        values.put(Tasks.COLUMN_START_DATE, startDate.getTimeInMillis());
        values.put(Tasks.COLUMN_END_DATE, endDate);
        values.put(Tasks.COLUMN_RECURRING, true);
        values.put(Tasks.COLUMN_ONE_TIME, false);
        values.put(Tasks.COLUMN_LONGTERM, false);

        open();
        long taskId = database.insert(Tasks.TABLE_NAME, null, values);

        Cursor cursor = database.query(Tasks.TABLE_NAME, ALL_COLUMNS, Tasks._ID + " = " + taskId, null, null, null, null );
        cursor.moveToFirst();
        TaskEntity newtask = cursorToTask(cursor);
        cursor.close();
        close();

        TaskFtEventDataSource taskFtEventDataSource = new TaskFtEventDataSource(context);
        for(FtEventEntity ftEvent : ftEventInstances) {
            taskFtEventDataSource.createTaskFtEvent(ftEvent.getId(), taskId);
        }

        return newtask;
    }

    public void deleteTask(TaskEntity task) {
        long id = task.getId();
        System.out.println("Task deleted with id: " + id);
        open();
        database.delete(Tasks.TABLE_NAME, Tasks._ID + " = " + id, null);
        close();
    }

    public ArrayList<TaskEntity>  getAllTasks() {
        ArrayList<TaskEntity> tasks = new ArrayList<TaskEntity>();
        String selection = Tasks.COLUMN_LONGTERM + " = ? ";
        String[] selArgs = new String[] {String.valueOf(1)};
        open();
        Cursor cursor = database.query(Tasks.TABLE_NAME,
                ALL_COLUMNS, selection, selArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tasks.add(cursorToTask(cursor));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();

        return tasks;
    }

    public ArrayList<TaskEntity>  getTasksInRange(long startRange, long endRange) {
        ArrayList<TaskEntity> tasksInRange = new ArrayList<TaskEntity>();
        String selection = Tasks.COLUMN_START_DATE + " >= ? AND " + Tasks.COLUMN_START_DATE + " < ? ";
        String[] selArgs = new String[] {String.valueOf(startRange), String.valueOf(endRange)};

        Cursor cursor = database.query(Tasks.TABLE_NAME,
                ALL_COLUMNS, selection, selArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tasksInRange.add(cursorToTask(cursor));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tasksInRange;
    }

    //modify the cursorToTask method so it queries the FtEvent table and adds a list of FtEvents attached to the TaskEntity;
    public TaskEntity cursorToTask(Cursor cursor) {
        return new TaskEntity(cursor.getLong(ID), cursor.getString(TITLE),
                        cursor.getLong(START), cursor.getLong(END))
                       .setDescription(cursor.getString(DESCRIPTION))
                       .setHourEstimation(cursor.getInt(ESTIMATION))
                       .setPriority(cursor.getInt(PRIORITY))
                       .setWeight(cursor.getDouble(WEIGHT))
                       .setRecurring(cursor.getInt(RECURRING)==1)
                       .setOneTime(cursor.getInt(ONE_TIME)==1)
                       .setLongterm(cursor.getInt(LONGTERM)==1);
    }

    public void open()  { super.open(); }
    public void close() { super.close(); }
}
