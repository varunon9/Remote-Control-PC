package image;


import java.io.File;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author KULDEEP KUMAR
 */
public class ImageViewer {
    
    static Stage stage = null;
    
    public void start(String name, String path) {
        if (stage == null) {
            stage = new Stage();
        }
        Image image = new Image(new File(path).toURI().toString());
        ImageView imageView = new ImageView(image);
        BorderPane root = new BorderPane();
        imageView.setPreserveRatio(true); 
        imageView.fitWidthProperty().bind(stage.widthProperty()); 
        imageView.fitHeightProperty().bind(stage.heightProperty());
        root.getChildren().add(imageView);
        Scene scene = new Scene(root, 650, 400);
        stage.setScene(scene);
        stage.setTitle(name);
        stage.show();
    }
    
    //http://stackoverflow.com/questions/17850191/why-am-i-getting-java-lang-illegalstateexception-on-javafx
    public void showImage(final String name, final String path) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                start(name, path);
            }
        });
    }
    
    public void closeImage() {
        if (stage != null) {
            stage.hide();
        }
    }
    
    public void closeImageViewer() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                closeImage();
            }
        });
    }
}
