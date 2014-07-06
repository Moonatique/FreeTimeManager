package fr.unice.polytech.freetimealgorithm.gui;

import fr.unice.polytech.freetimealgorithm.optimiser.Optimiser;
import fr.unice.polytech.freetimealgorithm.tools.DateTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Hakim on 15/06/14.
 */
public class CurrentTimePanel extends JPanel implements ActionListener{
    private DatePicker currentTimePicker = new DatePicker();
    private JButton setCurrentTimeButton = new JButton("Set current time");
    private JLabel currentTimeLabel = new JLabel("Now: " + DateTools.print(Optimiser.getNow()));

    public CurrentTimePanel() {
        setLayout(new GridBagLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        setCurrentTimeButton.addActionListener(this);
        currentTimeLabel.setFont(new Font("Arial", Font.BOLD, 13));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel panelTitle = new JLabel("Set the current time");
        panelTitle.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridx=0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(panelTitle, gbc);
        gbc.gridy++;
        add(new JLabel("<html>Change the current time<br/>for testing purposes</html>"), gbc);

        gbc.gridy++;
        add(currentTimeLabel, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Time: "), gbc);
        gbc.gridx = 1;
        add(currentTimePicker, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        add(setCurrentTimeButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(setCurrentTimeButton)) {
            Optimiser.setNow(currentTimePicker.getDateInMilis());
            currentTimeLabel.setText("Now: " + DateTools.print(Optimiser.getNow()));
        }
    }
}
