package fr.unice.polytech.freetime.app.InitParam.freeTime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.unice.polytech.freetime.app.Entities.User;
import fr.unice.polytech.freetime.app.Entities.event;
import fr.unice.polytech.freetime.app.InitParam.FirstParam2;
import fr.unice.polytech.freetime.app.R;


public class FreeTimeSlotList extends Activity {

    private User user;
    private List<event> freeTime;
    private List<String> freeTimeTitle;
    private ListAdapter adapter;
    private event freeTimeSlotSelected;
    private HashMap<String,event> freeTimeH;
    private HashMap<String,String> etiq_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_time_slot_list);

        user= (User) getIntent().getSerializableExtra("userParam");
        freeTime= user.getFreeTime();
        freeTimeH= new HashMap<String,event>();
        freeTimeTitle= new ArrayList<String>();
        etiq_title= new HashMap<String,String>();

        for (event e : freeTime) {

            freeTimeTitle.add(e.getTitle()+" - "+e.getOneDay()+" - "+
                                e.getStartTimeH()+":"+e.getStartTimeM()+" to "+
                                e.getEndTimeH()+":"+e.getEndTimeM());


            if(etiq_title.containsKey(e.getTitle()+" - "+e.getOneDay()+" - "+
                    e.getStartTimeH()+":"+e.getStartTimeM()+" to "+
                    e.getEndTimeH()+":"+e.getEndTimeM())) {


                etiq_title.put(e.getTitle() + " - " + e.getOneDay() + " - " +
                        e.getStartTimeH() + ":" + e.getStartTimeM() + " to " +
                        e.getEndTimeH() + ":" + e.getEndTimeM(), e.getTitle());

            }
            freeTimeH.put(e.getTitle(), e);
        }



        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, freeTimeTitle);
        ListView list = (ListView) findViewById(R.id.listview_freeTimeSlot);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String titleSelected = etiq_title.get(freeTimeTitle.get(arg2));
                freeTimeSlotSelected= freeTimeH.get(titleSelected);
                Button button= (Button) findViewById(R.id.button_remove_freeTimeSlot);
                button.setVisibility(View.VISIBLE);
            }

        });

        Button button_remove=(Button) findViewById(R.id.button_remove_freeTimeSlot);
        button_remove.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.free_time_slot_list, menu);
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

    public void onClick_remove_freeTimeSlot(View view){
        if(freeTimeSlotSelected!=null) {
            Log.i("item selected", "*****non null");
            System.out.println("*****non null");
            freeTimeH.remove(freeTimeSlotSelected.getTitle());
            freeTime.remove(freeTimeSlotSelected);
            freeTimeTitle.remove(freeTimeSlotSelected.getTitle()+" - "+
                    freeTimeSlotSelected.getOneDay()+" - "+
                    freeTimeSlotSelected.getStartTimeH()+":"+
                    freeTimeSlotSelected.getStartTimeM()+" to "+
                    freeTimeSlotSelected.getEndTimeH()+":"+
                    freeTimeSlotSelected.getEndTimeM());
            adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, freeTimeTitle);
            ListView list = (ListView) findViewById(R.id.listview_freeTimeSlot);
            list.setAdapter(adapter);
            list.invalidateViews();
        }else{
            System.out.println("*****non null");

            Log.i("item selected", "*****item null");

        }

        Button button_remove=(Button) findViewById(R.id.button_remove_freeTimeSlot);
        button_remove.setVisibility(View.INVISIBLE);
    }

    public void onClick_add_freeTimeSlot(View view){
        Intent intent= new Intent(this,InitFreeTime.class);
        user.setFreeTime(freeTime);
        intent.putExtra("userParam", user);
        startActivity(intent);
    }

    public void onClick_finish_freeTimeList(View view){
        user.setSetFreeTime(true);
        int step=user.getStep();
        user.setStep(++step);
        Intent intent= new Intent(this,FirstParam2.class);
        user.setFreeTime(freeTime);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
