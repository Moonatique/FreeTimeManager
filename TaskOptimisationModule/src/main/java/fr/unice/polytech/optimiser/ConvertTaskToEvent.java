package fr.unice.polytech.optimiser;

import android.content.ContentResolver;
import android.provider.CalendarContract;

import java.util.TimeZone;

import fr.unice.polytech.calendarmodule.EventBuilder;
import fr.unice.polytech.entities.Task;

/**
 * Created by Clement on 12/06/2014.
 */
public class ConvertTaskToEvent {

    //region things we can add
    /*the organiser (email of the event creator) can be used for email-notifications
     */

    //endregion

    public void convert(long calendarId,ContentResolver contentResolver, Task task){
        if(task.getType().equals(Task.TASK_TYPE.TASK)){
            createTask(calendarId, contentResolver, task);
        }

        if(task.getType().equals(Task.TASK_TYPE.EVENT)){
            createEvent(calendarId, contentResolver, task);
        }

        if(task.getType().equals(Task.TASK_TYPE.FIXED)){
            createFixed(calendarId, contentResolver, task);
        }
    }

    private void createFixed(long calendarId, ContentResolver contentResolver, Task task) {
        EventBuilder eventBuilder = new EventBuilder(calendarId);
        eventBuilder.createEvent(task.getTitle());
        eventBuilder.allDay(task.isAllDay());
        eventBuilder.availability(CalendarContract.Events.AVAILABILITY_BUSY);
        eventBuilder.startDT(task.getTaskStart().getTimeInMillis());
        eventBuilder.endDT(task.getTaskEnd().getTimeInMillis());
    }

    private void createEvent(Long calendarId, ContentResolver contentResolver, Task task) {
        EventBuilder eventBuilder = new EventBuilder(calendarId);
        eventBuilder.createEvent(task.getTitle());
        eventBuilder.allDay(task.isAllDay());
        eventBuilder.availability(CalendarContract.Events.AVAILABILITY_FREE);
        eventBuilder.startDT(task.getTaskStart().getTimeInMillis());
        eventBuilder.endDT(task.getTaskEnd().getTimeInMillis());
    }


    public void createTask(long calendarId,ContentResolver contentResolver, Task task){
        EventBuilder eventBuilder = new EventBuilder(calendarId);
        eventBuilder.availability(CalendarContract.Events.AVAILABILITY_BUSY);
        //call optimiser
       // see what the optimiser return
    }

    public static long publish(long calendarId,ContentResolver contentResolver, Task task){
        EventBuilder result = new EventBuilder(calendarId);
        result.createEvent(task.getTitle());
        result.allDay(task.isAllDay());
        if(task.getType().equals(Task.TASK_TYPE.TASK) || task.getType().equals(Task.TASK_TYPE.FIXED)) {
            result.availability(CalendarContract.Events.AVAILABILITY_BUSY);
        }else{
            result.availability(CalendarContract.Events.AVAILABILITY_FREE);
        }
        result.startDT(task.getTaskStart().getTimeInMillis());
        result.finalizeStartTime();
        result.endDT(task.getTaskEnd().getTimeInMillis());
        result.finalizeEndTime();
        result.timeZone(TimeZone.getDefault().getID());
        result.organizer("moonatique@freetime.com");
        return result.finalizeEvent(contentResolver);
    }
}
