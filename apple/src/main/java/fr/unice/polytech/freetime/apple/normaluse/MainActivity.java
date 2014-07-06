package fr.unice.polytech.freetime.apple.normaluse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import fr.unice.polytech.freetime.apple.R;
import fr.unice.polytech.freetime.apple.firstuse.FirstUseActivity;
import fr.unice.polytech.freetime.apple.services.ImportCalendarService;
import fr.unice.polytech.freetime.apple.settingsuse.SettingsActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.reset_all) {
            Intent importCalendarIntent = new Intent(this, ImportCalendarService.class);
            importCalendarIntent.putExtra(ImportCalendarService.PARAM_METHOD, ImportCalendarService.METHOD_RESET);
            this.startService(importCalendarIntent);

            Intent firstUseIntent = new Intent(this, FirstUseActivity.class);
            this.startActivity(firstUseIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
