package fr.unice.polytech.freetimealgorithm.gui;

import fr.unice.polytech.freetimealgorithm.gui.listeners.CustomListener;
import fr.unice.polytech.freetimealgorithm.model.Task;
import fr.unice.polytech.freetimealgorithm.tools.DateTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Hakim on 14/06/14.
 */
public class TaskCreationPanel extends JPanel implements ActionListener{
    private String[] prioritiesList = {"Low", "Normal", "High"};
    private DatePicker startDatePicker = new DatePicker();
    private DatePicker endDatePicker = new DatePicker();
    private JTextField titleField = new JTextField();
    private JTextField timeEstimationField = new JTextField();
    private JButton createTaskButton = new JButton("Create Task");
    private JComboBox<String> prioritiesField = new JComboBox<String>(prioritiesList);
    private CustomListener listener;

    public TaskCreationPanel() {
        setLayout(new GridBagLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        createTaskButton.addActionListener(this);
        prioritiesField.setSelectedItem("Normal");

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel panelTitle = new JLabel("Add a Task");
        panelTitle.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridx=0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(panelTitle, gbc);
        gbc.gridy++;
        add(new JLabel("<html>Add Tasks and they will be automatically broken up<br/>"
                    + "into events and added to the calendar</html>"), gbc);

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

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Estimated time (hours): "), gbc);
        gbc.gridx = 1;
        add(timeEstimationField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Priority: "), gbc);
        gbc.gridx = 1;
        add(prioritiesField, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        add(createTaskButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(createTaskButton)) {
            String title = titleField.getText();
            long start = startDatePicker.getDateInMilis();
            long end = endDatePicker.getDateInMilis();
            long timeEstimation = DateTools.hoursToMilis(Double.parseDouble(timeEstimationField.getText()));
            int priority;
            switch(prioritiesField.getSelectedIndex()) {
                case 0 : priority = Task.LOW_PRIORITY; break;
                case 1 : priority = Task.NORMAL_PRIORITY; break;
                case 2 : priority = Task.HIGH_PRIORITY; break;
                default: priority = Task.NORMAL_PRIORITY;
            }

            listener.onCreateNewTask(title, timeEstimation, start, end, priority);
        }
    }

    public void setListener(CustomListener listener) {
        this.listener = listener;
    }
}
