/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

/**
 *
 * @author varun
 */
public class MusicPlayer {
    Media media;
    MediaPlayer mediaPlayer;
    Status status;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }
    
    public void playNewMedia(String path) {
        //path = path.replace("\\", "/");
        if (mediaPlayer != null) {
            status = mediaPlayer.getStatus();
            if (status == Status.PLAYING || status == Status.PAUSED || status == Status.READY) {
                mediaPlayer.stop();
            }
        }
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(new Runnable() {

            @Override
            public void run() {
                System.out.println("Duration: " + mediaPlayer.getTotalDuration().toSeconds());
                mediaPlayer.play();
            }
        });
    }
    
    public void resumeOrPauseMedia() {
        if (mediaPlayer != null) {
            status = mediaPlayer.getStatus();
            if (status == Status.PLAYING) {
                mediaPlayer.pause();
            } else if (status == Status.PAUSED) {
                mediaPlayer.play();
            }
        }
    }
    
    //in seconds, double
    public void slide(int d) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(d));
        }
    }
    
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    
    public void setVolume(double value) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(value);
        }
    }
}
