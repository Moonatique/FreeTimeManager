package fr.unice.polytech.freetime.app.ManageTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

import fr.unice.polytech.freetime.app.Entities.Task;
import fr.unice.polytech.freetime.app.R;
import fr.unice.polytech.freetime.app.utils.Fonction;

public class ViewTaskProject extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task_project);

        Task task=(Task) getIntent().getSerializableExtra("eventTask");
        TextView title = (TextView) findViewById(R.id.textview_titleProject);
        title.setText(task.getTitle());


        TextView descr= (TextView) findViewById(R.id.textview_Description);
        descr.setText(task.getDescription());

        TextView start= (TextView) findViewById(R.id.editText_DateStart);
        String dateStart= task.getStart().get(Calendar.MONTH)+","+task.getStart().get(Calendar.DAY_OF_MONTH)+","+task.getStart().get(Calendar.YEAR);
        start.setText(dateStart);

        TextView end= (TextView) findViewById(R.id.textview_deadline);
        String dateEnd= task.getEnd().get(Calendar.MONTH)+","+task.getEnd().get(Calendar.DAY_OF_MONTH)+","+task.getEnd().get(Calendar.YEAR);
        end.setText(dateEnd);

        TextView estimation = (TextView) findViewById(R.id.textview_estimation_hour);
        estimation.setText(""+task.getEstimationH());

        TextView priority = (TextView) findViewById(R.id.textview_priority);
        priority.setText(Fonction.getPriorityString(task.getPriority()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_task_project, menu);
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
}
