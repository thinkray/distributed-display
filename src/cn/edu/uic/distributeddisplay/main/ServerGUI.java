/**
 * Run the main method in this class to start the application
 *
 * @author Bohui WU
 * @since 12/18/2019
 */
package cn.edu.uic.distributeddisplay.main;

import cn.edu.uic.distributeddisplay.controller.ServerDashboardController;
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
            try {
                // Change the color theme of the program to Darcular
                UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");

                // Slider themes
                UIManager.getLookAndFeelDefaults().put("Slider.selectedTrackColor", new Color(43, 43, 43));
                UIManager.getLookAndFeelDefaults().put("Slider.disabledTickColor", new Color(43, 43, 43));
            } catch (ClassNotFoundException | InstantiationException |
                     IllegalAccessException | UnsupportedLookAndFeelException e) {
                // Fail to set the theme, use the default theme
                Log.logError(e.getMessage());
            }

            // Initialize managers
            ConfigManager.initConfigManager();
            LangManger.initLangManager();

            // Start Dashboard
            new ServerDashboardController();
        });
    }
}
