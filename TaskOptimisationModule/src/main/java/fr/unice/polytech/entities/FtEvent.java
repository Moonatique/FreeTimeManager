package fr.unice.polytech.entities;

import android.provider.CalendarContract;

/**
 * Created by Clement on 11/06/2014.
 */
public class FtEvent {
    private enum Type{EVENT,TASK,FIXED}
    private Long eventId;
    private Type type;
    private CalendarContract.Events event;

    //todo add the attributes of an event


    public FtEvent(CalendarContract.Events event, Long eventId, Type type){
        this.event = event;
        this.eventId = eventId;
        this.type = type;
    }



}
