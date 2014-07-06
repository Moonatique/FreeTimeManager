package fr.unice.polytech.freetime.app.ManageTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.GregorianCalendar;

import fr.unice.polytech.freetime.app.Calendar.CalendarViewDay;
import fr.unice.polytech.freetime.app.Control;
import fr.unice.polytech.freetime.app.Entities.Task;
import fr.unice.polytech.freetime.app.FreeTimeApplication;
import fr.unice.polytech.freetime.app.R;
import fr.unice.polytech.freetime.app.utils.Fonction;


public class CreateTaskProject extends Activity {

    private EditText title;
    private EditText description;
    private DatePicker startDate;
    private DatePicker endDate;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String[] priorities;
    private Control control;
    private EditText estimatedhour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_project);
        title= (EditText) findViewById(R.id.editText_title);
        estimatedhour= (EditText) findViewById(R.id.editText_estimatedTime);
        description= (EditText) findViewById(R.id.editText_description);
        startDate = (DatePicker) findViewById(R.id.datePicker_startProject);
        endDate= (DatePicker) findViewById(R.id.datePicker_endProject);
        spinner= (Spinner) findViewById(R.id.spinner_priority);
        control=new Control((FreeTimeApplication) getApplication());

        String[] priorities = {"HIGH","NORMAL","LOW"};
        adapter= new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,priorities);
        spinner.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_task_project, menu);
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

    public void onCLick_save_taskProject(View view){

        if(!title.getText().toString().equals("")){

            if(!description.getText().toString().equals("")){

                if(spinner.getSelectedItem().toString()!=null){

                    if(!(startDate.getDayOfMonth()==endDate.getDayOfMonth() && startDate.getMonth()==endDate.getMonth() && startDate.getYear()==endDate.getYear())){

                        Task task= new Task(title.getText().toString());
                        task.setDescription(description.getText().toString());
                        int hour= (int) Integer.parseInt(estimatedhour.getText().toString());
                        task.setEstimationH(hour);
                        GregorianCalendar dateS= new GregorianCalendar(startDate.getYear(),startDate.getMonth(),startDate.getDayOfMonth());
                        GregorianCalendar dateE = new GregorianCalendar(endDate.getYear(),endDate.getMonth(),endDate.getDayOfMonth());
                        task.setStart(dateS);
                        task.setEnd(dateE);
                        task.setPriority(Fonction.getPriority(spinner.getSelectedItem().toString()));

                        control.createTaskProject(task);

                        Intent intent= new Intent(this, CalendarViewDay.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(this,"Select a valid start date and deadLine",5).show();
                    }
                }else{
                    Toast.makeText(this,"Select a priority",5).show();
                }
            }else{
                Toast.makeText(this,"Enter a description for this task",5).show();
            }
        }else{
            Toast.makeText(this,"Enter a name for this task",5).show();
        }

    }
}
