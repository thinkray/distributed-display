/**
 * Run the main method in this class to start the application
 *
 * @author Bohui WU
 * @since 12/18/2019
 */
package cn.edu.uic.distributeddisplay.main;

import cn.edu.uic.distributeddisplay.controller.ServerDashboardController;
import cn.edu.uic.distributeddisplay.util.CommonUtils;
import cn.edu.uic.distributeddisplay.util.LangManger;
import cn.edu.uic.distributeddisplay.util.Log;
import cn.edu.uic.distributeddisplay.util.ConfigManager;

import javax.swing.*;
import java.awt.*;

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
