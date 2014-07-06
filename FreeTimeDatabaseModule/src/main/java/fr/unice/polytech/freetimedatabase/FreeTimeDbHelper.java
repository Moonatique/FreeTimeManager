package fr.unice.polytech.freetimedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.unice.polytech.freetimedatabase.FreeTimeDbContract.*;

/**
 * Created by Hakim on 09/06/2014.
 */
public class FreeTimeDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FreeTimeDb.db";

    //region empty_slots table SQL code
    private static final String SQL_CREATE_EMPTY_SLOTS_TABLE =
            "CREATE TABLE " + EmptySlots.TABLE_NAME + "(" +
            EmptySlots._ID + " INTEGER PRIMARY KEY, " +
            EmptySlots.COLUMN_START_TIME + " INTEGER, " +
            EmptySlots.COLUMN_END_TIME + " INTEGER" +
            ")";

    private static final String SQL_DELETE_EMPTY_SLOTS_TABLE =
            "DROP TABLE IF EXISTS " + EmptySlots.TABLE_NAME;
    //endregion

    //region freetime_block table SQL code
    private static final String SQL_CREATE_FREETIME_BLOCK_TABLE =
            "CREATE TABLE " + FreeTimeBlocks.TABLE_NAME + "(" +
            FreeTimeBlocks._ID + " INTEGER PRIMARY KEY, " +
            FreeTimeBlocks.COLUMN_DAY + " INTEGER, " +
            FreeTimeBlocks.COLUMN_EVENT_ID + " INTEGER, " +
            FreeTimeBlocks.COLUMN_START_TIME + " INTEGER, " +
            FreeTimeBlocks.COLUMN_END_TIME + " INTEGER" +
            ")";

    private static final String SQL_DELETE_FREETIME_BLOCK_TABLE =
            "DROP TABLE IF EXISTS " + FreeTimeBlocks.TABLE_NAME;
    //endregion

    //region tasks table SQL code
    private static final String SQL_CREATE_TASKS_TABLE =
            "CREATE TABLE " + Tasks.TABLE_NAME + "(" +
                    Tasks._ID + " INTEGER PRIMARY KEY, " + Tasks.COLUMN_TITLE + " TEXT, " +
                    Tasks.COLUMN_START_DATE + " INTEGER, " + Tasks.COLUMN_END_DATE + " INTEGER, " +
                    Tasks.COLUMN_DESCRIPTION + " TEXT, " + Tasks.COLUMN_ESTIMATION + " INTEGER, " +
                    Tasks.COLUMN_USER_PRIORITY + " INTEGER, " +Tasks.COLUMN_WEIGHT + " INTEGER, " +
                    Tasks.COLUMN_ONE_TIME + " INTEGER, " + Tasks.COLUMN_RECURRING + " INTEGER, " +
                    Tasks.COLUMN_LONGTERM + " INTEGER " +
                    ")";

    private static final String SQL_DELETE_TASKS_TABLE =
            "DROP TABLE IF EXISTS " + Tasks.TABLE_NAME;
    //endregion

    //region ft_events table SQL code
    private static final String SQL_CREATE_FT_EVENTS_TABLE =
            "CREATE TABLE " + FtEvents.TABLE_NAME + "(" +
                    FtEvents._ID + " INTEGER PRIMARY KEY, " +
                    FtEvents.COLUMN_EVENT_ID + " INTEGER, " +
                    FtEvents.COLUMN_START_TIME + " INTEGER, " +
                    FtEvents.COLUMN_END_TIME + " INTEGER, " +
                    FtEvents.COLUMN_COMPLETED + " INTEGER, " +
                    FtEvents.COLUMN_TITLE + " TEXT, " +
                    FtEvents.COLUMN_TYPE + " TEXT" +
                    ");";

    private static final String SQL_DELETE_FT_EVENTS_TABLE =
            "DROP TABLE IF EXISTS " + FtEvents.TABLE_NAME;
    //endregion

    //region task_ftevents table SQL code
    private static final String SQL_CREATE_TASK_FTEVENTS_TABLE =
            "CREATE TABLE " + TaskFtEvents.TABLE_NAME + "(" +
                    TaskFtEvents._ID + " INTEGER PRIMARY KEY, " +
                    TaskFtEvents.COLUMN_FTEVENT_ID + " INTEGER, " +
                    TaskFtEvents.COLUMN_TASK_ID + " INTEGER " +
                    ");";

    private static final String SQL_DELETE_TASK_FTEVENTS_TABLE =
            "DROP TABLE IF EXISTS " + TaskFtEvents.TABLE_NAME;
    //endregion

    public FreeTimeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_EMPTY_SLOTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FREETIME_BLOCK_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TASKS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FT_EVENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TASK_FTEVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //The upgrade policy for this database is to discard all data and start over
        sqLiteDatabase.execSQL(SQL_DELETE_EMPTY_SLOTS_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_TASK_FTEVENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_FT_EVENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_FREETIME_BLOCK_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_TASKS_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void reset(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_DELETE_EMPTY_SLOTS_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_TASK_FTEVENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_FT_EVENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_FREETIME_BLOCK_TABLE);
        sqLiteDatabase.execSQL(SQL_DELETE_TASKS_TABLE);


        onCreate(sqLiteDatabase);
    }
}
