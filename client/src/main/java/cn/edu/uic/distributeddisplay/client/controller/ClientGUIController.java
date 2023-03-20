package cn.edu.uic.distributeddisplay.client.controller;

import cn.edu.uic.distributeddisplay.client.util.ClientViewsManager;
import cn.edu.uic.distributeddisplay.client.view.ClientConfigView;
import cn.edu.uic.distributeddisplay.client.view.ClientDisplayWindowView;
import cn.edu.uic.distributeddisplay.core.util.ConfigManager;
import cn.edu.uic.distributeddisplay.core.util.DefaultConst;
import cn.edu.uic.distributeddisplay.core.util.LangManger;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ClientGUIController {
    private final RMIClientController rmiClientController;
    private final ClientConfigView clientConfigView;

    public ClientGUIController() {
        rmiClientController = new RMIClientController(
                this, new ClientDisplayController(this, new ClientDisplayWindowView()));
        clientConfigView = new ClientConfigView();
        // Register with ViewsManager
        ClientViewsManager.setNodeConfigView(clientConfigView);
        initController();
    }

    private void initController() {
        initNodeConfigView();

        // Initialize the menu
        clientConfigView.getEnUsItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "en-US");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });
        clientConfigView.getZhHansItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "zh-Hans");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });
        clientConfigView.getZhHantItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "zh-Hant");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });

        // Set the status bar's default text
        setComponentsStatus(true, LangManger.get("not_connected"));

        clientConfigView.setVisible(true);
    }

    private void initNodeConfigView() {
        // Define actions
        clientConfigView.getConnectButton().addActionListener((ActionEvent e) -> {
            setComponentsStatus(false, LangManger.get("connecting") + "...");
            SwingWorker<String, Object> worker = new SwingWorker<String, Object>() {
                @Override
                protected String doInBackground() throws Exception {
                    Boolean result = false;
                    try {
                        result = rmiClientController.startClient(clientConfigView.getNodeNameTextField().getText(),
                                clientConfigView.getServerAddressTextField().getText());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(clientConfigView, LangManger.get("cannot_connect"),
                                LangManger.get("error"),
                                JOptionPane.ERROR_MESSAGE);
                        setComponentsStatus(true, LangManger.get("not_connected"));
                        return null;
                    }
                    if (result) {
                        rmiClientController.getClientGUIController().getNodeConfigView().setVisible(false);
                        rmiClientController.getClientDisplayController().setNodeStatus(DefaultConst.CLIENT_CONNECTED);
                    } else {
                        JOptionPane.showMessageDialog(clientConfigView,
                                LangManger.get("check_in_failed") + ": " + LangManger.get("node_name_in_use"),
                                LangManger.get("error"),
                                JOptionPane.ERROR_MESSAGE);
                        setComponentsStatus(true, LangManger.get("not_connected"));
                    }
                    return null;
                }

                @Override
                protected void done() {
                }
            };
            worker.execute();
        });
    }

    public void setComponentsStatus(boolean enabled, String status) {
        clientConfigView.getConnectButton().setEnabled(enabled);
        clientConfigView.getNodeNameTextField().setEnabled(enabled);
        clientConfigView.getServerAddressTextField().setEnabled(enabled);
        clientConfigView.getStatusLabel().setText(status);
    }

    public void setComponentsStatus(boolean enabled) {
        setComponentsStatus(enabled, " ");
    }

    public ClientConfigView getNodeConfigView() {
        return clientConfigView;
    }

    public RMIClientController getRMIClientController() {
        return rmiClientController;
    }
}
