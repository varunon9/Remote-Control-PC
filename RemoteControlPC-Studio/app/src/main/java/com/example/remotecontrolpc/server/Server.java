package com.example.remotecontrolpc.server;

/**
 * Created by varun on 30/7/17.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.remotecontrolpc.filetransfer.FilesList;
import com.example.remotecontrolpc.filetransfer.SendFilesList;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import file.AvatarFile;

/**
 * This class will create a ServerSocket and connect to PC
 * It is mandatory to download or browse Android files on Desktop
 */

public class Server {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;
    private static Activity activity;

    public Server(Activity activity) {
        this.activity = activity;
    }

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch(Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Unable to start server", Toast.LENGTH_LONG).show();
                }
            });
            e.printStackTrace();
            return;
        }
        try {
            clientSocket = serverSocket.accept();
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            objectOutputStream = new ObjectOutputStream(outputStream);
            String filePath;
            final SendFilesList sendFilesList = new SendFilesList();
            while (true) {
                String message = (String) objectInputStream.readObject();
                if (message == null) {
                    // connection closed
                    closeServer();
                    break;
                }
                switch (message) {
                    case "FILE_DOWNLOAD_LIST_FILES":
                        filePath = (String) objectInputStream.readObject();
                        new FilesList() {

                            @Override
                            public void receiveData(Object result) {
                                ArrayList<AvatarFile> filesInFolder = (ArrayList<AvatarFile>) result;
                                sendFilesList.sendFilesList(filesInFolder, objectOutputStream);
                            }
                        }.execute(filePath);
                        break;
                    case "FILE_DOWNLOAD":
                        filePath = (String) objectInputStream.readObject();
                        new TransferFileToPC().transferFile(filePath, objectOutputStream);
                        break;
                    default: ;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
            if (objectInputStream != null) {
                objectInputStream.close();
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public static void sendMessageToServer(long message) {
        if (clientSocket != null) {
            try {
                objectOutputStream.writeObject(message);
                objectOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
                socketException();
            }
        }
    }

    private static void socketException() {
        Toast.makeText(activity, "Connection Closed", Toast.LENGTH_LONG).show();
        if (clientSocket != null) {
            try {
                clientSocket.close();
                objectOutputStream.close();
                clientSocket = null;
            } catch(Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}

