package cn.edu.uic.distributeddisplay.server.controller;

import cn.edu.uic.distributeddisplay.core.controller.DisplayWindowController;
import cn.edu.uic.distributeddisplay.core.profile.AbstractProfile;
import cn.edu.uic.distributeddisplay.server.util.ServerViewsManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerDisplayController extends DisplayWindowController {

    public ServerDisplayController(AbstractProfile serverSideProfile) {
        super(serverSideProfile);
    }

    protected void initController() {
        // In preview mode, users can use left-click or right-click to exit
        displayWindowView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closeView();
            }
        });
        displayWindowView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeView();
            }
        });

        // Render the components
        renderView();

        // Display the frame
        displayWindowView.setVisible(true);
    }

    protected void closeView() {
        displayWindowView.setVisible(false);
        ServerViewsManager.getMainWindowView().setVisible(true);
        displayWindowView.dispose();
    }
}
