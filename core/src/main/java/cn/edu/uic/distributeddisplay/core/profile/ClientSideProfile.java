/**
 * The Profile Object
 *
 * @author Team 3
 */
package cn.edu.uic.distributeddisplay.core.profile;

import cn.edu.uic.distributeddisplay.core.util.CommonUtils;

import javax.swing.*;

public class ClientSideProfile extends AbstractProfile {

    private ImageIcon backgroundImage;

    public ClientSideProfile() {
        super();
    }

    public ClientSideProfile(ServerSideProfile profile) {
        super(profile.text, profile.font, profile.color, profile.letterSpacing, profile.margin,
                profile.vOffset, profile.hOffset, profile.textOrientation, profile.imgFitStyle);

        // Verify the file is an image file before proceeding
        if (CommonUtils.isImage(profile.getBackgroundImageDir())) {
            backgroundImage = new ImageIcon(profile.getBackgroundImageDir().getPath());
        }
    }

    @Override
    public ImageIcon getBackgroundImage() {
        return backgroundImage;
    }
}
