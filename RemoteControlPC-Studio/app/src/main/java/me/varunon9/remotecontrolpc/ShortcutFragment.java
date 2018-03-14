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

public class ShortcutFragment extends Fragment {

    private ImageButton mbuttonFirefox;
    private final String command = "LAUNCH";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_shortcut, container, false);
        mbuttonFirefox = (ImageButton) rootView.findViewById(R.id.button_firefox);

        mbuttonFirefox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.sendMessageToServer(command);
                MainActivity.sendMessageToServer("firefox");
            }
        });

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.shortcut);

    }

}
