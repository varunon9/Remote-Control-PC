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
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class RedButtonFragment extends Fragment implements SensorEventListener, View.OnHoverListener, View.OnClickListener {
    private Button butt;
    private SensorManager sensors;
    private Sensor accelero;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_red_button, container, false);
        butt = (Button) rootView.findViewById(R.id.redButton);

        butt.setOnClickListener(this);
        butt.setOnHoverListener(this);
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
            if (Math.abs(event.values[0]) > 0.1 || Math.abs(event.values[1]) > 0.1) {
                MainActivity.sendMessageToServer("MOUSE_REMOTE");
                MainActivity.sendMessageToServer(-event.values[0]);
                MainActivity.sendMessageToServer(-event.values[1]);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view == butt){
/*            MainActivity.sendMessageToServer("COUCOU");

            try {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Socket sock;
                        DataOutputStream dos;
                        try {
                            sock = new Socket("192.168.43.123", 4000);
                            dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeInt(12);
                            dos.close();
                            socket.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        Toast.makeText(getContext(),"Boutton cliqué",Toast.LENGTH_SHORT).show();*/
    }}

    public boolean onHover(View view, MotionEvent m){
        return true;
    }
}
