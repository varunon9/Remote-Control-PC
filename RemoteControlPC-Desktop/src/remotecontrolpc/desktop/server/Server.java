/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolpc.desktop.server;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JLabel;
import remotecontrolpc.desktop.mousekeyboardcontrol.MouseKeyboardControl;

/**
 *
 * @author varun
 */
public class Server {
    public void connect(ServerSocket serverSocket, Socket clientSocket,
            JButton resetButton, JLabel connectionStatusLabel) {
        MouseKeyboardControl mouseControl = new MouseKeyboardControl();
        try {
            connectionStatusLabel.setText("Waiting for Phone to connect...");
            clientSocket = serverSocket.accept();
            resetButton.setEnabled(false);
            connectionStatusLabel.setText("Connected to: " + clientSocket.getRemoteSocketAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            while (true) {
                try {
                    message = in.readLine();
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
                                int scrollAmount = Integer.parseInt(in.readLine());
                                mouseControl.mouseWheel(scrollAmount);
                                break;
                            case "MOUSE_MOVE":
                                float x = Float.parseFloat(in.readLine());
                                float y = Float.parseFloat(in.readLine());
                                Point point = MouseInfo.getPointerInfo().getLocation(); //Get current mouse position
                                float nowx = point.x;
                                float nowy = point.y;
                                mouseControl.mouseMove((int) (nowx + x), (int) (nowy + y));
                                break;
                            case "KEY_PRESS":
                                keyCode = Integer.parseInt(in.readLine());
                                mouseControl.keyPress(keyCode);
                                break;
                            case "KEY_RELEASE":
                                keyCode = Integer.parseInt(in.readLine());
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
                                char ch = in.readLine().charAt(0);
                                mouseControl.typeCharacter(ch);
                                break;
                            case "TYPE_KEY": 
                                keyCode = Integer.parseInt(in.readLine());
                                mouseControl.typeCharacter(keyCode);
                                break;
                        }
                    } else {
                        //remote connection closed
                        in.close();
                        clientSocket.close();
                        serverSocket.close();
                        resetButton.setEnabled(true);
                        connectionStatusLabel.setText("Disconnected");
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void sendMessageToClient(Socket clientSocket) {
        try {
            //ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
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
