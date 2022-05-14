package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.util.ViewsManager;
import cn.edu.uic.distributeddisplay.view.NodeConfigView;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class NodeGUIController {
    private RMIClientController rmiClientController;
    private NodeConfigView v;

    public NodeGUIController() {
        rmiClientController = new RMIClientController(this, new DisplayController(DefaultConst.SERVICE_MODE));
        v = new NodeConfigView();
        // Register with ViewsManager
        ViewsManager.setNodeConfigView(v);
        initController();
    }

    private void initController() {
        initNodeConfigView();
//        updateFields();
        v.setVisible(true);
    }

    private void initNodeConfigView() {
        // Define actions
        v.getConnectButton().addActionListener((ActionEvent e) -> {
            setComponentsStatus(false, "Connecting...");
            Boolean result = false;
            try {
                result = rmiClientController.startClient(v.getNodeNameTextField().getText(),
                        v.getServerAddressTextField().getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(v, "Cannot connect to the server", "Error", JOptionPane.ERROR_MESSAGE);
                setComponentsStatus(true);
                return;
            }
            if (result) {
                rmiClientController.getNodeGUIController().getV().setVisible(false);
            } else {
                JOptionPane.showMessageDialog(v, "Check in failed: Node name is already in use", "Error",
                        JOptionPane.ERROR_MESSAGE);
                setComponentsStatus(true);
            }
        });
    }

    public void setComponentsStatus(boolean enabled, String status) {
        v.getConnectButton().setEnabled(enabled);
        v.getNodeNameTextField().setEnabled(enabled);
        v.getServerAddressTextField().setEnabled(enabled);
        v.getStatusLabel().setText(status);
    }

    public void setComponentsStatus(boolean enabled) {
        setComponentsStatus(enabled, " ");
    }

    public NodeConfigView getV() {
        return v;
    }
}
