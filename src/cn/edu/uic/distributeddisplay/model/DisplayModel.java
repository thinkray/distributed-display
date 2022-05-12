/**
 * The model for the frame display. It is responsible for getting the profile from the profile manager
 * and return the requested data required by the ControllerDisplay
 *
 * @author Bohui WU
 * @since 12/20/2019
 */
package cn.edu.uic.distributeddisplay.model;

import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class DisplayModel {

    private ServerSideProfile serverSideProfile;
    private boolean previewMode;
    private Dimension screenSize;

    public DisplayModel(ServerSideProfile serverSideProfile) {
        this.serverSideProfile = serverSideProfile;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }

    public ArrayList<JLabel> getLabels() {
        ArrayList<JLabel> labels = new ArrayList<>();
        String[] originalText = serverSideProfile.getText().split("\n");
        // Convert to HTML code
        for (String s : originalText) {
            StringBuilder htmlCode = new StringBuilder();
            Font font = serverSideProfile.getFont();
            Color color = serverSideProfile.getColor();
            htmlCode.append("<html><p>");
            if (serverSideProfile.getTextOrientation() != 0) {
                // When the text orientation is vertical
                String[] chars = s.split("");
                for (String currentChar : chars) {
                    htmlCode.append("<p style='margin-top: ")
                            .append(serverSideProfile.getLetterSpacing())
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
            label.setForeground(serverSideProfile.getColor());
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            labels.add(label);
        }
        if (serverSideProfile.getTextOrientation() == 2) {
            // If "vertical (inverse)" is selected
            Collections.reverse(labels);
        }
        return labels;
    }

    public ServerSideProfile getProfile() {
        return serverSideProfile;
    }

    public boolean isPreviewMode() {
        return previewMode;
    }

    public void setPreviewMode(boolean previewMode) {
        this.previewMode = previewMode;
    }

    public Dimension getScreenSize() {
        return screenSize;
    }
}
