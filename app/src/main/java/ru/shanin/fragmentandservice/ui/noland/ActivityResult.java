package ru.shanin.fragmentandservice.ui.noland;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ru.shanin.fragmentandservice.R;
import ru.shanin.fragmentandservice.ui.fragment.result.FragmentResult;
import ru.shanin.fragmentandservice.ui.fragment.sensors.FragmentSensors;

public class ActivityResult extends AppCompatActivity {

    private static final String ARGUMENT_FROM_INPUT_TEXT = "input_Text";
    private static final String DEFAULT_TEXT = "There is no input text";
    private static final int DEFAULT_NUM = Sensor.REPORTING_MODE_CONTINUOUS;
    private static final String ARGUMENT_FROM_INPUT_INT = "input_Int";
    private static final String EXTRA_MODE = "Extra_mode";
    private static final String EXTRA_MODE_SHOW_INPUT_DATA = "Extra_mode_input";
    private static final String EXTRA_MODE_SHOW_SENSORS = "Extra_mode_sensors";
    private String input_data_text;
    private int input_data_int;

    public static Intent newIntentActivityResultWithStringData(Context context, String arg) {
        Intent intent = new Intent(context, ActivityResult.class);
        intent.putExtra(EXTRA_MODE, EXTRA_MODE_SHOW_INPUT_DATA);
        intent.putExtra(ARGUMENT_FROM_INPUT_TEXT, arg);
        return intent;
    }

    public static Intent newIntentActivityResultWithSensorData(Context context, int arg) {
        Intent intent = new Intent(context, ActivityResult.class);
        intent.putExtra(EXTRA_MODE, EXTRA_MODE_SHOW_SENSORS);
        intent.putExtra(ARGUMENT_FROM_INPUT_INT, arg);
        return intent;
    }

    private void parseIntent() {
        input_data_int = DEFAULT_NUM;
        input_data_text = DEFAULT_TEXT;
        if (!getIntent().hasExtra(EXTRA_MODE))
            throw new RuntimeException("Extra mode is absent");
        String mode = getIntent().getStringExtra(EXTRA_MODE);
        if (mode.equals(EXTRA_MODE_SHOW_INPUT_DATA))
            if (!getIntent().hasExtra(ARGUMENT_FROM_INPUT_TEXT))
                throw new RuntimeException("Input text data is absent");
            else input_data_text = getIntent().getStringExtra(ARGUMENT_FROM_INPUT_TEXT);
        else if (mode.equals(EXTRA_MODE_SHOW_SENSORS))
            if (!getIntent().hasExtra(ARGUMENT_FROM_INPUT_INT))
                throw new RuntimeException("Input int data is absent");
            else input_data_int = getIntent().getIntExtra(ARGUMENT_FROM_INPUT_INT, DEFAULT_NUM);
        else throw new RuntimeException("Unknown extra mode " + mode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        parseIntent();
        if (savedInstanceState == null) {
            if (!input_data_text.equals(DEFAULT_TEXT))
                launchFragmentResultWithStringData(input_data_text);
            if (input_data_int != DEFAULT_NUM)
                launchFragmentSensorsWithSensorData(input_data_int);
        }
    }

    private void launchFragmentResultWithStringData(String data) {
        Fragment fragment = FragmentResult.newInstanceWithInputData(data);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("null")
                .replace(R.id.fragmentActivityResult, fragment)
                .commit();
    }

    private void launchFragmentSensorsWithSensorData(int data) {
        Fragment fragment = FragmentSensors.newInstanceWithInputSensorData(data);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("null")
                .add(R.id.fragmentActivityResult, fragment)
                .commit();
    }
}