package fr.unice.polytech.datasources;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fr.unice.polytech.freetimedatabase.FreeTimeDbHelper;

/**
 * Created by Hakim on 15/06/2014.
 */
public class DataSource {
    protected SQLiteDatabase database;
    protected FreeTimeDbHelper ftDbHelper;
    protected Context context;

    public DataSource(Context context) {
        this.context = context;
        ftDbHelper = new FreeTimeDbHelper(context);
    }

    public void open()  { database = ftDbHelper.getWritableDatabase(); }
    public void close() { ftDbHelper.close(); }
    public void setContext(Context context) { this.context = context; }

    public SQLiteDatabase getDatabase(){
        return database;
    }
}
