/**
 * Some utilities used by the program
 * 1). Convert a given Color object to the index of colorComboBox in the preference panel
 * 2). Convert a given index in the colorComboBox in the preference panel to a Color object
 * 3). Control the grid bag constrains object in the preference panel
 * 4). Get the screenshot of a given window (not used)
 *
 * @author Team 3
 * @version 1.0
 */
package cn.edu.uic.distributeddisplay.util;

import com.google.common.collect.HashBiMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CommonUtils {

    private static final HashBiMap<Color, Integer> colorHashMap = HashBiMap.create();
    private static final GridBagConstraints gbc;

    static {
        // Define the color map
        colorHashMap.put(Color.BLACK, 0);
        colorHashMap.put(Color.RED, 1);
        colorHashMap.put(Color.ORANGE, 2);
        colorHashMap.put(Color.YELLOW, 3);
        colorHashMap.put(Color.GREEN, 4);
        colorHashMap.put(Color.BLUE, 5);
        colorHashMap.put(Color.WHITE, 6);

        // Initialize the grid bag constraints
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
    }

    public static void initUI() {
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
    }

    public static void initManagers() {
        // Initialize managers
        ConfigManager.initConfigManager();
        LangManger.initLangManager();
    }

    public static int getColorFromIndex(Color color) throws NullPointerException {
        try {
            return colorHashMap.get(color);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public static Color getIndexFromColor(int index) throws NullPointerException {
        return colorHashMap.inverse().get(index);
    }

    public static GridBagConstraints getGridBagConstraints(int gridX, int gridWidth, int gridHeight, double weightX,
                                                           double weightY, Insets insets) {
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = gridX;
        gbc.gridwidth = gridWidth;
        gbc.gridheight = gridHeight;
        gbc.weightx = weightX;
        gbc.weighty = weightY;
        gbc.insets = insets;
        return gbc;
    }

    public static void gbcNewLine() {
        gbc.gridy += 1;
    }

    public static void gbcNewLine(int lines) {
        gbc.gridy += lines;
    }

    public static boolean openLocalFile(String loc) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().open(new File(loc));
                return true;  // Indicating success
            }
        } catch (Exception e) {
            Log.logError(e.getMessage());
        }
        return false;
    }

    public static boolean isImage(File file) {
        try {
            return ImageIO.read(file) != null;
        } catch (IOException e) {
            // May not even be a file
            return false;
        }
    }

}
