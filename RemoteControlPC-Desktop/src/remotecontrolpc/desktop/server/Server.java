/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolpc.desktop.server;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JLabel;
import remotecontrolpc.desktop.MainScreen;
import remotecontrolpc.desktop.filesharing.FileAPI;
import remotecontrolpc.desktop.filesharing.SendFile;
import remotecontrolpc.desktop.filesharing.SendFilesList;
import remotecontrolpc.desktop.mousekeyboardcontrol.MouseKeyboardControl;

/**
 *
 * @author varun
 */
public class Server {
    public void connect(JButton resetButton, JLabel connectionStatusLabel) {
        MouseKeyboardControl mouseControl = new MouseKeyboardControl();
        try {
            connectionStatusLabel.setText("Waiting for Phone to connect...");
            MainScreen.clientSocket = MainScreen.serverSocket.accept();
            resetButton.setEnabled(false);
            connectionStatusLabel.setText("Connected to: " +
                    MainScreen.clientSocket.getRemoteSocketAddress());
            MainScreen.inputStream = MainScreen.clientSocket.getInputStream();
            MainScreen.outputStream = MainScreen.clientSocket.getOutputStream();
            MainScreen.objectOutputStream = new ObjectOutputStream(MainScreen.outputStream);
            MainScreen.objectInputStream = new ObjectInputStream(MainScreen.inputStream);
            FileAPI fileAPI = new FileAPI();
            String message, path;
            while (true) {
                try {
                    message = (String) MainScreen.objectInputStream.readObject();
                    int keyCode;
                    if (message != null) {
                        switch (message) {
                            case "LEFT_CLICK":
                                mouseControl.leftClick();
                                break;
                            case "RIGHT_CLICK":
                                mouseControl.rightClick();
                                break;
                            case "MOUSE_WHEEL":
                                int scrollAmount = (int) MainScreen.objectInputStream.readObject();
                                mouseControl.mouseWheel(scrollAmount);
                                break;
                            case "MOUSE_MOVE":
                                int x = (int) MainScreen.objectInputStream.readObject();
                                int y = (int) MainScreen.objectInputStream.readObject();
                                Point point = MouseInfo.getPointerInfo().getLocation(); //Get current mouse position
                                float nowx = point.x;
                                float nowy = point.y;
                                mouseControl.mouseMove((int) (nowx + x), (int) (nowy + y));
                                break;
                            case "KEY_PRESS":
                                keyCode = (int) MainScreen.objectInputStream.readObject();
                                mouseControl.keyPress(keyCode);
                                break;
                            case "KEY_RELEASE":
                                keyCode = (int) MainScreen.objectInputStream.readObject();
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
                                char ch = ((String) MainScreen.objectInputStream.readObject()).charAt(0);
                                mouseControl.typeCharacter(ch);
                                break;
                            case "TYPE_KEY": 
                                keyCode = (int) MainScreen.objectInputStream.readObject();
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
                                path = (String) MainScreen.objectInputStream.readObject();
                                if (path.equals("/")) {
                                    path = fileAPI.getHomeDirectoryPath();
                                }
                                new SendFilesList().sendFilesList(fileAPI, path, MainScreen.objectOutputStream);
                                break;
                            case "FILE_DOWNLOAD_REQUEST":
                                //path is complete path including file name
                                path = (String) MainScreen.objectInputStream.readObject();
                                new SendFile().sendFile(path, MainScreen.objectOutputStream);
                                break;
                        }
                    } else {
                        //remote connection closed
                        connectionClosed();
                        resetButton.setEnabled(true);
                        connectionStatusLabel.setText("Disconnected");
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    connectionClosed();
                    resetButton.setEnabled(true);
                    connectionStatusLabel.setText("Disconnected");
                    break;
                }
            };
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void connectionClosed() {
        try {
            MainScreen.objectInputStream.close();
            MainScreen.clientSocket.close();
            MainScreen.serverSocket.close();
            MainScreen.inputStream.close();
            MainScreen.outputStream.close();
            MainScreen.objectOutputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
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
