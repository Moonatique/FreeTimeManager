package fr.unice.polytech.freetimealgorithm.gui.listeners;

import fr.unice.polytech.freetimealgorithm.optimiser.*;

import java.util.ArrayList;

/**
 * Created by Hakim on 14/06/14.
 */
public interface OptimiserListener {

    public void emptySlotsFound(ArrayList<EmptySlots.EmptySlot> emptySlots);
}
