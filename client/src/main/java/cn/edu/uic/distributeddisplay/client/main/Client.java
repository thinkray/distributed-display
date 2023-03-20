package cn.edu.uic.distributeddisplay.client.main;

import cn.edu.uic.distributeddisplay.client.controller.ClientGUIController;
import cn.edu.uic.distributeddisplay.core.util.CommonUtils;
import cn.edu.uic.distributeddisplay.core.util.Logger;

import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        // Initialize the logger
        Logger.initLogger("log.txt");

        // Start GUI interface
        SwingUtilities.invokeLater(() -> {
            CommonUtils.initUI();
            CommonUtils.initManagers();

            // Start GUI
            new ClientGUIController();
        });
    }
}
