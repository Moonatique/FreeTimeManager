package fr.unice.polytech.entities;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Hakim on 17/06/2014.
 */
public class FtEventEntity implements Comparable<FtEventEntity> {
    public enum FtEventType {FREETIME, IMPORTED, ONETIME, RECURRING, LONGTERM , ALLDAY }

    private long id;
    private long eventId;
    private boolean completed;
    private String title;
    private long startTime;
    private long endTime;
    private FtEventType ftEventType;

    public FtEventEntity(long id, long eventId, FtEventType ftEventType) {
        this.id = id;
        this.eventId = eventId;
        this.ftEventType = ftEventType;
    }

    public FtEventEntity setId(long id) { this.id = id; return this; }
    public FtEventEntity setCompleted(boolean completed) { this.completed = completed; return this;}
    public FtEventEntity setTitle(String title) { this.title = title; return this; }
    public FtEventEntity setStartTime(long startTime) { this.startTime = startTime; return this; }
    public FtEventEntity setEndTime(long endTime) { this.endTime = endTime; return this; }

    public long getId() { return id; }
    public boolean isCompleted() { return completed; }
    public String getTitle() { return title; }
    public long getStartTime() { return startTime;}
    public long getEndTime() { return endTime; }


    public long getEventId() {
        return eventId;
    }
    public FtEventType getFtEventType() { return ftEventType; }
    public long getDuration() {
        return endTime-startTime;
    }
    public long getStartHourAndMinute() {
        GregorianCalendar startCal = new GregorianCalendar();
        startCal.setTimeInMillis(startTime);
        long startHour = startCal.get(Calendar.HOUR_OF_DAY);
        long startMinute = startCal.get(Calendar.MINUTE);
        long start = (startHour*3600000 + startMinute*60000);

        return start;
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass().equals(this.getClass())) {
            return (this.id == ((FtEventEntity)o).id);
        }
        return false;
    }

    @Override
    public int compareTo(FtEventEntity ftEventEntity) {
        if(this.startTime < ftEventEntity.startTime) { return -1; }
        if(this.startTime == ftEventEntity.startTime) {
            if(this.endTime < ftEventEntity.endTime) { return -1; }
            if(this.endTime == ftEventEntity.endTime) { return 0; }
            if(this.endTime > ftEventEntity.endTime) { return 1; }
        }
        return 1;
    }
}
