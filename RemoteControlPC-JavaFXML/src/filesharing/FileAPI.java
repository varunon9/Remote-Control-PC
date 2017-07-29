/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesharing;

import file.AvatarFile;
import java.io.File;
import java.util.ArrayList;
import remotecontrolpc.Utility;

/**
 *
 * @author varun
 */
public class FileAPI {
    
    public String getHomeDirectoryPath() {
        String path = System.getProperty("user.home");
        return path;
    }
    
    public ArrayList<AvatarFile> getFiles(String path) {
        ArrayList<AvatarFile> myFiles = new ArrayList<AvatarFile>();
        Utility utility = new Utility();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                String avatarHeading = files[i].getName();
                long lastModified = files[i].lastModified();
                String lastModifiedDate = utility.getDate(lastModified, "dd MMM yyyy hh:mm a");
                String itemsOrSize, filePath, type;
                if (files[i].isDirectory()) {
                    type = "folder";
                    File tempArray[] = files[i].listFiles();
                    if (tempArray != null) {
                        itemsOrSize = files[i].listFiles().length + " items";
                    } else {
                        itemsOrSize = 0 + " item";
                    }
                } else {
                    type = "file";
                    itemsOrSize = utility.getSize(files[i].length());
                    if (avatarHeading.length() > 3) {
                        String extension = avatarHeading.substring(avatarHeading.length() - 3).toLowerCase();
                        if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")
                                || extension.equals("svg")) {
                            type = "image";
                        } else if (extension.equals("mp3")) {
                            type = "mp3";
                        } else if (extension.equals("pdf")) {
                            type = "pdf";
                        }
                    }
                }
                filePath = files[i].getAbsolutePath();
                String subHeading = itemsOrSize + " " + lastModifiedDate;
                AvatarFile avatarFile = new AvatarFile(
                        -1, avatarHeading, subHeading, filePath, type);
                myFiles.add(avatarFile);
            }
        }
        return myFiles;
    }
    
}
