package fr.unice.polytech.freetimealgorithm.gui;

import fr.unice.polytech.freetimealgorithm.gui.listeners.CustomListener;
import fr.unice.polytech.freetimealgorithm.model.Event;
import fr.unice.polytech.freetimealgorithm.tools.EventImporter;

import javax.swing.*;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Hakim on 14/06/14.
 */
public class EventCreationPanel extends JPanel implements ActionListener{
    private DatePicker startDatePicker = new DatePicker();
    private DatePicker endDatePicker = new DatePicker();
    private JTextField titleField = new JTextField();
    private JButton addEventButton = new JButton("add event");
    private JButton importEventsButton = new JButton("import events from text file");
    private CustomListener listener;


    public EventCreationPanel() {
        setLayout(new GridBagLayout());
        initializeComponents();

    }

    private void initializeComponents() {
        addEventButton.addActionListener(this);
        importEventsButton.addActionListener(this);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel panelTitle = new JLabel("Add an Event");
        panelTitle.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridx=0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(panelTitle, gbc);
        gbc.gridy++;
        add(new JLabel("<html>Add Events to simulate the<br/>importation of events from<br/>one of the user's existing<br/>calendars</html>"), gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Title: "), gbc);
        gbc.gridx = 1;
        titleField.setColumns(12);
        add(titleField, gbc);


        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Start: "), gbc);
        gbc.gridx = 1;
        add(startDatePicker, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("End: "), gbc);
        gbc.gridx = 1;
        add(endDatePicker, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        add(addEventButton, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        add(importEventsButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(addEventButton)) {
            String title = titleField.getText();
            long start = startDatePicker.getDateInMilis();
            long end = endDatePicker.getDateInMilis();

            listener.onAddEvent(title, start, end);
        }

        if(e.getSource().equals(importEventsButton)) {
            for(Event event : EventImporter.importEventsFromFile("events")) {
                listener.onAddEvent(event.getTitle(), event.getStartTime(), event.getEndTime());
            }
        }
}

    public void setListener(CustomListener listener) { this.listener = listener; }
}
