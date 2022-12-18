package ru.shanin.fragmentandservice.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import ru.shanin.fragmentandservice.service.MySensorsService;

public class AppStart extends Application {
    public static MySensorsService mService;

    @Override
    public void onCreate() {
        super.onCreate();
       // onStartAppService(this);
        initService();
    }

    private void onStartAppService(Context context) {
        context.startForegroundService(MySensorsService.onMySensorsService(context));
    }

    private void initService() {
        final ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(
                    ComponentName className,
                    IBinder service
            ) {
                MySensorsService.LocalBinder binder = (MySensorsService.LocalBinder) service;
                mService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mService.stopMySensorListener();
            }
        };
        // Bind to LocalService
        Intent intent = new Intent(this, MySensorsService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }


}
