package me.varunon9.remotecontrolpc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;

/**
 * Created by alex on 25/02/18.
 */

public class BrightnessFragment extends Fragment implements View.OnTouchListener {

    private View mBrightnessView;
    private View mViewTopLimit;
    private View mViewBottomLimit;
    private float maxY = 0.0f;
    private float minY = 0.0f;
    private float dy = 0.0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_brightness, container, false);
        mBrightnessView = rootView.findViewById(R.id.brightness_controller_view);
        mBrightnessView.setOnTouchListener(this);
        mViewTopLimit= rootView.findViewById(R.id.brightness_view_top_limit);
        mViewBottomLimit = rootView.findViewById(R.id.brightness_view_bottom_limit);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                maxY = mViewTopLimit.getY();
                minY = mViewBottomLimit.getY() - mBrightnessView.getHeight();
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.brightness);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view == mBrightnessView) {
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
                    MainActivity.sendMessageToServer(MainActivity.bri);
                    MainActivity.sendMessageToServer(100.0f - bias * 100.0f);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

}
