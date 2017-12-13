package me.varunon9.remotecontrolpc;

/**
 * Created by david on 13/12/17.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import static android.widget.Toast.makeText;
import android.widget.TextView;

import me.varunon9.remotecontrolpc.MainActivity;
import me.varunon9.remotecontrolpc.R;

public class RedButtonFragment extends Fragment {
    private Button butt;

    public RedButtonFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_red_button, container, false);
        butt = (Button) rootView.findViewById(R.id.redButton);

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeText(getActivity(), "NOOOOOOOOOOO", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("DON'T CLICK ON THE RED BUTTON");
    }
}
