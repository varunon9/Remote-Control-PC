package com.example.remotecontrolpc.livescreen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.remotecontrolpc.MainActivity;
import com.example.remotecontrolpc.R;

import java.util.Timer;
import java.util.TimerTask;

public class LiveScreenFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int initX, initY, disX, disY;
    boolean mouseMoved = false, moultiTouch = false;
    private ImageView screenshotImageView;
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_live_screen, container, false);
        screenshotImageView = (ImageView) rootView.findViewById(R.id.screenshotImageView);
        screenshotImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (MainActivity.clientSocket != null) {
                    switch(event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_DOWN:
                            //save X and Y positions when user touches the TextView
                            initX = (int) event.getX();
                            initY = (int) event.getY();
                            mouseMoved = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if(moultiTouch == false) {
                                disX = (int) event.getX()- initX; //Mouse movement in x direction
                                disY = (int) event.getY()- initY; //Mouse movement in y direction
                                /*set init to new position so that continuous mouse movement
                                is captured*/
                                initX = (int) event.getX();
                                initY = (int) event.getY();
                                if (disX != 0 || disY != 0) {
                                    MainActivity.sendMessageToServer("MOUSE_MOVE");
                                    //send mouse movement to server
                                    MainActivity.sendMessageToServer(disX);
                                    MainActivity.sendMessageToServer(disY);
                                }
                            }
                            else {
                                disY = (int) event.getY()- initY; //Mouse movement in y direction
                                disY = (int) disY / 2;//to scroll by less amount
                                initY = (int) event.getY();
                                if(disY != 0) {
                                    MainActivity.sendMessageToServer("MOUSE_WHEEL");
                                    MainActivity.sendMessageToServer(disY);;
                                }
                            }
                            mouseMoved=true;
                            break;
                        case MotionEvent.ACTION_UP:
                            //consider a tap only if user did not move mouse after ACTION_DOWN
                            if(!mouseMoved){
                                MainActivity.sendMessageToServer("LEFT_CLICK");
                            }
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            initY = (int) event.getY();
                            mouseMoved = false;
                            moultiTouch = true;
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            if(!mouseMoved) {
                                MainActivity.sendMessageToServer("LEFT_CLICK");
                            }
                            moultiTouch = false;
                            break;
                    }
                }
                return true;
            }
        });
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateScreenshot();
            }
        }, 0, 1000);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(
                ARG_SECTION_NUMBER));
    }

    private void updateScreenshot() {
        if (MainActivity.clientSocket != null) {
            MainActivity.sendMessageToServer("SCREENSHOT_REQUEST");
            new UpdateScreenshot() {
                @Override
                public void receiveData(Object result) {
                    String path = (String) result;
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(-90);
                    Bitmap rotated = Bitmap.createBitmap(bitmap ,0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    screenshotImageView.setImageBitmap(rotated);
                }
            }.execute();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
        timer.purge();
    }
}
