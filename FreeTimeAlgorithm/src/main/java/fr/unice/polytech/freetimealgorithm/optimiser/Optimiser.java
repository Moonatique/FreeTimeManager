package fr.unice.polytech.freetimealgorithm.optimiser;

import fr.unice.polytech.freetimealgorithm.gui.listeners.OptimiserListener;
import fr.unice.polytech.freetimealgorithm.model.DummyCalendar;
import fr.unice.polytech.freetimealgorithm.model.Event;
import fr.unice.polytech.freetimealgorithm.optimiser.EmptySlots.EmptySlot;
import fr.unice.polytech.freetimealgorithm.model.Task;
import fr.unice.polytech.freetimealgorithm.tools.DateTools;

import java.util.*;

import static java.lang.Math.max;

/**
 * Created by Hakim on 13/06/14.
 */
public class Optimiser {
    private static OptimiserListener listener;
    private static ArrayList<Task> tasks = new ArrayList<Task>();

    /*
        Variables and parameters used in the algorithm
     */
    //the current time (the time when the optimiser is called). This value can be set to any date and time for testing purposes.
    private static long now = Calendar.getInstance().getTimeInMillis();
    private static long latestEndDate;  //the latest due date among all the tasks.
    private static EmptySlots emptySlots = new EmptySlots(); //The list of EmptySlots between now and latestEndDate.
    private static long nbFtEventsForNewTask;
    /*
       The sum of the requiredEstimatedTimeLeft for all the Tasks.
       Since some Tasks will have Events that are in the past (which might or might not have been completed), only the
       REMAINING estimated time of each Task is added to this sum. To get this value, call the timeLeftToDueDate() method
       of the task.
     */
    private static long totalEstimatedTimeRequired;
    private static ArrayList<Task> overlappingTasks; //list of Tasks which overlap with the new Task;

    public static void clearEmptySlots() { emptySlots.clearEmptySlots(); }

    /*
        Detects the empty slots in the given time range. An EmptySlot is a range of time that is not occupied by an Event.
     */
    public static ArrayList<EmptySlot> detectEmptySlots(DummyCalendar cal, long startTimeRange, long endTimeRange) {
        //all the Events between startTimeRange and endTimeRange (startTimeRange < event.startTime < endTimeRange)
        ArrayList<Event> detectedEvents = new ArrayList<Event>();

        for(Event e : cal.getEvents()) {
            if(e.getStartTime() >= startTimeRange && e.getStartTime() < endTimeRange) {
                detectedEvents.add(e);
            }
            else if(e.getEndTime() > startTimeRange && e.getEndTime() <= endTimeRange) {
                detectedEvents.add(e);
            }
        }

        //if there are no Events in the given time range, then [startTimeRange, endTimeRange] is an EmptySlot.
        if(detectedEvents.size() == 0 ){
            emptySlots.addEmptySlot(startTimeRange, endTimeRange);
        }

        //The following loop is only entered if there are Events in the given time range.
        for(int i = 0; i < detectedEvents.size(); i++) {
            Event e = detectedEvents.get(i);
            //for the first event detected, check if there is an EmptySlot before the event.
            if(i==0 && e.getStartTime() > startTimeRange) {
               emptySlots.addEmptySlot(startTimeRange, e.getStartTime());
            }

            //the time after the end of the last event is an EmptySlot if the last event ends within the given time range.
            if(i == (detectedEvents.size()-1) && e.getEndTime() < endTimeRange) {
                emptySlots.addEmptySlot(e.getEndTime(), endTimeRange);
            }

            //for events that are not the first or last Event in the given time range, check if there are EmptySlots
            //between to consecutive Events.
            else {
                Event eNext = detectedEvents.get(i+1);
                //System.out.println("detecting empty slot between two events");
                if(e.getEndTime() != eNext.getStartTime()) {
                    //System.out.println("detecting empty slot: " + DateTools.print(e.getEndTime()) + "  " + DateTools.print(eNext.getStartTime()));
                    emptySlots.addEmptySlot(e.getEndTime(), eNext.getStartTime());
                }
            }
        }

        listener.emptySlotsFound(emptySlots.getEmptySlots());
        return emptySlots.getEmptySlots();
    }

