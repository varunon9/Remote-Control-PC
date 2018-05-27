package microphone;

import remotecontrolpc.MainScreenController;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.BufferedInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Microphone {

    private int bufferSize;

    public Microphone(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void run() {

        ServerSocket audioServerSocket = null;
        Socket audioSocket = null;
        BufferedInputStream bis = null;
        SourceDataLine sourceDataLine = null;

        AudioFormat audioFormat = new AudioFormat(44100f, 16, 1, true, false);

        try {
            audioServerSocket = new ServerSocket(MainScreenController.clientSocket.getLocalPort() + 1);
            audioSocket = audioServerSocket.accept();
            bis = new BufferedInputStream(audioSocket.getInputStream());
            byte[] buf = new byte[bufferSize];

            sourceDataLine = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, audioFormat));
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            while (bis.read(buf) != -1) {
                sourceDataLine.write(buf, 0, bufferSize);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (sourceDataLine != null) {
                    Thread.sleep(sourceDataLine.getMicrosecondPosition()/1000);
                    sourceDataLine.flush();
                    sourceDataLine.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (audioSocket != null) {
                    audioSocket.close();
                }
                if (audioServerSocket != null) {
                    audioServerSocket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
