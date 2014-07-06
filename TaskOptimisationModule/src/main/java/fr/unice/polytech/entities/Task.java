package fr.unice.polytech.entities;

import android.content.ContentResolver;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import fr.unice.polytech.calendarmodule.EventBuilder;

/**
 * Created by Yoann on 07/06/2014.
 * This class represent a general task
 * The goal is having only one common structure to manipulate task
 * and show only one type to users
 *
 * modified by Clement on 10/06/2014
 */
public class Task {


    //Represent 3 type of task
    public enum TASK_TYPE {EVENT,TASK,FIXED};
    public static final int HIGH_PRIORITY = 3;
    public static final int MIDDLE_PRIORITY = 2;
    public static final int LOW_PRIORITY = 1;

    private TASK_TYPE type;
    private String title;
    private String description;
    private Calendar taskStart;
    private Calendar taskEnd;
    private boolean allDay;
    private int hourEstimation;
    private int priority;
    private ArrayList<Long> eventsId;
    private ArrayList<FtEvent> events;

    public Task(){
        this.taskStart = new GregorianCalendar();
        this.taskEnd = new GregorianCalendar();
        this.title = "Untitled";
        this.description = "";
        this.allDay = false;
        this.hourEstimation = 0;
        this.priority = LOW_PRIORITY;
        this.eventsId = new ArrayList<Long>();
    }

    public static TASK_TYPE stringToEnum(String s){
        switch (s.toUpperCase()){
            case "EVENT":return TASK_TYPE.EVENT;
            case "TASK":return TASK_TYPE.TASK;
            case "FIXED":
            default:return TASK_TYPE.FIXED;
        }
    }

    //region old code
   /* public Task(TASK_TYPE type,String title){
        this.type = type;
        this.title = title;
        this.description = "That did not use FreeTime to create this event :p";
        this.taskStart = new GregorianCalendar();
        this.taskEnd = new GregorianCalendar();
        this.allDay = false;
    }

    public long publish(long calendarId,ContentResolver contentResolver){
        EventBuilder result = new EventBuilder(calendarId);
        result.createEvent(title);
        result.allDay(allDay);
        if(type.equals(TASK_TYPE.TASK) || type.equals(TASK_TYPE.FIXED)) {
            result.availability(CalendarContract.Events.AVAILABILITY_BUSY);
        }else{
            result.availability(CalendarContract.Events.AVAILABILITY_FREE);
        }
        result.startDT(taskStart.getTimeInMillis());
        result.finalizeStartTime();
        result.endDT(taskEnd.getTimeInMillis());
        result.finalizeEndTime();
        result.timeZone(TimeZone.getDefault().getID());
        result.organizer("moonatique@freetime.com");
        return result.finalizeEvent(contentResolver);
    }

    */
    //endregion


    public TASK_TYPE getType() {
        return type;
    }
    public void setType(TASK_TYPE type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(Calendar taskStart) throws WrongStartTaskException {
        GregorianCalendar current = new GregorianCalendar();
        int currentMonth = current.get(Calendar.MONTH);
        current.set(Calendar.MONTH,currentMonth);
        if(taskStart.compareTo(new GregorianCalendar())<0)throw new WrongStartTaskException(taskStart,taskEnd);
        this.taskStart = taskStart;
    }

    public Calendar getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(Calendar taskEnd) throws WrongEndTaskException {
        if(taskStart.compareTo(taskStart)>0)throw new WrongEndTaskException(taskStart,taskEnd);
        this.taskEnd = taskEnd;
    }

    public boolean isAllDay() {
        return allDay;
    }
    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }


    public int getHighPriority(){return priority;}
    public void setPriority(int priority){this.priority = priority;}

    public int getHourEstimation() { return hourEstimation; }
    public void setHourEstimation(int hourEstimation) {this.hourEstimation = hourEstimation; }

}