    private static void detectEmptySlotsDayByDay(DummyCalendar cal, long start, long end) {
        final long TWENTY_FOUR_HOURS = DateTools.hoursToMilis(24);
        final long ONE_SECOND = 1000;
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(new Date(start));
        GregorianCalendar firstDayEndTime = new GregorianCalendar();
        firstDayEndTime.set(Calendar.YEAR, gCal.get(Calendar.YEAR));
        firstDayEndTime.set(Calendar.MONTH, gCal.get(Calendar.MONTH));
        firstDayEndTime.set(Calendar.DAY_OF_MONTH, gCal.get(Calendar.DAY_OF_MONTH));
        firstDayEndTime.set(Calendar.HOUR, 23);
        firstDayEndTime.set(Calendar.MINUTE, 59);
        firstDayEndTime.set(Calendar.SECOND, 59);

        detectEmptySlots(cal, start, firstDayEndTime.getTimeInMillis());
        long nextDayStart = firstDayEndTime.getTimeInMillis()+ONE_SECOND;
        long nextDayStop = firstDayEndTime.getTimeInMillis() + TWENTY_FOUR_HOURS;

        while(nextDayStop < end) {
            detectEmptySlots(cal, nextDayStart, nextDayStop);
            nextDayStart = nextDayStop + ONE_SECOND;
            nextDayStop = nextDayStop + TWENTY_FOUR_HOURS;
        }

        long lastDayStart = nextDayStart;
        detectEmptySlots(cal, lastDayStart, end);
    }

    public static void addTaskToCalendar(DummyCalendar cal, String title, long timeEstimation, long startDate, long endDate, int priority) {
        final long ONE_HOUR = DateTools.hoursToMilis(1);
        //Create a newTask, but don't add it to the tasks list yet (it should only be added at the end, once it has been
        //split up into Events
        Task newTask = new Task(title, timeEstimation, startDate, endDate, priority);

        /*
            Initialize the variables and parameters needed for the calculations in the algorithm.

            # The value of now can be set by calling Optimiser.setNow() before calling the other methods of this class.
              This can be useful for tests.
            # First we get a list of the overlappingTasks. We also find the latestEndDate.
            # Before detecting the empty slots, we call clearEmptySlots(), to clear the list of the previously found slots;
            # Then we find all the empty slots between now and latestEndDate, using detectEmptySlotsDayByDay().
              We call this method which calls the detectEmptySlots() method  in a loop, gradually filling up the EmptySlots
              list with the EmptySlots detected for each 24 hour block between now and latestEndDate.
         */
        if (now == 0 ) { now = Calendar.getInstance().getTimeInMillis(); }
        latestEndDate = newTask.getEndDate();
        overlappingTasks = findOverlappingTasksInGivenTimeRange(now, newTask.getEndDate());
        overlappingTasks.add(newTask);

        for(Task t : tasks) {
            //find the latestEndDate
            if(t.getEndDate() > latestEndDate) {
                latestEndDate = t.getEndDate();
            }
        }

        //Clear the EmptySlots list
        clearEmptySlots();
        //Call the detectEmptySlots method to fill up the EmptySlots list
        detectEmptySlotsDayByDay(cal, max(now, newTask.getStartDate()), latestEndDate);
        //We calculate the task weights for the overlappingTasks.
        calculateAndSetTaskWeights(overlappingTasks);
        //Sort the overlappingTasks in ascending order of weight
        Collections.sort(overlappingTasks);
        //Get the total empty slot duration for each Task (the duration of empty time between now and the endDate of the Task)
        for(Task t : overlappingTasks) {
            long emptySlotsDuration = emptySlots.getTotalEmptySlotDuration(now, t.getEndDate());
            System.out.println("empty time for Task " + t.getTitle() + " "
                             + DateTools.msToHours(emptySlotsDuration));
            if(t.estimatedRequiredTimeRemaining(now) > emptySlotsDuration) {
                System.err.println("Task requires more time than empty slots available (without considering overlapping tasks)");
            }
        }

        nbFtEventsForNewTask = newTask.estimatedRequiredTimeRemaining(now)/ONE_HOUR;

        //TODO the rest of the algorithm : iterate through the tasks, split them into FtEvents, place the events in the calendar.
    }

    private static void calculateAndSetTaskWeights(ArrayList<Task> tasks) {
        //for now, we're not taking into account the priority.
        for(Task t : tasks) {
            t.setWeight((double)t.estimatedRequiredTimeRemaining(now)/t.timeLeftToDueDate(now));
            System.out.println("Task: " + t.getTitle() + "\t Weight = " + t.getWeight());
        }
    }

    //prerequisites: startRange < endRage; t.startDate < t.endDate for every Task t;
    public static ArrayList<Task> findOverlappingTasksInGivenTimeRange(long startRange, long endRange) {
        ArrayList<Task> overlappingTasks = new ArrayList<Task>();
        for (Task t : tasks) {
            if(t.getStartDate() <= startRange) {
                if(t.getEndDate() > startRange) {
                    overlappingTasks.add(t);
                }
            }
            else if (t.getStartDate() < endRange) {
                overlappingTasks.add(t);
            }
        }


        return overlappingTasks;
    }



    public ArrayList<Task> getTasks() { return tasks; }

    public static void setListener(OptimiserListener listener) {
        Optimiser.listener = listener;
    }
    public static void setNow(long now) { Optimiser.now = now; }
    public static long getNow() { return Optimiser.now; }

    public static void clearTasks(){ tasks.clear();  }
}
