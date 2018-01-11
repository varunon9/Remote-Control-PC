package remotecontrolpc;

import filesharing.Screenshot;
import image.ImageViewer;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import filesharing.FileAPI;
import filesharing.ReceiveFile;
import filesharing.SendFile;
import filesharing.SendFilesList;
import java.net.InetAddress;
import javafx.application.Platform;
import mousekeyboardcontrol.MouseKeyboardControl;
import poweroff.PowerOff;
import music.MusicPlayer;

/**
 *
 * @author varun
 */

public class Server {

    private Label messageLabel;

    public void connect(Button resetButton, Label connectionStatusLabel,
            Label messageLabel, int port) {
        this.messageLabel = messageLabel;
        MouseKeyboardControl mouseControl = new MouseKeyboardControl();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        try {
            MainScreenController.clientSocket =
                    MainScreenController.serverSocket.accept();
            Platform.runLater(() -> {
                resetButton.setDisable(true);
            });
            InetAddress remoteInetAddress =
                    MainScreenController.clientSocket.getInetAddress();
            String connectedMessage = "Connected to: " + remoteInetAddress;
            Platform.runLater(() -> {
                connectionStatusLabel.setText(connectedMessage);
            });
            showMessage(connectedMessage);

            // connecting another socket to app (Peer to Peer)
            new ClientToAndroid().connect(remoteInetAddress, port);
            MainScreenController.inputStream =
                    MainScreenController.clientSocket.getInputStream();
            MainScreenController.outputStream =
                    MainScreenController.clientSocket.getOutputStream();
            MainScreenController.objectOutputStream =
                    new ObjectOutputStream(MainScreenController.outputStream);
            MainScreenController.objectInputStream =
                    new ObjectInputStream(MainScreenController.inputStream);
            FileAPI fileAPI = new FileAPI();
            String message, filePath, fileName;
            int slideDuration;
            float volume;
            PowerOff  powerOff = new PowerOff();
            MusicPlayer musicPlayer = new MusicPlayer();
            ImageViewer imageViewer = new ImageViewer();
            while (true) {
                try {
                    message =
                            (String) MainScreenController.objectInputStream.readObject();
                    int keyCode;
                    if (message != null) {
                        switch (message) {
                            case "LEFT_CLICK":
                                mouseControl.leftClick();
                                break;
                            case "RIGHT_CLICK":
                                mouseControl.rightClick();
                                break;
                            case "DOUBLE_CLICK":
                                mouseControl.doubleClick();
                                break;
                            case "MOUSE_WHEEL":
                                int scrollAmount =
                                        (int) MainScreenController.objectInputStream.readObject();
                                mouseControl.mouseWheel(scrollAmount);
                                break;
                            case "MOUSE_MOVE":
                                int x = (int) MainScreenController.objectInputStream.readObject();
                                int y = (int) MainScreenController.objectInputStream.readObject();
                                Point point = MouseInfo.getPointerInfo().getLocation();
                                // Get current mouse position
                                float nowx = point.x;
                                float nowy = point.y;
                                mouseControl.mouseMove((int) (nowx + x), (int) (nowy + y));
                                break;
                            case "MOUSE_MOVE_LIVE":
                                // need to adjust coordinates
                                float xCord = (float) MainScreenController.objectInputStream.readObject();
                                float yCord = (float) MainScreenController.objectInputStream.readObject();
                                xCord = xCord * screenWidth;
                                yCord = yCord * screenHeight;
                                mouseControl.mouseMove((int) xCord, (int) yCord);
                                break;
                            case "KEY_PRESS":
                                keyCode = (int) MainScreenController.objectInputStream.readObject();
                                mouseControl.keyPress(keyCode);
                                break;
                            case "KEY_RELEASE":
                                keyCode = (int) MainScreenController.objectInputStream.readObject();
                                mouseControl.keyRelease(keyCode);
                                break;
                            case "CTRL_ALT_T":
                                mouseControl.ctrlAltT();
                                break;
                            case "CTRL_SHIFT_Z":
                                mouseControl.ctrlShiftZ();
                                break;
                            case "ALT_F4":
                                mouseControl.altF4();
                                break;
                            case "TYPE_CHARACTER":
                                //handle StringIndexOutOfBoundsException here when pressing soft enter key
                                char ch = ((String) MainScreenController.objectInputStream.readObject()).charAt(0);
                                mouseControl.typeCharacter(ch);
                                break;
                            case "TYPE_KEY":
                                keyCode = (int) MainScreenController.objectInputStream.readObject();
                                mouseControl.typeCharacter(keyCode);
                                break;
                            case "LEFT_ARROW_KEY":
                                mouseControl.pressLeftArrowKey();
                                break;
                            case "DOWN_ARROW_KEY":
                                mouseControl.pressDownArrowKey();
                                break;
                            case "RIGHT_ARROW_KEY":
                                mouseControl.pressRightArrowKey();
                                break;
                            case "UP_ARROW_KEY":
                                mouseControl.pressUpArrowKey();
                                break;
                            case "F5_KEY":
                                mouseControl.pressF5Key();
                                break;
                            case "FILE_DOWNLOAD_LIST_FILES":
                                filePath = (String) MainScreenController.objectInputStream.readObject();
                                if (filePath.equals("/")) {
                                    filePath = fileAPI.getHomeDirectoryPath();
                                }
                                new SendFilesList().sendFilesList(
                                        fileAPI, filePath, MainScreenController.objectOutputStream
                                );
                                break;
                            case "FILE_DOWNLOAD_REQUEST":
                                //filePath is complete path including file name
                                filePath = (String) MainScreenController.objectInputStream.readObject();
                                new SendFile().sendFile(filePath, MainScreenController.objectOutputStream);
                                break;
                            case "FILE_TRANSFER_REQUEST":
                                fileName = (String) MainScreenController.objectInputStream.readObject();
                                long fileSize = (long) MainScreenController.objectInputStream.readObject();
                                //not in thread, blocking action
                                new ReceiveFile().receiveFile(
                                        fileName, fileSize, MainScreenController.objectInputStream
                                );
                                break;
                            case "SHUTDOWN_PC":
                                powerOff.shutdown();
                                break;
                            case "RESTART_PC":
                                powerOff.restart();
                                break;
                            case "SLEEP_PC":
                                powerOff.suspend();
                                break;
                            case "LOCK_PC":
                                powerOff.lock();
                                break;
                            case "PLAY_MUSIC":
                                fileName = (String) MainScreenController.objectInputStream.readObject();
                                filePath = new FileAPI().getHomeDirectoryPath();
                                filePath = filePath + "/RemoteControlPC/" + fileName;
                                try {
                                    musicPlayer.playNewMedia(filePath);
                                    showMessage("Playing: " + fileName);
                                } catch(Exception e) {
                                    showMessage("Unsupported Media: " + fileName);
                                }
                                break;
                            case "SLIDE_MUSIC":
                                slideDuration = (int) MainScreenController.objectInputStream.readObject();
                                musicPlayer.slide(slideDuration);
                                break;
                            case "PAUSE_OR_RESUME_MUSIC":
                                musicPlayer.resumeOrPauseMedia();
                                break;
                            case "STOP_MUSIC":
                                musicPlayer.stopMusic();
                                break;
                            case "SET_VOLUME_MUSIC":
                                volume = (float) MainScreenController.objectInputStream.readObject();
                                musicPlayer.setVolume(volume);
                                break;
                            case "SHOW_IMAGE":
                                fileName = (String) MainScreenController.objectInputStream.readObject();
                                filePath = new FileAPI().getHomeDirectoryPath();
                                filePath = filePath + "/RemoteControlPC/" + fileName;
                                imageViewer.showImage(fileName, filePath);
                                break;
                            case "CLOSE_IMAGE_VIEWER":
                                imageViewer.closeImageViewer();
                                break;
                            case "SCREENSHOT_REQUEST":
                                new Screenshot().sendScreenshot(
                                        MainScreenController.objectOutputStream
                                );
                                break;
                            case "COUCOU":
                                showMessage("Bien reçu bien reçu !");
                                System.out.println("coucou");
                                break;
                        }
                    } else {
                        //remote connection closed
                        Platform.runLater(() -> {
                            resetButton.setDisable(false);
                            connectionStatusLabel.setText("Disconnected");
                        });
                        connectionClosed();
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    connectionClosed();
                    ClientToAndroid.closeConnectionToAndroid();
                    Platform.runLater(() -> {
                        resetButton.setDisable(false);
                        connectionStatusLabel.setText("Disconnected");
                    });
                    break;
                }
            };
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void connectionClosed() {
        try {
            MainScreenController.objectInputStream.close();
            MainScreenController.clientSocket.close();
            MainScreenController.serverSocket.close();
            MainScreenController.inputStream.close();
            MainScreenController.outputStream.close();
            MainScreenController.objectOutputStream.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String message) {
        Platform.runLater(() -> {
            messageLabel.setText(message);
        });
    }
}
/*
* Codes:
* 1. LEFT_CLICK
* 2. RIGHT_CLICK
* 3. MOUSE_WHEEL
* 4. MOUSE_MOVE
* 5. KEY_PRESS
* 6. KEY_RELEASE
* 7. CTRL_ALT_T
* 8. CTRL_SHIFT_Z
* 9. ALT_F4
* 10. TYPE_CHARACTER
* 11. TYPE_KEY
*/
