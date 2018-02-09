package me.varunon9.remotecontrolpc;

/**
 * Created by david on 13/12/17.
 */
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MouseRemoteFragment extends Fragment implements SensorEventListener, View.OnHoverListener, View.OnClickListener {
    private Button butt;
    private SensorManager sensors;
    private Sensor accelero;
    private Button leftclick;
    private Button rightclick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_mouseremote, container, false);
        butt = (Button) rootView.findViewById(R.id.mouseremotebutton);
        leftclick = (Button) rootView.findViewById(R.id.left_click);
        rightclick = (Button) rootView.findViewById(R.id.right_click);

       // butt.setOnClickListener(this);
        butt.setOnHoverListener(this);
        leftclick.setOnClickListener(this);
        rightclick.setOnClickListener(this);


        sensors = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelero = sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensors.registerListener(this, accelero, SensorManager.SENSOR_DELAY_NORMAL);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("PRESS THE RED BUTTON");
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void onSensorChanged(SensorEvent event){
        if(butt.isPressed()) {
            //if (Math.abs(event.values[0]) > 0.1 || Math.abs(event.values[1]) > 0.1) {
                MainActivity.sendMessageToServer("MOUSE_REMOTE");
                MainActivity.sendMessageToServer(-event.values[0]);
                MainActivity.sendMessageToServer(-event.values[1]);
            //}
        }
    }

    @Override
  public void onClick(View view) {
     if((Button) view == leftclick)
         MainActivity.sendMessageToServer("LEFT_CLICK");
     else
         MainActivity.sendMessageToServer("RIGHT_CLICK");
    }

    public boolean onHover(View view, MotionEvent m){
        return true;
    }
}
