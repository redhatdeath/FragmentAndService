package ru.shanin.fragmentandservice.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import ru.shanin.fragmentandservice.R;
import ru.shanin.fragmentandservice.service.MySensorsService;
import ru.shanin.fragmentandservice.ui.fragment.result.FragmentResult;
import ru.shanin.fragmentandservice.ui.fragment.sensors.FragmentSensors;
import ru.shanin.fragmentandservice.ui.noland.ActivityResult;

public class ActivityMain extends AppCompatActivity {

    private FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        onStartAppService(this);
    }

    private void onStartAppService(Context context) {
        context.startForegroundService(MySensorsService.onMySensorsService(context));
    }

    private void initLayout() {
        fragmentContainerView = findViewById(R.id.fragmentActivityResult);
        new ButtonSensorHandler(this, R.id.bt_list + 0, Sensor.TYPE_ALL + 0);
        new ButtonSensorHandler(this, R.id.bt_accel, Sensor.TYPE_ACCELEROMETER);
        new ButtonSensorHandler(this, R.id.bt_gyros, Sensor.TYPE_GYROSCOPE);
        new ButtonSensorHandler(this, R.id.bt_light, Sensor.TYPE_LIGHT);
        new ButtonSensorHandler(this, R.id.bt_magne, Sensor.TYPE_MAGNETIC_FIELD);
        new ButtonHandler(this, R.id.bt + 0, R.id.et + 0);
    }

    private boolean isOnePaneMode() {
        return fragmentContainerView == null;
    }

    private final class ButtonSensorHandler implements View.OnClickListener {
        private final int sensorType;

        public ButtonSensorHandler(Activity activity, int btId, int sensorType) {
            (activity.findViewById(btId)).setOnClickListener(this);
            this.sensorType = sensorType;
        }

        @Override
        public void onClick(View v) {
            if (isOnePaneMode()) launchActivityResultWithSensorData(sensorType);
            else launchFragmentSensorsWithSensorData(sensorType);
        }
    }

    private final class ButtonHandler implements View.OnClickListener {
        private final EditText et;

        public ButtonHandler(Activity activity, int btId, int etId) {
            et = activity.findViewById(etId);
            (activity.findViewById(btId)).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String input = et.getText().toString();
            if (isOnePaneMode()) launchActivityResultWithStringData(input);
            else launchFragmentResultWithStringData(input);
        }
    }

    private void launchActivityResultWithStringData(String data) {
        Intent i = ActivityResult
                .newIntentActivityResultWithStringData(getApplicationContext(), data);
        startActivity(i);
    }

    private void launchActivityResultWithSensorData(int data) {
        Intent i = ActivityResult
                .newIntentActivityResultWithSensorData(getApplicationContext(), data);
        startActivity(i);
    }

    private void launchFragmentResultWithStringData(String data) {
        Fragment fragment;
//        if(bla bla bla){}
//        else {}
        fragment = FragmentResult.newInstanceWithInputData(data);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("null")
//                .replace(R.id.fragmentActivityResult,
//                        ResultFragment.newInstanceWithInputData(data))
                .add(R.id.fragmentActivityResult, fragment)
                .commit();
    }

    private void launchFragmentSensorsWithSensorData(int data) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("null")
                .replace(R.id.fragmentActivityResult,
                        FragmentSensors.newInstanceWithInputSensorData(data))
                .commit();
    }

    @Override
    protected void onDestroy() {
        stopService(MySensorsService.onMySensorsService(getApplicationContext()));
        super.onDestroy();
    }
}