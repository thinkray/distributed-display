/**
 * The controller for the frame Display
 *
 * @author Team 3
 */

package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.model.DisplayModel;
import cn.edu.uic.distributeddisplay.profile.AbstractProfile;
import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;
import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.util.LangManger;
import cn.edu.uic.distributeddisplay.util.Log;
import cn.edu.uic.distributeddisplay.util.ViewsManager;
import cn.edu.uic.distributeddisplay.view.DisplayView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class DisplayController {

    private NodeGUIController nodeGUIController = null;
    private final DisplayModel displayModel;
    private final DisplayView displayView;

    public DisplayController(AbstractProfile serverSideProfile, int mode) {
        this.displayModel = new DisplayModel(serverSideProfile);
        this.displayModel.setMode(mode);
        this.displayView = new DisplayView();
        initController();
    }

    public DisplayController(int mode, NodeGUIController nodeGUIController) {
        // Show a blank screen using the default profile.
        this(new ServerSideProfile(), mode);
        this.nodeGUIController = nodeGUIController;
    }

    private void initController() {
        if (displayModel.getMode() == DefaultConst.PREVIEW_MODE) {
            // In preview mode, users can use left-click or right-click to exit
            displayView.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    closeView();
                }
            });
            displayView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    closeView();
                }
            });
        } else {
            // When it is not in preview mode
            if (ViewsManager.getDisplayView() == null) {
                ViewsManager.setDisplayView(displayView);
            } else {
                Log.logWarning("Warning: Double initialization.");
            }

            // Initialize the default text for status item in the right-click menu
            setNodeStatus(DefaultConst.CLIENT_NOT_CONNECTED);

            // Register right-click events
            displayView.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayView.repaint();
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        // Right click
                        displayView.getRightClickMenu().show(displayView, e.getX(), e.getY());
                    }
                }
            });

            // Define menu item events
            if (displayModel.getMode() == DefaultConst.DISPLAY_MODE) {
                displayView.getDisconnectItem().addActionListener(e -> closeView());
            } else if (displayModel.getMode() == DefaultConst.SERVICE_MODE) {
                displayView.getDisconnectItem().addActionListener(e -> {
                    nodeGUIController.getRMIClientController().stopClient();
                    displayView.repaint();
                });
            }

            displayView.getExitItem().addActionListener(e -> {
                if (displayModel.getMode() == DefaultConst.SERVICE_MODE) {
                    nodeGUIController.getRMIClientController().stopClient();
                }
                System.exit(0);
            });

            if (displayModel.getMode() == DefaultConst.SERVICE_MODE) {
                displayView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        nodeGUIController.getRMIClientController().stopClient();
                    }
                });
            }
        }

        // Render the components
        renderView();

        // Display the frame
        displayView.setVisible(true);
    }

    private void renderView() {
        // Initialize the text panel
        JPanel textPanel = new JPanel();
        textPanel.setBackground(new Color(0, 0, 0, 0));
        setTextPanelBounds(textPanel, displayModel.getScreenSize());

        // Load labels into the frame
        ArrayList<JLabel> labels = displayModel.getLabelsHTML();
        for (JLabel label : labels) {
            textPanel.add(label);
        }

        // Define the orientation of the layout according to the profile
        int rows = 1, cols = 1;
        if (displayModel.getProfile().isHorizontal()) {
            rows = labels.size();
        } else {
            cols = labels.size();
        }
        textPanel.setLayout(new GridLayout(rows, cols, 0, 0));

        // Add text overlay
        displayView.setTextPanel(textPanel);

        // Add background
        displayView.setBackgroundLabel(renderBackground());

        displayView.revalidate();
        displayView.repaint();
    }

    private void setTextPanelBounds(JPanel textPanel, Dimension screenSize) {
        double vMargin = displayModel.getProfile().isHorizontal() ?
                (double) displayModel.getProfile().getMargin() / 100 : 0;
        double hMargin = displayModel.getProfile().isHorizontal() ? 0 :
                (double) displayModel.getProfile().getMargin() / 100;
        double vOffset = (double) displayModel.getProfile().getvOffset() / 100;
        double hOffset = (double) displayModel.getProfile().gethOffset() / 100;
        textPanel.setBounds((int) (screenSize.getWidth() * hMargin) + (int) (screenSize.getWidth() * hOffset),
                (int) (screenSize.getHeight() * vMargin) + (int) (screenSize.getHeight() * vOffset),
                (int) (screenSize.getWidth() * (1 - 2 * hMargin)), (int) (screenSize.getHeight() * (1 - 2 * vMargin)));
    }

    private JLabel renderBackground() {
        Dimension screenSize = displayModel.getScreenSize();

        // Load Background image
        ImageIcon img = displayModel.getProfile().getBackgroundImage();

        JLabel imgLabel;
        if (img != null) {
            // Accommodate different fitting styles
            int width = 0, height = 0;
            switch (displayModel.getProfile().getImgFitStyle()) {
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
                case DefaultConst.TILE:
                    // Tile
                    width = img.getImage().getWidth(null);
                    height = img.getImage().getHeight(null);
                    break;
            }
            Image backgroundImg = img.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imgLabel = new JLabel("", new ImageIcon(backgroundImg), JLabel.CENTER);
        } else {
            imgLabel = new JLabel("", JLabel.CENTER);
        }

        imgLabel.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        return imgLabel;
    }

    private void closeView() {
        displayView.setVisible(false);
        ViewsManager.getMainWindowView().setVisible(true);
        displayView.dispose();
    }

    public AbstractProfile getProfile() {
        return displayModel.getProfile();
    }

    public void setProfile(AbstractProfile profile) {
        // Change the profile after the current profile is displaying. In other scenarios,
        // pass the Profile object as an argument to the constructor of DisplayController.
        displayModel.setProfile(profile);
        renderView();
    }

    public void setNodeStatus(int nodeStatus) {
        String status = "";
        switch (nodeStatus) {
            case DefaultConst.CLIENT_NOT_CONNECTED:
                status = LangManger.get("not_connected");
                break;
            case DefaultConst.CLIENT_CONNECTED:
                status = LangManger.get("connected");
                break;
            case DefaultConst.CLIENT_DISCONNECTED:
                status = LangManger.get("disconnected");
                break;
            case DefaultConst.CLIENT_RETRYING:
                status = LangManger.get("retrying");
                break;
        }
        displayView.getStatusItem().setText(LangManger.get("status:") + status);
        displayView.repaint();
    }

    public void setViewVisibility(boolean visible) {
        displayView.setVisible(visible);
    }

}
