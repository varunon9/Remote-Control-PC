package me.varunon9.remotecontrolpc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import me.varunon9.remotecontrolpc.filetransfer.TransferFileToServer;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class MicrophoneFragment extends Fragment {

    private Button mRecordButton;
    private Button mPlayButton;

    private static String mFileName;
    private boolean mIsRecording = false;

    private MediaRecorder mRecorder;
    private AudioRecord mAudio;
    private MediaPlayer mPlayer;

    public static boolean permissionToRecordAccepted = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_microphone, container, false);

        mFileName = getActivity().getExternalCacheDir().getAbsolutePath() + "/audiorecordtest";

        if (getActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            permissionToRecordAccepted = true;
        }

        mRecordButton = (Button) rootView.findViewById(R.id.button_record);
        mPlayButton = (Button) rootView.findViewById(R.id.button_play);

        mRecordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (permissionToRecordAccepted) {
                    switch (motionEvent.getActionMasked()) {
                        case ACTION_DOWN:
                            startRecording();
                            return true;
                        case ACTION_UP:
                            stopRecording();
                            return false;
                        case ACTION_CANCEL:
                            stopRecording();
                            return false;
                        default:
                            return false;
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission not accepted", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        });

        return rootView;
    }

    private class AudioSendThread implements Runnable {

        @Override
        public void run() {
            int length = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            Socket socket = MainActivity.clientSocket;
            BufferedOutputStream bos;
            byte[] stopBuf = new byte[length];
            for (int i = 0; i < length; ++i) {
                stopBuf[i] = 127;
            }

            MainActivity.sendMessageToServer("MICROPHONE");
            MainActivity.sendMessageToServer(length);

            try {

                bos = new BufferedOutputStream(socket.getOutputStream());
                byte[] buf = new byte[length];
                while (mIsRecording) {
                    mAudio.read(buf, 0, length);
                    bos.write(buf);
                }
                bos.write(stopBuf);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*finally {
                try {
                    if(bos != null)
                        bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }

    private void startRecording() {
        mAudio = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
        mAudio.startRecording();
        mIsRecording = true;

        Thread audioSendThread = new Thread(new AudioSendThread());
        audioSendThread.start();

    }

    private void stopRecording() {
        mAudio.stop();
        mIsRecording = false;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
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
