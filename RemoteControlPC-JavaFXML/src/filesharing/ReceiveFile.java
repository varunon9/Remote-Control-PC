package filesharing;

import java.io.File;
import java.io.FileOutputStream;
import remotecontrolpc.MainScreenController;

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
            long fileSize = (long) MainScreenController.objectInputStream.readObject();
            int read = 0;
            long totalRead = 0;
            int remaining = (int) fileSize;
            while ((read = MainScreenController.objectInputStream.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                //System.out.println("Receive Progress: " + ((totalRead * 100) / fileSize));
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
