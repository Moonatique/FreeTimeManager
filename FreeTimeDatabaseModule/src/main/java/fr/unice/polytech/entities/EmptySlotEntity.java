package fr.unice.polytech.entities;

/**
 * Created by Hakim on 15/06/2014.
 */
public class EmptySlotEntity implements Comparable<EmptySlotEntity>{
    private long id;
    private long startTime;
    private long endTime;

    public EmptySlotEntity(long id, long startTime, long endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getId()        { return id; }
    public long getStartTime() { return startTime; }
    public long getEndTime()   { return endTime; }

    @Override
    public int compareTo(EmptySlotEntity emptySlot) {
        if(this.startTime < emptySlot.startTime) { return -1; }
        else if(this.startTime == emptySlot.startTime) {
            if(this.endTime < emptySlot.endTime) { return -1; }
            if(this.endTime == emptySlot.endTime) { return 0; }
            if(this.endTime > emptySlot.endTime) { return 1; }
        }
        return 1;
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass().equals(this.getClass())) {
            return (this.id == ((EmptySlotEntity)o).id);
        }
        return false;
    }
}
