package fr.unice.polytech.freetimealgorithm.gui;

import fr.unice.polytech.freetimealgorithm.gui.listeners.CustomListener;
import fr.unice.polytech.freetimealgorithm.model.DummyCalendar;
import fr.unice.polytech.freetimealgorithm.optimiser.Optimiser;

/**
 * Created by Hakim on 14/06/14.
 */
public class Controller implements CustomListener {

    private MainFrame gui;
    private DummyCalendar calendar = new DummyCalendar();

    public Controller() {
        gui = new MainFrame();
        gui.setListeners(this);
        calendar.setListener(gui.getCalendarEventsPanel());
        Optimiser.setListener(gui.getEmptySlotsPanel());
    }

    @Override
    public void onAddEvent(String title, long start, long end) {
        calendar.addEvent(title, start, end);
    }

    @Override
    public void onDetectEmptySlotsButtonClick(long start, long end) {
        Optimiser.detectEmptySlots(calendar, start, end);
    }

    @Override
    public void onCreateNewTask(String title, long timeEstimation, long startDate, long endDate, int priority) {
        Optimiser.addTaskToCalendar(calendar, title, timeEstimation, startDate, endDate, priority);
    }

    @Override
    public void onClearEvents() {
        calendar.clearEvents();
        Optimiser.clearTasks();
    }
}


