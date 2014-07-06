package fr.unice.polytech.freetime.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import fr.unice.polytech.calendarmodule.FreeTimeCalendarService;

/**
 * Created by Hakim on 07/06/2014.
 */
public class FreeTimeApplication extends Application {
    private FreeTimeCalendarService ftcService;
    private boolean bound = false;
    private ServiceConnection ftcServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            ftcService = ((FreeTimeCalendarService.FreeTimeBinder)service).getService();

            Toast.makeText(getApplicationContext(), R.string.service_connected, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            ftcService = null;

            Toast.makeText(getApplicationContext(), R.string.service_disconnected, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        bound = bindService(new Intent(this, FreeTimeCalendarService.class), ftcServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public FreeTimeCalendarService getFtcService() {
        return ftcService;
    }
}
