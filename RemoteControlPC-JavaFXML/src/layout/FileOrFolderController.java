package layout;

import filesharing.ReceiveFile;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import remotecontrolpc.ClientToAndroid;
import static remotecontrolpc.MainScreenController.mainScreenController;

/**
 * FXML Controller class
 *
 * @author varun
 */
public class FileOrFolderController implements Initializable {
    
    @FXML
    ImageView iconImageView;
    @FXML
    Label headingLabel;
    @FXML
    VBox vBox;
    
    private Image icon;
    private String heading;
    private String subHeading;
    private boolean doubleClick = false;
    private String path;
    private String fileType;

    public String getPath() {
        return path;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    @FXML
    private void showDetails(MouseEvent event) {
        if (doubleClick) {
            if (getFileType().equals("folder")) {
                ClientToAndroid.fetchDirectory(getPath());
            } else {
                // download file
                try {
                    ClientToAndroid.sendMessageToAndroid("FILE_DOWNLOAD");
                    ClientToAndroid.sendMessageToAndroid(getPath());
                    long fileSize = (long) ClientToAndroid.objectInputStream.readObject();
                    if (fileSize > 0) {
                        new ReceiveFile().receiveFile(
                                getHeading(), fileSize, ClientToAndroid.objectInputStream
                        );
                    } else {
                        mainScreenController.showMessage("Permission Denied");
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            doubleClick = false;
            return;
        }
        doubleClick = true;
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                doubleClick = false;
            }
            
        }, 2000);
        mainScreenController.showMessage(getSubHeading());
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
        iconImageView.setImage(icon);
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
        headingLabel.setText(heading);
    }

    public String getSubHeading() {
        return subHeading;
    }

    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
