package fr.unice.polytech.freetime.app.utils;

import android.content.Context;

import java.util.Calendar;

import fr.unice.polytech.entities.TaskEntity;

/**
 * Created by user on 18/06/2014.
 */
public class Fonction {

    public static int dpToPx(Context cxt, int dp)
    {
        float density = cxt.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

   /* public static long intToLong(int value){

    }*/

    // return the int associated to the day of week in the calendar class
    public static int getIntDayCal(String day){
            if(day.equals("MONDAY")){
                return Calendar.MONDAY;
            }
            if(day.equals("TUESDAY")){
                return Calendar.TUESDAY;
            }
            if(day.equals("WEDNESDAY")){
                return Calendar.WEDNESDAY;
            }
            if(day.equals("THURSDAY")){
                return Calendar.THURSDAY;
            }
            if(day.equals("FRIDAY")){
                return Calendar.FRIDAY;
            }
            if(day.equals("SATURDAY")){
                return Calendar.SATURDAY;
            }
            if(day.equals("SUNDAY")){
                return Calendar.SUNDAY;
            }
            return 0;
    }

    public static int getPriority(String p){
        if(p.equals("LOW")){
            return TaskEntity.LOW_PRIORITY;

        }
        if(p.equals("HIGH")){
            return TaskEntity.HIGH_PRIORITY;
        }
        if(p.equals("NORMAL")){
            return TaskEntity.NORMAL_PRIORITY;
        }
        return TaskEntity.LOW_PRIORITY;
    }

    public static String getPriorityString(int p){
        if(p==TaskEntity.HIGH_PRIORITY){
            return "High";
        }
        if(p==TaskEntity.NORMAL_PRIORITY){
            return "Normal";
        }
        if(p==TaskEntity.LOW_PRIORITY){
            return "Low";
        }
        return "Low";
    }
}
