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
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.remotecontrolpc.MainActivity;
import com.example.remotecontrolpc.R;

import java.util.Timer;
import java.util.TimerTask;

public class LiveScreenFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int xCord, yCord, initX, initY;
    boolean mouseMoved = false, moultiTouch = false;
    private ImageView screenshotImageView;
    private Timer timer;
    private int screenshotImageViewX, screenshotImageViewY;
    long currentPressTime, lastPressTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_live_screen, container, false);
        screenshotImageView = (ImageView) rootView.findViewById(R.id.screenshotImageView);
        ViewTreeObserver vto = screenshotImageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                screenshotImageViewX = screenshotImageView.getHeight();
                screenshotImageViewY = screenshotImageView.getWidth();
                ViewTreeObserver obs = screenshotImageView.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
            }
        });
        screenshotImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (MainActivity.clientSocket != null) {
                    switch(event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_DOWN:
                            xCord = screenshotImageViewX - (int) event.getY();
                            yCord = (int) event.getX();
                            initX = xCord;
                            initY = yCord;
                            MainActivity.sendMessageToServer("MOUSE_MOVE_LIVE");
                            //send mouse movement to server by adjusting coordinates
                            MainActivity.sendMessageToServer((float) xCord / screenshotImageViewX);
                            MainActivity.sendMessageToServer((float) yCord / screenshotImageViewY);
                            mouseMoved = false;
                            /*startTime = System.currentTimeMillis();
                            clickCount++;*/
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (moultiTouch == false) {
                                xCord = screenshotImageViewX - (int) event.getY();
                                yCord = (int) event.getX();
                                if ((xCord - initX) != 0 && (yCord - initY) != 0) {
                                    initX = xCord;
                                    initY = yCord;
                                    MainActivity.sendMessageToServer("MOUSE_MOVE_LIVE");
                                    //send mouse movement to server by adjusting coordinates
                                    MainActivity.sendMessageToServer((float) xCord / screenshotImageViewX);
                                    MainActivity.sendMessageToServer((float) yCord / screenshotImageViewY);
                                    mouseMoved=true;
                                }
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            // supporting only single click
                            currentPressTime = System.currentTimeMillis();
                            long interval = currentPressTime - lastPressTime;
                            if (interval >= 500 && !mouseMoved) {
                                MainActivity.sendMessageToServer("LEFT_CLICK");
                                delayedUpdateScreenshot();
                            }
                            lastPressTime = currentPressTime;
                            break;
                    }
                }
                return true;
            }
        });
        timer = new Timer();
        updateScreenshot();
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
                    try {
                        Bitmap rotated = Bitmap.createBitmap(bitmap ,0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        screenshotImageView.setImageBitmap(rotated);
                    } catch(Exception e) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

    private void delayedUpdateScreenshot() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateScreenshot();
            }
        }, 500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
        timer.purge();
    }
}
