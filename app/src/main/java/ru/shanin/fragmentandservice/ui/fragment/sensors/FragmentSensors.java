package ru.shanin.fragmentandservice.ui.fragment.sensors;

import android.hardware.Sensor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import ru.shanin.fragmentandservice.R;
import ru.shanin.fragmentandservice.app.AppStart;
import ru.shanin.fragmentandservice.service.MySensorsService;

public class FragmentSensors extends Fragment {

    private static final String ARGUMENT_FROM_INPUT_INT = "input_Int";
    private static final int ARGUMENT_FROM_INPUT_TEXT_DEFAULT = Sensor.REPORTING_MODE_CONTINUOUS;
    private int sensorType;
    private TextView tv_sensor;
    private MySensorsService mService;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = AppStart.mService;
        parseParams();
    }

    @Override
    public void onDestroyView() {
        if (sensorType != Sensor.TYPE_ALL)
            mService.stopMySensorListener();
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.fragment_sensors,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout(view);
        initViewModel();
    }

    private void initViewModel() {
        SensorsViewModel viewModel = new ViewModelProvider(this).get(SensorsViewModel.class);
        if (sensorType == Sensor.TYPE_ALL) viewModel.sensorsData
                .observe(getViewLifecycleOwner(), new MyObserverListData());
        else viewModel.mySensorData
                .observe(getViewLifecycleOwner(), new MyObserverStringData());
    }

    public static FragmentSensors newInstanceWithInputSensorData(int input) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_FROM_INPUT_INT, input);
        FragmentSensors fragment = new FragmentSensors();
        fragment.setArguments(args);
        return fragment;
    }

    private void parseParams() {
        sensorType = ARGUMENT_FROM_INPUT_TEXT_DEFAULT;
        Bundle args = requireArguments();
        if (!args.containsKey(ARGUMENT_FROM_INPUT_INT))
            throw new RuntimeException("Arguments are absent");
        sensorType = args.getInt(ARGUMENT_FROM_INPUT_INT);
        if (sensorType != Sensor.TYPE_ALL) {
            if (mService.newMySensor(sensorType))
                mService.startMySensorListener();
        }
    }

    private void initLayout(View view) {
        tv_sensor = view.findViewById(R.id.tv_sensors);
        (view.findViewById(R.id.bt)).setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private class MyObserverListData implements Observer<List<Sensor>> {
        @Override
        public void onChanged(List<Sensor> sensorsList) {
            StringBuilder result = new StringBuilder("Sensors:\n");
            if (!sensorsList.isEmpty()) {
                for (Sensor s : sensorsList) result = result.append(s.getName()).append("\n");
            } else {
                result = result.append("null\n");
            }
            tv_sensor.setText(result.toString());
        }
    }

    private class MyObserverStringData implements Observer<String> {
        @Override
        public void onChanged(String sensorData) {
            String result = "Sensor data:\n" + sensorData;
            tv_sensor.setText(result);
        }
    }
}