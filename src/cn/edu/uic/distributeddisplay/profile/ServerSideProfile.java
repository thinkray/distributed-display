/**
 * The Profile Object
 *
 * @author Bohui WU
 * @since 12/20/2019
 */
package cn.edu.uic.distributeddisplay.profile;

import cn.edu.uic.distributeddisplay.util.DefaultConst;

import java.awt.*;
import java.io.File;
import java.io.Serializable;

public class ServerSideProfile implements Serializable {

//    static final long serialVersionUID = 10L;

    // The current profile
    private String name;  // The name of the profile
    private String text;
    private Font font;
    private Color color;
    private int letterSpacing;
    private int margin;  // In percentage or in pixels
    private int vOffset;  // Vertical offset
    private int hOffset; // Horizontal offset
    private File backgroundImageDir;
    private int textOrientation;  // 0: Horizontal, 1: Vertical
    private int imgFitStyle; // 0: Fit, 1: Stretch, 2: Tile
    private boolean active;

    public ServerSideProfile(String name, String text, Font font, Color color, int letterSpacing, int margin, int vOffset, int hOffset,
                             File backgroundImageDir, int textOrientation, int imgFitStyle, boolean active) {
        // Initialize instance variables
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
        this.active = active;
    }

    public ServerSideProfile() {
        this(DefaultConst.DEFAULT_NAME, DefaultConst.DEFAULT_TEXT, DefaultConst.DEFAULT_FONT,
                DefaultConst.DEFAULT_COLOR, DefaultConst.DEFAULT_LETTER_SPACING, DefaultConst.DEFAULT_MARGIN,
                DefaultConst.DEFAULT_V_OFFSET, DefaultConst.DEFAULT_H_OFFSET, null,
                DefaultConst.DEFAULT_TEXT_ORIENTATION, DefaultConst.DEFAULT_FIT_STYLE, false);
    }

    public ServerSideProfile(ServerSideProfile serverSideProfile) {
        this(serverSideProfile.getName(), serverSideProfile.getText(), serverSideProfile.getFont(), serverSideProfile.getColor(), serverSideProfile.getLetterSpacing(),
                serverSideProfile.getMargin(), serverSideProfile.getvOffset(), serverSideProfile.gethOffset(), serverSideProfile.getBackgroundImageDir(),
                serverSideProfile.textOrientation, serverSideProfile.getImgFitStyle(), serverSideProfile.isActive());
    }

    public boolean isHorizontal() {
        return getTextOrientation() == 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public File getBackgroundImageDir() {
        return backgroundImageDir;
    }

    public void setBackgroundImageDir(File backgroundImageDir) {
        this.backgroundImageDir = backgroundImageDir;
    }

    public int getTextOrientation() {
        return textOrientation;
    }

    public void setTextOrientation(int textOrientation) {
        this.textOrientation = textOrientation;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getvOffset() {
        return vOffset;
    }

    public void setvOffset(int vOffset) {
        this.vOffset = vOffset;
    }

    public int gethOffset() {
        return hOffset;
    }

    public void sethOffset(int hOffset) {
        this.hOffset = hOffset;
    }

    public int getImgFitStyle() {
        return imgFitStyle;
    }

    public void setImgFitStyle(int imgFitStyle) {
        this.imgFitStyle = imgFitStyle;
    }

    public int getLetterSpacing() {
        return letterSpacing;
    }

    public void setLetterSpacing(int letterSpacing) {
        this.letterSpacing = letterSpacing;
    }
}
