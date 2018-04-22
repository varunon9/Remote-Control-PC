package me.varunon9.remotecontrolpc;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import me.varunon9.remotecontrolpc.filetransfer.TransferFileToServer;

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

        mFileName = getActivity().getExternalCacheDir().getAbsolutePath() + "/audiorecordtest";

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

                    try {
                        mPlayer.setDataSource(mFileName);
                        mPlayer.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    int duration = mPlayer.getDuration();
                    duration /= 1000; //in seconds
                    transferFile("audiorecordtest", mFileName, duration);
                }
                mIsPlaying = !mIsPlaying;
            }
        });

        return rootView;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

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

    private void transferFile(final String name, String path, final int duration) {
        if (MainActivity.clientSocket != null) {
            MainActivity.sendMessageToServer("FILE_TRANSFER_REQUEST");
            MainActivity.sendMessageToServer(name);
            Toast.makeText(getActivity(), "Wait for music controls", Toast.LENGTH_LONG).show();
            new TransferFileToServer(getActivity()){

                @Override
                public void receiveData(Object result) {
                    Intent intent = new Intent(getActivity(), MusicControlActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("MUSIC_FILE_NAME", name);
                    bundle.putInt("MUSIC_DURATION", duration);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

            }.execute(new String[]{name, path});
        } else {
            Toast.makeText(getActivity(), "Not Connected", Toast.LENGTH_LONG).show();
        }
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

    public void onDestroy() {
        super.onDestroy();
        MainActivity.sendMessageToServer("STOP_MUSIC");
    }

}
