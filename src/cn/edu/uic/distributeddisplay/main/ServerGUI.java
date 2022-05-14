/**
 * Run the main method in this class to start the application
 *
 * @author Team 3
 */
package cn.edu.uic.distributeddisplay.main;

import cn.edu.uic.distributeddisplay.controller.ServerDashboardController;
import cn.edu.uic.distributeddisplay.util.CommonUtils;
import cn.edu.uic.distributeddisplay.util.Log;

import javax.swing.*;

public class ServerGUI {

    public static void main(String[] args) {
        // Initialize the logger
        Log.initLogger("log.txt");

        // Start GUI interface
        SwingUtilities.invokeLater(() -> {
            CommonUtils.initUI();
            CommonUtils.initManagers();

            // Start Dashboard
            new ServerDashboardController();
        });
    }
}
