/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolpc;

import file.AvatarFile;
import ipaddress.GetFreePort;
import ipaddress.GetMyIpAddress;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import layout.FileOrFolderController;

/**
 *
 * @author varun
 */
public class MainScreenController implements Initializable {
    
    public static MainScreenController mainScreenController;
    public static ServerSocket serverSocket = null;
    public static Socket clientSocket = null;
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;
    public static ObjectOutputStream objectOutputStream = null;
    public static ObjectInputStream objectInputStream = null;
    
    @FXML
    private TilePane tilePane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label ipAddressLabel;
    @FXML
    private Label portNumberLabel;
    @FXML
    private Label connectionStatusLabel;
    @FXML
    private Button resetButton;
    @FXML
    private Label messageLabel;
    
    public MainScreenController getMainScreenController() {
        return MainScreenController.mainScreenController;
    }
    
    public void setMainScreenController(MainScreenController mainScreenController) {
        MainScreenController.mainScreenController = mainScreenController;
    }
    
    @FXML
    private void resetConnection(ActionEvent event) {
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
            e.printStackTrace();
        }
        setConnectionDetails();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setConnectionDetails();
    } 
    
    private void setConnectionDetails() {
        String ipAddresses[] = new GetMyIpAddress().ipAddress();
        String connectionStatus = "Not Connected";
        int port = new GetFreePort().getFreePort();
        String ipAddress = ipAddresses[0];
        if (ipAddresses[1] != null) {
            ipAddress = ipAddress + " | " + ipAddresses[1];
        }
        ipAddressLabel.setText(ipAddress);
        portNumberLabel.setText(Integer.toString(port));
        connectionStatusLabel.setText(connectionStatus);
        if (ipAddresses[0].equals("127.0.0.1")) {
            showMessage("Connect your PC to Android phone hotspot or" +
                    " connect both devices to a local network.");
        } else {
            try {
                serverSocket = new ServerSocket(port);
                startServer(port);
            } catch(Exception e) {
                showMessage("Error in initializing server");
                e.printStackTrace();
            }
        }
    }
    
    
    private void startServer(int port) throws Exception {
        new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        new Server().connect(resetButton, connectionStatusLabel,
                                messageLabel, port);
                        return null;
                    }
                    
                };
            }
            
        }.start();
    }
    
    public void showMessage(String message) {
        Platform.runLater(() -> {
            messageLabel.setText(message);
        });
    }
    
    public void showImage(String name, String path) {
        showMessage(name);
        Image image = new Image(new File(path).toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        Platform.runLater(() -> {
            imageView.fitWidthProperty().bind(tilePane.widthProperty());
            imageView.fitHeightProperty().bind(tilePane.heightProperty());
            tilePane.getChildren().clear();
            tilePane.getChildren().add(imageView);
        });
    }
    
    public void closeImageViewer() {
        Platform.runLater(() -> {
            tilePane.getChildren().clear();
        });
    }
    
    public void showFiles(ArrayList<AvatarFile> filesInFolder) {
        Platform.runLater(() -> {
            tilePane.getChildren().clear();
            for (AvatarFile file : filesInFolder) {
                FXMLLoader fxmlLoader = new FXMLLoader(
                        getClass().getResource("/layout/FileOrFolder.fxml")
                );
                Parent root;
                try {
                    root = (Parent) fxmlLoader.load();
                } catch(Exception e) {
                    e.printStackTrace();
                    return;
                }
                FileOrFolderController fileOrFolderController = 
                        (FileOrFolderController) fxmlLoader.getController();
                String fileType = file.getType();
                Image icon = null;
                switch(fileType) {
                    case "folder":
                        icon = new Image(
                                getClass().getResourceAsStream("/resources/folder.png")
                        );
                        break;
                    case "file":
                        icon = new Image(
                                getClass().getResourceAsStream("/resources/file.png")
                        );
                        break;
                    case "image":
                        icon = new Image(
                                getClass().getResourceAsStream("/resources/image.png")
                        );
                        break;
                    case "mp3":
                        icon = new Image(
                                getClass().getResourceAsStream("/resources/music.png")
                        );
                        break;
                    case "pdf":
                        icon = new Image(
                                getClass().getResourceAsStream("/resources/pdf.png")
                        );
                        break;
                    default: ;
                }
                fileOrFolderController.setIcon(icon);
                fileOrFolderController.setHeading(file.getHeading());
                fileOrFolderController.setSubHeading(file.getSubheading());
                tilePane.getChildren().add(root);
            }
        });
    }
    
}
