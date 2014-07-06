package fr.unice.polytech.calendarmodule;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import fr.unice.polytech.datasources.EmptySlotDataSource;
import fr.unice.polytech.datasources.FtEventDataSource;
import fr.unice.polytech.entities.FtEventEntity;

/**
 * Created by Yoann on 11/06/2014.
 */
public class IndexMonthOutOfBoundException extends Throwable {
    public IndexMonthOutOfBoundException(int month) {
        super("#The month("+month+") is out of bound (which should be between "+ Calendar.JANUARY+"-"+Calendar.DECEMBER+")");
    }
}
