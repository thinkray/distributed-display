/**
 * The controller for the frame Display
 *
 * @author Team 3
 */

package cn.edu.uic.distributeddisplay.core.controller;

import cn.edu.uic.distributeddisplay.core.model.DisplayWindowModel;
import cn.edu.uic.distributeddisplay.core.profile.AbstractProfile;
import cn.edu.uic.distributeddisplay.core.util.DefaultConst;
import cn.edu.uic.distributeddisplay.core.view.DisplayWindowView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class DisplayWindowController {

    protected final DisplayWindowModel displayWindowModel;
    protected final DisplayWindowView displayWindowView;

    public DisplayWindowController(AbstractProfile serverSideProfile) {
        this(serverSideProfile, new DisplayWindowView());
    }

    public DisplayWindowController(AbstractProfile serverSideProfile, DisplayWindowView displayWindowView) {
        this.displayWindowModel = new DisplayWindowModel(serverSideProfile);
        this.displayWindowView = displayWindowView;
        initController();
    }

    protected abstract void initController();

    protected void renderView() {
        renderTextPanel();
        renderBackground();

        displayWindowView.revalidate();
        displayWindowView.repaint();
    }

    private void setTextPanelBounds(JPanel textPanel, Dimension screenSize) {
        double vMargin = displayWindowModel.getProfile().isHorizontal() ?
                (double) displayWindowModel.getProfile().getMargin() / 100 : 0;
        double hMargin = displayWindowModel.getProfile().isHorizontal() ? 0 :
                (double) displayWindowModel.getProfile().getMargin() / 100;
        double vOffset = (double) displayWindowModel.getProfile().getvOffset() / 100;
        double hOffset = (double) displayWindowModel.getProfile().gethOffset() / 100;
        textPanel.setBounds((int) (screenSize.getWidth() * hMargin) + (int) (screenSize.getWidth() * hOffset),
                (int) (screenSize.getHeight() * vMargin) + (int) (screenSize.getHeight() * vOffset),
                (int) (screenSize.getWidth() * (1 - 2 * hMargin)), (int) (screenSize.getHeight() * (1 - 2 * vMargin)));
    }

    private void renderTextPanel() {
        // Initialize the text panel
        JPanel textPanel = displayWindowView.getTextPanel();
        if (textPanel == null) {
            textPanel = new JPanel();
        } else {
            textPanel.removeAll();
        }
        textPanel.setBackground(new Color(0, 0, 0, 0));
        setTextPanelBounds(textPanel, displayWindowModel.getScreenSize());

        // Load labels into the frame
        ArrayList<JLabel> labels = displayWindowModel.getLabelsHTML();
        for (JLabel label : labels) {
            textPanel.add(label);
        }

        // Define the orientation of the layout according to the profile
        int rows = 1, cols = 1;
        if (displayWindowModel.getProfile().isHorizontal()) {
            rows = labels.size();
        } else {
            cols = labels.size();
        }
        textPanel.setLayout(new GridLayout(rows, cols, 0, 0));
    }

    private void renderBackground() {
        JLabel backgroundLabel = displayWindowView.getBackgroundLabel();
        backgroundLabel.removeAll();

        Dimension screenSize = displayWindowModel.getScreenSize();

        // Load Background image
        ImageIcon img = displayWindowModel.getProfile().getBackgroundImage();

        if (img != null) {
            // Accommodate different fitting styles
            int width = 0, height = 0;
            switch (displayWindowModel.getProfile().getImgFitStyle()) {
                case DefaultConst.FIT:
                    // Fit
                    double imgRatio = (double) img.getImage().getWidth(null) / (double) img.getImage().getHeight(null);
                    // When the ration is greater than 1, the image is wide.
                    // When the ration is in the interval of (0, 1), the image is tall.
                    double screenRatio = screenSize.getWidth() / screenSize.getHeight();
                    // When the ration is greater than 1, the screen is in landscape mode.
                    // When the ration is in the interval of (0, 1), the screen is in portrait mode.
                    width = Math.min((int) (imgRatio * screenSize.getHeight()), (int) screenSize.getWidth());
                    height = Math.min((int) (1 / imgRatio * screenSize.getWidth()), (int) screenSize.getHeight());
                    break;
                case DefaultConst.STRETCH:
                    // Stretch
                    width = (int) screenSize.getWidth();
                    height = (int) screenSize.getHeight();
                    break;
                case DefaultConst.RAW:
                    // Tile
                    width = img.getImage().getWidth(null);
                    height = img.getImage().getHeight(null);
                    break;
            }
            Image backgroundImg = img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

            backgroundLabel.setText("");
            backgroundLabel.setIcon(new ImageIcon(backgroundImg));
            backgroundLabel.setHorizontalAlignment(JLabel.CENTER);
        } else {
            backgroundLabel.setText("");
            backgroundLabel.setIcon(null);
            backgroundLabel.setHorizontalAlignment(JLabel.CENTER);
        }

        backgroundLabel.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
    }

    protected abstract void closeView();

    public AbstractProfile getProfile() {
        return displayWindowModel.getProfile();
    }

    public void setProfile(AbstractProfile profile) {
        // Change the profile after the current profile is displaying. In other scenarios,
        // pass the Profile object as an argument to the constructor of DisplayController.
        displayWindowModel.setProfile(profile);
        renderView();
    }

    public void setViewVisibility(boolean visible) {
        displayWindowView.setVisible(visible);
    }

}
