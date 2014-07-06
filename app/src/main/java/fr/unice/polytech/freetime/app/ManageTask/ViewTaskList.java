package fr.unice.polytech.freetime.app.ManageTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import fr.unice.polytech.entities.FtEventEntity;
import fr.unice.polytech.entities.TaskEntity;
import fr.unice.polytech.freetime.app.Control;
import fr.unice.polytech.freetime.app.Entities.Task;
import fr.unice.polytech.freetime.app.FreeTimeApplication;
import fr.unice.polytech.freetime.app.R;
import fr.unice.polytech.freetime.app.utils.Fonction;

public class ViewTaskList extends Activity {

    private ArrayList<TaskEntity> taskList;
    private HashMap<String,TaskEntity> listNameTask;
    private List<String> titleTask;
    private ListAdapter adapter;
    private Control control;
    private TaskEntity taskSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task_list);

        control= new Control((FreeTimeApplication) getApplication());
        taskList=  control.getListTask();
        listNameTask = new HashMap<String,TaskEntity>();
        titleTask= new ArrayList<String>();


        for (TaskEntity e : taskList) {
            listNameTask.put(e.getTitle(), e);
            titleTask.add(e.getTitle());
        }


        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, titleTask);
        ListView list = (ListView) findViewById(R.id.list_view_list_task);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String titleSelected = titleTask.get(arg2);
                taskSelected= listNameTask.get(titleSelected);

                selectTask(taskSelected);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectTask(TaskEntity task){
        Intent intent=new Intent(this,ViewTaskProject.class);

        Task taskEntity= new Task(task.getTitle());
        taskEntity.setPriority(task.getPriority());
        taskEntity.setDescription(task.getDescription());
        int hour=task.getHourEstimation();
        taskEntity.setEstimationH(hour);


        GregorianCalendar start= new GregorianCalendar();
        start.setTimeInMillis(task.getStartDate());

        taskEntity.setStart(start);
        GregorianCalendar end= new GregorianCalendar();
        end.setTimeInMillis(task.getEndDate());
        taskEntity.setEnd(end);

        intent.putExtra("eventTask",taskEntity);
        startActivity(intent);
    }
}
