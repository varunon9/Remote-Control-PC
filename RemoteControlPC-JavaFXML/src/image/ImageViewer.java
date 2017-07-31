package image;


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
