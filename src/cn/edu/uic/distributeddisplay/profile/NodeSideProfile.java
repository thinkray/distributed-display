/**
 * The Profile Object
 *
 * @author Bohui WU
 * @since 12/20/2019
 */
package cn.edu.uic.distributeddisplay.profile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NodeSideProfile extends AbstractProfile {

    private ImageIcon backgroundImage;

    public NodeSideProfile(ServerSideProfile profile) {
        super(profile.text, profile.font, profile.color, profile.letterSpacing, profile.margin, profile.vOffset,
                profile.hOffset, profile.textOrientation, profile.imgFitStyle);

        try {
            File file = new File(profile.getBackgroundImageDir().getPath());
            backgroundImage = new ImageIcon(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            backgroundImage = new ImageIcon();
        }
    }

    @Override
    public ImageIcon getBackgroundImage() {
        return backgroundImage;
    }
}
