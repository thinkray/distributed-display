package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.util.ConfigManager;
import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.util.LangManger;
import cn.edu.uic.distributeddisplay.util.ViewsManager;
import cn.edu.uic.distributeddisplay.view.NodeConfigView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NodeGUIController {
    private final RMIClientController rmiClientController;
    private final NodeConfigView nodeConfigView;

    public NodeGUIController() {
        rmiClientController = new RMIClientController(this, new DisplayController(DefaultConst.SERVICE_MODE, this));
        nodeConfigView = new NodeConfigView();
        // Register with ViewsManager
        ViewsManager.setNodeConfigView(nodeConfigView);
        initController();
    }

    private void initController() {
        initNodeConfigView();

        // Initialize the menu
        nodeConfigView.getEnusItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "en-US");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });
        nodeConfigView.getZhhansItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "zh-Hans");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });
        nodeConfigView.getZhhantItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "zh-Hant");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });

        nodeConfigView.setVisible(true);
    }

    private void initNodeConfigView() {
        // Define actions
        nodeConfigView.getConnectButton().addActionListener((ActionEvent e) -> {
            setComponentsStatus(false, LangManger.get("connecting") + "...");
            Boolean result = false;
            try {
                result = rmiClientController.startClient(nodeConfigView.getNodeNameTextField().getText(),
                        nodeConfigView.getServerAddressTextField().getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(nodeConfigView, LangManger.get("cannot_connect"), LangManger.get("error"),
                        JOptionPane.ERROR_MESSAGE);
                setComponentsStatus(true);
                return;
            }
            if (result) {
                rmiClientController.getNodeGUIController().getNodeConfigView().setVisible(false);
            } else {
                JOptionPane.showMessageDialog(nodeConfigView,
                        LangManger.get("check_in_failed") + ": " + LangManger.get("node_name_in_use"),
                        LangManger.get("error"),
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
