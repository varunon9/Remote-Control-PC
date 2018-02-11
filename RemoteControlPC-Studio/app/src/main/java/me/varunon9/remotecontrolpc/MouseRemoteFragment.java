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
    private Button mButton;
    private SensorManager mSensors;
    private Sensor mAccelero;
    private Button mLeftClick;
    private Button mRightClick;
    private boolean mResetAccelero = false;
    float mDefaultValues[] = {0, 0};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_mouseremote, container, false);
        mButton = (Button) rootView.findViewById(R.id.mouseremotebutton);
        mLeftClick = (Button) rootView.findViewById(R.id.left_click);
        mRightClick = (Button) rootView.findViewById(R.id.right_click);

        mButton.setOnHoverListener(this);
        mLeftClick.setOnClickListener(this);
        mRightClick.setOnClickListener(this);


        mSensors = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelero = mSensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensors.registerListener(this, mAccelero, SensorManager.SENSOR_DELAY_NORMAL);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.mouse_remote);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void onSensorChanged(SensorEvent event){
        if(mButton.isPressed()) {
            if (mResetAccelero) {
                mDefaultValues[0] = event.values[0];
                mDefaultValues[1] = event.values[1];
                mResetAccelero = false;
            }
            if (Math.abs(event.values[0]-mDefaultValues[0]) > 1 || Math.abs(event.values[1]-mDefaultValues[1]) > 1) {
                MainActivity.sendMessageToServer("MOUSE_REMOTE");
                MainActivity.sendMessageToServer(-event.values[0]+mDefaultValues[0]);
                MainActivity.sendMessageToServer(-event.values[1]+mDefaultValues[1]);
            }

        }
        else {
            mResetAccelero = true;
        }
    }

    @Override
  public void onClick(View view) {
     if((Button) view == mLeftClick)
         MainActivity.sendMessageToServer("LEFT_CLICK");
     else
         MainActivity.sendMessageToServer("RIGHT_CLICK");
    }

    public boolean onHover(View view, MotionEvent m){
        return true;
    }
}
