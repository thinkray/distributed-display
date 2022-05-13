/**
 * The Profile Object
 *
 * @author Bohui WU
 * @since 12/20/2019
 */
package cn.edu.uic.distributeddisplay.profile;

import cn.edu.uic.distributeddisplay.util.DefaultConst;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.Serializable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ServerSideProfile extends Profile {

    private File backgroundImageDir;

    public ServerSideProfile(String name, String text, Font font, Color color, int letterSpacing, int margin, int vOffset, int hOffset,
                        File backgroundImageDir, int textOrientation, int imgFitStyle) {
        super(name, text, font, color, letterSpacing, margin, vOffset, hOffset, textOrientation, imgFitStyle);
        this.name = name;
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

    public ServerSideProfile() {
        this(DefaultConst.DEFAULT_NAME, DefaultConst.DEFAULT_TEXT, DefaultConst.DEFAULT_FONT,
                DefaultConst.DEFAULT_COLOR, DefaultConst.DEFAULT_LETTER_SPACING, DefaultConst.DEFAULT_MARGIN,
                DefaultConst.DEFAULT_V_OFFSET, DefaultConst.DEFAULT_H_OFFSET, DefaultConst.DEFAULT_BACKGROUND_IMG_DIR,
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
