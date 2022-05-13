/**
 * The Profile Object
 *
 * @author Bohui WU
 * @since 12/20/2019
 */
package cn.edu.uic.distributeddisplay.profile;

import javax.swing.*;

public class NodeSideProfile extends AbstractProfile {

    private ImageIcon backgroundImage;

    public NodeSideProfile(ServerSideProfile profile) {
        super(profile.text, profile.font, profile.color, profile.letterSpacing, profile.margin,
                profile.vOffset, profile.hOffset, profile.textOrientation, profile.imgFitStyle);
        backgroundImage = new ImageIcon(profile.getBackgroundImageDir().getPath());
    }

    @Override
    public ImageIcon getBackgroundImage() {
        return backgroundImage;
    }
}
