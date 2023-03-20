package cn.edu.uic.distributeddisplay.client.controller;

import cn.edu.uic.distributeddisplay.client.util.ClientViewsManager;
import cn.edu.uic.distributeddisplay.client.view.ClientDisplayWindowView;
import cn.edu.uic.distributeddisplay.core.controller.DisplayWindowController;
import cn.edu.uic.distributeddisplay.core.profile.ClientSideProfile;
import cn.edu.uic.distributeddisplay.core.profile.ServerSideProfile;
import cn.edu.uic.distributeddisplay.core.util.DefaultConst;
import cn.edu.uic.distributeddisplay.core.util.LangManger;
import cn.edu.uic.distributeddisplay.core.util.Logger;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientDisplayController extends DisplayWindowController {

    private ClientGUIController clientGUIController;
    private ClientDisplayWindowView clientDisplayWindowView;

    public ClientDisplayController(ClientGUIController clientGUIController, ClientDisplayWindowView clientDisplayWindowView) {
        // Show a blank screen using the default profile.
        super(new ClientSideProfile(), clientDisplayWindowView);
        this.clientDisplayWindowView = clientDisplayWindowView;
        this.clientGUIController = clientGUIController;
        initRightClickMenuEvents();
    }

    protected void initController() {
        // Render the components
        renderView();

        // Display the frame
        displayWindowView.setVisible(true);
    }

    private void initRightClickMenuEvents() {
        if (ClientViewsManager.getClientDisplayWindowView() == null) {
            ClientViewsManager.setClientDisplayWindowView(clientDisplayWindowView);
        } else {
            Logger.logWarning("Warning: Double initialization.");
        }

        // Initialize the default text for status item in the right-click menu
        setNodeStatus(DefaultConst.CLIENT_NOT_CONNECTED);

        // Register right-click events
        clientDisplayWindowView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clientDisplayWindowView.repaint();
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // Right click
                    clientDisplayWindowView.getRightClickMenu().show(clientDisplayWindowView, e.getX(), e.getY());
                }
            }
        });

        // Define menu item events
        clientDisplayWindowView.getDisconnectItem().addActionListener(e -> {
            clientGUIController.getRMIClientController().stopClient();
            clientDisplayWindowView.repaint();
        });

        clientDisplayWindowView.getExitItem().addActionListener(e -> {
            clientGUIController.getRMIClientController().stopClient();
            System.exit(0);
        });

        clientDisplayWindowView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientGUIController.getRMIClientController().stopClient();
            }
        });
    }

    protected void closeView() {
        displayWindowView.setVisible(false);
        ClientViewsManager.getNodeConfigView().setVisible(true);
        displayWindowView.dispose();
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
        clientDisplayWindowView.getStatusItem().setText(LangManger.get("status:") + status);
        clientDisplayWindowView.repaint();
    }

}
