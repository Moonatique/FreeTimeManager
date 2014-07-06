package fr.unice.polytech.freetimealgorithm.tools;

import fr.unice.polytech.freetimealgorithm.model.Event;
import sun.util.calendar.Gregorian;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Hakim on 14/06/14.
 */
public class EventImporter {
    public static ArrayList<Event> importEventsFromFile(String filename) {
        ArrayList<Event> events = new ArrayList<Event>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("resources/"+filename+".txt"));
            String line = reader.readLine();

            while(line != null) {
                if (!line.equals("")) {
                    String[] data = line.split("/");
                    String title = data[0].trim();
                    String[] startDateTimeStrings = data[1].split("-");
                    String[] startDateStrings = startDateTimeStrings[0].split(":");
                    String[] startTimeStrings = startDateTimeStrings[1].split(":");
                    String[] endDateTimeStrings = data[2].split("-");
                    String[] endDateStrings = endDateTimeStrings[0].split(":");
                    String[] endTimeStrings = endDateTimeStrings[1].split(":");

                    int startYear = Integer.parseInt(startDateStrings[0].trim());
                    int startMonth = Integer.parseInt(startDateStrings[1].trim())-1;
                    int startDay = Integer.parseInt(startDateStrings[2].trim());
                    int startHour = Integer.parseInt(startTimeStrings[0].trim());
                    int startMinute = Integer.parseInt(startTimeStrings[1].trim());

                    int endYear = Integer.parseInt(endDateStrings[0].trim());
                    int endMonth = Integer.parseInt(endDateStrings[1].trim())-1;
                    int endDay = Integer.parseInt(endDateStrings[2].trim());
                    int endHour = Integer.parseInt(endTimeStrings[0].trim());
                    int endMinute = Integer.parseInt(endTimeStrings[1].trim());

                    long startTime = new GregorianCalendar(startYear, startMonth, startDay, startHour, startMinute).getTimeInMillis();
                    long endTime = new GregorianCalendar(endYear, endMonth, endDay, endHour, endMinute).getTimeInMillis();

                    events.add(new Event(title, startTime, endTime));
                }

                line = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;
    }
}
