package fr.unice.polytech.freetimealgorithm.gui;

import fr.unice.polytech.freetimealgorithm.gui.listeners.CustomListener;
import fr.unice.polytech.freetimealgorithm.gui.listeners.DummyCalendarListener;
import fr.unice.polytech.freetimealgorithm.model.Event;
import fr.unice.polytech.freetimealgorithm.tools.DateTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Hakim on 14/06/14.
 */
public class CalendarEventsPanel extends JPanel implements DummyCalendarListener, ActionListener{
    private JTextPane text = new JTextPane();
    private JButton clearEventsButton = new JButton("Clear events");
    private CustomListener listener;

    public CalendarEventsPanel() {
        setLayout(new GridBagLayout());
        initializeComponent();
    }

    private void initializeComponent() {
        clearEventsButton.addActionListener(this);
        text.setContentType("text/html");
        JScrollPane scrollText = new JScrollPane(text);
        scrollText.setPreferredSize(new Dimension(400, 780));

        JLabel panelTitle = new JLabel("Calendar events");
        panelTitle.setFont(new Font("Arial", Font.BOLD, 16));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = 0;
        add(panelTitle, gbc);

        gbc.gridx = 1;
        add(clearEventsButton, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1; gbc.gridwidth = 2;
        add(scrollText, gbc);

    }

    @Override
    public void updateEvents(List<Event> events) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><font size =  2 face = \"Courier New\">");
        sb.append("***************************************************<br/>");
        for(Event e : events) {
            sb.append("<font color=red>"+e.getTitle()+"</font>"
                    + "<br/>  start: " + DateTools.print(e.getStartTime())
                    + "<br/>  end: &nbsp " + DateTools.print(e.getEndTime()) + "\n");
            sb.append("***************************************************<br/>");
        }
        sb.append("</font></html>");
        sb.trimToSize();
        text.setText(sb.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(clearEventsButton)) {
            text.setText("");
            listener.onClearEvents();
        }
    }

    public void setListener(CustomListener listener) {
        this.listener = listener;
    }
}
