package me.varunon9.remotecontrolpc.levelcontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import me.varunon9.remotecontrolpc.MainActivity;
import me.varunon9.remotecontrolpc.R;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;

/**
 * Created by alex on 27/02/18.
 */

public class LevelControl extends Fragment implements View.OnTouchListener {

    private Bundle args;
    private View mViewController;
    private View mViewTopLimit;
    private View mViewBottomLimit;
    private float maxY = 0.0f;
    private float minY = 0.0f;
    private float dy = 0.0f;


    public static LevelControl newInstance(int title, String command) {
        LevelControl levelControl = new LevelControl();

        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("command", command);
        levelControl.setArguments(args);

        return levelControl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.level_control, container, false);
        mViewController = rootView.findViewById(R.id.view_controller);
        mViewController.setOnTouchListener(this);
        mViewTopLimit= rootView.findViewById(R.id.view_top_limit);
        mViewBottomLimit = rootView.findViewById(R.id.view_bottom_limit);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                maxY = mViewTopLimit.getY();
                minY = mViewBottomLimit.getY() - mViewController.getHeight();
            }
        });
        args = getArguments();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(args.getInt("title")));
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view == mViewController) {
            float bias = event.getRawY();
            switch (event.getActionMasked()) {
                case ACTION_DOWN:
                    dy = view.getY() - bias;
                    break;
                case ACTION_MOVE:
                    bias = 1.0f - (bias + dy) / (maxY - minY) + minY / (maxY - minY);
                    if (bias < 0.0f)
                        bias = 0.0f;
                    else if (bias > 1.0f)
                        bias = 1.0f;
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.verticalBias = bias;
                    view.setLayoutParams(params);
                    MainActivity.sendMessageToServer(args.getString("command"));
                    MainActivity.sendMessageToServer(100.0f - bias * 100.0f);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

}
