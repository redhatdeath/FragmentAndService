package ru.shanin.fragmentandservice.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class MySensorsService extends Service {

    private static SensorManager sensorManager;
    private MySensor mySensor;
    private MutableLiveData<String> mySensorDataLiveData;
    private MutableLiveData<List<Sensor>> listOfSensorsLiveData;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MySensorsService getService() {
            return MySensorsService.this;
        }
    }

    public static Intent onMySensorsService(Context context) {
        return new Intent(context, MySensorsService.class);
    }

    public boolean newMySensor(int sensorType) {
        mySensorDataLiveData = new MutableLiveData<>();
        mySensor = new MySensor(sensorType, mySensorDataLiveData);
        return true;
    }

    public void startMySensorListener() {
        mySensor.startWork();
    }

    public void stopMySensorListener() {
        mySensor.stopWork();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSensor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager == null)
            throw new RuntimeException("There is no sensors...");
        // get list of sensors
        listOfSensorsLiveData = new MutableLiveData<>();
        listOfSensorsLiveData.postValue(sensorManager.getSensorList(Sensor.TYPE_ALL));
    }

    public MutableLiveData<List<Sensor>> getListOfSensorsLiveData() {
        return listOfSensorsLiveData;
    }

    public MutableLiveData<String> getMySensorDataLiveData() {
        return mySensorDataLiveData;
    }

    public static SensorManager getSensorManager() {
        return sensorManager;
    }

}

