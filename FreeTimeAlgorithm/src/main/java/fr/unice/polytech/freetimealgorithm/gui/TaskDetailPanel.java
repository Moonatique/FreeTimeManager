package fr.unice.polytech.freetimealgorithm.gui;

import fr.unice.polytech.freetimealgorithm.model.Event;

import javax.swing.*;

/**
 * Created by Hakim on 14/06/14.
 */
public class TaskDetailPanel extends JPanel {
    private JLabel titleLabel = new JLabel("Title");
    private JLabel startTimeLabel = new JLabel("start");
    private JLabel endTimeLabel = new JLabel("end");
    private JList<Event> eventsList = new JList<Event>();
}
