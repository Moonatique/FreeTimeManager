package fr.unice.polytech.freetime.app.Entities;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Created by user on 15/06/2014.
 */
public class Task implements Serializable{

    private String title;
    private String description;
    private GregorianCalendar start;
    private GregorianCalendar end;
    private int estimationH;
    private int getEstimationM;
    private int priority;

    public Task(String title){
        super();
        this.title=title;
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

    public GregorianCalendar getStart() {
        return start;
    }

    public void setStart(GregorianCalendar start) {
        this.start = start;
    }

    public GregorianCalendar getEnd() {
        return end;
    }

    public void setEnd(GregorianCalendar end) {
        this.end = end;
    }

    public int getEstimationH() {
        return estimationH;
    }

    public void setEstimationH(int estimationH) {
        this.estimationH = estimationH;
    }

    public int getGetEstimationM() {
        return getEstimationM;
    }

    public void setGetEstimationM(int getEstimationM) {
        this.getEstimationM = getEstimationM;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
