package fr.unice.polytech.calendarmodule;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by Yoann on 11/06/2014.
 */
public class RecurrenceStringBuilder {
    /**
     * CONSTANTES
     */
    //Type
    //private static final String DTSTART         = "DTSTART";
    //private static final String TIME_ZONE_ID    = "TZID";
    private static final String RRULE           = "RRULE";
    //private static final String EXDATE          = "EXDATE";

    //Separator
    private static final String SEP         = ":";
    private static final String SEP_ARG     = ";";
    private static final String SEP_ATTR    = ",";

    //Recursive rules
    private static final String FREQUENCE   = "FREQ";
    private static final String UNTIL       = "UNTIL";
    private static final String COUNT       = "COUNT";
    private static final String INTERVAL    = "INTERVAL";
    private static final String BYDAY       = "BYDAY";
    private static final String BYMONTH     = "BYMONTH";
    private static final String BYMONTHDAY  = "BYMONTHDAY";
    private static final String BYYEARDAY   = "BYYEARDAY";
    private static final String BYWEEKNO    = "BYWEEKNO";
    private static final String BYHOUR      = "BYHOUR";
    private static final String BYMINUTE    = "BYMINUTE";
    private static final String WEEK_START  = "WKST";

    //Recursive attributes
    private static final String YEARLY  = "YEARLY";
    private static final String MONTHLY   = "MONTHLY";
    private static final String WEEKLY  = "WEEKLY";
    private static final String DAILY   = "DAILY";
    private static final String HOURLY   = "HOURLY";
    private static final String MINUTELY   = "MINUTELY";


    //Days begin by sunday
    private static final String[] DAYS  = {"SU","MO","TU","WE","TH","FR","SA"};
    public static String getDayToString(int day) throws IndexDayOutOfBoundException {
        switch (day){
            case Calendar.SUNDAY:return DAYS[0];
            case Calendar.MONDAY:return DAYS[1];
            case Calendar.TUESDAY:return DAYS[2];
            case Calendar.WEDNESDAY:return DAYS[3];
            case Calendar.THURSDAY:return DAYS[4];
            case Calendar.FRIDAY:return DAYS[5];
            case Calendar.SATURDAY:return DAYS[6];
            default:throw  new IndexDayOutOfBoundException(day);
        }
    }
    //Month
    private static final String[] MONTH = {"01","02","03","04","05","06","07","08","09","10","11","12"};
    public static String getMonthToString(int month) throws IndexMonthOutOfBoundException {
        switch (month){
            case Calendar.JANUARY:return MONTH[0];
            case Calendar.FEBRUARY:return MONTH[1];
            case Calendar.MARCH:return MONTH[2];
            case Calendar.APRIL:return MONTH[3];
            case Calendar.MAY:return MONTH[4];
            case Calendar.JUNE:return MONTH[5];
            case Calendar.JULY:return MONTH[6];
            case Calendar.AUGUST:return MONTH[7];
            case Calendar.SEPTEMBER:return MONTH[8];
            case Calendar.OCTOBER:return MONTH[9];
            case Calendar.NOVEMBER:return MONTH[10];
            case Calendar.DECEMBER:return MONTH[11];
            default:throw new IndexMonthOutOfBoundException(month);
        }
    }

    /**
     * ATTRIBUTES
     */
    private HashMap<String,String> rruleArgs;

    public HashMap<String, String> getRruleArgs() {
        return rruleArgs;
    }

    public void setRruleArgs(HashMap<String, String> rruleArgs) {
        this.rruleArgs = rruleArgs;
    }
    /**
     * PRIVATE METHOD
     */

//Transform GregorianCalendar to string for rrule
    public static String gcanlendarToString(GregorianCalendar g){
        String m = MONTH[g.get(Calendar.MONTH)];
        String d = (g.get(Calendar.DAY_OF_MONTH)<10)? "0"+g.get(Calendar.DAY_OF_MONTH):g.get(Calendar.DAY_OF_MONTH)+"";
        String result = g.get(Calendar.YEAR)+m+d+"T";
        int pm_am = g.get(Calendar.AM_PM);
        int hh = (pm_am == Calendar.PM)?g.get(Calendar.HOUR)+12:g.get(Calendar.HOUR);
        String h = (hh<10)?"0"+hh:hh+"";
        String mi = (g.get(Calendar.MINUTE)<10)?"0"+g.get(Calendar.MINUTE):g.get(Calendar.MINUTE)+"";
        String s = (g.get(Calendar.SECOND)<10)?"0"+g.get(Calendar.SECOND):g.get(Calendar.SECOND)+"";
        return g.get(Calendar.YEAR)+m+d+"T"+h+mi+s;

    }
    /**
     * CONSTRUTOR
     */
    public RecurrenceStringBuilder(){
        rruleArgs = new HashMap<String,String>();
    }

