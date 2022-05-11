package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.util.CommonUtils;
import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.view.NodeConfigView;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;

public class NodeGUIController {

    private NodeConfigView v;

    public NodeGUIController() {
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
            v.getStatusLabel().setText("Connecting...");

            JOptionPane.showMessageDialog(v, "Cannot connect to the server",
                    "Error", JOptionPane.ERROR_MESSAGE);
            v.getStatusLabel().setText(" ");
            v.getConnectButton().setEnabled(true);
        });
    }
}
