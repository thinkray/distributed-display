package cn.edu.uic.distributeddisplay.main;

import cn.edu.uic.distributeddisplay.controller.NodeGUIController;
import cn.edu.uic.distributeddisplay.util.CommonUtils;
import cn.edu.uic.distributeddisplay.util.Log;

import javax.swing.*;

public class NodeGUI {
    public static void main(String[] args) {
        // Initialize the logger
        Log.initLogger("log.txt");

        // Start GUI interface
        SwingUtilities.invokeLater(() -> {
            CommonUtils.initUI();
            CommonUtils.initManagers();

            // Start GUI
            new NodeGUIController();
        });
    }
}
