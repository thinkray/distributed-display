package cn.edu.uic.distributeddisplay.controller;

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
        rmiClientController = new RMIClientController(this/*,new DisplayController()*/);
        v = new NodeConfigView();
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
            v.getConnectButton().setEnabled(false);
            v.getNodeNameTextField().setEnabled(false);
            v.getServerAddressTextField().setEnabled(false);
            v.getStatusLabel().setText("Connecting...");
            Boolean result = false;
            try {
                result = rmiClientController.startClient(v.getNodeNameTextField().getText(),
                        v.getServerAddressTextField().getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(v, "Cannot connect to the server", "Error", JOptionPane.ERROR_MESSAGE);
                v.getStatusLabel().setText(" ");
                v.getNodeNameTextField().setEnabled(true);
                v.getServerAddressTextField().setEnabled(true);
                v.getConnectButton().setEnabled(true);
                return;
            }
            if (result) {
                // TODO: Hide the panel
            } else {
                JOptionPane.showMessageDialog(v, "Check in failed: Node name is already in use", "Error",
                        JOptionPane.ERROR_MESSAGE);
                v.getNodeNameTextField().setEnabled(true);
                v.getServerAddressTextField().setEnabled(true);
                v.getStatusLabel().setText(" ");
                v.getConnectButton().setEnabled(true);
            }
        });
    }
}
