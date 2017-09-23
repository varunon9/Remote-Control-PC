package me.varunon9.remotecontrolpc.poweroff;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.varunon9.remotecontrolpc.MainActivity;
import me.varunon9.remotecontrolpc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PowerOffFragment extends Fragment implements View.OnClickListener  {

    private Button shutdownButton, restartButton, sleepButton, lockButton;
    DialogInterface.OnClickListener dialogClickListener;
    AlertDialog.Builder builder;
    String action;

    public PowerOffFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_power_off, container, false);
        shutdownButton = (Button) rootView.findViewById(R.id.shutdownButton);
        restartButton = (Button) rootView.findViewById(R.id.restartButton);
        sleepButton = (Button) rootView.findViewById(R.id.sleepButton);
        lockButton = (Button) rootView.findViewById(R.id.lockButton);
        shutdownButton.setOnClickListener(this);
        restartButton.setOnClickListener(this);
        sleepButton.setOnClickListener(this);
        lockButton.setOnClickListener(this);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.power_off));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.shutdownButton:
                action = "Shutdown_PC";
                break;
            case R.id.restartButton:
                action = "Restart_PC";
                break;
            case R.id.sleepButton:
                action = "Sleep_PC";
                break;
            case R.id.lockButton:
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
