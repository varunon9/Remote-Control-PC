package com.example.remotecontrolpc.livescreen;

import android.os.AsyncTask;

import com.example.remotecontrolpc.CallbackReceiver;
import com.example.remotecontrolpc.FileAPI;
import com.example.remotecontrolpc.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;


/**
 * Created by varun on 23/5/17.
 */

public abstract class UpdateScreenshot extends AsyncTask<Void, Void, String> implements CallbackReceiver {
    @Override
    protected String doInBackground(Void... voids) {
        FileOutputStream fos = null;
        String path = new FileAPI().getExternalStoragePath();
        path = path + "/RemoteControlPC/screenshot.png";
        System.out.println("Screenshot url: " + path);
        File file = new File(path);
        File dirs = new File(file.getParent());
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        try {
            if (MainActivity.clientSocket != null) {
                if (MainActivity.objectInputStream == null) {
                    MainActivity.objectInputStream = new ObjectInputStream(
                            MainActivity.clientSocket.getInputStream());
                }
                fos = new FileOutputStream(file);
                byte buffer[] = new byte[4096];
                int fileSize = (int) MainActivity.objectInputStream.readObject();
                int read = 0;
                int remaining = fileSize;
                while ((read = MainActivity.objectInputStream.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                    remaining -= read;
                    fos.write(buffer, 0, read);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    protected void onPostExecute(String path) {
        receiveData(path);
    }
}
