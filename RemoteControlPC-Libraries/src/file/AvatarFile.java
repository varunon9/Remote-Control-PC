package file;

import java.io.Serializable;

/**
 *
 * @author varun
 */
public class AvatarFile implements Serializable {
    private final int icon;
    private final String avatarHeading, avatarSubheading, path, type;

    public AvatarFile(int icon, String avatarHeading, String avatarSubheading, String path, String type) {
        this.icon = icon;
        this.avatarHeading = avatarHeading;
        this.avatarSubheading = avatarSubheading;
        this.path = path;
        this.type = type;
    }

    public String getHeading() {
        return avatarHeading;
    }

    public String getPath() {
        return path;
    }

    public String getSubheading() {
        return avatarSubheading;
    }

    public int getIcon() {
        return icon;
    }
    
    public String getType() {
        return type;
    }
}
