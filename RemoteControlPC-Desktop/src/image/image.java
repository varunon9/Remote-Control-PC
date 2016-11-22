package image;


import java.awt.Button;
import java.awt.Label;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author KULDEEP KUMAR
 */
public class image extends Application {

    public static void main(String [] args)
    {
        Application.launch(args);
    }
   
    public void start(Stage stage) {
    //String imagePath ="http://mikecann.co.uk/wp-content/uploads/2009/12/javafx_logo_color_1.jpg";
    String imagePath="image/nike.jpg";
    Image image = new Image(imagePath);
    ImageView imageView = new ImageView(image);
    BorderPane root = new BorderPane();
    imageView.setPreserveRatio(true); 
    imageView.fitWidthProperty().bind(stage.widthProperty()); 
    imageView.fitHeightProperty().bind(stage.heightProperty());
    root.getChildren().add(imageView);
    Scene scene = new Scene(root,500,300);
    stage.setScene(scene);
    stage.setTitle("hello");
    stage.show();  
  }
}