    public RecurrenceStringBuilder freqByDay(){
        rruleArgs.put(FREQUENCE,DAILY);return this;
    }
    public RecurrenceStringBuilder freqByWeek(){
        rruleArgs.put(FREQUENCE,WEEKLY);return this;
    }
    public RecurrenceStringBuilder freqByYear(){
        rruleArgs.put(FREQUENCE,YEARLY);return this;
    }
    public RecurrenceStringBuilder freqByMonth(){
        rruleArgs.put(FREQUENCE,MONTHLY);return this;
    }
    public RecurrenceStringBuilder freqByMinute(){
        rruleArgs.put(FREQUENCE,MINUTELY);return this;
    }
    public RecurrenceStringBuilder freqByHour(){
        rruleArgs.put(FREQUENCE,HOURLY);return this;
    }

    public RecurrenceStringBuilder until(GregorianCalendar dtEnd){
        rruleArgs.put(UNTIL,gcanlendarToString(dtEnd)+"Z");return this;
    }
    public RecurrenceStringBuilder repetition(int repetition){
        rruleArgs.put(COUNT,repetition+"");return this;
    }
    public RecurrenceStringBuilder interval(int gap){
        rruleArgs.put(INTERVAL,gap+"");return this;
    }

    public RecurrenceStringBuilder byDay(int day) throws IndexDayOutOfBoundException {
        String d = getDayToString(day);
        if(rruleArgs.containsKey(BYDAY)){
            rruleArgs.put(BYDAY, rruleArgs.get(BYDAY)+SEP_ATTR+d);
        }else {
            rruleArgs.put(BYDAY, d);
        }
        return this;
    }
    public RecurrenceStringBuilder byMonth(int month) throws IndexMonthOutOfBoundException {
        String m  = getMonthToString(month);
        if(rruleArgs.containsKey(BYMONTH)){
            rruleArgs.put(BYMONTH, rruleArgs.get(BYMONTH)+SEP_ATTR+m);
        }else {
            rruleArgs.put(BYMONTH, m);
        }
        ;return this;
    }
    public RecurrenceStringBuilder byMonthDay(int dayNumber){
        if(rruleArgs.containsKey(BYMONTHDAY)){
            rruleArgs.put(BYMONTHDAY, rruleArgs.get(BYMONTHDAY)+SEP_ATTR+dayNumber);
        }else {
            rruleArgs.put(BYMONTHDAY, dayNumber + "");
        }
        return this;
    }
    public RecurrenceStringBuilder byYearDay(int dayNumber){
        if(rruleArgs.containsKey(BYYEARDAY)){
            rruleArgs.put(BYYEARDAY, rruleArgs.get(BYYEARDAY)+SEP_ATTR+dayNumber);
        }else {
            rruleArgs.put(BYYEARDAY, dayNumber + "");
        }
        return this;
    }
    public RecurrenceStringBuilder byWeekNumber(int weekNumber){
        if(rruleArgs.containsKey(BYWEEKNO)){
            rruleArgs.put(BYWEEKNO, rruleArgs.get(BYWEEKNO)+SEP_ATTR+weekNumber);
        }else {
            rruleArgs.put(BYWEEKNO, weekNumber + "");
        }
        return this;
    }
    public RecurrenceStringBuilder byHour(int hour){
        if(rruleArgs.containsKey(BYHOUR)){
            rruleArgs.put(BYHOUR, rruleArgs.get(BYHOUR)+SEP_ATTR+hour);
        }else {
            rruleArgs.put(BYHOUR, hour + "");
        }
        return this;
    }
    public RecurrenceStringBuilder byMinute(int minute){
        if(rruleArgs.containsKey(BYMINUTE)){
            rruleArgs.put(BYMINUTE, rruleArgs.get(BYMINUTE)+SEP_ATTR+minute);
        }else {
            rruleArgs.put(BYMINUTE, minute + "");
        }
        return this;
    }
    public RecurrenceStringBuilder weekStart(int day) throws IndexDayOutOfBoundException {
        String d = getDayToString(day);
        rruleArgs.put(WEEK_START,d);return this;
    }

    public String getRRule(){
        String result ="";
        int i=0;
        for(String key : rruleArgs.keySet()){
            if(i==0) {
                result += key + "=" + rruleArgs.get(key);
                i++;
            }else{
                result +=SEP_ARG + key + "=" + rruleArgs.get(key);
            }
        }
        return result;
    }

    public static String timeToDuration(long time){
        String result = "PT";
        int totalS = (int)time/1000;
        int h = totalS/3600;
        int m = (totalS%3600)/60;
        int s = (totalS%3600)%60;

        if(h != 0){
            result+=(h<10)?"0"+h+"H":h+"H";
        }
        if(m != 0){
            result+=(m<10)?"0"+m+"M":m+"M";
        }
        if(s != 0){
            result+=(s<10)?"0"+s+"S":s+"S";
        }
        return result;
    }
}
