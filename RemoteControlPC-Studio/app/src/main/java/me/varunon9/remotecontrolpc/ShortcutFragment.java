package me.varunon9.remotecontrolpc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by alex on 12/03/18.
 */

public class ShortcutFragment extends Fragment implements View.OnClickListener{

    private ImageButton mbuttonFirefox;
    private ImageButton mbuttonGimp;
    private ImageButton mbuttonTerminal;
    private ImageButton mbuttonChromium;
    private ImageButton mbuttonStudio;
    private final String command = "LAUNCH";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_shortcut, container, false);
        mbuttonFirefox = (ImageButton) rootView.findViewById(R.id.button_firefox);
        mbuttonGimp = (ImageButton) rootView.findViewById(R.id.button_gimp);
        mbuttonTerminal = (ImageButton) rootView.findViewById(R.id.button_terminal);
        mbuttonChromium = (ImageButton) rootView.findViewById(R.id.button_chromium);
        mbuttonStudio = (ImageButton) rootView.findViewById(R.id.button_studio);

        mbuttonFirefox.setOnClickListener(this);
        mbuttonGimp.setOnClickListener(this);
        mbuttonTerminal.setOnClickListener(this);
        mbuttonChromium.setOnClickListener(this);
        mbuttonStudio.setOnClickListener(this);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.shortcut);

    }

    @Override
    public void onClick(View v) {
        if (v == mbuttonFirefox) {
            MainActivity.sendMessageToServer(command);
            MainActivity.sendMessageToServer("firefox");
        }
        else if (v == mbuttonGimp) {
            MainActivity.sendMessageToServer(command);
            MainActivity.sendMessageToServer("gimp");
        }
        else if (v == mbuttonTerminal) {
            MainActivity.sendMessageToServer(command);
            MainActivity.sendMessageToServer("gnome-terminal");
        }
        else if (v == mbuttonChromium) {
            MainActivity.sendMessageToServer(command);
            MainActivity.sendMessageToServer("chromium");
        }
        else if (v == mbuttonStudio) {
            MainActivity.sendMessageToServer(command);
            MainActivity.sendMessageToServer("studio");
        }
    }

}
