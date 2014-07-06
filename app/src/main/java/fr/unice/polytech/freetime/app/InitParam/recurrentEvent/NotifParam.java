package fr.unice.polytech.freetime.app.InitParam.recurrentEvent;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fr.unice.polytech.freetime.app.Entities.User;
import fr.unice.polytech.freetime.app.InitParam.FirstParam2;
import fr.unice.polytech.freetime.app.R;


public class NotifParam extends ActionBarActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_param);
        user = (User) getIntent().getSerializableExtra("userParam");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notif_param, menu);
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

    public void onClick_okValid(View view){
        user.setNotificationParam(true);
        int step=user.getStep();
        //user.setStep(++step);
        Intent intent= new Intent(this, FirstParam2.class);
        intent.putExtra("user",user);
        startActivity(intent);

    }
}
