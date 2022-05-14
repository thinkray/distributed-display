/**
 * The model for the frame display. It is responsible for getting the profile from the profile manager
 * and return the requested data required by the ControllerDisplay
 *
 * @author Team 3
 */
package cn.edu.uic.distributeddisplay.model;

import cn.edu.uic.distributeddisplay.profile.AbstractProfile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class DisplayModel {

    private AbstractProfile profile;
    private int mode;
    private Dimension screenSize;

    public DisplayModel(AbstractProfile profile) {
        this.profile = profile;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }

    public ArrayList<JLabel> getLabelsHTML() {
        ArrayList<JLabel> labels = new ArrayList<>();
        String[] originalText = profile.getText().split("\n");
        // Convert to HTML code
        for (String s : originalText) {
            StringBuilder htmlCode = new StringBuilder();
            Font font = profile.getFont();
            htmlCode.append("<html><p>");
            if (profile.getTextOrientation() != 0) {
                // When the text orientation is vertical
                String[] chars = s.split("");
                for (String currentChar : chars) {
                    htmlCode.append("<p style='margin-top: ")
                            .append(profile.getLetterSpacing())
                            .append(";'>")
                            .append(currentChar)
                            .append("</p>");
                }
            } else {
                // When the orientation is horizontal, append the text directly
                htmlCode.append(s);
            }
            htmlCode.append("</p></html>");
            // Generate the label and add it to the array list
            JLabel label = new JLabel(htmlCode.toString());
            label.setFont(font);
            label.setForeground(profile.getColor());
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            labels.add(label);
        }
        if (profile.getTextOrientation() == 2) {
            // If "vertical (inverse)" is selected
            Collections.reverse(labels);
        }
        return labels;
    }

    public AbstractProfile getProfile() {
        return profile;
    }

    public void setProfile(AbstractProfile profile) {
        this.profile = profile;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Dimension getScreenSize() {
        return screenSize;
    }
}
