/**
 * The controller for the frame Display
 *
 * @author Bohui WU
 * @since 12/20/2019
 */

package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;
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

    public DisplayController(ServerSideProfile serverSideProfile, boolean previewMode) {
        this.m = new DisplayModel(serverSideProfile);
        this.m.setPreviewMode(previewMode);
        this.v = new DisplayView();
        initController();
    }

    private void initController() {
        if (m.isPreviewMode()) {
            // In preview mode, users can use left-click or right-click to exit
            v.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    closeView();
                }
            });
        } else {
            // When it is not in preview mode
            ViewsManager.setDisplayView(v);

            // Register right-click events
            v.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON3) {
                        // Right click
                        v.getRightClickMenu().show(v, e.getX(), e.getY());
                    }
                }
            });

            // Define menu item events
            v.getMainWindowItem().addActionListener(e -> closeView());
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
        ArrayList<JLabel> labels = m.getLabels();
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

        v.add(textPanel);
        v.add(getBackgroundLabel(m.getScreenSize()));
    }

    public void setTextPanelBounds(JPanel textPanel, Dimension screenSize) {
        double vMargin = m.getProfile().isHorizontal() ? (double) m.getProfile().getMargin() / 100 : 0;
        double hMargin = m.getProfile().isHorizontal() ? 0 : (double) m.getProfile().getMargin() / 100;
        double vOffset = (double) m.getProfile().getvOffset() / 100;
        double hOffset = (double) m.getProfile().gethOffset() / 100;
        textPanel.setBounds((int)(screenSize.getWidth() * hMargin) + (int)(screenSize.getWidth() * hOffset),
                (int)(screenSize.getHeight() * vMargin) + (int)(screenSize.getHeight() * vOffset),
                (int)(screenSize.getWidth() * (1 - 2 * hMargin)), (int)(screenSize.getHeight() * (1 - 2 * vMargin)));
    }

    public JLabel getBackgroundLabel(Dimension screenSize) {
        // TO-DO: Stretch and Scale modes
        // Load Background image
        ImageIcon img = new ImageIcon(m.getProfile().getBackgroundImageDir().getPath());
        // Accommodate different fitting styles
        int width = 0, height = 0;
        switch (m.getProfile().getImgFitStyle()) {
            case 0:
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
            case 1:
                // Stretch
                width = (int)screenSize.getWidth();
                height = (int)screenSize.getHeight();
                break;
            case 2:
                // Tile
                width = img.getImage().getWidth(null);
                height = img.getImage().getHeight(null);
                break;
        }
        Image backgroundImg = img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel ("", new ImageIcon(backgroundImg), JLabel.CENTER);
        imgLabel.setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
        return imgLabel;
    }

    private void closeView() {
        v.setVisible(false);
        ViewsManager.getMainWindowView().setVisible(true);
        v.dispose();
    }

    public ServerSideProfile getProfile() {
        return m.getProfile();
    }

}
