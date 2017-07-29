/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesharing;

import file.AvatarFile;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author varun
 */
public class SendFilesList {
    public void sendFilesList(final FileAPI fileAPI, final String path, final ObjectOutputStream out) {
        new Thread() {
            public void run() {
                ArrayList<AvatarFile> myFiles = fileAPI.getFiles(path);
                System.out.println(path);
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
