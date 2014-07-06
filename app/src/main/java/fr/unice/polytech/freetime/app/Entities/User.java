package fr.unice.polytech.freetime.app.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.freetime.app.Entities.event;

/**
 * Created by user on 09/06/2014.
 */
public class User implements Serializable{

    private String name;
    private Long cal_ID;
    private List<event> events;
    private List<event> freeTime;
    private boolean notificationParam;
    private int startDayHour;
    private int startDayMinute;
    private int endDayMinute;
    private int endDayHour;
    private int step;
    private boolean importCal;
    private boolean setFreeTime;
    private boolean setDailiesAct;
    private boolean setNotif;

    public User(){
        super();
        this.step=0;
        importCal=false;
        setFreeTime=false;
        setDailiesAct=false;
        setNotif=false;
        notificationParam=false;
        freeTime= new ArrayList<event>();
        cal_ID = null;
        name="defaultName";
        events= new ArrayList<event>();
        startDayHour =0;
        endDayHour =0;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCal() {
        return cal_ID;
    }

    public void setCal(Long cal) {
        this.cal_ID = cal;
    }

    public List<event> getEvents() {
        return events;
    }

    public void setEvents(List<event> events) {
        this.events = events;
    }

    public List<event> getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(List<event> freeTime) {
        this.freeTime = freeTime;
    }

    public boolean isNotificationParam() {
        return notificationParam;
    }

    public void setNotificationParam(boolean notificationParam) {
        this.notificationParam = notificationParam;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public boolean isImportCal() {
        return importCal;
    }

    public void setImportCal(boolean importCal) {
        this.importCal = importCal;
    }

    public boolean isSetFreeTime() {
        return setFreeTime;
    }

    public void setSetFreeTime(boolean setFreeTime) {
        this.setFreeTime = setFreeTime;
    }

    public boolean isSetDailiesAct() {
        return setDailiesAct;
    }

    public void setSetDailiesAct(boolean setDailiesAct) {
        this.setDailiesAct = setDailiesAct;
    }

    public boolean isSetNotif() {
        return setNotif;
    }

    public void setSetNotif(boolean setNotif) {
        this.setNotif = setNotif;
    }

    public int getStartDayHour() {
        return startDayHour;
    }

    public void setStartDayHour(int startDayHour) {
        this.startDayHour = startDayHour;
    }

    public Long getCal_ID() {
        return cal_ID;
    }

    public void setCal_ID(Long cal_ID) {
        this.cal_ID = cal_ID;
    }

    public int getStartDayMinute() {
        return startDayMinute;
    }

    public void setStartDayMinute(int startDayMinute) {
        this.startDayMinute = startDayMinute;
    }

    public int getEndDayMinute() {
        return endDayMinute;
    }

    public void setEndDayMinute(int endDayMinute) {
        this.endDayMinute = endDayMinute;
    }

    public int getEndDayHour() {
        return endDayHour;
    }

    public void setEndDayHour(int endDayHour) {
        this.endDayHour = endDayHour;
    }
}
