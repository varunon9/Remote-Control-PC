package com.example.remotecontrolpc.poweroff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.remotecontrolpc.MainActivity;
import com.example.remotecontrolpc.R;

public class PowerOffFragment extends Fragment implements OnClickListener {
	private static final String ARG_SECTION_NUMBER = "section_number";
	private ImageButton shutdownImageButton, restartImageButton, sleepImageButton, lockImageButton;
	DialogInterface.OnClickListener dialogClickListener;
	AlertDialog.Builder builder;
	String action;
	public View onCreateView (LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.power_off_fragment, container, false);
		shutdownImageButton = (ImageButton) rootView.findViewById(R.id.shutdownImageButton);
		restartImageButton = (ImageButton) rootView.findViewById(R.id.restartImageButton);
		sleepImageButton = (ImageButton) rootView.findViewById(R.id.sleepImageButton);
		lockImageButton = (ImageButton) rootView.findViewById(R.id.lockImageButton);
		shutdownImageButton.setOnClickListener(this);
		restartImageButton.setOnClickListener(this);
		sleepImageButton.setOnClickListener(this);
		lockImageButton.setOnClickListener(this);
        dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which) {
				case DialogInterface.BUTTON_POSITIVE:
					sendActionToServer(action.toUpperCase());
					dialog.dismiss();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					dialog.dismiss();
					break;
				}
				
			}
		};
		builder = new AlertDialog.Builder(getActivity());
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
		switch(id) {
		case R.id.shutdownImageButton: 
			action = "Shutdown_PC";
			break;
		case R.id.restartImageButton:
			action = "Restart_PC";
			break;
		case R.id.sleepImageButton:
			action = "Sleep_PC";
			break;
		case R.id.lockImageButton:
			action = "Lock_PC";
			break;
		}
		showConfirmDialog();
		
	}
	private void showConfirmDialog() {
		builder.setTitle(action)
		    .setMessage("Are you sure?")
		    .setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener)
		    .show();
	}
	private void sendActionToServer(String action) {
		MainActivity.sendMessageToServer(action);
	}
}
