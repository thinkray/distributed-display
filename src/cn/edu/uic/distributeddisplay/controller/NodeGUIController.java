package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.util.ViewsManager;
import cn.edu.uic.distributeddisplay.view.NodeConfigView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NodeGUIController {
    private RMIClientController rmiClientController;
    private NodeConfigView nodeConfigView;

    public NodeGUIController() {
        rmiClientController = new RMIClientController(this, new DisplayController(DefaultConst.SERVICE_MODE, this));
        nodeConfigView = new NodeConfigView();
        // Register with ViewsManager
        ViewsManager.setNodeConfigView(nodeConfigView);
        initController();
    }

    private void initController() {
        initNodeConfigView();
//        updateFields();
        nodeConfigView.setVisible(true);
    }

    private void initNodeConfigView() {
        // Define actions
        nodeConfigView.getConnectButton().addActionListener((ActionEvent e) -> {
            setComponentsStatus(false, "Connecting...");
            Boolean result = false;
            try {
                result = rmiClientController.startClient(nodeConfigView.getNodeNameTextField().getText(),
                        nodeConfigView.getServerAddressTextField().getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(nodeConfigView, "Cannot connect to the server", "Error",
                        JOptionPane.ERROR_MESSAGE);
                setComponentsStatus(true);
                return;
            }
            if (result) {
                rmiClientController.getNodeGUIController().getNodeConfigView().setVisible(false);
            } else {
                JOptionPane.showMessageDialog(nodeConfigView, "Check in failed: Node name is already in use", "Error",
                        JOptionPane.ERROR_MESSAGE);
                setComponentsStatus(true);
            }
        });
    }

    public void setComponentsStatus(boolean enabled, String status) {
        nodeConfigView.getConnectButton().setEnabled(enabled);
        nodeConfigView.getNodeNameTextField().setEnabled(enabled);
        nodeConfigView.getServerAddressTextField().setEnabled(enabled);
        nodeConfigView.getStatusLabel().setText(status);
    }

    public void setComponentsStatus(boolean enabled) {
        setComponentsStatus(enabled, " ");
    }

    public NodeConfigView getNodeConfigView() {
        return nodeConfigView;
    }

    public RMIClientController getRMIClientController() {
        return rmiClientController;
    }
}
