package fr.unice.polytech.freetimealgorithm.main;

import fr.unice.polytech.freetimealgorithm.gui.Controller;
import fr.unice.polytech.freetimealgorithm.optimiser.Optimiser;
import fr.unice.polytech.freetimealgorithm.tools.DateTools;

/**
 * Created by Hakim on 13/06/14.
 */
public class Main {
    public static void main(String[] args) {
        new Controller();
        System.out.println("Calendar loaded. Optimiser loaded. Now is " + DateTools.print(Optimiser.getNow()));
    }
}
