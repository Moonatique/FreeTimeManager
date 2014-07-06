package fr.unice.polytech.freetime.app.Entities;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Created by user on 09/06/2014.
 */
public class event implements Serializable{

    private TypeEvent type;
    private String title;
    private int startTimeH;
    private int endTimeH;
    private int startTimeM;
    private int endTimeM;
    private String[] day;
    private String oneDay;
    private GregorianCalendar dateStart;
    private GregorianCalendar dateEnd;

    public event(String title,TypeEvent type){
        super();
        this.endTimeH=0;
        this.startTimeH=0;
        this.endTimeM=0;
        this.startTimeM=0;
        this.title=title;
        this.type=type;
        this.dateStart=null;
        this.dateEnd=null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStartTimeH() {
        return startTimeH;
    }

    public void setStartTimeH(int startTime) {
        this.startTimeH = startTime;
    }

    public int getEndTimeH() {
        return endTimeH;
    }

    public void setEndTimeH(int endTime) {
        this.endTimeH = endTime;
    }

    public String[] getDay() {
        return day;
    }

    public void setDay(String[] day) {
        this.day = day;
    }

    public GregorianCalendar getDateStart() {
        return dateStart;
    }

    public void setDateStart(GregorianCalendar dateStart) {
        this.dateStart = dateStart;
    }

    public GregorianCalendar getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(GregorianCalendar dateEnd) {
        this.dateEnd = dateEnd;
    }

    public TypeEvent getType() {
        return type;
    }

    public void setType(TypeEvent type) {
        this.type = type;
    }

    public int getStartTimeM() {
        return startTimeM;
    }

    public void setStartTimeM(int startTimeM) {
        this.startTimeM = startTimeM;
    }

    public int getEndTimeM() {
        return endTimeM;
    }

    public void setEndTimeM(int endTimeM) {
        this.endTimeM = endTimeM;
    }

    public String getOneDay() {
        return oneDay;
    }

    public void setOneDay(String oneDay) {
        this.oneDay = oneDay;
    }

    public String getDisplayReduceRecurentEvent(){
        String days="";
        for(String s:this.day){
            if(s.equals("MONDAY")){
                days=days+"-Mon";
            }
            if(s.equals("TUESDAY")){
                days=days+"-Tue";
            }
            if(s.equals("WEDNESDAY")){
                days=days+"-Wed";
            }
            if(s.equals("THURSDAY")){
                days=days+"-Thu";
            }
            if(s.equals("FRIDAY")){
                days=days+"-Fri";
            }
            if(s.equals("SATURDAY")){
                days=days+"-Sat";
            }
            if(s.equals("SUNDAY")){
                days=days+"-Sun";
            }
        }
        return this.title + "\n" +startTimeH+":"+startTimeM+ " to "+ endTimeH+":"+endTimeM+"\n "+days ;
    }
}
