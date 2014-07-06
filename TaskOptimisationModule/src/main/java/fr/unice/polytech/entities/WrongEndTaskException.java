package fr.unice.polytech.entities;

import java.util.Calendar;

/**
 * Created by Yoann on 07/06/2014.
 */
public class WrongEndTaskException extends Throwable {
    public WrongEndTaskException(Calendar taskStart, Calendar taskEnd) {
        super("#Wrong end task {The end of the task: "+taskEnd.get(Calendar.DAY_OF_MONTH)+"/"+taskEnd.get(Calendar.MONTH)+"/"+taskEnd.get(Calendar.YEAR)+
                " is before the begin of the task: "+taskStart.get(Calendar.DAY_OF_MONTH)+"/"+taskStart.get(Calendar.MONTH)+"/"+taskStart.get(Calendar.YEAR)+"}");
    }
}
