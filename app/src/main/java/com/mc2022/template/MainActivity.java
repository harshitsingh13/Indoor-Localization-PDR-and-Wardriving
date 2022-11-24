package com.mc2022.template;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    TextView stepsText,distance,diststride;
    EditText ht;
    Button bt,scanbtn;
    TextView accl1,accl2,accl3;
    ToggleButton accel_btn,mag_btn;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    public static List<ScanResult> results;
    public static ArrayList<String> wname=new ArrayList<>();
    LocationListener mlocListener;
    public static double latitude=0.0;
    public static double longitude=0.0;
    private CircularProgressBar bar;
    boolean inStep;
    int steps=0;
    double magnitudeprevious=0;
    float height;
    MyDatabase mydb;
    float dist=0;
    //MyDatabaseLight mydbl;
    public static ArrayList<String> arr1, arr2;
    ImageView ig;
    Sensor accelerometer;
    Sensor magneticfield;

    float[] floatgravity=new float[3];
    float[] floatgeomagnetic=new float[3];

    float[] floatrotationmatrix=new float[9];
    float[] floatorientation=new float[3];



    protected void Stop(int i) {

        if (i == 1) {
            sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
            // sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));

        }
        else if (i == 2) {
            sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));

        }

    }

    public void reset(View view){
        ig.setRotation(180);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = MyDatabase.getInstance(MainActivity.this);
        stepsText=(TextView) findViewById(R.id.stepsText);
        distance=(TextView) findViewById(R.id.stride);
        diststride=(TextView) findViewById(R.id.dist);
        bar=(CircularProgressBar)findViewById(R.id.circularProgressBar);

        scanbtn=(Button)findViewById(R.id.wifiscanbtn);
        //accl1 = (TextView) findViewById(R.id.acc1);
        //accl2 = (TextView) findViewById(R.id.acc2);
        //accl3 = (TextView) findViewById(R.id.acc3);

        ig=(ImageView) findViewById(R.id.img);

        accel_btn = (ToggleButton) findViewById(R.id.toggleButton1);
        mag_btn = (ToggleButton) findViewById(R.id.toggleButton2);

        bt = (Button)findViewById(R.id.btn);
        ht   = (EditText)findViewById(R.id.height);

        bt.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        height= Float.valueOf(ht.getText().toString());
                        ht.setText("");
                    }
                });

        scanbtn.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Intent intent=new Intent(MainActivity.this,wifiscan.class);
                        startActivity(intent);
                    }
                });

        Log.d(TAG, "Initializing sensor service");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    //magneticfield = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                    if (accelerometer != null) {

                        //sensorManager.registerListener(MainActivity.this, magneticfield, sensorManager.SENSOR_DELAY_NORMAL);

                        sensorManager.registerListener(MainActivity.this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);
                        Toast.makeText(MainActivity.this, "Accelerometer sensor started", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "Accelerometer Not supported");
                        Toast.makeText(MainActivity.this, "Accelerometer sensor Not supported", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Stop(1);
                    //accl1.setText("0");
                    // accl2.setText("0");
                    //accl3.setText("0");
                    stepsText.setText("0");
                    steps=0;
                    dist=0;
                    distance.setText("0");
                    diststride.setText("0");
                    Toast.makeText(MainActivity.this, "Accelerometer sensor stopped", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mag_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    magneticfield = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                    if (magneticfield != null) {

                        sensorManager.registerListener(MainActivity.this, magneticfield, sensorManager.SENSOR_DELAY_NORMAL);
                        Toast.makeText(MainActivity.this, "magnetometer sensor started", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "magnetometer Not supported");
                        Toast.makeText(MainActivity.this, "magnetometer sensor Not supported", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Stop(2);
                    ig.setRotation(180);
                    Toast.makeText(MainActivity.this, "magnetometer sensor stopped", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x, y, z;
            x = (float) sensorEvent.values[0];
            y = (float) sensorEvent.values[1];
            z = (float) sensorEvent.values[2];

            floatgravity=sensorEvent.values;
            //sensorManager.getRotationMatrix(floatrotationmatrix,null,floatgravity,floatgeomagnetic);
            //sensorManager.getOrientation(floatrotationmatrix,floatorientation);
            //ig.setRotation((float) (-floatorientation[0]*180/3.14));
            long timestamp = sensorEvent.timestamp;
            //iswalking(x, y, z);
            //detect_walking(x,y,z);
            Log.d(TAG, "Accelerometer X:" + sensorEvent.values[0] + "Y:" + sensorEvent.values[1]
                    + "Z:" + sensorEvent.values[2]);
            //accl1.setText(Float.toString(sensorEvent.values[0]));
            //accl2.setText(Float.toString(sensorEvent.values[1]));
            //accl3.setText(Float.toString(sensorEvent.values[2]));

            // Insert into Database
            //Accelerometer accelerometer = new Accelerometer(Float.toString(sensorEvent.values[0]), Float.toString(sensorEvent.values[1]), Float.toString(sensorEvent.values[2]), Long.toString(timestamp));
            Log.d(TAG, "Before inserting Accelerometer X:" + sensorEvent.values[0] + "Y:" + sensorEvent.values[1]
                    + "Z:" + sensorEvent.values[2]);
            //mydb.mydao().insertAccelerometer(accelerometer);

            double magnitude = Math.sqrt(x * x + y * y + z * z);
            double magnitudediff = magnitude - magnitudeprevious;
            magnitudeprevious = magnitude;

            if (magnitudediff > 6)
            {
                steps++;
            }

            bar.setProgressWithAnimation(steps, (long) 1);
            if (steps / bar.getProgressMax() < 0.25) {
                bar.setProgressBarColor(Color.YELLOW);
            }
            else if (steps / bar.getProgressMax() < 0.5) {
                bar.setProgressBarColor(Color.BLUE);
            }
            else if(steps / bar.getProgressMax() < 0.75){
                bar.setProgressBarColor(Color.GREEN);
            }
            else{
                bar.setProgressBarColor(Color.RED);
            }

            dist =calculateDistance(steps, height);
            stepsText.setText(String.valueOf(steps));

            diststride.setText(String.valueOf((double) (Math.round(dist*100.0))/100.0)+" m");
            distance.setText(String.valueOf((double) (Math.round(((height*0.43)/30.48)*100.0))/100.0)+" feet");

        }
        else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            floatgeomagnetic=sensorEvent.values;
            sensorManager.getRotationMatrix(floatrotationmatrix,null,floatgravity,floatgeomagnetic);
            sensorManager.getOrientation(floatrotationmatrix,floatorientation);
            ig.setRotation((float) (-floatorientation[0]*180/3.14));
        }

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public static float calculateDistance(int num_steps, float height) { double distance = num_steps*height*0.3937*0.414*2.54e-2; return (float) distance; }
}