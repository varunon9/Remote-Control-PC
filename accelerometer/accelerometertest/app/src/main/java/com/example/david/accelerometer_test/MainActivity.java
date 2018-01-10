package com.example.david.accelerometer_test;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnHoverListener {

    private SensorManager sensors;
    private Sensor accelero;
    private float[] gravity = new float[3];
    private String TAG = "MainActivity";
    private View v;
    private float acceleration[] = new float[3];
    private View button;
    private boolean appuie = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = (TextView) findViewById(R.id.hello);
        button = (Button)findViewById(R.id.butt);
        sensors = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelero = sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //Log.d("MainActivity", accelero.getName());
        sensors.registerListener(this, accelero, SensorManager.SENSOR_DELAY_NORMAL);
        //button.setOnTouchListener(this);
        button.setOnHoverListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        //Log.d("MainActivity", "Acc : " + accuracy);
    }

    public void onSensorChanged(SensorEvent event){
        //Log.d("MainActivity", "Event : "+event.values[0]+" "+event.values[1] + " "+ event.values[2]);
        float alpha = 0.8f; // t / (t + dt)


        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]; /* Low Pass */
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        acceleration[0] = event.values[0] - gravity[0];
        acceleration[1] = event.values[1] - gravity[1];
        acceleration[2] = event.values[2] - gravity[2];

        if(button.isPressed()) {
            if (Math.abs(acceleration[0]) > 0.05) {
                Log.d(TAG, acceleration[0] + " " + acceleration[1] + " " + acceleration[2]);
                v.setX(v.getX() + acceleration[0] * -20);
            }
            if (Math.abs(acceleration[1]) > 0.05) {
                Log.d(TAG, acceleration[0] + " " + acceleration[1] + " " + acceleration[2]);
                v.setY(v.getY() + acceleration[1] * 20);
            }

        }
    }

    public boolean onHover(View view, MotionEvent m){
        //button.setPressed(true);
        //Log.d(TAG, "onHover: ");
        //button.setPressed(true);

        return true;
    }
}
