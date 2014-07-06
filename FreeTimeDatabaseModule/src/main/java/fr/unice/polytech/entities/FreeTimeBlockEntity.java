package fr.unice.polytech.entities;

/**
 * Created by Hakim on 16/06/2014.
 */
public class FreeTimeBlockEntity {

    private long id;
    private String day;
    private long eventId;
    private long startTime;
    private long endTime;

    public FreeTimeBlockEntity(long id, String day, long startTime, long endTime) {
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public FreeTimeBlockEntity setEventId(long eventId) { this.eventId = eventId; return this; }
    public long getId() { return id; }

    @Override
    public boolean equals(Object o){
        if(o.getClass().equals(this.getClass())) {
            return (this.id == ((FreeTimeBlockEntity)o).id);
        }
        return false;
    }
}
