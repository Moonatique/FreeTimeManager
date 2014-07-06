package fr.unice.polytech.freetimealgorithm.model;

/**
 * Created by Hakim on 14/06/14.
 */
public class FtEvent implements Comparable<Object> {
    private Event event;
    private boolean completed = false;
    private Task parentTask;

    public FtEvent(Event event, Task parent) {
        this.event = event;
        this.parentTask = parent;
    }

    public long getDuration() { return event.getEndTime() - event.getStartTime(); }

    public void setCompleted(boolean completed) { this.completed = completed; }
    public boolean isCompleted() { return completed;}
    public Task getParent() { return parentTask; }
    public long getStartTime() { return event.getStartTime(); }
    public long getEndTime() { return event.getEndTime(); }

    @Override
    public int compareTo(Object o) {
        if(o.getClass().equals(FtEvent.class)){
            FtEvent e = (FtEvent)o;
            if(this.event.getStartTime() < e.event.getStartTime()) { return -1; }
            if(this.event.getStartTime() == e.event.getStartTime()) {
                if(this.event.getEndTime() < e.event.getEndTime()) { return -1; }
                if(this.event.getEndTime() == e.event.getEndTime()) { return 0; }
                if(this.event.getEndTime() > e.event.getEndTime()) { return 1; }
            }
            return 1;
        }

        else if(o.getClass().equals(Event.class)){
            Event e = (Event)o;
            if(this.event.getStartTime() < e.getStartTime()) { return -1; }
            if(this.event.getStartTime() == e.getStartTime()) {
                if(this.event.getEndTime() < e.getEndTime()) { return -1; }
                if(this.event.getEndTime() == e.getEndTime()) { return 0; }
                if(this.event.getEndTime() > e.getEndTime()) { return 1; }
            }
            return 1;
        }

        else return -1;
    }

}
