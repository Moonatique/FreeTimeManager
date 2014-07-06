package fr.unice.polytech.freetimealgorithm.model;

/**
 * Created by Hakim on 13/06/14.
 */
public class Event implements Comparable<Event>{
    private String title;
    private long startTime;
    private long endTime;

    public Event(String title, long startTime, long endTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTitle() { return title; }
    public long getStartTime() {
        return startTime;
    }
    public long getEndTime() { return endTime; }

    @Override
    public int compareTo(Event o) {
        //return (int) (this.startTime - o.startTime);

        if(this.startTime < o.startTime) { return -1; }
        if(this.startTime == o.startTime) {
            if(this.endTime < o.endTime) { return -1; }
            if(this.endTime == o.endTime) { return 0; }
            if(this.endTime > o.endTime) { return 1; }
        }
        //if(this.startTime > o.startTime) {return 1;}
        return 1;
    }
}