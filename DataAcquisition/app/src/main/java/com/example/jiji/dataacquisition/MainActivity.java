package com.example.jiji.dataacquisition;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity {
    private final String TAG = MainActivity.class.getSimpleName();
    private SensorManager sensorManager;
    private BufferedWriter file;
    private Map<Integer, String> sensorTypes = new HashMap<>();
    private Map<Integer, Sensor> sensors = new HashMap<>();
    private TextView filenameDisplay;
    private TextView logDisplay;
    //private Sensor accelerometer;
    //private long lastTime = 0;
    //private float lastX, lastY, lastZ;
    //private static final int THRESHOLD = 600;
    TextView coordinates;
    TextView address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get sensors to be captured
        sensorTypes.put(Sensor.TYPE_ACCELEROMETER, "ACCEL");
        sensorTypes.put(Sensor.TYPE_GYROSCOPE, "GYRO");
        sensorTypes.put(Sensor.TYPE_LINEAR_ACCELERATION, "LINEAR");
        sensorTypes.put(Sensor.TYPE_MAGNETIC_FIELD, "MAG");
        sensorTypes.put(Sensor.TYPE_GRAVITY, "GRAV");
        sensorTypes.put(Sensor.TYPE_ROTATION_VECTOR, "ROTATION");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        for (Integer type : sensorTypes.keySet()){
            sensors.put(type, sensorManager.getDefaultSensor(type));
        }

        /*sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);*/

        findViewById(R.id.button1).setOnClickListener(clickListener);
        filenameDisplay = (TextView) findViewById(R.id.filename);
        logDisplay = (TextView) findViewById(R.id.log);

        Button update = (Button) findViewById(R.id.update_button);
        update.setOnClickListener(new UpdateLocationClick());
        coordinates = (TextView) findViewById(R.id.location_points);
        address = (TextView) findViewById(R.id.location_address);
        if (ServiceApp.LOCATION != null) {
            double lat = ServiceApp.LOCATION.getLatitude();
            double lon = ServiceApp.LOCATION.getLongitude();
            coordinates.setText(lat + " " + lon);
            Geocoder geocoder = new Geocoder(getApplicationContext(), new Locale("en"));
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                if (addresses != null && addresses.size() != 0) {
                    StringBuilder builder = new StringBuilder();
                    Address returnAddress = addresses.get(0);
                    for (int i = 0; i < returnAddress.getMaxAddressLineIndex(); i++) {
                        builder.append(returnAddress.getAddressLine(i));
                        builder.append(" ");
                    }
                    address.setText(builder);
                    address.setVisibility(View.VISIBLE);
                } else {
                    Log.e(TAG, "Addresses null");
                }
            } catch (IOException e) {
                Log.e(TAG, "Geocoder exception " + e);
            }
        } else {
            coordinates.setText("No location yet");
            address.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRecording();
    }

    private void stopRecording() {
    }

    private void startRecording() {
        // Prepare data storage
        File directory = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String name = "AllData_" + System.currentTimeMillis() + ".csv";
        File filename = new File(directory, name);
        try {
            file = new BufferedWriter(new FileWriter(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        filenameDisplay.setText(name);

        // Register sensor listeners
        for (Sensor sensor : sensors.values()) {
            sensorManager.registerListener(sensorListener, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button1:
                    if (((ToggleButton) view).isChecked()) {
                        startRecording();
                    } else {
                        stopRecording();
                    }
                    break;
            }
        }
    };

    private SensorEventListener sensorListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            write(sensorTypes.get(event.sensor.getType()), event.values);
        }

    };

    private void write(String tag, String[] values) {
        if (file == null) {
            return;
        }

        String line = "";
        if (values != null) {
            for (String value : values) {
                line += "," + value;
            }
        }
        line = Long.toString(System.currentTimeMillis()) + "," + tag + line
                + "\n";

        try {
            file.write(line);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        logDisplay.setText(line);
    }

    private void write(String tag, float[] values) {
        String[] array = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Float.toString(values[i]);
        }
        write(tag, array);
    }

    private void write(String tag, double[] values) {
        String[] array = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Double.toString(values[i]);
        }
        write(tag, array);
    }

    private void write(String tag) {
        write(tag, (String[]) null);
    }

    /*@Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastTime) > 100) {
                long diffTime = (currentTime - lastTime);
                lastTime = currentTime;
                float speed = Math.abs(x + y + z - lastX - lastY - lastZ)/ diffTime * 10000;
                if (speed > THRESHOLD) {
                    getRandomNumber();
                }
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    private void getRandomNumber() {
        Random randNumber = new Random();
        int iNumber = randNumber.nextInt(100);
        TextView text = (TextView)findViewById(R.id.number);
        text.setText("" + iNumber);
        RelativeLayout ball = (RelativeLayout) findViewById(R.id.ball);
        ball.setVisibility(View.INVISIBLE);
        ball.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }*/
}
