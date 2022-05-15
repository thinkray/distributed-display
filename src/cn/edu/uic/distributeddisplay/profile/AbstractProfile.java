package cn.edu.uic.distributeddisplay.profile;

import cn.edu.uic.distributeddisplay.util.DefaultConst;
import org.apache.commons.text.StringEscapeUtils;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public abstract class AbstractProfile implements Serializable {

//    static final long serialVersionUID = 10L;

    // The current profile
    protected String text;
    protected Font font;
    protected Color color;
    protected int letterSpacing;
    protected int margin;  // In percentage or in pixels
    protected int vOffset;  // Vertical offset
    protected int hOffset; // Horizontal offset
    protected int textOrientation;  // 0: Horizontal, 1: Vertical
    protected int imgFitStyle; // 0: Fit, 1: Stretch, 2: Tile

    public AbstractProfile(String text, Font font, Color color, int letterSpacing, int margin, int vOffset, int hOffset,
                           int textOrientation, int imgFitStyle) {
        // Initialize instance variables
        this.text = text;
        this.font = font;
        this.color = color;
        this.letterSpacing = letterSpacing;
        this.margin = margin;
        this.vOffset = vOffset;
        this.hOffset = hOffset;
        this.textOrientation = textOrientation;
        this.imgFitStyle = imgFitStyle;
    }

    public AbstractProfile() {
        this(DefaultConst.DEFAULT_TEXT, DefaultConst.DEFAULT_FONT,
                DefaultConst.DEFAULT_COLOR, DefaultConst.DEFAULT_LETTER_SPACING, DefaultConst.DEFAULT_MARGIN,
                DefaultConst.DEFAULT_V_OFFSET, DefaultConst.DEFAULT_H_OFFSET, DefaultConst.DEFAULT_TEXT_ORIENTATION,
                DefaultConst.DEFAULT_FIT_STYLE);
    }

    // LocalProfile and RemoteProfile each implement their own getBackgroundImage
    public abstract ImageIcon getBackgroundImage();

    public boolean isHorizontal() {
        return getTextOrientation() == DefaultConst.HORIZONTAL;
    }

    public String getText() {
        return StringEscapeUtils.escapeHtml3(text);
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

    public int getTextOrientation() {
        return textOrientation;
    }

    public void setTextOrientation(int textOrientation) {
        this.textOrientation = textOrientation;
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

    @Override
    public String toString() {
        return "Profile{" +
                "text='" + text + '\'' +
                ", font=" + font +
                ", color=" + color +
                ", letterSpacing=" + letterSpacing +
                ", margin=" + margin +
                ", vOffset=" + vOffset +
                ", hOffset=" + hOffset +
                ", textOrientation=" + textOrientation +
                ", imgFitStyle=" + imgFitStyle +
                '}';
    }
}
