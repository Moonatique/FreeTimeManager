package fr.unice.polytech.freetimealgorithm.optimiser;

import fr.unice.polytech.freetimealgorithm.gui.listeners.OptimiserListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Hakim on 13/06/14.
 */
public class EmptySlots {
    public class EmptySlot implements Comparable<EmptySlot>{
        private long startTime, endTime;
        public EmptySlot(long start, long end) { startTime = start; endTime = end; }
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }

        @Override
        public int compareTo(EmptySlot o) {
            if(this.startTime < o.startTime) { return -1; }
            if(this.startTime == o.startTime) {
                if(this.endTime < o.endTime) { return -1; }
                if(this.endTime == o.endTime) { return 0; }
                if(this.endTime > o.endTime) { return 1; }
            }
            return 1;
        }
    }

    private ArrayList<EmptySlot> emptySlots = new ArrayList<EmptySlot>();

    public void addEmptySlot(long start, long end) {
        emptySlots.add(new EmptySlot(start, end));
        Collections.sort(emptySlots);
    }

    public void clearEmptySlots() { emptySlots.clear(); }

    public ArrayList<EmptySlot> getEmptySlots() {
        return emptySlots;
    }

    public long getTotalEmptySlotDuration(long startRange, long endRange) {
        long total = 0;
        for (EmptySlot es : emptySlots) {
            if(es.getStartTime() >= startRange && es.getEndTime() <= endRange) {
                total += (es.getEndTime() - es.getStartTime());
            }
            else if(es.getStartTime() < startRange && es.getEndTime() > startRange && es.getEndTime() <= endRange ) {
                total += (es.getEndTime() - startRange);
            }
            else if(es.getStartTime() > startRange && es.getStartTime() < endRange && es.getEndTime() > endRange) {
                total += (endRange - es.getStartTime());
            }
        }
        return total;
    }

}
