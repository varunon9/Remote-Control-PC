package me.varunon9.remotecontrolpc.filetransfer;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

import file.AvatarFile;

/**
 * Created by varun on 30/7/17.
 */

public class SendFilesList {

    public void sendFilesList(final ArrayList<AvatarFile> myFiles, final ObjectOutputStream out) {
        new Thread() {
            public void run() {
                try {
                    out.writeObject(myFiles);
                    out.flush();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
