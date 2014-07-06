package fr.unice.polytech.freetimealgorithm.gui;

import fr.unice.polytech.freetimealgorithm.gui.listeners.CustomListener;
import fr.unice.polytech.freetimealgorithm.gui.listeners.OptimiserListener;
import fr.unice.polytech.freetimealgorithm.optimiser.EmptySlots;
import fr.unice.polytech.freetimealgorithm.optimiser.Optimiser;
import fr.unice.polytech.freetimealgorithm.tools.DateTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Hakim on 14/06/14.
 */
public class EmptySlotsPanel extends JPanel implements ActionListener, OptimiserListener{
    private JButton detectEmptySlotsButton = new JButton("Detect empty slots");
    private DatePicker startDatePicker = new DatePicker();
    private DatePicker endDatePicker = new DatePicker();
    private JTextArea emptySlotsDisplay = new JTextArea();
    private CustomListener listener;
    private JButton clearButton = new JButton("Clear detected empty slots");

    public EmptySlotsPanel() {
        setLayout(new GridBagLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();

        detectEmptySlotsButton.addActionListener(this);
        clearButton.addActionListener(this);
        emptySlotsDisplay.setFont(new Font("Courier New", Font.PLAIN, 9));
        emptySlotsDisplay.setLineWrap(true);

        JLabel panelTitle = new JLabel("Detect empty slots");
        panelTitle.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridx=0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(panelTitle, gbc);
        gbc.gridy++;
        add(new JLabel("<html>Detect empty slots in the calendar<br/>between two given dates.</html>"), gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Start: "), gbc);
        gbc.gridx = 1;
        add(startDatePicker, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("End: "), gbc);
        gbc.gridx = 1;
        add(endDatePicker, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        add(detectEmptySlotsButton, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        JScrollPane scrollingDisplay = new JScrollPane(emptySlotsDisplay);
        scrollingDisplay.setPreferredSize(new Dimension(200, 400));
        add(scrollingDisplay, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        add(clearButton, gbc);
    }

    public void setListener(CustomListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(detectEmptySlotsButton)) {
            long start = startDatePicker.getDateInMilis();
            long end = endDatePicker.getDateInMilis();

            listener.onDetectEmptySlotsButtonClick(start, end);
        }

        if(e.getSource().equals(clearButton)) {
            emptySlotsDisplay.setText("");
            Optimiser.clearEmptySlots();
        }
    }

    @Override
    public void emptySlotsFound(ArrayList<EmptySlots.EmptySlot> emptySlots) {
        emptySlotsDisplay.setText("*******************************************\n");
        for(EmptySlots.EmptySlot es : emptySlots) {
            emptySlotsDisplay.append(DateTools.print(es.getStartTime())
                                   + " until " + DateTools.print(es.getEndTime())+"\n\n");
        }
    }
}
