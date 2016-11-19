/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolpc.desktop.filesharing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import remotecontrolpc.desktop.MainScreen;

/**
 *
 * @author varun
 */
public class ReceiveFile {
    public void receiveFile(final String fileName) {
        FileOutputStream fos = null;
        String path = new FileAPI().getHomeDirectoryPath();
        path = path + "/RemoteControlPC/" + fileName;
        File file = new File(path);
        File dirs = new File(file.getParent());
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        try {
            fos = new FileOutputStream(file);
            byte buffer[] = new byte[4096];
            int fileSize = (int) MainScreen.objectInputStream.readObject();
            int read = 0;
            int totalRead = 0;
            int remaining = fileSize;
            while ((read = MainScreen.objectInputStream.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                System.out.println("Receive Progress: " + ((totalRead * 100) / fileSize));
                fos.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
