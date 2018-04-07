package me.varunon9.remotecontrolpc;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static android.view.MotionEvent.ACTION_BUTTON_PRESS;
import static android.view.MotionEvent.ACTION_BUTTON_RELEASE;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class MicrophoneFragment extends Fragment {

    private Button mRecordButton;
    private Button mPlayButton;

    private static String mFileName;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_microphone, container, false);

        mFileName = getActivity().getExternalCacheDir().getAbsolutePath() + "/audiorecordtest.3gp";

        mRecordButton = (Button) rootView.findViewById(R.id.button_record);
        mPlayButton = (Button) rootView.findViewById(R.id.button_play);

        mRecordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case ACTION_DOWN:
                        startRecording();
                        return true;
                    case ACTION_UP:
                        stopRecording();
                        return false;
                    default:
                        return false;
                }
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            boolean mIsPlaying = false;
            @Override
            public void onClick(View view) {
                if (mIsPlaying) {
                    mPlayer.release();
                } else {
                    mPlayer = new MediaPlayer();
                    System.out.println(mFileName);

                    try {
                        mPlayer.setDataSource(mFileName);
                        mPlayer.prepare();
                        mPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mIsPlaying = !mIsPlaying;
            }
        });

        return rootView;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}
