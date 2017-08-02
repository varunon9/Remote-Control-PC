package filesharing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import remotecontrolpc.MainScreenController;
import static remotecontrolpc.MainScreenController.mainScreenController;

/**
 *
 * @author varun
 */
public class ReceiveFile {
    
    public void receiveFile(final String fileName, long fileSize, 
            ObjectInputStream objectInputStream) {
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
            int read = 0;
            long totalRead = 0;
            int remaining = (int) fileSize;
            while ((read = objectInputStream.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                mainScreenController.showMessage("Receive Progress: " + 
                        ((totalRead * 100) / fileSize));
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
