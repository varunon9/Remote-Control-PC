package me.varunon9.remotecontrolpc.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;

/**
 * Created by varun on 2/8/17.
 */

public class TransferFileToPC {

    public void transferFile(final String path, final ObjectOutputStream objectOutputStream) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (objectOutputStream == null) {
                    return;
                }
                FileInputStream fis = null;
                try {
                    File file = new File(path);
                    fis = new FileInputStream(file);
                    long fileSize = file.length();
                    Server.sendMessageToServer(fileSize);
                    byte[] buffer = new byte[4096];
                    int read = 0;
                    long totalRead = 0;
                    int remaining = (int) fileSize;
                    while (totalRead < fileSize && (read = fis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                        totalRead += read;
                        remaining -= read;
                        objectOutputStream.write(buffer, 0, read);
                        objectOutputStream.flush();
                    }
                    objectOutputStream.flush();
                } catch(FileNotFoundException eof) {
                    Server.sendMessageToServer(0);
                } catch (Exception e) {
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
        }).start();
    }
}
