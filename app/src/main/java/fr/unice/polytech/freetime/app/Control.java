package fr.unice.polytech.freetime.app;

import android.database.Cursor;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import fr.unice.polytech.calendarmodule.FreeTimeCalendarService;
import fr.unice.polytech.calendarmodule.IndexDayOutOfBoundException;
import fr.unice.polytech.entities.FtEventEntity;
import fr.unice.polytech.entities.TaskEntity;
import fr.unice.polytech.freetime.app.Entities.Task;
import fr.unice.polytech.freetime.app.Entities.event;
import fr.unice.polytech.freetime.app.utils.Fonction;

/**
 * Created by user on 10/06/2014.
 */
public class Control {

    private FreeTimeApplication app;
    private FreeTimeCalendarService ftcService;

    public Control(FreeTimeApplication application){
        this.app= application;
        this.ftcService= this.app.getFtcService();
    }


    public Cursor getListCalendar(){
        Cursor calCursor = ftcService.getAllCalendars();
          return calCursor;
    }

    public void importCalendar(long id){
        ftcService.importEventsFromCalendar(id);
    }


    public void createFreeTimeSlot(event freeTimeBlock){
        try{
            int day= Fonction.getIntDayCal(freeTimeBlock.getOneDay());
            ftcService.createFreeTimeBlock(day,freeTimeBlock.getStartTimeH(),
                    freeTimeBlock.getStartTimeM(),freeTimeBlock.getEndTimeH(),
                    freeTimeBlock.getEndTimeM());

        }catch (Exception e){
            Toast.makeText(app.getApplicationContext(),"cannot place this freeTime Slot",5).show();
        } catch (IndexDayOutOfBoundException e) {
            Toast.makeText(app.getApplicationContext(),"cannot place this freeTime Slot",5).show();
        }
    }


    public void createFixEvent(event fixEvent){
       try{
            ftcService.createOneTimeTask(fixEvent.getTitle(),
                    fixEvent.getDateStart().getTimeInMillis(),
                    fixEvent.getDateEnd().getTimeInMillis());

           Toast.makeText(app.getApplicationContext(),"the event has been correctly created",5).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(app.getApplicationContext(),"cannot create this fix event",5).show();
        }
    }

    public void createAllDayEvent(event allDayEvent){
        try{
            app.getFtcService().createEvent(allDayEvent.getTitle(),"",true,
                    allDayEvent.getDateStart().get(Calendar.YEAR),
                    allDayEvent.getDateStart().get(Calendar.MONTH),
                    allDayEvent.getDateStart().get(Calendar.DAY_OF_MONTH),
                    0,0,0,0,0,0,0,true);
            Toast.makeText(app.getApplicationContext(),"all day event added",5).show();
        }catch(Exception e){
            Toast.makeText(app.getApplicationContext(),"cannot create this event",5).show();
        }
    }

    public void createTaskProject(Task task){
        try{
                app.getFtcService().createLongTermTask(task.getTitle(),task.getDescription(),
                        task.getStart().getTimeInMillis(),task.getEnd().getTimeInMillis(),
                        task.getEstimationH(),task.getPriority());
                Toast.makeText(app.getApplicationContext(),"task created",5).show();

        }catch (Exception e){
            Toast.makeText(app.getApplicationContext(), "task cannot be create",5).show();
        }

    }

    public void createRecurrentEvent(event recurrentEvent){
            try{
                GregorianCalendar deb= new GregorianCalendar(2014,2,3,0,0,0);
                deb.set(Calendar.HOUR_OF_DAY,recurrentEvent.getStartTimeH());
                deb.set(Calendar.MINUTE,recurrentEvent.getStartTimeM());

                GregorianCalendar fin= new GregorianCalendar(2014,2,3,0,0,0);
                fin.set(Calendar.HOUR_OF_DAY, recurrentEvent.getEndTimeH());
                fin.set(Calendar.MINUTE, recurrentEvent.getEndTimeM());
                //System.out.println();
                int[] days= new int[recurrentEvent.getDay().length];
                int j=0;
                for(String i: recurrentEvent.getDay()){
                   days[j]=Fonction.getIntDayCal(i);
                    j++;
                }

                ftcService.createRecurringTask(recurrentEvent.getTitle(),days,deb.getTimeInMillis(),
                        fin.getTimeInMillis(),recurrentEvent.getDateEnd().getTimeInMillis());
            } catch (IndexDayOutOfBoundException e) {

                Toast.makeText(app.getApplicationContext(),"cannot create recurrente event",5).show();

            }
    }

    public ArrayList<FtEventEntity> getFTeventByDay(GregorianCalendar jour) {
        GregorianCalendar start = new GregorianCalendar(jour.get(Calendar.YEAR), jour.get(Calendar.MONTH), jour.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        GregorianCalendar end = new GregorianCalendar(jour.get(Calendar.YEAR), jour.get(Calendar.MONTH), jour.get(Calendar.DAY_OF_MONTH), 23, 59, 0);
        try {
            ArrayList<FtEventEntity> events = ftcService.findFtEventsInTimeRange(start.getTimeInMillis(), end.getTimeInMillis());
            return events;
        } catch (Exception e) {
            Toast.makeText(app.getApplicationContext(), "cannot get event slot for this day" + jour.toString(), 5).show();
        }
            return null;
    }

    public ArrayList<TaskEntity> getListTask(){
        //try{
           ArrayList<TaskEntity> listTask = ftcService.findAllTasks();
            return listTask;
      /*  }catch (Exception e){
            Toast.makeText(app.getApplicationContext(),"cannot get the list of task",5).show();
        }*/
      //  return null;
    }

}
