package fr.unice.polytech.freetimealgorithm.gui;

import fr.unice.polytech.freetimealgorithm.gui.listeners.CustomListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Hakim on 14/06/14.
 */
public class MainFrame extends JFrame {
    private EventCreationPanel eventCreationPanel = new EventCreationPanel();
    private EmptySlotsPanel emptySlotsPanel = new EmptySlotsPanel();
    private TaskCreationPanel taskCreationPanel = new TaskCreationPanel();
    private TaskListPanel taskListPanel = new TaskListPanel();
    private CalendarEventsPanel calendarEventsPanel = new CalendarEventsPanel();
    private CurrentTimePanel currentTimePanel = new CurrentTimePanel();


    public MainFrame() {
        setLayout(new BorderLayout());
        JPanel controlPanels = new JPanel();

        JPanel eventControlPanel = new JPanel();
        eventControlPanel.setLayout(new BorderLayout());
        eventControlPanel.add(eventCreationPanel, BorderLayout.NORTH);
        eventControlPanel.add(emptySlotsPanel, BorderLayout.SOUTH);
        eventControlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        emptySlotsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel taskControlPanel = new JPanel();
        taskControlPanel.setLayout(new BorderLayout());
        taskControlPanel.add(taskCreationPanel, BorderLayout.NORTH);
        taskControlPanel.add(taskListPanel, BorderLayout.SOUTH);
        taskControlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        taskListPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        controlPanels.setLayout(new BorderLayout());
        controlPanels.add(eventControlPanel, BorderLayout.WEST);
        controlPanels.add(taskControlPanel, BorderLayout.EAST);

        calendarEventsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(controlPanels, BorderLayout.WEST);
        add(calendarEventsPanel, BorderLayout.CENTER);
        add(currentTimePanel, BorderLayout.EAST);

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setListeners(CustomListener listener) {
        eventCreationPanel.setListener(listener);
        emptySlotsPanel.setListener(listener);
        taskCreationPanel.setListener(listener);
        calendarEventsPanel.setListener(listener);
    }

    public CalendarEventsPanel getCalendarEventsPanel() { return calendarEventsPanel; }
    public EmptySlotsPanel getEmptySlotsPanel() {
        return emptySlotsPanel;
    }
}
