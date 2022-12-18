package ru.shanin.fragmentandservice.ui.fragment.sensors;

import android.hardware.Sensor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.shanin.fragmentandservice.app.AppStart;

public class SensorsViewModel extends ViewModel {

    public MutableLiveData<List<Sensor>> sensorsData = AppStart.mService.getListOfSensorsLiveData();
    public MutableLiveData<String> mySensorData = AppStart.mService.getMySensorDataLiveData();


}
