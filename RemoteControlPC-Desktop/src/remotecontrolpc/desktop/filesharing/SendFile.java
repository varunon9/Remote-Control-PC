/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolpc.desktop.filesharing;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author varun
 */
public class SendFile {
    public void sendFile(final String path, final ObjectOutputStream out) {
        new Thread() {
            @Override
            public void run() {
                FileInputStream fis = null;
                try {
                    File file = new File(path);
                    int fileSize = (int) file.length();
                    //sending fileSize first
                    out.writeObject(fileSize);
                    out.flush();
                    System.out.println(path + " " + fileSize);
                    fis = new FileInputStream(file);
                    byte[] buffer = new byte[4096];
                    int read = 0;
                    int totalRead = 0;
                    int remaining = fileSize;
                    while ((read = fis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                        totalRead += read;
                        remaining -= read;
                        System.out.println("Transfer Progress: " + ((totalRead * 100) / fileSize));
                        out.write(buffer, 0, read);
                    }
                    out.flush();
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
