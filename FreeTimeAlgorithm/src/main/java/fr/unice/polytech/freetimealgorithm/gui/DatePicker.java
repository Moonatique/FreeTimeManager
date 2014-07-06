package fr.unice.polytech.freetimealgorithm.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Hakim on 14/06/14.
 */
public class DatePicker extends JPanel {
    private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                               "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private Integer[] years = {2014, 2015, 2016};
    private Integer[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 , 16,
                              17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};

    private JComboBox<Integer> yearField = new JComboBox<Integer>(years);
    private JComboBox<String> monthField = new JComboBox<String>(months);
    private JComboBox<Integer> dayField = new JComboBox<Integer>(days);
    private JTextField hourField = new JTextField();
    private JTextField minuteField = new JTextField();

    public DatePicker() {
        setLayout(new GridBagLayout());
        initializeComponents();
    }

    public long getDateInMilis(){
        int year = (Integer)yearField.getSelectedItem();
        int month = monthField.getSelectedIndex();
        int day = (Integer)dayField.getSelectedItem();
        int hour = Integer.parseInt(hourField.getText());
        int minute = Integer.parseInt(minuteField.getText());
        Calendar cal = new GregorianCalendar(year, month, day, hour, minute);

        return cal.getTimeInMillis();
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();

        hourField.setColumns(2);
        minuteField.setColumns(2);

        gbc.gridy = 0; gbc.gridx = 0;  gbc.gridwidth = 4;
        add(yearField, gbc);
        gbc.gridy = 1;
        add(new JLabel("year"), gbc);

        gbc.gridwidth = 2;
        gbc.gridy = 0; gbc.gridx = 4;
        add(monthField, gbc);
        gbc.gridy = 1;
        add(new JLabel("month"), gbc);

        gbc.gridy = 0; gbc. gridx = 6;
        add(dayField, gbc);
        gbc.gridy = 1;
        add(new JLabel("day"), gbc);

        gbc.gridy = 0; gbc.gridx = 8;
        add(hourField, gbc);
        gbc.gridy = 1;
        add(new JLabel("hour"), gbc);

        gbc.gridy = 0; gbc.gridx = 10;
        add(minuteField, gbc);
        gbc.gridy = 1;
        add(new JLabel("min"), gbc);
    }
}