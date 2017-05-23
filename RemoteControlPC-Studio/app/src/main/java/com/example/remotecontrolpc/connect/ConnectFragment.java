package com.example.remotecontrolpc.connect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.example.remotecontrolpc.MainActivity;
import com.example.remotecontrolpc.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConnectFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";
	private Button connectButton;
	private EditText ipAddressEditText, portNumberEditText;
	private SharedPreferences sharedPreferences;
	public View onCreateView (LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.connect_fragment, container, false);
		ipAddressEditText = (EditText) rootView.findViewById(R.id.ipAddress);
		portNumberEditText = (EditText) rootView.findViewById(R.id.portNumber);
		connectButton = (Button) rootView.findViewById(R.id.connectButton);
		sharedPreferences = getActivity().getSharedPreferences("lastConnectionDetails", Context.MODE_PRIVATE);
		String lastConnectionDetails[] = getLastConnectionDetails();
		ipAddressEditText.setText(lastConnectionDetails[0]);
		portNumberEditText.setText(lastConnectionDetails[1]);
		if (MainActivity.clientSocket != null) {
			connectButton.setText("connected");
			connectButton.setEnabled(false);
		}
		connectButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				makeConnection();
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
	public void makeConnection() {
		String ipAddress = ipAddressEditText.getText().toString();
		String port = portNumberEditText.getText().toString();
		if (ValidateIP.validateIP(ipAddress) && ValidateIP.validatePort(port)) {
			setLastConnectionDetails(new String[] {ipAddress, port});
			connectButton.setText("Connecting...");
			connectButton.setEnabled(false);
			new MakeConnection(ipAddress, port, getActivity()) {
				@Override
				public void receiveData(Object result) {
					// TODO Auto-generated method stub
					MainActivity.clientSocket = (Socket) result;
					if (MainActivity.clientSocket == null) {
						Toast.makeText(getActivity(), "Server is not listening", Toast.LENGTH_SHORT).show();
						connectButton.setText("connect");
						connectButton.setEnabled(true);
					} else {
						connectButton.setText("connected");
					}
				}
			}.execute();
		} else {
			Toast.makeText(getActivity(), "Invalid IP Address or port", Toast.LENGTH_SHORT).show();
		}
	}
	private String[] getLastConnectionDetails() {
		String arr[] = new String[2];
		arr[0] = sharedPreferences.getString("lastConnectedIP", "");
		arr[1] = sharedPreferences.getString("lastConnectedPort", "3000");
		return arr;
	}
	private void setLastConnectionDetails(String arr[]) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("lastConnectedIP", arr[0]);
		editor.putString("lastConnectedPort", arr[1]);
		editor.apply();
	}
}
