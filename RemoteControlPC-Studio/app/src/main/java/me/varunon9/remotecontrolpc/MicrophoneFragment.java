package me.varunon9.remotecontrolpc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.net.Socket;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class MicrophoneFragment extends Fragment {

    private Button mRecordButton;

    private boolean mIsRecording = false;

    private AudioRecord mAudio;

    public static boolean permissionToRecordAccepted = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_microphone, container, false);

        if (getActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            permissionToRecordAccepted = true;
        }

        mRecordButton = (Button) rootView.findViewById(R.id.button_record);

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
                    Toast.makeText(getActivity(), "Permission not accepted", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.microphone);

    }

    private class AudioSendThread implements Runnable {

        @Override
        public void run() {
            Socket audioSocket = null;
            BufferedOutputStream bos = null;
            int bufferSize = 4096;

            MainActivity.sendMessageToServer("MICROPHONE");
            MainActivity.sendMessageToServer(bufferSize);

            try {
                audioSocket = new Socket(MainActivity.clientSocket.getInetAddress(), MainActivity.clientSocket.getPort() + 1);
                bos = new BufferedOutputStream(audioSocket.getOutputStream());
                byte[] buf = new byte[bufferSize];

                while (mAudio.read(buf, 0, bufferSize, AudioRecord.READ_BLOCKING) > 0) {
                    bos.write(buf);
                    bos.flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            finally {
                try {

                    if (mAudio != null) {
                        mAudio.release();
                        mAudio = null;
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    if (audioSocket != null) {
                        audioSocket.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startRecording() {
        mAudio = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, 1048576);
        mAudio.startRecording();

        Thread audioSendThread = new Thread(new AudioSendThread());
        audioSendThread.start();
    }

    private void stopRecording() {
        mAudio.stop();
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
        if (mAudio != null) {
            mAudio.release();
            mAudio = null;
        }
    }
}
