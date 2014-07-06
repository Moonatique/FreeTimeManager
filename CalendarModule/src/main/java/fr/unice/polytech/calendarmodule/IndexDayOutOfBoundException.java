package fr.unice.polytech.calendarmodule;

import java.util.Calendar;

/**
 * Created by Yoann on 11/06/2014.
 */
public class IndexDayOutOfBoundException extends Throwable {
    public IndexDayOutOfBoundException(int day) {
        super("#The day("+day+") is out of bound (which should be between "+ Calendar.SUNDAY+"-"+Calendar.SATURDAY+")");
    }
}
