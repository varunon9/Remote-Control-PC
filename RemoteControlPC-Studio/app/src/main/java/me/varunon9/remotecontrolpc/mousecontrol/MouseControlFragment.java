package me.varunon9.remotecontrolpc.mousecontrol;

/**
 * Created by david on 13/12/17.
 */

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.varunon9.remotecontrolpc.MainActivity;
import me.varunon9.remotecontrolpc.R;


public class MouseControlFragment extends Fragment implements SensorEventListener, View.OnClickListener {
    private Button mButton;
    private SensorManager mSensors;
    private Sensor mAccelero;
    private Button mLeftClick;
    private Button mRightClick;
    private boolean mResetAccelero = false;
    float mDefaultValues[] = {0, 0}, mCurrentValues[] = {0,0};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_mousecontrol, container, false);
        mButton = (Button) rootView.findViewById(R.id.mouseremotebutton);
        mLeftClick = (Button) rootView.findViewById(R.id.left_click);
        mRightClick = (Button) rootView.findViewById(R.id.right_click);

        mLeftClick.setOnClickListener(this);
        mRightClick.setOnClickListener(this);


        mSensors = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelero = mSensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensors.registerListener(this, mAccelero, SensorManager.SENSOR_DELAY_NORMAL);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


        SendDataThread t = new SendDataThread();
        t.start();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.mouse_control);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void onSensorChanged(SensorEvent event){
        if(mButton.isPressed()) {
            if (mResetAccelero) {
                mDefaultValues[0] = event.values[0];
                mDefaultValues[1] = event.values[1];
                mResetAccelero = false;


            }

            mCurrentValues[0] = -event.values[0]+mDefaultValues[0];
            mCurrentValues[1] = -event.values[1]+mDefaultValues[1];
        }
        else {
            mResetAccelero = true;
        }
    }

    private class SendDataThread extends Thread {
        private float normalize(float x) {
            float ret = 0.0f;

            if(Math.abs(x) < 2.f) {
                ret = 2.f;
            }
            else if(Math.abs(x) >=  2.f && Math.abs(x) < 6.f) {
                ret = 4.f;
            }
            else if(Math.abs(x) >=  6.f && Math.abs(x) < 11.f) {
                ret = 6.f;
            }

            return ret*(x/Math.abs(x));
        }

        public void run() {
            long tps, ticks;

            while(true) {

                if(mButton.isPressed()) {
                    MainActivity.sendMessageToServer("MOUSE_REMOTE");
                    MainActivity.sendMessageToServer(mCurrentValues[0]);
                    MainActivity.sendMessageToServer(mCurrentValues[1]);
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
  public void onClick(View view) {
     if(view == mLeftClick)
         MainActivity.sendMessageToServer("LEFT_CLICK");
     else
         MainActivity.sendMessageToServer("RIGHT_CLICK");
    }
}
