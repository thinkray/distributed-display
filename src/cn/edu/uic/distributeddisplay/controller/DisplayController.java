/**
 * The controller for the frame Display
 *
 * @author Bohui WU
 * @since 12/20/2019
 */

package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.AbstractProfile;
import cn.edu.uic.distributeddisplay.profile.NodeSideProfile;
import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;
import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.util.Log;
import cn.edu.uic.distributeddisplay.util.ViewsManager;
import cn.edu.uic.distributeddisplay.model.DisplayModel;
import cn.edu.uic.distributeddisplay.view.DisplayView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DisplayController {

    private DisplayModel m;
    private DisplayView v;

    public DisplayController(AbstractProfile serverSideProfile, int mode) {
        this.m = new DisplayModel(serverSideProfile);
        this.m.setMode(mode);
        this.v = new DisplayView();
        initController();
    }

    public DisplayController(int mode) {
        // Show a blank screen using the default profile.
        this(new ServerSideProfile(), mode);
    }

    private void initController() {
        if (m.getMode() == DefaultConst.PREVIEW_MODE) {
            // In preview mode, users can use left-click or right-click to exit
            v.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    closeView();
                }
            });
        } else {
            // When it is not in preview mode
            if (ViewsManager.getDisplayView() == null) {
                ViewsManager.setDisplayView(v);
            } else {
                Log.logWarning("Warning: Double initialization.");
            }

            // Register right-click events
            v.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    v.repaint();
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        // Right click
                        v.getRightClickMenu().show(v, e.getX(), e.getY());
                    }
                }
            });

            // Define menu item events
            if (m.getMode() == DefaultConst.DISPLAY_MODE) {
                v.getPreferenceItem().addActionListener(e -> closeView());
            } else if (m.getMode() == DefaultConst.SERVICE_MODE) {
                v.getPreferenceItem().addActionListener(e -> {
                    ViewsManager.getNodeConfigView().setVisible(true);
                    v.repaint();
                });
            }
            v.getExitItem().addActionListener(e -> System.exit(0));
        }

        // Render the components
        renderView();

        // Display the frame
        v.setVisible(true);
    }

    private void renderView() {
        // Initialize the text panel
        JPanel textPanel = new JPanel();
        textPanel.setBackground(new Color(0,0,0,0));
        setTextPanelBounds(textPanel, m.getScreenSize());

        // Load labels into the frame
        ArrayList<JLabel> labels = m.getLabelsHTML();
        for(JLabel label : labels) {
            textPanel.add(label);
        }

        // Define the orientation of the layout according to the profile
        int rows = 1, cols = 1;
        if (m.getProfile().isHorizontal()) {
            rows = labels.size();
        } else {
            cols = labels.size();
        }
        textPanel.setLayout(new GridLayout(rows, cols, 0, 0));

        // Add text overlay
        v.setTextPanel(textPanel);

        // Add background
        v.setBackgroundLabel(renderBackground());

        v.revalidate();
        v.repaint();
    }

    private void setTextPanelBounds(JPanel textPanel, Dimension screenSize) {
        double vMargin = m.getProfile().isHorizontal() ? (double) m.getProfile().getMargin() / 100 : 0;
        double hMargin = m.getProfile().isHorizontal() ? 0 : (double) m.getProfile().getMargin() / 100;
        double vOffset = (double) m.getProfile().getvOffset() / 100;
        double hOffset = (double) m.getProfile().gethOffset() / 100;
        textPanel.setBounds((int)(screenSize.getWidth() * hMargin) + (int)(screenSize.getWidth() * hOffset),
                (int)(screenSize.getHeight() * vMargin) + (int)(screenSize.getHeight() * vOffset),
                (int)(screenSize.getWidth() * (1 - 2 * hMargin)), (int)(screenSize.getHeight() * (1 - 2 * vMargin)));
    }

    private JLabel renderBackground() {
        Dimension screenSize = m.getScreenSize();

        // Load Background image
        ImageIcon img = m.getProfile().getBackgroundImage();

        JLabel imgLabel;
        if (img != null) {
            // Accommodate different fitting styles
            int width = 0, height = 0;
            switch (m.getProfile().getImgFitStyle()) {
                case DefaultConst.FIT:
                    // Fit
                    double imgRatio = (double) img.getImage().getWidth(null) / (double) img.getImage().getHeight(null);
                    // When the ration is greater than 1, the image is wide.
                    // When the ration is in the interval of (0, 1), the image is tall.
                    double screenRatio = screenSize.getWidth() / screenSize.getHeight();
                    // When the ration is greater than 1, the screen is in landscape mode.
                    // When the ration is in the interval of (0, 1), the screen is in portrait mode.
                    width = Math.min((int) (imgRatio * screenSize.getHeight()), (int)screenSize.getWidth());
                    height = Math.min((int) (1 / imgRatio * screenSize.getWidth()), (int)screenSize.getHeight());
                    break;
                case DefaultConst.STRETCH:
                    // Stretch
                    width = (int)screenSize.getWidth();
                    height = (int)screenSize.getHeight();
                    break;
                case DefaultConst.TILE:
                    // Tile
                    width = img.getImage().getWidth(null);
                    height = img.getImage().getHeight(null);
                    break;
            }
            Image backgroundImg = img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imgLabel = new JLabel ("", new ImageIcon(backgroundImg), JLabel.CENTER);
        } else {
            imgLabel = new JLabel ("", JLabel.CENTER);
        }

        imgLabel.setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
        return imgLabel;
    }

    private void closeView() {
        v.setVisible(false);
        ViewsManager.getMainWindowView().setVisible(true);
        v.dispose();
    }

    public AbstractProfile getProfile() {
        return m.getProfile();
    }

    public void setProfile(AbstractProfile profile) {
        // Change the profile after the current profile is displaying. In other scenarios,
        // pass the Profile object as an argument to the constructor of DisplayController.
        m.setProfile(profile);
        renderView();
    }

    public void setViewVisibility(boolean visible) {
        v.setVisible(visible);
    }

}
