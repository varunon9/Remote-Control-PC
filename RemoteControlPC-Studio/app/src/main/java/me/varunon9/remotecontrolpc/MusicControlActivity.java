package me.varunon9.remotecontrolpc;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MusicControlActivity extends AppCompatActivity {

    Button pauseOrResumeButton;
    SeekBar volumeSeekBar, musicProgressSeekBar;
    private boolean isPaused = false;
    int progress, duration;
    Handler handler;
    Runnable updateProgressRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_control);
        pauseOrResumeButton = (Button) findViewById(R.id.pauseOrResumeButton);
        volumeSeekBar = (SeekBar) findViewById(R.id.volumeSeekBar);
        musicProgressSeekBar = (SeekBar) findViewById(R.id.musicProgressSeekBar);
        progress = 1;
        handler = new Handler();
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("MUSIC_FILE_NAME");
        duration = bundle.getInt("MUSIC_DURATION");
        playMusic(name);
        volumeSeekBar.setMax(10);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                float volume = (float) progress / 10;
                setVolume(volume);

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

        });
        musicProgressSeekBar.setMax(duration);
        musicProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar arg0, int p, boolean fromUser) {
                if (fromUser) {
                    slideMusic(p);
                    progress = p;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

        });
        updateProgressRunnable = new Runnable() {

            @Override
            public void run() {
                updateProgress();

            }

        };
        updateProgress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.music_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void playMusic(String name) {
        if (MainActivity.clientSocket != null) {
            MainActivity.sendMessageToServer("PLAY_MUSIC");
            MainActivity.sendMessageToServer(name);
        }
    }

    private void slideMusic(int duration) {
        if (MainActivity.clientSocket != null) {
            MainActivity.sendMessageToServer("SLIDE_MUSIC");
            MainActivity.sendMessageToServer(duration);
        }
    }

    public void pauseOrResume(View view) {
        if (MainActivity.clientSocket != null) {
            MainActivity.sendMessageToServer("PAUSE_OR_RESUME_MUSIC");
            String buttonText = pauseOrResumeButton.getText().toString().toLowerCase();
            if (buttonText.equals("pause")) {
                pauseOrResumeButton.setText("Resume");
                isPaused = true;
            } else {
                pauseOrResumeButton.setText("Pause");
                isPaused = false;
                updateProgress();
            }
        }
    }

    private void setVolume(float volume) {
        if (MainActivity.clientSocket != null) {
            MainActivity.sendMessageToServer("SET_VOLUME_MUSIC");
            MainActivity.sendMessageToServer(volume);
        }
    }

    private void updateProgress() {
        if (!isPaused) {
            handler.removeCallbacks(updateProgressRunnable);
            musicProgressSeekBar.setProgress(progress);
            progress++;
            handler.postDelayed(updateProgressRunnable, 1000);
        }
    }
}
