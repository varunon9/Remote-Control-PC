package me.varunon9.remotecontrolpc.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import me.varunon9.remotecontrolpc.MainActivity;
import me.varunon9.remotecontrolpc.R;

public class PresentationFragment extends Fragment implements OnClickListener{
	private Button downArrowButton, upArrowButton, f5Button, leftArrowButton, rightArrowButton;
	private static final String ARG_SECTION_NUMBER = "section_number";
	public View onCreateView (LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.presentation_fragment, container, false);
		downArrowButton = (Button) rootView.findViewById(R.id.downArrowButton); 
		upArrowButton = (Button) rootView.findViewById(R.id.upArrowButton); 
		leftArrowButton = (Button) rootView.findViewById(R.id.leftArrowButton); 
		rightArrowButton = (Button) rootView.findViewById(R.id.rightArrowButton); 
		f5Button = (Button) rootView.findViewById(R.id.f5Button); 
		downArrowButton.setOnClickListener(this);
		leftArrowButton.setOnClickListener(this);
		upArrowButton.setOnClickListener(this);
		rightArrowButton.setOnClickListener(this);
		f5Button.setOnClickListener(this);
		return rootView;
		
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		String action = "F5_KEY";
		switch (id) {
		case R.id.downArrowButton: 
			action = "DOWN_ARROW_KEY";
		    break;
		case R.id.leftArrowButton:
			action = "LEFT_ARROW_KEY";
	        break;
		case R.id.upArrowButton:
			action = "UP_ARROW_KEY";
	        break;
		case R.id.rightArrowButton:
			action = "RIGHT_ARROW_KEY";
	        break;
		case R.id.f5Button:
			action = "F5_KEY";
	        break;
		}
		sendActionToServer(action);
		
	}
	private void sendActionToServer(String action) {
		MainActivity.sendMessageToServer(action);
	}
}
