/**
 * The Profile Object
 *
 * @author Team 3
 */
package cn.edu.uic.distributeddisplay.profile;

import cn.edu.uic.distributeddisplay.util.DefaultConst;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ServerSideProfile extends AbstractProfile {

    private File backgroundImageDir;

    public ServerSideProfile(String text, Font font, Color color, int letterSpacing, int margin, int vOffset,
                             int hOffset, File backgroundImageDir, int textOrientation, int imgFitStyle) {
        super(text, font, color, letterSpacing, margin, vOffset, hOffset, textOrientation, imgFitStyle);
        this.text = text;
        this.font = font;
        this.color = color;
        this.letterSpacing = letterSpacing;
        this.margin = margin;
        this.vOffset = vOffset;
        this.hOffset = hOffset;
        this.backgroundImageDir = backgroundImageDir;
        this.textOrientation = textOrientation;
        this.imgFitStyle = imgFitStyle;
    }

    public ServerSideProfile(ServerSideProfile serverSideProfile) {
        super(serverSideProfile.text, serverSideProfile.font, serverSideProfile.color,
                serverSideProfile.letterSpacing, serverSideProfile.margin, serverSideProfile.vOffset,
                serverSideProfile.hOffset, serverSideProfile.textOrientation, serverSideProfile.imgFitStyle);
        this.text = serverSideProfile.text;
        this.font = serverSideProfile.font;
        this.color = serverSideProfile.color;
        this.letterSpacing = serverSideProfile.letterSpacing;
        this.margin = serverSideProfile.margin;
        this.vOffset = serverSideProfile.vOffset;
        this.hOffset = serverSideProfile.hOffset;
        this.backgroundImageDir = serverSideProfile.backgroundImageDir;
        this.textOrientation = serverSideProfile.textOrientation;
        this.imgFitStyle = serverSideProfile.imgFitStyle;
    }

    public ServerSideProfile() {
        this(DefaultConst.DEFAULT_TEXT, DefaultConst.DEFAULT_FONT, DefaultConst.DEFAULT_COLOR,
                DefaultConst.DEFAULT_LETTER_SPACING, DefaultConst.DEFAULT_MARGIN, DefaultConst.DEFAULT_V_OFFSET,
                DefaultConst.DEFAULT_H_OFFSET, new File(DefaultConst.DEFAULT_BACKGROUND_IMG_DIR),
                DefaultConst.DEFAULT_TEXT_ORIENTATION, DefaultConst.DEFAULT_FIT_STYLE);
    }

    @Override
    public ImageIcon getBackgroundImage() {
        return new ImageIcon(backgroundImageDir.getPath());
    }

    public File getBackgroundImageDir() {
        return backgroundImageDir;
    }

    public void setBackgroundImageDir(File backgroundImageDir) {
        this.backgroundImageDir = backgroundImageDir;
    }
}
