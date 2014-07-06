package fr.unice.polytech.freetime.app.InitParam;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import fr.unice.polytech.freetime.app.Calendar.CalendarViewDay;
import fr.unice.polytech.freetime.app.Entities.User;
import fr.unice.polytech.freetime.app.InitParam.recurrentEvent.NotifParam;
import fr.unice.polytech.freetime.app.R;
import fr.unice.polytech.freetime.app.InitParam.recurrentEvent.RecurrentEventList;


public class FirstParam2 extends ActionBarActivity {

   // private int step;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_param2);

        user= (User) getIntent().getSerializableExtra("user");
        EditText textName= (EditText) findViewById(R.id.editText_name);

        int step = user.getStep();
        switch (step){

            case 0:break;
            case 1:LinearLayout l1= (LinearLayout) findViewById(R.id.ll1);
                    l1.setVisibility(View.VISIBLE);
                    textName.setText(user.getName());
                    if(user.isImportCal()){
                    ImageButton ib= (ImageButton) findViewById(R.id.done_button1);
                    ib.setVisibility(View.VISIBLE);
                    }
                break;

            case 2:
                LinearLayout l12= (LinearLayout) findViewById(R.id.ll1);
                l12.setVisibility(View.VISIBLE);
                LinearLayout l2= (LinearLayout) findViewById(R.id.ll2);
                l2.setVisibility(View.VISIBLE);
                textName.setText(user.getName());

                if(user.isImportCal()){
                    ImageButton ib= (ImageButton) findViewById(R.id.done_button1);
                    ib.setVisibility(View.VISIBLE);
                }
                if(user.isSetFreeTime()){
                    ImageButton ib= (ImageButton) findViewById(R.id.done_button2);
                    ib.setVisibility(View.VISIBLE);

                }
                break;

            case 3:
                LinearLayout l13= (LinearLayout) findViewById(R.id.ll1);
                l13.setVisibility(View.VISIBLE);
                LinearLayout l23= (LinearLayout) findViewById(R.id.ll2);
                l23.setVisibility(View.VISIBLE);
                LinearLayout l3= (LinearLayout) findViewById(R.id.ll3);
                l3.setVisibility(View.VISIBLE);
                textName.setText(user.getName());

                if(user.isImportCal()){
                    ImageButton ib= (ImageButton) findViewById(R.id.done_button1);
                    ib.setVisibility(View.VISIBLE);
                }
                if(user.isSetFreeTime()){
                    ImageButton ib= (ImageButton) findViewById(R.id.done_button2);
                    ib.setVisibility(View.VISIBLE);
                }
                if(user.isSetDailiesAct()){
                    ImageButton ib= (ImageButton) findViewById(R.id.done_button3);
                    ib.setVisibility(View.VISIBLE);
                }
                break;

            default:
                LinearLayout l14= (LinearLayout) findViewById(R.id.ll1);
                l14.setVisibility(View.VISIBLE);
                LinearLayout l24= (LinearLayout) findViewById(R.id.ll2);
                l24.setVisibility(View.VISIBLE);
                LinearLayout l34= (LinearLayout) findViewById(R.id.ll3);
                l34.setVisibility(View.VISIBLE);
                LinearLayout l4= (LinearLayout) findViewById(R.id.ll4);
                l4.setVisibility(View.VISIBLE);
                textName.setText(user.getName());

                if(user.isImportCal()){
                    ImageButton ib= (ImageButton) findViewById(R.id.done_button1);
                    ib.setVisibility(View.VISIBLE);
                }
                if(user.isSetFreeTime()){
                    ImageButton ib= (ImageButton) findViewById(R.id.done_button2);
                    ib.setVisibility(View.VISIBLE);
                }
                if(user.isSetDailiesAct()){
                ImageButton ib= (ImageButton) findViewById(R.id.done_button3);
                ib.setVisibility(View.VISIBLE);
                }

                if(user.isNotificationParam()){
                    ImageButton ib= (ImageButton) findViewById(R.id.done_button4);
                    ib.setVisibility(View.VISIBLE);
                }
                Button bNext= (Button) findViewById(R.id.button_next);
                bNext.setText("Finish");
                break;


        }

        textName.clearFocus();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first_param2, menu);
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


    public void onClick_text(View view){
        EditText textName= (EditText) findViewById(R.id.editText_name);
        String name= textName.getText().toString();
            if(name.equals("Enter your name")) {
                textName.setText("");
            }
    }

    public void onClick_next(View view){
        int step= user.getStep();
        switch (step){
            case 0:
                EditText nameEdit= (EditText) findViewById(R.id.editText_name);

                String name= nameEdit.getText().toString();
                if(name.equals("Enter your name") || name.equals("")){
                    Toast.makeText(getApplicationContext(), "name doesn't valid",5).show();
                }else {
                    user.setName(name);
                    LinearLayout l1 = (LinearLayout) findViewById(R.id.ll1);
                    l1.setVisibility(view.VISIBLE);
                    user.setStep(++step);
                }
                break;

            case 1:
                LinearLayout l2= (LinearLayout) findViewById(R.id.ll2);
                l2.setVisibility(View.VISIBLE);user.setStep(++step);break;

            case 2:LinearLayout l3=(LinearLayout) findViewById(R.id.ll3);
                l3.setVisibility(View.VISIBLE);
                user.setStep(++step);break;

            case 3:LinearLayout l4= (LinearLayout) findViewById(R.id.ll4);
                l4.setVisibility(View.VISIBLE);
                Button bNext= (Button) findViewById(R.id.button_next);
                bNext.setText("Finish");
                user.setStep(++step);break;

            //prochaine etape
            case 4:Intent intent= new Intent(this,CalendarViewDay.class);
                    intent.putExtra("user",user);
                    startActivity(intent);break;

            default:
                Intent intentD= new Intent(this,CalendarViewDay.class);
                intentD.putExtra("user",user);
                startActivity(intentD);break;
        }
    }


    public void onClick_import(View view){
        Intent intent= new Intent(this, ImportCalendar.class);

        intent.putExtra("userParam",user);
        startActivity(intent);
    }

    public void onClick_freetime(View view){
        Intent intent= new Intent(this, DefineEffectiveHours.class);

        intent.putExtra("userParam",user);
        startActivity(intent);
    }

    public void onClick_dailyAct(View view){
        Intent intent= new Intent(this, RecurrentEventList.class);

        intent.putExtra("userParam",user);
        startActivity(intent);
    }

    public void onClick_notif(View view){
        Intent intent= new Intent(this, NotifParam.class);

        intent.putExtra("userParam",user);
        startActivity(intent);
    }
}
