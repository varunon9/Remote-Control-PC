package me.varunon9.remotecontrolpc;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;

/**
 * Created by alex on 14/02/18.
 */

public class VolumeFragment extends Fragment implements View.OnTouchListener {

    private Button mVolumeButton;
    private TextView mMax;
    private TextView mMin;
    private float maxY = 0.0f;
    private float minY = 0.0f;
    private float dy = 0.0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_volume, container, false);
        mVolumeButton = (Button) rootView.findViewById(R.id.SoundButton);
        mVolumeButton.setOnTouchListener(this);
        mMax = (TextView) rootView.findViewById(R.id.MaxSound);
        mMin = (TextView) rootView.findViewById(R.id.MinSound);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.volume);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view == mVolumeButton) {
            float y = event.getRawY();
            switch (event.getActionMasked()) {
                case ACTION_DOWN:
                    dy = view.getY() - y;
                    maxY = mMax.getY();
                    minY = mMin.getY();
                case ACTION_MOVE:
                    y += dy;
                    if (y > maxY && y < minY) {
                        view.setY(y);
                        MainActivity.sendMessageToServer(MainActivity.vol);
                        MainActivity.sendMessageToServer(100.0f * y / (maxY - minY) - 100.0f * minY / (maxY - minY));
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }

}
