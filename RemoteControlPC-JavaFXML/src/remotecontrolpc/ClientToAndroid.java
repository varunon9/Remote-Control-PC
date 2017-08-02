package remotecontrolpc;

import file.AvatarFile;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import static remotecontrolpc.MainScreenController.mainScreenController;

/**
 *
 * @author varun
 */
public class ClientToAndroid {
    
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    public static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;
    
    public void connect(InetAddress inetAddress, int port) {
        new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(3000);
                        connectToAndroid(inetAddress, port);
                        return null;
                    }
                    
                };
            }
            
        }.start();
    }
    
    private void connectToAndroid(InetAddress inetAddress, int port) {
            try {
                SocketAddress socketAddress
                        = new InetSocketAddress(inetAddress, port);
                clientSocket = new Socket();
                
                // 3s timeout
                clientSocket.connect(socketAddress, 3000);
                inputStream = clientSocket.getInputStream();
                outputStream = clientSocket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectInputStream = new ObjectInputStream(inputStream);
                
                // Request Android to fetch files list for root directory
                fetchDirectory("/");
            } catch(Exception e) {
                e.printStackTrace();
            }
    }

    public static void closeConnectionToAndroid() {
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
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void sendMessageToAndroid(String message) {
        if (clientSocket != null) {
            try {
                objectOutputStream.writeObject(message);
                objectOutputStream.flush();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void fetchDirectory(String path) {
        try {
            sendMessageToAndroid("FILE_DOWNLOAD_LIST_FILES");
            sendMessageToAndroid(path);
            ArrayList<AvatarFile> filesInFolder
                    = (ArrayList<AvatarFile>) objectInputStream.readObject();
            if (filesInFolder == null || filesInFolder.isEmpty()) {
            } else {
                mainScreenController.showFiles(filesInFolder);
                mainScreenController.displayPath(path);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
