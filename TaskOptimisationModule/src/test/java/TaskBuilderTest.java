import android.content.ContentResolver;
import android.content.Context;

import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.unice.polytech.calendarmodule.FreeTimeCalendarService;
import fr.unice.polytech.entities.TaskBuilder;
import fr.unice.polytech.entities.WrongEndTaskException;
import fr.unice.polytech.entities.WrongStartTaskException;

/**
 * Created by user on 14/06/2014.
 */
public class TaskBuilderTest {
    public void testTaskCreation() throws WrongStartTaskException, WrongEndTaskException {
        Calendar calStart = new GregorianCalendar(2014, 5, 12, 0, 0, 0);
        Calendar calEnd = new GregorianCalendar(2014, 5, 18, 23, 59, 0);


        TaskBuilder t = new TaskBuilder();
        t.hourEstimation(30)
                .taskStartDate(calStart)
                .taskEndDate(calEnd);

        ///create task
    }
}
