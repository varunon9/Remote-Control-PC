package image;


import java.io.File;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import static remotecontrolpc.MainScreenController.mainScreenController;

/**
 *
 * @author varun
 */

public class ImageViewer {
    
    public void showImage(final String name, final String path) {
        mainScreenController.showImage(name, path);
    }
    
    public void closeImageViewer() {
        mainScreenController.closeImageViewer();
    }
}
