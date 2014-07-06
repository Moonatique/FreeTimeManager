package fr.unice.polytech.entities;

import java.util.Calendar;

/**
 * Created by Yoann on 07/06/2014.
 */
public class WrongStartTaskException extends Throwable {
    public WrongStartTaskException(Calendar taskStart, Calendar currentDay) {
        super("#Wrong begin task {The begin of the task: "+taskStart.get(Calendar.DAY_OF_MONTH)+"/"+taskStart.get(Calendar.MONTH)+"/"+taskStart.get(Calendar.YEAR)
                + " is before today: "+currentDay.get(Calendar.DAY_OF_MONTH)+"/"+currentDay.get(Calendar.MONTH)+"/"+currentDay.get(Calendar.YEAR)+"}");
    }
}
