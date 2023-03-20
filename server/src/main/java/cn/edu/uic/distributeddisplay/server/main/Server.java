/**
 * Run the main method in this class to start the application
 *
 * @author Team 3
 */
package cn.edu.uic.distributeddisplay.server.main;

import cn.edu.uic.distributeddisplay.core.util.CommonUtils;
import cn.edu.uic.distributeddisplay.core.util.Logger;
import cn.edu.uic.distributeddisplay.server.controller.ServerDashboardController;

import javax.swing.*;

public class Server {

    public static void main(String[] args) {
        // Initialize the logger
        Logger.initLogger("log.txt");

        // Start GUI interface
        SwingUtilities.invokeLater(() -> {
            CommonUtils.initUI();
            CommonUtils.initManagers();

            // Start Dashboard
            new ServerDashboardController();
        });
    }
}
