package layout;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
    
    @FXML
    private void showDetails(MouseEvent event) {
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
