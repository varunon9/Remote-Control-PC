package com.example.remotecontrolpc.touchpad;


import com.example.remotecontrolpc.MainActivity;
import com.example.remotecontrolpc.R;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TouchpadFragment extends Fragment {
	private Button leftClickButton, rightClickButton;
	private TextView touchPadTextView;
	private static final String ARG_SECTION_NUMBER = "section_number";
	private int initX, initY, disX, disY;
	boolean mouseMoved = false, moultiTouch = false;
	public View onCreateView (LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.touchpad_fragment, container, false);
		leftClickButton = (Button) rootView.findViewById(R.id.leftClickButton);
		rightClickButton = (Button) rootView.findViewById(R.id.rightClickButton);
		touchPadTextView = (TextView) rootView.findViewById(R.id.touchPadTextView);
		leftClickButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				simulateLeftClick();	
			}
		});
        rightClickButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				simulateRightClick();	
			}
		});
        touchPadTextView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
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
                                    mouseMoved=true;
                                }
                            }
                            else {
                        	    disY = (int) event.getY()- initY; //Mouse movement in y direction
                        	    disY = (int) disY / 2;//to scroll by less amount
                        	    initY = (int) event.getY();
                        	    if(disY != 0) {
                        	    	MainActivity.sendMessageToServer("MOUSE_WHEEL");
                        	    	MainActivity.sendMessageToServer(disY);
                                    mouseMoved=true;
                        	    }
                            }
                            break;
						case MotionEvent.ACTION_CANCEL:
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
		return rootView;
		
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
	private void simulateLeftClick() {
		String message = "LEFT_CLICK";
		MainActivity.sendMessageToServer(message);
	}
	private void simulateRightClick() {
		String message = "RIGHT_CLICK";
		MainActivity.sendMessageToServer(message);
	}
}

