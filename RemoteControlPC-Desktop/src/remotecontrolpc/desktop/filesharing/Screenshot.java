/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolpc.desktop.filesharing;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author varun
 */
public class Screenshot {
    
    public void sendScreenshot(final ObjectOutputStream out) {
        new Thread() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    BufferedImage screenshot
                            = new Robot().createScreenCapture(new Rectangle(
                                    Toolkit.getDefaultToolkit()
                                                    .getScreenSize()));
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(screenshot, "png", os);
                    is = new ByteArrayInputStream(os.toByteArray());
                    int fileSize = os.size();
                    //sending fileSize first
                    out.writeObject(fileSize);
                    out.flush();
                    System.out.println("screenshot: " + fileSize);
                    byte[] buffer = new byte[4096];
                    int read = 0;
                    long totalRead = 0;
                    int remaining = (int) fileSize;
                    while ((read = is.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                        totalRead += read;
                        remaining -= read;
                        System.out.println("Transfer Progress: " + ((totalRead * 100) / fileSize));
                        out.write(buffer, 0, read);
                        out.flush();
                    }
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
