package ru.shanin.fragmentandservice.service;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.lifecycle.MutableLiveData;

public class MySensor {
    private final Sensor sensor;
    private SensorEventListener listener;

    public MySensor(int sensorType, MutableLiveData<String> sensorLiveData) {
        sensor = MySensorsService.getSensorManager().getDefaultSensor(sensorType);
        if (sensor == null)
            sensorLiveData.postValue("There is no Sensor with Type = " + sensorType);
        else
            listener = this.SettingListener(sensorLiveData);
    }

    private SensorEventListener SettingListener(MutableLiveData<String> liveData) {
        return new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {
                liveData.setValue(String.valueOf(event.values[0]));
            }
        };
    }

    private void onStartListenSens() {
        MySensorsService
                .getSensorManager()
                .registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void onStopListenSens() {
        MySensorsService
                .getSensorManager()
                .unregisterListener(listener, sensor);
    }

    public void startWork() {
        if (sensor != null) onStartListenSens();
    }

    public void stopWork() {
        if (sensor != null) onStopListenSens();
    }
}